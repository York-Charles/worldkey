package com.worldkey.controller;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.SensitiveWord;
import com.worldkey.service.SensitiveWordService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping("sensitiveWord")
public class SensitiveWordController {

    private static Logger log= LoggerFactory.getLogger(SensitiveWordController.class);

    @Resource
    private SensitiveWordService sensitiveWordService;

    @GetMapping({"showWord", "search"})
    @RequiresRoles("sensitiveWord")
    public ModelAndView showAll(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "20") Integer pageSize,
                                @RequestParam(defaultValue = "") String search) {
        ModelAndView modelAndView = new ModelAndView("sensitiveWord/list");
        PageInfo<SensitiveWord> allWord = sensitiveWordService.search(pageNum, pageSize, search);
        modelAndView.addObject("words", allWord);
        modelAndView.addObject("search", search);
        return modelAndView;
    }

    @PostMapping("add")
    @RequiresRoles("sensitiveWord")
    public String add(@Validated SensitiveWord word) {
        Boolean isWord = word.getIsWord();
        log.error("isWord:{}",isWord);
        if (isWord) {
            sensitiveWordService.insertWord(word);
        } else {
            sensitiveWordService.insertStopWord(word);
        }
        return "redirect:/sensitiveWord/showWord";
    }

    @PostMapping("del")
    @ResponseBody
    @RequiresRoles("sensitiveWord")
    public Integer del(Integer id) {
        return sensitiveWordService.del(id);
    }

}
