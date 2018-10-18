package com.worldkey.controller;

import com.worldkey.entity.Friend;
import com.worldkey.entity.FriendExample;
import com.worldkey.entity.Record;
import com.worldkey.entity.Users;
import com.worldkey.mapper.FriendMapper;
import com.worldkey.service.FriendService;
import com.worldkey.service.SystemConfigService;
import com.worldkey.service.UsersService;
import com.worldkey.service.impl.FriendServiceImpl;
import com.worldkey.util.ResultUtil;
import io.rong.models.CodeSuccessResult;
import io.rong.models.TokenResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HP
 */
@RestController
@RequestMapping("friend")
public class RongYunController {
    @Resource
    private FriendService friendService;
    @Resource
    private UsersService usersService;
    @Resource
    private SystemConfigService systemConfigService;
    @Resource
    private FriendMapper friendMapper;


    private Logger log = LoggerFactory.getLogger(RongYunController.class);

    /**
     * 获取融云Token
     *
     * @param token AppService的Token
     */
    @RequestMapping("{token}")
    public TokenResult getToken(@PathVariable String token) throws Exception {
        Users users = usersService.findByToken(token);
        if (users != null) {
            log.info(users.toString());
            return friendService.getToken(users.getLoginName(), users.getPetName(), users.getHeadImg());
        } else {
            log.info("users为空");
            return new TokenResult(406, "", "", "NO LOGIN");
        }


    }

    @RequestMapping("del/{token}")
    public ResultUtil delFriend(@PathVariable String token, String friendId) {

        Users byToken = this.usersService.findByToken(token);

        if (byToken == null) {
            log.debug("Users为空，用户未登陆");
            return new ResultUtil(406, "no", "not login");
        }
        log.debug("Users不为空，用户已登陆");
        log.debug("删除自己好友列表中的好友关系");
        this.friendMapper.delRecord(byToken.getLoginName(), friendId);
        this.friendMapper.delRecord(friendId, byToken.getLoginName());
        int i = this.friendService.delFriend(new Friend(byToken.getLoginName(), friendId, ""));
        log.debug("删除结果：" + i);
        log.debug("删除好友列表中的自己的好友关系");
        int i1 = this.friendService.delFriend(new Friend(friendId, byToken.getLoginName(), ""));
        log.debug("删除结果：" + i1);

        return new ResultUtil(200, "ok", i);
    }

    /**
     * @param token     用户在APP Service端的token
     * @param toUsersID 要添加的好友的usersId
     * @param message   附带的消息
     * @return CodeSuccessResult，融云返回的结果
     */
    @RequestMapping("request/{token}")
    public Object requestFriend(@PathVariable String token, @NotNull(message = "收信人不能为空") String toUsersID, String message) throws Exception {
        Users users = usersService.findByToken(token);
        if (null != users) {
            log.info("add friend request " + users.getLoginName() + " to " + toUsersID);
            
            
            Record r = this.friendMapper.selectR(users.getLoginName(),toUsersID);
            if(r!=null&&r.getType()==0){
            	this.friendMapper.cover(users.getLoginName(),toUsersID,message);
            	  return this.friendService.addFriend(users.getLoginName(), FriendServiceImpl.REQUEST, message, toUsersID);
            }else if(r!=null&&r.getType()==1){
            	 HashMap<String,Object> map = new HashMap<String,Object>();
                 map.put("status", 1);
            	  return map;
            }else if(r==null){
            	this.friendMapper.insertNotify(users.getLoginName(), toUsersID, message);
            }
            
            
//            this.friendService.record(users.getLoginName(),toUsersID,message);
            return this.friendService.addFriend(users.getLoginName(), FriendServiceImpl.REQUEST, message, toUsersID);
        }
        log.error("未登录");
        return "未登录";
    }

    @RequestMapping("accept/{token}")
    public Object acceptFriend(@PathVariable String token, @NotNull(message = "收信人不能为空") String toUsersID, String message) {
        Users users = usersService.findByToken(token);
        if (users != null) {
            CodeSuccessResult result = null;
            try {
            	 this.friendMapper.updateType(users.getLoginName(),toUsersID);
                result = this.friendService.addFriend(users.getLoginName(), FriendServiceImpl.ACCEPT, message, toUsersID);
            } catch (Exception e) {
                e.printStackTrace();
            }
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("result", result);
            map.put("status", 1);
            return map;
        }
        return "请先登录";
    }

    @RequestMapping("reject/{token}")
    public Object rejectFriend(@PathVariable String token, @NotNull(message = "收信人不能为空") String toUsersID, String message) throws Exception {
        Users users = usersService.findByToken(token);
        if (users != null) {

            return this.friendService.addFriend(users.getLoginName(), FriendServiceImpl.REJECT, message, toUsersID);
        }
        return null;
    }

    /**
     * 获取好友列表
     *
     * @param token 用户token
     * @return 好友列表
     */
    @RequestMapping("list/{token}")
    public ResultUtil friendList(@PathVariable String token) {
        Users users = usersService.findByToken(token);
        if (users == null) {
            return new ResultUtil(200, "no", "SB你谁呀");
        }
        List<FriendExample> list = this.friendService.findFriend(users.getLoginName());
        //在好友列表中加入本身
        Users user = usersService.findByToken(token);
        FriendExample map = new FriendExample();
        map.setUsersname(user.getPetName());
        map.setNote(user.getPetName());
//        String defaultHeadImg = systemConfigService.find().getDefaultHeadimg();
//        map.setHeadimg(user.getHeadImg() == null ? defaultHeadImg : user.getHeadImg());
        map.setHeadimg(this.friendMapper.getHeadImg(user.getId()));
        map.setUsersId(user.getLoginName());
        map.setUserId(user.getId());
        map.setSex(user.getSex());
        map.setTelNum(user.getTelNum());
        map.setBirthday(user.getBirthday());
        map.setEmail(user.getEmail());
        map.setSignature(user.getSignature());
        list.add(map);
        return new ResultUtil(200, "ok", list);
    }

    /**
     * 存在好友关系返回 1，不存在 0 ， 未登录 -1
     */
    @RequestMapping("checkFriend/{token}")
    public ResultUtil checkFriend(@PathVariable String token, String friend) {

        Users user = this.usersService.findByToken(token);
        int a = -1;
        if (user != null) {
            a = this.friendService.checkFriend(new Friend(user.getLoginName(), friend));
        }
        return new ResultUtil(200, "ok", a);
    }
    
    
    @RequestMapping("notify/{token}")
    public ResultUtil notify(@PathVariable String token){
    	Users user = this.usersService.findByToken(token);
    	String toUserId = user.getLoginName();
        List<Record> list = this.friendService.select(toUserId);
		return new ResultUtil (200, "ok", list);
    	
    }
    
    @RequestMapping("findUserId")
    public ResultUtil selectUserId(String loginName){
    	Long i = this.friendService.selectUserId(loginName);
		return new ResultUtil (200, "ok", i);
    	
    }
}
