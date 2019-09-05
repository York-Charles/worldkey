package com.worldkey.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.worldkey.config.SystemProperties;
import com.worldkey.entity.BrowseAddJd;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.JdDetail;
import com.worldkey.entity.Praise;
import com.worldkey.entity.PraiseApp;
import com.worldkey.entity.ShareInfoRecord;
import com.worldkey.entity.ShareInfoRecordApp;
import com.worldkey.entity.Users;
import com.worldkey.enumpackage.JdRewardType;
import com.worldkey.jdpush.Jdpush;
import com.worldkey.mapper.BrowseAddJdMapper;
import com.worldkey.mapper.InformationAllMapper;
import com.worldkey.mapper.ShareInfoRecordMapper;
import com.worldkey.mapper.UsersMapper;
import com.worldkey.service.InformationAllService;
import com.worldkey.service.JdDetailService;
import com.worldkey.service.ShareInfoRecordService;
import com.worldkey.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author wu
 */
@Slf4j
@Service
public class ShareInfoRecordServiceImpl extends ServiceImpl<ShareInfoRecordMapper, ShareInfoRecord>
		implements ShareInfoRecordService {
	@Resource
	private UsersService usersService;
	@Resource
	private JdDetailService jdDetailService;
	@Resource
	private SystemProperties systemProperties;
	@Resource
	private ShareInfoRecordMapper shareInfoRecordMapper;
	@Resource
	private BrowseAddJdMapper browseAddJdMapper;
	@Resource
	private InformationAllMapper informationAllMapper;
	@Resource
	private InformationAllService informationAllService;
	@Resource
	private UsersMapper uMapper;

	/**
	 * 用户分享，转发文章获得金豆
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer shareInfo(String token, Long informationId) {
		// 验证用户是否存在
		Users users = usersService.findByToken(token);
		// 4.29 推送相关
		InformationAll informationAll = this.informationAllService.info(informationId);
		String s = informationAll.getUsers().getLoginName();
		Long a1 = informationAll.getUsers().getId();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("img", users.getHeadImg());
		map.put("Mid", Long.toString(users.getId()));
		map.put("name", users.getPetName());
		map.put("infoImg", informationAll.getTitleImg());
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("MM月dd日 HH:mm");
		String date = df.format(day);
		map.put("date", date);
		map.put("webUrl", informationAll.getWeburl());
		map.put("id", Long.toString(informationAll.getId()));
		map.put("fenlei", "3");
		map.put("title", informationAll.getTitle());
		map.put("abstracte", informationAll.getAbstracte());
		if (users == null) {
			return 0;
		}
		// 4.29转发推送
		if (!(users.getId().equals(a1))) {
			Jdpush.jpushAndriod3(users.getPetName(), s, informationAll.getTitle(), map);
		}
		List<ShareInfoRecord> siRecord = this.shareInfoRecordMapper.findByUidANDInfoId(users.getId(), informationId);
		if (siRecord.size() < 1) {

			ShareInfoRecord shareInfoRecord = new ShareInfoRecord();
			shareInfoRecord.setUsers(users.getId());
			shareInfoRecord.setCreateTime(new Date());

			shareInfoRecord.setInformation(informationId);
			this.shareInfoRecordMapper.insert(shareInfoRecord);

			List<ShareInfoRecord> siRecords = shareInfoRecordMapper.findByuId(users.getId());
			if (siRecords.size() <= 5) {

				// 转发文章后，用户金豆增加
				usersService.addJd(systemProperties.getTranspondJd(), users.getId());
				// 金豆明细表
				JdDetail jdDetailIn = new JdDetail(new Date(), systemProperties.getTranspondJd(),
						JdRewardType.shareInfo.getIndex(), users.getId());
				jdDetailIn.setMsg("分享获得" + systemProperties.getTranspondJd() + "金豆");
				jdDetailService.insert(jdDetailIn);
				return 1;
			}
		}
		return 2;
	}

	/**
	 * 用户浏览文章获得金豆，接口在informationAll-获取详情中。 同一用户每天浏览同一文章，只加一次金豆，浏览不同文章，加金豆上限3次
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer browseAddJd(String token, Long informationId) {

		Users users = usersService.findByToken(token);
		if (users == null) {
			return 0;
		}

		List<BrowseAddJd> browseAddJd1 = this.browseAddJdMapper.findByUidAndInfoId(users.getId(), informationId);
		// 控制同一用户，同一文章不能因为重复浏览而再获得金豆
		if (browseAddJd1.size() < 1) {
			BrowseAddJd browseAddJd = new BrowseAddJd();
			browseAddJd.setUsers(users.getId());
			browseAddJd.setCreateTime(new Date());
			browseAddJd.setInformation(informationId);
			this.browseAddJdMapper.insert(browseAddJd);
			List<BrowseAddJd> browseAddJds = this.browseAddJdMapper.findByUid(users.getId());
			if (browseAddJds.size() <= 3) {
				// 用户金豆增加
				usersService.addJd(systemProperties.getBrowseJd(), users.getId());
				// 金豆明细表增加
				JdDetail jdDetailIn = new JdDetail(new Date(), systemProperties.getBrowseJd(),
						JdRewardType.browseAddJd.getIndex(), users.getId());
				jdDetailIn.setMsg("浏览获得" + systemProperties.getBrowseJd() + "金豆");
				jdDetailService.insert(jdDetailIn);
				return 1;
			}
		}
		return 2;
	}

	@Override
	public List<ShareInfoRecordApp> share(Long userId) {
		List<Long> chooseUsers = this.shareInfoRecordMapper.chooseUsers(userId);
		List<ShareInfoRecord> share = this.shareInfoRecordMapper.Share(userId);
		List<ShareInfoRecordApp> shareApp1 = new ArrayList<ShareInfoRecordApp>();
		List<ShareInfoRecordApp> shareApp2 = new ArrayList<ShareInfoRecordApp>();
		List<ShareInfoRecordApp> shareApp = new ArrayList<ShareInfoRecordApp>();
		for (int i = 0; i < share.size(); i++) {
			ShareInfoRecordApp s = new ShareInfoRecordApp();
			s.setInformation(share.get(i).getInformation());
			s.setCreateTime(share.get(i).getCreateTime());
			Long id = share.get(i).getInformation();
			InformationAll a = this.informationAllMapper.selectinfo(id);
			s.setTitle(a.getTitle());
			s.setTitleImg(a.getTitleImg());
			s.setWebUrl(a.getWeburl());
			shareApp1.add(s);
		}
		for (int i = 0; i < chooseUsers.size(); i++) {
			ShareInfoRecordApp s = new ShareInfoRecordApp();
			s.setUserId(chooseUsers.get(i));
			Long id = chooseUsers.get(i);
			Users u = this.uMapper.selectPetNameById(id);
			s.setPetName(u.getPetName());
			s.setLoginName(u.getLoginName());
			s.setHeadImg(u.getHeadImg());
			shareApp2.add(s);
		}
		for (int i = 0; i < shareApp1.size(); i++) {
			ShareInfoRecordApp s1 = shareApp1.get(i);
			ShareInfoRecordApp s2 = shareApp2.get(i);
			ShareInfoRecordApp s = new ShareInfoRecordApp();
			s.setUserId(s2.getUserId());
			s.setPetName(s2.getPetName());
			s.setLoginName(s2.getLoginName());
			s.setHeadImg(s2.getHeadImg());
			s.setTitle(s1.getTitle());
			s.setTitleImg(s1.getTitleImg());
			s.setWebUrl(s1.getWebUrl());
			s.setInformation(s1.getInformation());
			s.setCreateTime(s1.getCreateTime());
			shareApp.add(s);
		}
		return shareApp;
	}

}
