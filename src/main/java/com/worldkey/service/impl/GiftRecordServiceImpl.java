package com.worldkey.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.worldkey.entity.Gift;
import com.worldkey.entity.GiftRecord;
import com.worldkey.entity.GiftRecordApp;
import com.worldkey.entity.GiftShow;
import com.worldkey.entity.PraiseApp;
import com.worldkey.entity.Users;
import com.worldkey.mapper.GiftMapper;
import com.worldkey.mapper.GiftRecordMapper;
import com.worldkey.mapper.GiftUsersMapper;
import com.worldkey.mapper.UsersMapper;
import com.worldkey.service.GiftRecordService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GiftRecordServiceImpl extends ServiceImpl<GiftRecordMapper, GiftRecord> implements GiftRecordService {
	@Resource
	private GiftRecordMapper giftRecordMapper;

	@Resource
	GiftMapper giftMapper;
	@Resource
	private UsersMapper uMapper;
	@Resource
	private GiftUsersMapper giftUsersMapper;
	@Resource
	private GiftMapper giftsMapper;

	@Override
	public List<Map<String, Object>> findGiftRecordByToInformation(Long id) {
		return giftRecordMapper.selectGiftRecordByToInformation(id);
	}

	@Override
	public List<GiftRecordApp> gift(Long userId) {
		List<Long> chooseUsers = this.giftRecordMapper.chooseUsers(userId);
		List<GiftRecord> GiftRecord = this.giftRecordMapper.Gift(userId);
		List<GiftRecordApp> GiftRecordApp1 = new ArrayList<GiftRecordApp>();
		List<GiftRecordApp> GiftRecordApp2 = new ArrayList<GiftRecordApp>();
		List<GiftRecordApp> GiftRecordApp = new ArrayList<GiftRecordApp>();
		for (int i = 0; i < GiftRecord.size(); i++) {
			GiftRecordApp g = new GiftRecordApp();
			g.setToInformation(GiftRecord.get(i).getToInformation());
			g.setCreateTime(GiftRecord.get(i).getCreateTime());
			g.setGiftName(this.giftMapper.GiftName(GiftRecord.get(i).getGiftId()));
			GiftRecordApp1.add(g);
		}
		for (int i = 0; i < chooseUsers.size(); i++) {
			GiftRecordApp g = new GiftRecordApp();
			g.setUserId(chooseUsers.get(i));
			Long id = chooseUsers.get(i);
			Users u = this.uMapper.selectPetNameById(id);
			g.setPetName(u.getPetName());
			g.setLoginName(u.getLoginName());
			GiftRecordApp2.add(g);
		}
		for (int i = 0; i < GiftRecordApp1.size(); i++) {
			GiftRecordApp g1 = GiftRecordApp1.get(i);
			GiftRecordApp g2 = GiftRecordApp2.get(i);
			GiftRecordApp g = new GiftRecordApp();
			g.setUserId(g2.getUserId());
			g.setPetName(g2.getPetName());
			g.setLoginName(g2.getLoginName());
			g.setToInformation(g1.getToInformation());
			g.setCreateTime(g1.getCreateTime());
			g.setGiftName(g1.getGiftName());
			GiftRecordApp.add(g);
		}
		return GiftRecordApp;
	}

	@Override
	public List<Map<String,Object>> findNum(Long userId) {
		List<GiftShow> findNum = this.giftRecordMapper.findNum(userId);
		List<GiftShow> findNumByUser = this.giftUsersMapper.findNumByUser(userId);
		Integer total = 0;
		if (findNum != null) {
			for (GiftShow g1 : findNum) {
				for (GiftShow g2 : findNumByUser) {
					if (g1.getGiftId() == g2.getGiftId()) {
						g1.setGiftNum(g1.getGiftNum() + g2.getGiftNum());
						findNumByUser.remove(g2);
						break;
					}
				}
			}
		}
		for (GiftShow g2 : findNumByUser) {
			findNum.add(g2);
		}
		findNum.sort(new Comparator<GiftShow>() {

			@Override
			public int compare(GiftShow o1, GiftShow o2) {
				// TODO Auto-generated method stub
				int sort = 0;
				int a = o1.getZs() - o2.getZs();
				if (a != 0) {
					sort = (a < 0) ? 1 : -1;
				} else {
					a = o1.getJd() - o2.getJd();
					if (a != 0) {
						sort = (a < 0) ? 1 : -1;
					}
				}
				return sort;
			}

		});
		for(GiftShow g1 : findNum){
			total += g1.getGiftNum();
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("findNum", findNum);
		map.put("total", total);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		return list;
	}
}
