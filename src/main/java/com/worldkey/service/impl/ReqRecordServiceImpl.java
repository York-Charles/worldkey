package com.worldkey.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.worldkey.entity.OnChatting;
import com.worldkey.entity.ReqRecord;
import com.worldkey.mapper.CoffeeBarUserMapper;
import com.worldkey.mapper.ReqRecordMapper;
import com.worldkey.service.ReqRecordService;

@Service
public class ReqRecordServiceImpl implements ReqRecordService {
	
	@Resource
	private ReqRecordMapper rrm;
	@Resource
	private CoffeeBarUserMapper cbum;

	@Override
	public OnChatting getCouple(Long userId,String channel) {
		Integer id = Integer.parseInt(userId+"");
		ReqRecord rr = this.rrm.SelectByChannel((Long.parseLong(channel)/1000)+"");
		OnChatting oc = new OnChatting();
		oc.setUserId1(id);
		String reqIcon = this.cbum.selectIcon2(rr.getReqId());
		String isreqIcon = this.cbum.selectIcon2(rr.getIsReqId());
		if(id.equals(rr.getReqId())){
			oc.setLabel1(reqIcon);
			oc.setLabel2(isreqIcon);
			oc.setUserId2(rr.getIsReqId());
		}else{
			oc.setLabel1(isreqIcon);
			oc.setLabel2(reqIcon);
			oc.setUserId2(rr.getReqId());
		}
		return oc;
	}

}
