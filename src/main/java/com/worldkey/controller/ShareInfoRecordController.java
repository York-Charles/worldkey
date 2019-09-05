package com.worldkey.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.BrowseAddJd;
import com.worldkey.entity.JdDetail;
import com.worldkey.entity.PraiseApp;
import com.worldkey.entity.ShareInfoRecord;
import com.worldkey.entity.ShareInfoRecordApp;
import com.worldkey.entity.Users;
import com.worldkey.mapper.BrowseAddJdMapper;
import com.worldkey.mapper.ShareInfoRecordMapper;
import com.worldkey.service.ShareInfoRecordService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wu
 */
@Slf4j
@RestController
@RequestMapping("shareInfo")
public class ShareInfoRecordController {
    @Resource
    private ShareInfoRecordService shareInfoRecordService;
    @Resource
    private BrowseAddJdMapper browseAddJdMapper;
    @Resource
    private UsersService usersService;
    @Resource
    private ShareInfoRecordMapper shareInfoRecordMapper;

    /**
     * 分享获得金豆接口
     */
    @RequestMapping("record/{token}")
    public ResultUtil shareInfoRecord(@PathVariable String token, Long informationId) {
        Integer i = this.shareInfoRecordService.shareInfo(token, informationId);
        if (i == 0) {
            return new ResultUtil(200, "ok", "users null");
        } else if (i == 1) {
        	
            return new ResultUtil(200, "ok", "getJd success");
        }
        return new ResultUtil(200, "ok", "getJd fail");
    }

    /**
     * 浏览，分享转发，发布，每日次数查询
     */
    @RequestMapping("getNumAboutInfo/{token}")
    public ResultUtil browseNum(@PathVariable String token) {
        //用户不能为空
        Users users = this.usersService.findByToken(token);
        if (users == null) {
            return new ResultUtil(200, "ok", "token error");
        }
        List allNum = new ArrayList();
        //浏览文章次数
        List<BrowseAddJd> browseAddJds = browseAddJdMapper.findByUid(users.getId());
        allNum.add(0, browseAddJds.size());

        //分享文章次数
        List<ShareInfoRecord> shareInfoRecords = shareInfoRecordMapper.findByuId(users.getId());
        allNum.add(1, shareInfoRecords.size());

        //发布文章次数
        List<JdDetail> jdDetails = shareInfoRecordMapper.findByuIdANDTime(users.getId());
        allNum.add(2, jdDetails.size());

        return new ResultUtil(200, "ok", allNum);
    }
    
    
	/**5.19
     * 对应ID查找所有文章进行的操作
     */
	@RequestMapping("find")
	public @ResponseBody ResultUtil find(Long userId
			,@RequestParam(defaultValue = "1") Integer page
			,@RequestParam(defaultValue = "10") Integer pageSize) {
		List<ShareInfoRecordApp> shows = this.shareInfoRecordService.share(userId);
		PageHelper.startPage(page, pageSize, true);
		PageInfo<ShareInfoRecordApp> pageInfo = new PageInfo<>(shows);
		return new ResultUtil(200, "ok", pageInfo);
	}
}
