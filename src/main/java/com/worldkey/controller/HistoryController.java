package com.worldkey.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.worldkey.entity.CommentApp;
import com.worldkey.entity.GiftRecordApp;
import com.worldkey.entity.History;
import com.worldkey.entity.PraiseApp;
import com.worldkey.entity.ShareInfoRecordApp;
import com.worldkey.entity.Users;
import com.worldkey.service.CommentService;
import com.worldkey.service.GiftRecordService;
import com.worldkey.service.HistoryService;
import com.worldkey.service.PraiseService;
import com.worldkey.service.ShareInfoRecordService;
import com.worldkey.service.UsersService;
import com.worldkey.util.Pager;
import com.worldkey.util.Pager1;
import com.worldkey.util.ResultUtil;

@Controller
@ResponseBody
public class HistoryController {
	@Resource
	HistoryService hService;
	@Resource
	UsersService usersService;
	@Resource
	GiftRecordService giftRecordService;
	@Resource
	PraiseService praiseService;
	@Resource
	ShareInfoRecordService shareInfoRecordService;
	@Resource
	CommentService commentService;

	@RequestMapping(value = "history/{token}", method = RequestMethod.GET)
	public ResultUtil pushHistory(@RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize, @PathVariable String token) {

		Users user = this.usersService.findByToken(token);
		Long userId = user.getId();
		 List<GiftRecordApp> giftRecord = this.giftRecordService.gift(userId);
		List<PraiseApp> praise = this.praiseService.praise(userId);
		List<ShareInfoRecordApp> shareInfoRecord = this.shareInfoRecordService.share(userId);
		List<CommentApp> comment = this.commentService.comment(userId);
		List<History> list = this.hService.selectHistory(userId);

		 for (int i = 0; i < giftRecord.size(); i++) {
		 History p = new History();
		 p.setCreateTime(giftRecord.get(i).getCreateTime());
		 p.setUserId(giftRecord.get(i).getUserId());
		 p.setGiftName(giftRecord.get(i).getGiftName());
		 p.setPetName(giftRecord.get(i).getPetName());
		 p.setLoginName(giftRecord.get(i).getLoginName());
		 p.setInformation(giftRecord.get(i).getToInformation());
		 p.setClassify(2);
		 list.add(p);
		 }
		for (int i = 0; i < praise.size(); i++) {
			History p = new History();
			p.setCreateTime(praise.get(i).getCreateTime());
			p.setUserId(praise.get(i).getUserId());
			p.setPetName(praise.get(i).getPetName());
			p.setLoginName(praise.get(i).getLoginName());
			p.setHeadImg(praise.get(i).getHeadImg());
			p.setInformation(Long.parseLong(praise.get(i).getInformation() + ""));
			p.setTitle(praise.get(i).getTitle());
			p.setTitleImg(praise.get(i).getTitleImg());
			p.setWebUrl(praise.get(i).getWebUrl());
			p.setClassify(0);
			list.add(p);
		}
		for (int i = 0; i < shareInfoRecord.size(); i++) {
			History p = new History();
			p.setCreateTime(shareInfoRecord.get(i).getCreateTime());
			p.setInformation(shareInfoRecord.get(i).getInformation());
			p.setUserId(shareInfoRecord.get(i).getUserId());
			p.setPetName(shareInfoRecord.get(i).getPetName());
			p.setLoginName(shareInfoRecord.get(i).getLoginName());
			p.setHeadImg(shareInfoRecord.get(i).getHeadImg());
			p.setTitle(shareInfoRecord.get(i).getTitle());
			p.setTitleImg(shareInfoRecord.get(i).getTitleImg());
			p.setWebUrl(shareInfoRecord.get(i).getWebUrl());
			p.setClassify(3);
			list.add(p);
		}
		for (int i = 0; i < comment.size(); i++) {
			History p = new History();
			p.setCreateTime(comment.get(i).getGmtCreate());
			p.setUserId(comment.get(i).getUserId());
			p.setPetName(comment.get(i).getPetName());
			p.setLoginName(comment.get(i).getLoginName());
			p.setInformation(comment.get(i).getInformation());
			p.setHeadImg(comment.get(i).getHeadImg());
			p.setInfo(comment.get(i).getInfo());
			p.setTitle(comment.get(i).getTitle());
			p.setTitleImg(comment.get(i).getTitleImg());
			p.setWebUrl(comment.get(i).getWebUrl());
			p.setClassify(1);
			list.add(p);
		}
		for (int i = 0; i < list.size(); i++) {
			for (int j = i; j < list.size(); j++) {
				History temp = list.get(i);
				if (list.get(i).getCreateTime().getTime() < list.get(j).getCreateTime().getTime()) {
					list.set(i, list.get(j));
					list.set(j, temp);
				}
			}
		}
		int pageCount = 0;
		if(list.size()%pageSize==0){
			pageCount=list.size()/pageSize;
		}else{
			pageCount=list.size()/pageSize+1;//

		}
		List<History> listson = new ArrayList<History>();
		if (pageNum < pageCount) {
			for (int i = 0; i < pageSize; i++) {
				listson.add(list.get(i + pageSize * (pageNum - 1)));
			}
			Pager1 p = new Pager1(pageNum, pageSize, list.size(), listson);
			return new ResultUtil(200, "OK ", p);
		} else {
			for (int i = 0; i < list.size() % pageSize; i++) {
				listson.add(list.get(i + pageSize * (pageNum - 1)));
			}
			Pager1 p = new Pager1(pageNum, list.size() % pageSize, list.size(), listson);
			return new ResultUtil(200, "OK ", p);
		}

	}
}

