package com.worldkey.service;

import java.util.List;
import java.util.Map;

import com.worldkey.entity.CoffeeBarUser;
import com.worldkey.entity.Users;
import com.worldkey.util.PrivateRequest;
import com.worldkey.util.ResultUtil;

public interface CoffeeBarUserService {
	
	ResultUtil matchingState(Integer userId,Integer state);
	Integer matchingState1(Integer userId,Integer state);
	Map<String,Object> putSeat(Integer userId);
	
	Map<String,Object> leaving(Integer userId);
	
	Map<String,Object> accepted(Integer userId);
	
	List<Integer> getUsersByBarIdAndRoomId(Integer barId,Integer sceneId);
	
	CoffeeBarUser getByUserId(Integer userId);
	
	Map<String,Object> getUserSeatAfterLeaving(Integer userId);
	
	Integer getUserSeat(Long userId);
	
	String getHeadIcon(Integer seatId,Integer barId);
	
	PrivateRequest infoFromBoth(Users user,Users user1);
}
