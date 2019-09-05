package com.worldkey.service;

import com.worldkey.entity.OnChatting;

public interface ReqRecordService {
	
	OnChatting getCouple(Long userId,String channel);

}
