package com.worldkey.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.worldkey.entity.PraiseApp;
import com.worldkey.entity.ShareInfoRecord;
import com.worldkey.entity.ShareInfoRecordApp;

/**
 * @author wu
 */

public interface ShareInfoRecordService extends IService<ShareInfoRecord> {
    /**
     * 分享文章记录
     */
    Integer shareInfo(String token, Long informationId);

    /**
     * 浏览记录
     */
    Integer browseAddJd(String token, Long informationId);
    
  //5.18根据ID查找所有文章的所有操作
    List<ShareInfoRecordApp> share(Long userId);
}
