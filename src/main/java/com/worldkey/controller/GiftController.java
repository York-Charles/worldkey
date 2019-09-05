package com.worldkey.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Gift;
import com.worldkey.entity.GiftRecord;
import com.worldkey.entity.GiftRecordApp;
import com.worldkey.mapper.GiftMapper;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.JdDetail;
import com.worldkey.entity.KbDetail;
import com.worldkey.entity.PraiseApp;
import com.worldkey.entity.Users;
import com.worldkey.entity.ZsDetail;
import com.worldkey.enumpackage.JdRewardType;
import com.worldkey.enumpackage.KbObtainType;
import com.worldkey.enumpackage.ZsObtainType;
import com.worldkey.jdpush.Jdpush;
import com.worldkey.service.GiftRecordService;
import com.worldkey.service.GiftService;
import com.worldkey.util.ResultUtil;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


import java.util.List;


@RestController
@RequestMapping("gift")
public class GiftController {
    @Resource
    private GiftService giftService;
    @Resource
    private GiftRecordService service;

    @RequestMapping("all")
    public ResultUtil list() {
        List<Gift> list = giftService.selectList(new EntityWrapper<>());
        return new ResultUtil(200, "ok", list);
    }
    @RequestMapping("getUrl")
    public ResultUtil getUrl() {
        List<Gift> list = giftService.getUrl();
        return new ResultUtil(200, "ok", list);
    }


	/**5.19
     * 对应ID查找所有文章进行的操作
     */
	@RequestMapping("find")
	public @ResponseBody ResultUtil find(Long userId
			,@RequestParam(defaultValue = "1") Integer page
			,@RequestParam(defaultValue = "10") Integer pageSize) {
		List<GiftRecordApp> shows = this.service.gift(userId);
		PageHelper.startPage(page, pageSize, true);
		PageInfo<GiftRecordApp> pageInfo = new PageInfo<>(shows);
		return new ResultUtil(200, "ok", pageInfo);
	}
	@RequestMapping("findNum")
	public ResultUtil findnum(Long userId){
		return new ResultUtil(200,"ok",this.service.findNum(userId));		
	}
	
	@RequestMapping("giftusers/{token}")
	public ResultUtil giftUsers(@PathVariable String token,Integer giftId,Long userId){	
			return new ResultUtil(200,"ok",this.giftService.giftUsers(token, giftId, userId));
	}

}
