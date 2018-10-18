package com.worldkey.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.worldkey.config.SystemProperties;
import com.worldkey.entity.CoffeeBarUser;
import com.worldkey.entity.Friend;
import com.worldkey.entity.FriendExample;
import com.worldkey.entity.Gift;
import com.worldkey.entity.GiftUsers;
import com.worldkey.entity.JdDetail;
import com.worldkey.entity.KbDetail;
import com.worldkey.entity.Record;
import com.worldkey.entity.ReqRecord;
import com.worldkey.entity.Users;
import com.worldkey.entity.ZsDetail;
import com.worldkey.enumpackage.JdRewardType;
import com.worldkey.enumpackage.KbObtainType;
import com.worldkey.enumpackage.ZsObtainType;
import com.worldkey.exception.Z406Exception;
import com.worldkey.mapper.CoffeeBarMapper;
import com.worldkey.mapper.CoffeeBarUserMapper;
import com.worldkey.mapper.FriendMapper;
import com.worldkey.mapper.GiftMapper;
import com.worldkey.mapper.GiftUsersMapper;
import com.worldkey.mapper.ReqRecordMapper;
import com.worldkey.mapper.UsersMapper;
import com.worldkey.service.CoffeeBarUserService;
import com.worldkey.service.FriendService;
import com.worldkey.service.JdDetailService;
import com.worldkey.service.KbDetailService;
import com.worldkey.service.SystemConfigService;
import com.worldkey.service.UsersService;
import com.worldkey.service.ZsDetailService;
import com.worldkey.util.ArrayDeleteUtil;
import com.worldkey.util.Barrage;
import com.worldkey.util.BestowUtil;
import com.worldkey.util.BestowUtilCustomize;
import com.worldkey.util.CustomizeBroadcastMessage;
import com.worldkey.util.CustomizeElope;
import com.worldkey.util.CustomizeMessage;
import com.worldkey.util.DissVote;
import com.worldkey.util.FileUploadUtilAsync;
import com.worldkey.util.Notice;
import com.worldkey.util.PrivateRequest;

import io.rong.RongCloud;
import io.rong.models.CodeSuccessResult;
import io.rong.models.TokenResult;
import io.rong.util.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author HP
 */
@Service
@ConfigurationProperties(prefix = "rongyun")
public class FriendServiceImpl extends ServiceImpl<GiftMapper, Gift> implements FriendService {
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
    public static final String EnlopeRequest = "EnlopeRequest";
	public static final String EnlopeAccept = "EnlopeAccept";
	public static final String EnlopeReject = "EnlopeReject";
	public static final String EnlopeBroadcast = "EnlopeBroadcast";
	public static final String NOTICE = "NOTICE";
	public static final String BARRAGE = "BARRAGE";


    private static Logger log= LoggerFactory.getLogger(FriendServiceImpl.class);

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
	@Resource
	private JdDetailService jdDetailService;
	@Resource
	private GiftUsersMapper giftUsersMapper;
	@Resource
	private SystemProperties systemProperties;
	@Resource
	private ZsDetailService zsDetailService;
	@Resource
	private KbDetailService kbDetailService;
	@Resource
	private GiftMapper gm;
	@Resource
	private CoffeeBarMapper cbm;

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
		list.add(Integer.parseInt(user.getId()+""));
		list.add(Integer.parseInt(toUserId[toUserId.length - 1]));
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
		cbm.setIcon(this.cbum.selectIcon4(user.getId()));
		cbm.setIconRot(this.cbum.selectIcon1(user.getId()));
		cbm.setContent(GsonUtil.toJson(cbm, CustomizeBroadcastMessage.class));
		ArrayDeleteUtil.delete(toString().length(), toUserId);
		try {
				messagePublishPrivateResult = rongCloud.message.publishPrivate(fromUserId, toUserId, cbm, "私奔请求",
						"{\"pushData\":\"hello\"}", "4", 0, 0, 0, 0);
		} catch (Exception e) {
				e.printStackTrace();
		}
		return list;
	}


	@Override
	@CachePut(value = "channel", key = "#user.id+'inchannel'")
	public Map<String,Object> elopeHandler(Users user, String type, String message,String... toUserId) {
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
		Map<String,Object> result = new HashMap<>();
		result.put("label",this.cbum.selectLabelByUserId(user.getId().toString()));
		result.put("channelName",this.cbm.getBarName(this.cbum.selectBarIdByUserId1(user.getId().intValue())));
		log.info(map.toString());
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
			result.put("channel",rr.getCreateDate().getTime()+"");
		}
		return result;
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
				log.info(messagePublishPrivateResult.toString()+">>>>>>>>>>>>>>>>>>>>>>>推送成功了啊啊啊啊");
			}catch (Exception e) {
				e.printStackTrace();
			}
	}
	@Override
	public void dissPeople(Users user, Users user1) {
		RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
		CodeSuccessResult messagePublishPrivateResult = null;
		DissVote dis = new DissVote("您已被踢出房间！");
		dis.setContent(GsonUtil.toJson(dis, DissVote.class));
		String[] s = {user1.getLoginName()};
		 try {
				messagePublishPrivateResult  = rongCloud.message.publishPrivate(user.getLoginName(), s, dis,
								"私奔请求", "{\"pushData\":\"hello\"}", "4", 0, 0, 0, 0);
				log.info(messagePublishPrivateResult.toString()+">>>>>>>>>>>>>>>>>>>>>>>"+dis.toString());
			}catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public void barrage(String userName,String message,String icon,String type,String... toUserId){
		RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
		CodeSuccessResult messagePublishPrivateResult = null;
		if(type.equals(FriendServiceImpl.NOTICE)){
			Notice n = new Notice(message);
			n.setContent(GsonUtil.toJson(n,Notice.class));
			try {
				messagePublishPrivateResult  = rongCloud.message.publishPrivate(userName, toUserId, n,
						"私奔请求", "{\"pushData\":\"hello\"}", "4", 0, 0, 0, 0);
				log.info(messagePublishPrivateResult.toString()+">>>>>>>>>>>>>>>>>>>>>>>");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			Barrage b = new Barrage(message,icon);
			b.setContent(GsonUtil.toJson(b,Barrage.class));
			try {
				messagePublishPrivateResult  = rongCloud.message.publishPrivate(userName, toUserId, b,
						"私奔请求", "{\"pushData\":\"hello\"}", "4", 0, 0, 0, 0);
				log.info(messagePublishPrivateResult.toString()+">>>>>>>>>>>>>>>>>>>>>>>");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer giftUsers(String token, Integer giftId, Long userId,String... toUserId) {

		Gift gift = this.baseMapper.selectById(giftId);
		// 礼物ID不存在返回0
		if (gift == null) {
			return 0;
		}
		Users users = usersService.findByToken(token);
		Users users1 = usersService.selectByPrimaryKey(userId);
		Users jdAndZsAndKb = usersService.getJdAndZsAndKb(users.getId());
		// 判断是否是金豆礼物
		boolean jdPrice = gift.getJdPrice() != null && gift.getJdPrice() > 0;
		// 判断余额是否充足
		if (jdPrice && jdAndZsAndKb.getJd() >= gift.getJdPrice()) {
			// 赠送礼物操作
			// 1.用户的金豆数量减少
			usersService.addJd(gift.getJdPrice() * (-1), jdAndZsAndKb.getId());
			// 2.被赠送的用户的金豆增加
			usersService.addJd(gift.getJdPrice(),userId);
			// 记录两人的金豆收支记录
			// 赠送人的支出记录
			JdDetail jdDetail = new JdDetail(new Date(), gift.getJdPrice() * -1, JdRewardType.giveGift.getIndex(),
					users.getId(),userId, users1.getPetName(),
					"送给" + users1.getPetName() + "一个" + gift.getName());
			// 受赠人的收入记录
			JdDetail jdDetail1 = new JdDetail(new Date(), gift.getJdPrice(), JdRewardType.giveGift.getIndex(),
					userId, users.getId(), users.getPetName(),
					"收到" + users.getPetName() + "送的一个" + gift.getName());
			List<JdDetail> jdDetails = new ArrayList<>(2);
			jdDetails.add(jdDetail);
			jdDetails.add(jdDetail1);
			// 存入数据库
			jdDetailService.insertBatch(jdDetails);
			// 记录礼物赠送记录
			GiftUsers giftUsers = new GiftUsers(new Date(), users.getId(), userId, giftId);
			boolean insert = giftUsersMapper.insert(giftUsers);
			// 成功返回3，插入失败返回4
			return this.Bestowed(insert,users,users1,giftId,toUserId);
		}
		// 判断是否是钻石礼物
		boolean zsPrice = gift.getZsPrice() != null && gift.getZsPrice() > 0;
		// 判断余额是否充足
		if (zsPrice && jdAndZsAndKb.getZs() >= gift.getZsPrice()) {
			// 赠送礼物操作
			// 赠送放的钻石减少
			usersService.addOrSubZs(gift.getZsPrice() * (-1), users.getId());
			// 记录赠送方的钻石支出记录
			ZsDetail zsDetail = new ZsDetail(new Date(), gift.getZsPrice() * -1, ZsObtainType.giveGift.getIndex(),
					users.getId(), userId, users1.getPetName());
			zsDetail.setMsg("赠送给" + users1.getPetName() + "一个" + gift.getName());
			zsDetailService.insert(zsDetail);
			// 受赠方的K币增加
			BigDecimal zs2kb = new BigDecimal(systemProperties.getZs2kb() * 0.01);
			BigDecimal giftPrice = new BigDecimal(gift.getZsPrice());
			BigDecimal kb = (zs2kb.multiply(giftPrice)).setScale(2, BigDecimal.ROUND_HALF_DOWN);

			// 此处使用BigDecimal.ROUND_HALF_DOWN方法，得到两位且四舍五入的精度
			usersService.addOrSubKb(kb.setScale(2, BigDecimal.ROUND_HALF_DOWN), userId);

			// 记录受赠方的K币收入记录
			String msg = "收到" + users.getPetName() + "赠送的一个" + gift.getName();
			KbDetail kbDetail = new KbDetail(new Date(), kb.setScale(2, BigDecimal.ROUND_HALF_DOWN),
					KbObtainType.getGift.getIndex(), users1.getId(), users.getId(), users.getPetName(), msg);
			kbDetailService.insert(kbDetail);
			// 记录礼物赠送记录
			GiftUsers giftUsers = new GiftUsers(new Date(), users.getId(), userId, giftId);
			boolean insert = giftUsersMapper.insert(giftUsers);
			// 成功返回3，插入失败返回4
			return this.Bestowed(insert,users,users1,giftId,toUserId);
		}
		// 余额不足返回2
		return 2;
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    /**
     * 获取好友列表
     *
     * @param loginName 用户的usersId
     */
    @Override
    public List<FriendExample> findFriend(String loginName) {
        List<FriendExample> list = this.friendMapper.selectByUsersId(loginName);
//        String defaultHeadImg = systemConfigService.find().getDefaultHeadimg();
//        list.stream().filter(e -> e.getHeadimg() == null).forEach(e -> e.setHeadimg(defaultHeadImg));
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

//	@Override
//	public int record(String userId, String toUserID, String message) {
//		Record r = this.friendMapper.selectR(userId,toUserID);
//        if(r!=null){
//        	int i= this.friendMapper.cover(userId,toUserID,message);
//        	 return i;
//        }
//		return this.friendMapper.insertNotify(userId, toUserID, message);
//	}

	@Override
	public List<Record> select(String toUserId) {
		List<Record> list=this.friendMapper.selectRecord(toUserId);
		return list;
	}

	@Override
	public Long selectUserId(String loginName) {
		Long a = this.friendMapper.selectUserId(loginName);
		return a;
	}
	
	
	
	
	
	private Integer Bestowed(boolean insert,Users users,Users users1,Integer giftId,String... toUserId){
		if(insert){
			RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
			CodeSuccessResult messagePublishPrivateResult = null;
			String s = this.cbum.selectLabelByUserId(users.getId().toString())+"给"+
					this.cbum.selectLabelByUserId(users1.getId().toString())+"赠送了"+this.gm.getName(giftId);
			BestowUtilCustomize best = new BestowUtilCustomize(200,"ok",
					new BestowUtil(s,
					this.cbum.selectSeatByUserId(users.getId()),
					this.cbum.selectSeatByUserId(users1.getId()),
					this.gm.getGiftImg(giftId)));
			best.setContent(GsonUtil.toJson(best,BestowUtilCustomize.class));
			try {
				messagePublishPrivateResult  = rongCloud.message.publishPrivate(
						users.getLoginName(), toUserId, best,
						"私奔请求", "{\"pushData\":\"hello\"}",
						"4", 0, 0, 0, 0);
				log.info(messagePublishPrivateResult.toString()+">>>>>>>>>>>>>>>>>>>>>>>"+best.getContent());
			}catch (Exception e) {
				e.printStackTrace();
			}
			return 3;
		}else {
			return 4;
		}
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
