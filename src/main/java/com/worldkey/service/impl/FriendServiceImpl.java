package com.worldkey.service.impl;

import com.worldkey.entity.CoffeeBarUser;
import com.worldkey.entity.Friend;
import com.worldkey.entity.FriendExample;
import com.worldkey.entity.Record;
import com.worldkey.entity.ReqRecord;
import com.worldkey.entity.Users;
import com.worldkey.exception.Z406Exception;
import com.worldkey.mapper.CoffeeBarUserMapper;
import com.worldkey.mapper.FriendMapper;
import com.worldkey.mapper.ReqRecordMapper;
import com.worldkey.mapper.UsersMapper;
import com.worldkey.service.CoffeeBarUserService;
import com.worldkey.service.FriendService;
import com.worldkey.service.SystemConfigService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ArrayDeleteUtil;
import com.worldkey.util.CustomizeBroadcastMessage;
import com.worldkey.util.CustomizeElope;
import com.worldkey.util.CustomizeMessage;
import com.worldkey.util.FileUploadUtilAsync;
import com.worldkey.util.PrivateRequest;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author HP
 */
@Service
@ConfigurationProperties(prefix = "rongyun")
public class FriendServiceImpl implements FriendService {
	/**
	 * 替换成您的appKey,在配置文件中，使用自动配置的方式注入
	 */
	private String appKey;
	/**
	 * 替换成匹配上面key的secret，在配置文件中，使用自动配置的方式注入
	 */
	private String appSecret;
	public static final String REQUEST = "Request";
	public static final String ACCEPT = "AcceptResponse";
	public static final String REJECT = "RejectResponse";
	public static final String EnlopeRequest = "EnlopeRequest";
	public static final String EnlopeAccept = "EnlopeAccept";
	public static final String EnlopeReject = "EnlopeReject";
	public static final String EnlopeBroadcast = "EnlopeBroadcast";

	private static Logger log = LoggerFactory.getLogger(FriendServiceImpl.class);

	@Resource
	private FriendMapper friendMapper;
	@Resource
	private UsersService usersService;
	@Resource
	private SystemConfigService systemConfigService;
	@SuppressWarnings("rawtypes")
	@Resource
	private RedisTemplate redisTemplate;
	@Resource
	private CoffeeBarUserMapper cbum;
	@Resource
	private CoffeeBarUserService cbus;
	@Resource
	private ReqRecordMapper rrm;
	@Resource
	private UsersMapper um;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String updateHeadImg(String token, String host, MultipartFile image) throws Exception {
		Users usersVo = usersService.findByToken(token);
		if (Objects.equals(usersVo, null)) {
			throw new Z406Exception("未登录");
		}
		String headImg = null;
		if (!image.isEmpty()) {
			headImg = new FileUploadUtilAsync().getFileName(host, image);
			usersVo.setHeadImg(headImg);
			usersService.updateByPrimaryKeySelective(usersVo);
			@SuppressWarnings("unchecked")
			ValueOperations<String, Users> operations = redisTemplate.opsForValue();
			operations.set(token, usersVo);
			// 刷新用户信息 融云API
			refreshInfo(usersVo.getLoginName(), usersVo.getPetName(), usersVo.getHeadImg());
		}
		return headImg;
	}

	/**
	 * 获取token
	 * 
	 * @param portraitUri
	 *            头像地址
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
		// RequestMessage requestMessage = new RequestMessage(fromUserId, type,
		// message, toUserId);

		try {
			messagePublishPrivateResult = rongCloud.message.publishPrivate(fromUserId, toUserId, friendMessage,
					"新的好友请求", "{\"pushData\":\"hello\"}", "4", 0, 0, 0, 0);

			log.info("发送好友请求" + type);
			// 请求添加好友
			if (type.equals(FriendServiceImpl.REQUEST)) {
				return messagePublishPrivateResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 同意添加好友
		if (type.equals(FriendServiceImpl.ACCEPT)) {
			Friend friend = new Friend(fromUserId, toUserId[0]);
			this.friendMapper.insertSelective(friend);
			this.friendMapper.insertSelective(new Friend(toUserId[0], fromUserId));
			CustomizeMessage friendMessage1 = new CustomizeMessage(type, "extra", fromUserId, toUserId[0], "accept");
			messagePublishPrivateResult = rongCloud.message.publishPrivate(fromUserId, toUserId, friendMessage1,
					"新的好友请求", "{\"pushData\":\"hello\"}", "4", 0, 0, 0, 0);
			log.info("同意添加好友结果：" + messagePublishPrivateResult.toString());
		}
		return messagePublishPrivateResult;
	}

	
	/**
	 * 私奔请求
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<Integer> enlopeRequest(Users user, String type, String message, String... toUserId){
		List<Integer> list = new ArrayList<Integer>();
		RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
		String fromUserId = user.getLoginName();
		CodeSuccessResult messagePublishPrivateResult = null;
			CoffeeBarUser user1 = this.cbum.selectByUserId1(user.getId());
			CoffeeBarUser user2 = this.cbum.selectByUserId1(Long.parseLong(toUserId[toUserId.length - 1]));
			CustomizeBroadcastMessage cbm = new CustomizeBroadcastMessage(user1,user2);
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			for (int i = 1; i <= 6; i++) {
				if (user2.getSeat().equals(i)) {
					map.put(i, 1);
				} else {
					map.put(i, 0);
				}
			}
			cbm.setMap(map);
			cbm.setIcon(this.cbum.selectIcon1(user.getId()));
			cbm.setContent(GsonUtil.toJson(cbm, CustomizeBroadcastMessage.class));
			ArrayDeleteUtil.delete(toString().length(), toUserId);
			try {
				messagePublishPrivateResult = rongCloud.message.publishPrivate(fromUserId, toUserId, cbm, "私奔请求",
						"{\"pushData\":\"hello\"}", "4", 0, 0, 0, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			list.add(Integer.parseInt(user.getId()+""));
			list.add(Integer.parseInt(toUserId[toUserId.length - 1]));
			return list;
	}


	@Override
	public String elopeHandler(Users user, String type, String message,String... toUserId) {
		Users user2 = this.um.selectByLoginName(toUserId[0]);
		RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
		String fromUserId = user.getLoginName();
		CodeSuccessResult messagePublishPrivateResult = null;
		CustomizeElope friendMessage1 = new CustomizeElope(this.cbum.selectLabelByUserId(user.getId().toString()),
				type, user.getId().toString());
		ReqRecord rr = this.rrm.selectByIsReqId(user.getId());
		CoffeeBarUser user1 = this.cbum.selectByUserId(rr.getReqId());
		friendMessage1.setFromSeat((user1.getSeat()));
		friendMessage1.setToSeat(this.cbum.selectByUserId(rr.getIsReqId()).getSeat());
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 1; i <= 6; i++) {
			if (user1.getSeat().equals(i)) {
				map.put(i, 1);
			} else {
				map.put(i, 0);
			}
		}
		friendMessage1.setMap(map);
		
		if(type.equals(FriendServiceImpl.EnlopeAccept)){
			friendMessage1.setChannel(rr.getCreateDate().getTime()+"");
			log.info(rr.getCreateDate().getTime()+">>>>>>>>>>>>>>>>>>>>>>>");
			this.cbum.elopeSucceed(Integer.parseInt(user.getId()+""),Integer.parseInt(user2.getId()+""));
			this.rrm.elopeSucceed(rr.getId());
		}
		friendMessage1.setContent(GsonUtil.toJson(friendMessage1, CustomizeElope.class));
		try {
			messagePublishPrivateResult  = rongCloud.message.publishPrivate(fromUserId, toUserId, friendMessage1,
							"私奔请求", "{\"pushData\":\"hello\"}", "4", 0, 0, 0, 0);
			log.info(messagePublishPrivateResult+">>>>>>>>>>>>>>>>>>>>");
		} catch (Exception e) {
			e.printStackTrace();
		}if(type.equals(FriendServiceImpl.EnlopeAccept)){
			return rr.getCreateDate().getTime()+"";
		}
		return "";
	}
	
	@SuppressWarnings("unused")
	@Override
	public PrivateRequest infoCheck(String reqName,String message,String... toUserId) {
		RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
		CodeSuccessResult messagePublishPrivateResult = null;
		PrivateRequest pr = new PrivateRequest();
		pr.setMessage(1);
//		try {
//			messagePublishPrivateResult  = rongCloud.message.publishPrivate(reqName, toUserId, pr,
//							"私奔请求", "{\"pushData\":\"hello\"}", "4", 0, 0, 0, 0);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
		return pr;
	}
	
	@Override
	public void infoCheck1(Users user, Users user1) {
		RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
		CodeSuccessResult messagePublishPrivateResult = null;
		 PrivateRequest pr = new PrivateRequest(user1.getLoginName(),user1.getHeadImg(),user1.getId(),
					user.getLoginName(),user.getHeadImg(),user.getId());
		 pr.setMessage(2);
		 pr.setContent(GsonUtil.toJson(pr, PrivateRequest.class));
		 String[] s = {user1.getLoginName()};
		 try {
				messagePublishPrivateResult  = rongCloud.message.publishPrivate(user.getLoginName(), s, pr,
								"私奔请求", "{\"pushData\":\"hello\"}", "4", 0, 0, 0, 0);
				log.info(messagePublishPrivateResult.toString()+">>>>>>>>>>>>>>>>>>>>>>>");
			}catch (Exception e) {
				e.printStackTrace();
			}
	}
	@Override
	public void dissPeople(Users user, Users user1) {
		RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
		CodeSuccessResult messagePublishPrivateResult = null;
		 PrivateRequest pr = new PrivateRequest("您已被踢出房间！ ");
		 pr.setContent(GsonUtil.toJson(pr, PrivateRequest.class));
		 String[] s = {user1.getLoginName()};
		 try {
				messagePublishPrivateResult  = rongCloud.message.publishPrivate(user.getLoginName(), s, pr,
								"私奔请求", "{\"pushData\":\"hello\"}", "4", 0, 0, 0, 0);
				log.info(messagePublishPrivateResult.toString()+">>>>>>>>>>>>>>>>>>>>>>>");
			}catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * 获取好友列表
	 *
	 * @param loginName
	 *            用户的usersId
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
	 * @param f
	 *            Friend
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
		List<Record> list = this.friendMapper.selectRecord(toUserId);
		return list;
	}


	/*
	 * 
	 * //请求信息的封装类
	 * 
	 * private class RequestMessage {
	 * 
	 * private String fromUserId; private String type; private String message;
	 * private String[] toUserId;
	 * 
	 * @Override public boolean equals(Object o) { if (this == o) { return true;
	 * } if (o == null || getClass() != o.getClass()) { return false; }
	 * 
	 * RequestMessage that = (RequestMessage) o;
	 * 
	 * if (fromUserId != null ? !fromUserId.equals(that.fromUserId) :
	 * that.fromUserId != null) { return false; } if (type != null ?
	 * !type.equals(that.type) : that.type != null) { return false; } // if
	 * (message != null ? !message.equals(that.message) : that.message != null)
	 * return false; // Probably incorrect - comparing Object[] arrays with
	 * Arrays.equals return Arrays.equals(toUserId, that.toUserId); }
	 * 
	 * @Override public int hashCode() { int result = fromUserId != null ?
	 * fromUserId.hashCode() : 0; result = 31 * result + (type != null ?
	 * type.hashCode() : 0); result = 31 * result + (message != null ?
	 * message.hashCode() : 0); result = 31 * result +
	 * Arrays.hashCode(toUserId); return result; }
	 * 
	 * RequestMessage(String fromUserId, String type, String message, String[]
	 * toUserId) { this.fromUserId = fromUserId; this.type = type; this.message
	 * = message; this.toUserId = toUserId; } }
	 */

}
