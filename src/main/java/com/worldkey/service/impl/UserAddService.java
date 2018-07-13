package com.worldkey.service.impl;

import com.worldkey.entity.Users;
import com.worldkey.mapper.UsersMapper;
import com.worldkey.service.CaptService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author WU
 */
@Service
public class UserAddService {

    @Resource
    private CaptService captService;
    @Resource
    private UsersMapper uMapper;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 绑定手机号
     */
    @CachePut(value = "token", key = "#token")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Users addTel(Users u, String token, String code, String phone) throws Exception {
        String codeCo = captService.getCapt(phone);
        if (!Objects.equals(code, codeCo)) {
            throw new Exception("code error");
        }
        Users users1 = uMapper.selectBytelNum(phone);
        if (users1 != null) {
            throw new Exception("telNum is exist");
        }
        Users users = new Users();
        users.setId(u.getId());
        users.setTelNum(phone);
        u.setTelNum(phone);
        this.uMapper.updateByPrimaryKeySelective(users);
        return u;

    }
	/**
	 * 6.15绑定手机号（不需要token）
	 * @throws Exception 
	 */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Users addTel1(String code, String phone) throws Exception{
    	String codeCo = captService.getCapt(phone);
        if (!Objects.equals(code, codeCo)) {
            throw new Exception("code error");
        }
        Users users1 = uMapper.selectBytelNum(phone);
        if (users1 != null) {
            throw new Exception("telNum is exist");
        }
        Users u = new Users();
        u.setTelNum(phone);
        this.uMapper.insertSelective(u);
        Users us = this.uMapper.selectBytelNum(phone);
    	return us;
    }
    /**
     * 更换手机号
     */
    @CachePut(value = "token", key = "#token")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Users updateTel(Users u, String token, String code, String phone) throws Exception {
        String codeCo = captService.getCapt(phone);
        if (codeCo == null || !(codeCo.equals(code))) {
            throw new Exception("验证码错误：" + code);
        }
        //判断原手机号是否在数据库中存在
        Users us = uMapper.selectBytelNum(phone);
        if (us != null) {
            throw new Exception("phone is exist");
        }
        u.setTelNum(phone);
        this.uMapper.updateByPrimaryKeySelective(u);
        return u;
    }

    /**
     * 修改密码
     * 核对后发送MD5
     */

//    public String checkPwd(String loginName, String telNum, String code, Integer timeout) throws Exception {
//        ValueOperations<String, String> vp = redisTemplate.opsForValue();
//        Users u = this.uMapper.selectByLoginName(loginName);
//        if (Objects.isNull(u)) {
//            throw new Exception("用户名不存在：" + loginName);
//        }
//
//        String codeCo = captService.getCapt(telNum);
//        if (!Objects.equals(telNum, u.getTelNum())) {
//            throw new Exception("手机号不匹配：" + telNum);
//        }
//        if (Objects.isNull(codeCo) || !Objects.equals(codeCo, code)) {
//            throw new Exception("验证码错误：" + code);
//        }
//        String md5 = DigestUtils.md5Hex(telNum + code);
//        vp.set(telNum, md5, timeout, TimeUnit.SECONDS);
//        return md5;
//    }
    
    public String checkPwd1(String telNum, String code, Integer timeout) throws Exception {
        ValueOperations<String, String> vp = redisTemplate.opsForValue();
        String codeCo = captService.getCapt(telNum);
        Users u = this.uMapper.selectBytelNum(telNum);
        if(u==null){
        	throw new Exception("用户不存在，或未绑定该手机号");
        }
        if (Objects.isNull(codeCo) || !Objects.equals(codeCo, code)) {
            throw new Exception("验证码错误：" + code);
        }
        String md5 = DigestUtils.md5Hex(telNum + code);
        vp.set(telNum, md5, timeout, TimeUnit.SECONDS);
        return md5;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Users updatePwd(Users u, String temToken) {
        //根据传来的loginName从redis中取出临时加密的MD5.
        String s = captService.getTelNumAndCodeMD5(u.getTelNum());
        //将取出的MD5和前端传入的temToken进行匹配
        if (!(temToken.equals(s))) {
            return null;
        }
        Users us = new Users();
        if(u.getLoginName()!=null){
        //通过用户名查找对象
        	us = this.uMapper.selectByLoginName(u.getLoginName());
        }else if(u.getTelNum()!=null){
        	us = this.uMapper.selectBytelNum(u.getTelNum());
        }
        //通过查出的对象写入密码
        us.setPassword(DigestUtils.md5Hex(u.getPassword()));
        //通过对象找对应主键
        this.uMapper.updateByPrimaryKeySelective(us);
        return us;
    }


}
