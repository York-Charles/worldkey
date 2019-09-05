package com.worldkey.scheduled;

import com.worldkey.mapper.BrowseAddJdMapper;
import com.worldkey.mapper.ShareInfoRecordMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wu
 */
@Component
public class ShareInfoRecordScheduler {
    @Resource
    private ShareInfoRecordMapper shareInfoRecordMapper;
    @Resource
    private BrowseAddJdMapper browseAddJdMapper;

    /**
     * 分享文章记录每日删除
     * 23:59:59
     */
    @Scheduled(cron = "59 59 23 * * ? ")
    public void deleteRecord() {
        shareInfoRecordMapper.deleteRecord();

    }

    /**
     * 浏览获得金豆，每日删除
     * 23:59:59
     */
    @Scheduled(cron = "59 59 23 * * ? ")
    public void deleteBrowseRecord() {
        browseAddJdMapper.deleteBrowseRecord();
    }

}
