package com.worldkey.controller;

import com.worldkey.entity.SystemConfig;
import com.worldkey.service.SystemConfigService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author HP
 */
@Controller
@RequestMapping("system")
public class SystemConfigController {
    @Resource
    private SystemConfigService systemConfigService;

    @RequiresRoles(value = "administrator")
    @RequestMapping("update")
    public ModelAndView update(@Validated SystemConfig sc, BindingResult result, ModelAndView model, MultipartFile file,
                               @RequestHeader("host") String host) throws IOException {

        if (sc == null || sc.getFilesrc() == null) {
            model.addObject("system", systemConfigService.find());
            model.setViewName("system/system");
            return model;
        }

        if (result.hasErrors()) {
            model.setViewName("system/system");
            return model;
        } else {
            systemConfigService.update(sc, file, host);
            model.setViewName("redirect:/manage");
            return model;
        }
    }
    @RequestMapping("tongZhi")
    public String tongZhi(){
        return "system/tongZhi";
    }
    

}
