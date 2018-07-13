package com.worldkey.service;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.SensitiveWord;

public interface SensitiveWordService {

    /**
     * 获取全部敏感词和提顿符
     *
     */
    PageInfo<SensitiveWord> search(Integer pageNum, Integer pageSize, String search);

    /**
     * 删除敏感词或停顿符，以ID为区别码
     */
    Integer del(Integer id);


    /**
     * 新增敏感词
     */
    Integer insertWord(SensitiveWord word);

    /**
     * 新增停顿符
     */
    Integer insertStopWord(SensitiveWord word);


}
