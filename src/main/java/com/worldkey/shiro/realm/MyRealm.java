package com.worldkey.shiro.realm;

import com.worldkey.entity.Action;
import com.worldkey.entity.Admin;
import com.worldkey.entity.Role;
import com.worldkey.service.ActionService;
import com.worldkey.service.AdminService;
import com.worldkey.service.RoleService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Set;

public class MyRealm extends AuthorizingRealm {

	@Resource
	private AdminService userInfoService;
	@Resource
	private RoleService roleService;
	@Resource
	private ActionService actionService;

	private Logger log=LoggerFactory.getLogger(MyRealm.class);
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		/*
		        * 当没有使用缓存的时候，不断刷新页面的话，这个代码会不断执行，
		        * 当其实没有必要每次都重新设置权限信息，所以我们需要放到缓存中进行管理；
		        * 当放到缓存中时，这样的话，doGetAuthorizationInfo就只会执行一次了，
		        * 缓存过期之后会再次执行。
		        */
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		 Admin userInfo = (Admin)principals.fromRealm(this.getClass().getName()).iterator().next();
		 //超级管理员拥有所有角色，但不是全部保存在数据库中，只是在代码中处理逻辑
		 for (Role role : userInfo.getRoles()) {
			 if (role.getId()==1) {
				 userInfo.setRoles(roleService.findAll());
				 break;
			}
		 }
		Set<Role> byAdminId = this.roleService.findByAdminId(userInfo.getId());
		userInfo.setRoles(byAdminId);
		 //获取用户的角色和权限集，并传递给shiro
		 for (Role role : userInfo.getRoles()) {
			 authorizationInfo.addRole(role.getName());
			 for (Action permission : actionService.findByRoleId(role.getId())) {
				 authorizationInfo.addStringPermission(role.getName()+":"+permission.getName());
			}
		}
		return authorizationInfo;
	}

	/**
	 * 认证信息.(身份验证)  
	 *  Authentication 是用来验证用户身份    
	 *  @param token    
	 *	@return
	 *  @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken utoken=(UsernamePasswordToken) token;
		//获取用户输入的帐号
		String username=utoken.getUsername();
		//获取用户对象
		Admin userInfo=userInfoService.findActionByName(username);
		if (userInfo!=null) {
			log.debug("userInfo:-->"+userInfo.getName());
		} else {
			log.debug("用户不存在：" + username);
		}
		  //放入shiro.调用CredentialsMatcher检验密码，使用的是自定义密码比较器
		//规避当用户不存在发生的空指针异常
		  return new SimpleAuthenticationInfo(userInfo==null?new Admin():userInfo,userInfo==null?"":userInfo.getPassword(),this.getClass().getName());
	}

}
