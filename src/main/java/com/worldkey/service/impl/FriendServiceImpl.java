package com.worldkey.service.impl;

import com.worldkey.entity.Friend;
import com.worldkey.entity.FriendExample;
import com.worldkey.entity.Record;
import com.worldkey.entity.Users;
import com.worldkey.exception.Z406Exception;
import com.worldkey.mapper.FriendMapper;
import com.worldkey.service.FriendService;
import com.worldkey.service.SystemConfigService;
import com.worldkey.service.UsersService;
import com.worldkey.util.CustomizeMessage;
import com.worldkey.util.FileUploadUtilAsync;
import io.rong.RongCloud;
import io.rong.models.CodeSuccessResult;
import io.rong.models.TokenResult;
import io.rong.util.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author HP
 */
@Service
@ConfigurationProperties(prefix = "rongyun")
public class FriendServiceImpl implements FriendService {
    /**
     替换成您的appKey,在配置文件中，使用自动配置的方式注入
     */
    private String appKey;
    /**
     * 替换成匹配上面key的secret，在配置文件中，使用自动配置的方式注入
     */
    private String appSecret;
    public static final String REQUEST = "Request";
    public static final String ACCEPT = "AcceptResponse";
    public static final String REJECT = "RejectResponse";


    private static Logger log= LoggerFactory.getLogger(FriendServiceImpl.class);

    @Resource
    private FriendMapper friendMapper;
    @Resource
    private UsersService usersService;
    @Resource
    private SystemConfigService systemConfigService;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor = Exception.class)
    public String updateHeadImg(String token, String host, MultipartFile image) throws Exception {
        Users usersVo = usersService.findByToken(token);
        if (Objects.equals(usersVo,null)){
            throw new Z406Exception("未登录");
        }
        String headImg=null;
        if (!image.isEmpty()) {
            headImg = new FileUploadUtilAsync().getFileName(host, image);
            usersVo.setHeadImg(headImg);
            usersService.updateByPrimaryKeySelective(usersVo);
            @SuppressWarnings("unchecked")
            ValueOperations<String, Users> operations = redisTemplate.opsForValue();
            operations.set(token, usersVo);
            //刷新用户信息 融云API
            refreshInfo(usersVo.getLoginName(), usersVo.getPetName(), usersVo.getHeadImg());
        }
        return  headImg;
    }

    /**
     * 获取token
     * @param portraitUri 头像地址
     */
    @Override
    public TokenResult getToken(String userId, String name, String portraitUri) throws Exception {
        RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
        TokenResult userGetTokenResult = null;
        try {
            userGetTokenResult = rongCloud.user.getToken(userId, name, portraitUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rongCloud.user.refresh(userId, name, portraitUri);
        return userGetTokenResult;
    }

    @Override
    public CodeSuccessResult refreshInfo(String userId, String name, String portraitUri) throws Exception {
        RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
        return rongCloud.user.refresh(userId, name, portraitUri);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CodeSuccessResult addFriend(String fromUserId, String type, String message, String... toUserId)
            throws Exception {
        RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
        Users user = usersService.findByLoginName(fromUserId);
        CustomizeMessage friendMessage = new CustomizeMessage(type, "extra", fromUserId, toUserId[0], message);
        friendMessage.setUser(user);
        friendMessage.setContent(GsonUtil.toJson(friendMessage, CustomizeMessage.class));
        CodeSuccessResult messagePublishPrivateResult = null;
        //RequestMessage requestMessage = new RequestMessage(fromUserId, type, message, toUserId);

        try {
            messagePublishPrivateResult = rongCloud.message.publishPrivate(fromUserId, toUserId, friendMessage,
                    "新的好友请求", "{\"pushData\":\"hello\"}", "4", 0, 0, 0, 0);

            log.info("发送好友请求"+type);
            //请求添加好友
            if (type.equals(FriendServiceImpl.REQUEST)) {
                return messagePublishPrivateResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //同意添加好友
        if (type.equals(FriendServiceImpl.ACCEPT)) {
            Friend friend = new Friend(fromUserId, toUserId[0]);
           this.friendMapper.insertSelective(friend);
            this.friendMapper.insertSelective(new Friend(toUserId[0], fromUserId));
            CustomizeMessage friendMessage1 = new CustomizeMessage(type, "extra", fromUserId, toUserId[0], "accept");
            messagePublishPrivateResult = rongCloud.message.publishPrivate(fromUserId, toUserId, friendMessage1,
                    "新的好友请求", "{\"pushData\":\"hello\"}", "4", 0, 0, 0, 0);
                log.info("同意添加好友结果："+messagePublishPrivateResult.toString());
        }

        return messagePublishPrivateResult;
    }

    /**
     * 获取好友列表
     *
     * @param loginName 用户的usersId
     */
    @Override
    public List<FriendExample> findFriend(String loginName) {
        List<FriendExample> list = this.friendMapper.selectByUsersId(loginName);
        String defaultHeadImg = systemConfigService.find().getDefaultHeadimg();
        list.stream().filter(e -> e.getHeadimg() == null).forEach(e -> e.setHeadimg(defaultHeadImg));
        return list;
    }

    /**
     * 检查当前用户和其他用户是否存在好友关系
     *
     * @param f Friend
     * @return int
     */
    @Override
    public int checkFriend(Friend f) {
        if (f.getFriend().equals(f.getUsers())) {
            return 1;
        }
        int c = this.friendMapper.checkFriend(f);
        // 存在好友关系
        if (c != 0) {
            return 1;
        }
        // 不存在好友关系
        return 0;

    }



    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }




    @Override
    public int delFriend(Friend friend) {
        return this.friendMapper.delByUserAndFriend(friend);
    }

	@Override
	public int record(String userId, String toUserID, String message) {
		return this.friendMapper.insertNotify(userId, toUserID, message);
	}

	@Override
	public List<Record> select(String toUserId) {
		List<Record> list=this.friendMapper.selectRecord(toUserId);
		return list;
	}

    /*

      //请求信息的封装类

   private class RequestMessage {

        private String fromUserId;
        private String type;
        private String message;
        private String[] toUserId;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            RequestMessage that = (RequestMessage) o;

            if (fromUserId != null ? !fromUserId.equals(that.fromUserId) : that.fromUserId != null) {
                return false;
            }
            if (type != null ? !type.equals(that.type) : that.type != null) {
                return false;
            }
            // if (message != null ? !message.equals(that.message) : that.message != null) return false;
            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            return Arrays.equals(toUserId, that.toUserId);
        }

        @Override
        public int hashCode() {
            int result = fromUserId != null ? fromUserId.hashCode() : 0;
            result = 31 * result + (type != null ? type.hashCode() : 0);
            result = 31 * result + (message != null ? message.hashCode() : 0);
            result = 31 * result + Arrays.hashCode(toUserId);
            return result;
        }

        RequestMessage(String fromUserId, String type, String message, String[] toUserId) {
            this.fromUserId = fromUserId;
            this.type = type;
            this.message = message;
            this.toUserId = toUserId;
        }
    }*/

}
