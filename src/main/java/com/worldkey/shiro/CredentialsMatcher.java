package com.worldkey.shiro;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义密码比较器
 * @author HP
 *
 */
public class CredentialsMatcher extends SimpleCredentialsMatcher{

	private Logger log=LoggerFactory.getLogger(CredentialsMatcher.class);
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		log.debug("运行密码比较器");
		UsernamePasswordToken uToken=(UsernamePasswordToken) token;
		String inPassword=new String(uToken.getPassword());
		String dbPassword=info.getCredentials().toString();
		//使用加密算法 这有坑哦，但是填坑的代价太大，如果可以，建议填平
		return this.equals(dbPassword, DigestUtils.md5Hex(inPassword));
	}

}
