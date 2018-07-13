package com.worldkey.search;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.InformationAll;
import com.worldkey.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author wu
 */
@Controller
@Slf4j
@RequestMapping("search")
public class SearchController {
    @Resource
    private SearchService searchService;

    /**
     * 通过关键字进行搜索
     *
     * @param pageNum  页数
     * @param pageSize 每页数量
     * @param keyWords 关键字
     * @return 返回
     * @throws Exception 异常
     */
    @RequestMapping("find")
    @ResponseBody
    public ResultUtil search(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                             String keyWords) throws Exception {
        PageInfo<InformationAll> list = this.searchService.findByKeyWords(pageNum, pageSize, keyWords);
        return new ResultUtil(200, "ok", list);
    }
}
