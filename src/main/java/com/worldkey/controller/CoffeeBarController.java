package com.worldkey.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.worldkey.entity.*;
import com.worldkey.mapper.*;
import com.worldkey.util.BestowUtil;
import org.springframework.web.bind.annotation.*;

import com.worldkey.service.CoffeeBarUserService;
import com.worldkey.service.FriendService;
import com.worldkey.service.ReqRecordService;
import com.worldkey.service.UsersService;
import com.worldkey.service.impl.FriendServiceImpl;
import com.worldkey.util.ResultUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author 北京都百01
 *
 */
@RestController
@RequestMapping("coffeeBar")
@Slf4j
public class CoffeeBarController {
	@Resource
	CoffeeBarUserService cbus;
	@Resource
	UsersService us;
	@Resource
	FriendService fs;
	@Resource
	CoffeeBarUserMapper cbum;
	@Resource
	UsersService um;
	@Resource
	ReqRecordMapper rrm;
	@Resource
	ReqRecordService rs;
	@Resource
	DissRecordMapper dr;
	@Resource
	GiftMapper gm;
	@Resource
	CoffeeBarMapper coffeeBarMapper;

	/**
	 * 随机匹配咖啡厅
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping("matching")
	public ResultUtil matching(Integer userId) {
		return this.cbus.matchingState(userId,0);
	}

	@RequestMapping("getUser")
	public ResultUtil getUser(Integer userId) {
		return new ResultUtil(200, "ok", this.cbus.getByUserId(userId));
	}

	/**
	 !*     attention!!!!
	 * @param userId
	 *            离开用户Id
	 * @param isToUserId
	 *            当前用户Id
	 * @return
	 */
	@SuppressWarnings({ "finally", "unchecked" })
	@RequestMapping("getUserSeat")
	public ResultUtil getUserSeat(Integer userId, Integer isToUserId) {
		Map<String, Object> map = this.cbus.getUserSeatAfterLeaving(userId);
		List<Integer> list = new ArrayList<Integer>();
		list.add((Integer) map.get("seat"));
		list.add(this.dr.getExist(userId, isToUserId.longValue())==1?1:0);
		this.dr.truncRecord(userId);
		try {
			List<ReqRecord> record = (List<ReqRecord>) map.get("requestList");
			Iterator<ReqRecord> it = record.iterator();
			while (it.hasNext()) {
				ReqRecord next = it.next();
				if (userId.equals(next.getReqId())) {
					list.add(1);
					list.add(this.cbum.selectSeatByUserId1(Long.parseLong(next.getIsReqId() + "")));
				}
				if (userId.equals(next.getIsReqId())) {
					list.add(0);
					list.add((Integer) map.get("seat"));
					if (this.rrm.selectByIsReqId1(isToUserId) != null) {
						list.add(2);
					} else {
						list.add(next.getReqId().equals(isToUserId) ? 1 : 0);
					}
				}
			}
		} finally {
			return new ResultUtil(200, "ok", list);
		}
	}

	/**
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping("getUserSeat1")
	public ResultUtil getUserSeat1(Long userId) {
		return new ResultUtil(200, "ok", this.cbus.getUserSeat(userId));
	}

	/**
	 * 退出房间
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping("leaving")
	public ResultUtil leave(Integer userId) {
		return new ResultUtil(200, "ok", (Integer) this.cbus.leaving(userId).get("seat"));
	}

	/**
	 * 更换场景
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping("changing")
	public ResultUtil changingRoom(Integer userId) {
		this.cbus.putSeat(userId);
		if (this.rrm.selectByIsReqId(Long.parseLong(userId + "")) != null) {
			this.rrm.delRecord1(userId);
		}
		return this.cbus.matchingState(userId, 1);
	}

	/**
	 * 依次为请求私奔、接受和拒绝
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("elopeRequest/{token}")
	public ResultUtil enlopeRequest(@PathVariable String token, Integer seat, Integer barId, String message) {
		Users user = this.us.findByToken(token);
		if (null != user) {
			Long idBySeat = this.cbum.selectUserIdBySeat(seat, barId);
			this.fs.enlopeRequest(user, FriendServiceImpl.EnlopeRequest, message,
					this.us.selectByPrimaryKey(idBySeat).getLoginName());
			return new ResultUtil(200, "ok", "");
		}
		return new ResultUtil(500, "error", "未登录！");
	}

	@RequestMapping("elopeHandler/{token}")
	public ResultUtil enlopeAccept(@PathVariable String token, Integer barId, String message, Integer type)
			throws Exception {
		Users user = this.us.findByToken(token);
		if (null != user) {
			List<String> list = new ArrayList<String>();
			List<Integer> seated = this.cbum.selectSeated(barId);
			Integer sss = this.cbum.selectSeatByUserId(user.getId());
			if (seated.contains(sss)) {
				seated.remove(sss);
			}
			for (int i = 0; i < seated.size(); i++) {
				Users u = this.us.selectByPrimaryKey(this.cbum.selectUserIdBySeat(seated.get(i), barId));
				list.add(u.getLoginName());
			}
			String[] s = new String[list.size()];
			String[] array = list.toArray(s);
			if (type == 1) {
				return new ResultUtil(200, "ok",
						(String)this.fs.elopeHandler(user, FriendServiceImpl.EnlopeAccept, message, array).get("channel"));
			} else {
				return new ResultUtil(200, "ok",
						this.fs.elopeHandler(user, FriendServiceImpl.EnlopeReject, message, array));
			}
		}
		return new ResultUtil(500, "error", "未登录！");

	}

	@SuppressWarnings("unused")
	@RequestMapping("broadcastRequest/{token}")
	public ResultUtil enlopeReject(@PathVariable String token, Integer barId, String message, Integer seatId)
			throws Exception {
		Users user = us.findByToken(token); //请求人
		Long userId = this.cbum.selectUserIdBySeat(seatId, barId); //被请求人id
		Integer rr = this.rrm.selectByIsReqId2(userId); //记录
		Integer rr1 = this.rrm.selectByIsReqId2(user.getId());  //记录
		if(rr != 0){
			return new ResultUtil(200, "error1", null);
		}if(rr1 != 0){
			return new ResultUtil(200, "error2", null);
		}
		if (user != null) {
			List<Integer> seated = this.cbum.selectSeated(barId);  //所有座位
			Integer seat = this.cbum.selectSeatByUserId(user.getId()); //请求人座位号
			seated.remove(seat); //除了请求人座位号
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < seated.size(); i++) {
				Users u = this.us.selectByPrimaryKey(this.cbum.selectUserIdBySeat(seated.get(i), barId));
				list.add(u.getLoginName());//所有需要推送人的用户名
			}
			String isreq = this.cbum.selectUserIdBySeat(seatId, barId).toString();  //被请求者id
			if (this.rrm.selectExist(user.getId(), userId) != null) {
				return new ResultUtil(200, "error3", null);
			}
			list.add(isreq);
			String[] s = new String[list.size()];
			String[] array = list.toArray(s);
			ReqRecord reqRecord = new ReqRecord(barId, Integer.parseInt(user.getId() + ""), Integer.parseInt(isreq));
			this.rrm.addRecord(reqRecord);
			List<Integer> enlopeRequest = this.fs.enlopeRequest(user, FriendServiceImpl.EnlopeBroadcast, message,
					array);
			log.info(enlopeRequest.toString() + ">>>>>>>>>>>>>>>>>>");
			Portrait p = new Portrait(seat, this.cbum.selectLabelByUserId(user.getId().toString()), seatId,
					this.cbum.selectIcon1(user.getId()));
			return new ResultUtil(200, "ok", p);
		}
		return new ResultUtil(200, "error4", null); 
	}

	@RequestMapping("chattingRoom/{channel}")
	@ResponseBody
	public ResultUtil getChattingRoom(String token, @PathVariable String channel) {
		Users u = this.us.findByToken(token);
		return new ResultUtil(200, "ok", this.rs.getCouple(u.getId(), channel));
	}

	@RequestMapping("accepted")
	@ResponseBody
	public ResultUtil putChatting(Integer userId) {
		return new ResultUtil(200, "ok", (Integer) this.cbus.accepted(userId).get("seat"));
	}
	
	@RequestMapping("getHeadIcon")
	@ResponseBody
	public ResultUtil getHeadIcon(Integer barId,Integer seatId){
		return new ResultUtil(200,"ok",this.cbus.getHeadIcon(seatId, barId));
	}
	
	@RequestMapping("infocheck/{token}")
	@ResponseBody
	public ResultUtil infoCheck(Integer userId,@PathVariable String token){
		Users user = this.us.findByToken(token);
		Users user1 = this.um.selectByPrimaryKey(userId.longValue());
		Integer id = user.getId().intValue();
		if(this.cbum.selectChecked(userId).getInfoChk()==0&this.cbum.selectChecked(id).getInfoChk()==0){
			this.cbum.infoCheck(userId);
			return new ResultUtil(200,"ok",this.fs.infoCheck(user.getLoginName(),"对方向您发送了查看私人信息访问请求"
					, user1.getLoginName()));
		}else if(this.cbum.selectChecked(userId).getInfoChk()==1
				|this.cbum.selectChecked(id).getInfoChk()==1){
			this.fs.infoCheck1(user, user1);
			 return new ResultUtil(200,"ok",this.cbus.infoFromBoth(user, user1));
		}
		return new ResultUtil(500,"error","有错误！");
	}
	
	@RequestMapping("diss/{token}")
	@ResponseBody
	public ResultUtil dissSomeone(@PathVariable String token,Integer dissId,Integer barId){
		Users u = this.us.findByToken(token);
		Long dissuId = this.cbum.selectUserIdBySeat(dissId, barId);
		Users u1 = this.us.selectByPrimaryKey(dissuId);
		DissRecord drecord = new DissRecord(u.getId(), dissuId.intValue());
		if(this.dr.getExist(dissuId.intValue(), u.getId()).equals(0)){
			if(this.dr.getDissCount(dissuId.intValue())<2){
				this.dr.dissSomeone(drecord);
			}else{
				this.dr.dissSomeone(drecord);
				this.fs.dissPeople(u, u1);
			}
		}else{
//			Integer d1 = this.dr.getDissIdByUserId(u.getId());
			this.dr.changeDissId(drecord);
		}
		return new ResultUtil(200,"ok",this.cbum.selectSeatByUserId(dissuId));
	}

	@RequestMapping("barrage")
	public ResultUtil Barrage(Integer userId,String message,Integer barId){
		List<Integer> seated = this.cbum.selectSeated(barId);
		Integer seat = this.cbum.selectSeatByUserId(userId.longValue());
		seated.remove(seat);
		List<String> list = new ArrayList<>();
		if(seated.size()==0){
			return new ResultUtil(200,"ok",this.cbum.selectIcon3(userId));
		}
		for (int i = 0; i < seated.size(); i++) {
			Users u = this.us.selectByPrimaryKey(this.cbum.selectUserIdBySeat(seated.get(i), barId));
			list.add(u.getLoginName());
		}
		String[] s = new String[list.size()];
		String[] array = list.toArray(s);
		this.fs.barrage(this.us.selectByPrimaryKey(userId.longValue()).getLoginName()
				,message,this.cbum.selectIcon3(userId),FriendServiceImpl.BARRAGE,array);
		return new ResultUtil(200,"ok",this.cbum.selectIcon3(userId));
	}

	@RequestMapping("bestow/{token}")
	public ResultUtil Bestow(@PathVariable String token,Integer barId,Integer giftId,Integer toSeat){
		Users user = this.us.findByToken(token);
		Integer userId =user.getId().intValue();
		List<Integer> seated = this.cbum.selectSeated(barId);
		Integer seat = this.cbum.selectSeatByUserId(userId.longValue());
		seated.remove(seat);
		List<String> list = new ArrayList<>();
		for (int i = 0; i < seated.size(); i++) {
			Users u = this.us.selectByPrimaryKey(this.cbum.selectUserIdBySeat(seated.get(i), barId));
			list.add(u.getLoginName());
		}
		String[] s = new String[list.size()];
		String[] array = list.toArray(s);
		Integer code = this.fs.giftUsers(token, giftId, this.cbum.selectUserIdBySeat(toSeat, barId), array);
		String ss = this.cbum.selectLabelByUserId(user.getId().toString())+"给"+
				this.cbum.selectLabelByUserId(this.cbum.selectUserIdBySeat(toSeat, barId).toString())+"赠送了"+this.gm.getName(giftId);
		if(code.equals(3)){
			return new ResultUtil(200,"ok",new BestowUtil(ss,
					this.cbum.selectSeatByUserId(user.getId()),toSeat,this.gm.getGiftImg(giftId),code));
		}else{
			return new ResultUtil(500,"error",new BestowUtil(code));
		}

	}

	@RequestMapping("getHeadIcon1")
	public ResultUtil getHeadIcon1(Integer userId,Integer type){
		Map<String, Object> map = this.cbus.getUserSeatAfterLeaving(userId);
		Map<String, Object> map1 = this.cbus.WeggenNachrichten(userId);
		if(type.equals(1)) {
			return new ResultUtil(200, "ok",  map.get("label") + "离开了"
					+  map.get("channelName"));
		}else {
			return new ResultUtil(200, "ok",  map1.get("label") + "离开了"
					+  map1.get("channelName"));
		}
	}
}
