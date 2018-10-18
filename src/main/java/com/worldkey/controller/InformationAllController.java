package com.worldkey.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.BaseShow;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.Praise;
import com.worldkey.entity.ThreeType;
import com.worldkey.entity.TwoType;
import com.worldkey.entity.Users;
import com.worldkey.mapper.InformationAllMapper;
import com.worldkey.service.*;
import com.worldkey.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("informationall")
@Slf4j
public class InformationAllController {

    @Resource
    private InformationAllService allService;
    @Resource
    private OneTypeService oneTypeService;
    @Resource
    private TwoTypeService twoTypeService;
    @Resource
    private ThreeTypeService threeTypeService;
    @Resource
    private UsersService usersService;
    @Resource
    private BrowsingHistoryService browsingHistoryService;
    @Resource
    private ShareInfoRecordService shareInfoRecordService;
    @Resource
    private InformationAllMapper informationAllMapper;


    @RequestMapping(value = "ajaxUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Object ajaxUpdate(@RequestHeader("host") String host, String info, String title, String abstracte,
                             Long id, String titleImg, MultipartFile file, String oldTitleImg) {
        String result = "no";
        InformationAll informationAll = new InformationAll();
        if (id == null) {
            return null;
        }
        informationAll.setId(id);
        if (title != null) {
            informationAll.setTitle(title);
            InformationAll update = this.allService.update(informationAll, host, file, oldTitleImg);
            result = update.getTitle();

        }
        if (abstracte != null) {
            informationAll.setAbstracte(abstracte);
            InformationAll update = this.allService.update(informationAll, host, file, oldTitleImg);
            result = update.getAbstracte();
        }
        if (info != null) {
            //更新详情
            informationAll.setInfo(info);
            //更新标题图
            informationAll.setTitleImg(titleImg);
            InformationAll update = this.allService.update(informationAll, host, file, oldTitleImg);
            result = update.getInfo();
        }
        return result;
    }

    /*2018.4.19改为下方的changeThreeType方法
    @RequestMapping("changeTwoType")
    @RequiresRoles("informationall")
    public @ResponseBody
    ResultUtil changeTwoType(Integer changeId, Integer changeTwoType) {
        String typeName = this.allService.changeTwoType(changeId, changeTwoType);
        log.debug("changeTwoType:{};changeId:{}", changeTwoType, changeId);
        log.debug("typeName:{}", typeName);
        return new ResultUtil(200, "ok", typeName);
    }
     */
    @RequestMapping("changeThreeType")
    @RequiresRoles("informationall")
    public @ResponseBody
    ResultUtil changeThreeType(Integer changeId, Integer changeThreeType) {
        String typeName = this.allService.changeThreeType(changeId, changeThreeType);
        return new ResultUtil(200, "ok", typeName);
        }
    
    
    
    
    
    @RequestMapping("tuijian")
    @ResponseBody
    public ResultUtil tuijian(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                              @NotNull String token) throws Exception {
        Users user = usersService.findByToken(token);
        ResultUtil result = new ResultUtil();
        if (user == null) {
            result.setCode(406);
            result.setMsg("请先登录");
        } else {
            result.setCode(200);
            result.setMsg("ok");
            result.setResult(browsingHistoryService.tuijian(user.getId(), pageNum, pageSize));
        }
        return result;
    }


    /**
     * 审核记录的合法性
     *
     * @param checked 0为未审核，1为以审核，2为草稿，3未已下架
     * @param id      要审核的ID
     */

    @RequestMapping("checked")
    @ResponseBody
    @RequiresRoles("informationall")
    public ResultUtil checked(@NotNull Integer checked, @NotNull Long id) {
        InformationAll vo = new InformationAll();
        vo.setChecked(checked);
        vo.setId(id);
        return new ResultUtil(200, "ok", this.allService.checked(vo));

    }
    
    /**
     * bug反馈状态solve
     */

    @RequestMapping("solve")
    @ResponseBody
    @RequiresRoles("informationall")
    public ResultUtil solve(@NotNull Integer solve, @NotNull Long id) {
        InformationAll vo = new InformationAll();
        vo.setSolve(solve);
        vo.setId(id);
        return new ResultUtil(200, "ok", this.allService.solve(vo));

    }

    /**
     * 以点击量排序，获取全部
     */
    @RequestMapping("find")
    @ResponseBody
    public ResultUtil findAll(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {
        return new ResultUtil(200, "ok", this.allService.findAll(pageNum, pageSize));
    }
    
  

    /**
     * 以一级分类为区别查找
     */
    @RequestMapping("find/onetype/{type}")
    @ResponseBody
    public ResultUtil findByOneType(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize,
                                    @PathVariable Integer type) {
        InformationAll vo = new InformationAll();
        vo.setType(type);
        //checked=1 已审核
        vo.setChecked(1);
        PageInfo<InformationAll> info = this.allService.findByOneType(pageNum, pageSize, vo);
        return new ResultUtil(200, "ok", info);
    }


    /**
     * * 后台管理的list
     */
    @RequiresPermissions("informationall:list") 
    @RequestMapping("list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize,
                             InformationAll vo,
                             @RequestParam(defaultValue = "1")Integer onetype,Integer twotype,@RequestParam(defaultValue = "10000")Integer threetype,@RequestHeader("host") String host) {
        ModelAndView model = new ModelAndView("informationall/list");
        if (threetype != null && threetype == 0) {
            vo.setType(threetype);
        }
        if (vo.getChecked() != null && vo.getChecked() == -1) {
            vo.setChecked(null);
        }
        PageInfo<InformationAll> page = new PageInfo<InformationAll>();
        if (vo.getType() == null) {
            vo.setType(onetype);
//            page = this.allService.findByOneType(pageNum, pageSize, vo);
            page = this.allService.findByType(pageNum, pageSize, threetype,host);
            model.addObject("pageinfo", page);
            vo.setType(null);
        } else {
        	page = this.allService.findBySelective(pageNum, pageSize, vo,host);
            model.addObject("pageinfo",page);
        }
        model.addObject("onetype", onetype);
        model.addObject("select", vo);
        model.addObject("onetypes", this.oneTypeService.findAll());
        List<TwoType> list3 = this.twoTypeService.findByOne(onetype);
//        list3.add(new TwoType(0, "不限"));
//        排序接口，返回正，零，负，分别表示大于，等于，小于；从小到大排序
        list3.sort(Comparator.comparingInt(TwoType::getId));
        model.addObject("twotype",twotype);
        model.addObject("twotypes", list3);
        
        /*
         * 以下三级标签 2018.4.19添加
         */
//        List<ThreeType> list=this.threeTypeService.findAllByOne(onetype);
        List<ThreeType> list=this.threeTypeService.findByTwo(twotype);
        list.sort(Comparator.comparingInt(ThreeType::getId));
        model.addObject("threetype",threetype);
        model.addObject("threetypes",list);  
        return model;
    }

    /**
     * 以三级分类为区别码查询，以点击量排序
     *
     * @param type -》PathVariable方式 二级分类的ID
     */
    @RequestMapping("find/{type}")
    @ResponseBody
    public ResultUtil findByType(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize,
                                 @PathVariable Integer type,@RequestHeader("host") String host) {
        InformationAll vo = new InformationAll();
        vo.setType(type);
        vo.setChecked(1);
        return new ResultUtil(200, "ok", this.allService.findBySelective(pageNum, pageSize, vo,host));
    }
    
    @RequestMapping("find1/{type}")
    @ResponseBody
    public ResultUtil findByType1(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize,
                                 @PathVariable Integer type,String token) {
        InformationAll vo = new InformationAll();
        vo.setType(type);
        vo.setChecked(1);
        return new ResultUtil(200, "ok", this.allService.findBySelective1(pageNum, pageSize, vo,token));
    }

    /**
     * 更新接口
     */
    @RequiresPermissions("informationall:update")
    @RequestMapping("update")
    public ModelAndView update(@Validated InformationAll vo, Errors errors, MultipartFile file, HttpServletRequest request) {
        if (errors.hasErrors()) {
            log.error(errors.toString());
        }
        log.info("到达更新controller");
        this.allService.update(vo, request.getHeader("host"), file, "");
        log.info("更新完成");
        ModelAndView model = new ModelAndView("redirect:/informationall/list");
        log.info("页面跳转");
        return model;
    }

    /* 跳转到更新页面，并做一些预处理 
       2018.4.19 暂时关闭修改编辑页面    
    @RequiresPermissions("informationall:toupdate")  */ 
    @RequestMapping("toupdate/{id}")
    public ModelAndView toupdate(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("informationall/update");
        InformationAll co = this.allService.info(id);
        model.addObject("info", co);
        Integer threeType = co.getType();
        model.addObject("threetype", threeType);
        ThreeType type1 = threeTypeService.findById(threeType);
        model.addObject("twotype", type1.getTwoType());
        model.addObject("onetype",this.oneTypeService.selectByTwoType(type1.getTwoType()));
        model.addObject("onetypes", this.oneTypeService.findAll());
        model.addObject("twotypes", this.twoTypeService.findByOne(type1.getTwoType()));
        model.addObject("threetypes",threeTypeService.findByTwo(type1.getTwoType()));
        return model;
    }

    /**
     * 详情页
     */
    @RequestMapping("info/{id}")
    public ModelAndView info(@PathVariable Long id, String token) {
        ModelAndView model = new ModelAndView("informationall/info");
        InformationAll info;
        if (token != null) {
            Users users = usersService.findByToken(token);
            if (users != null) {
                Long userID = users.getId();
                info = this.allService.info(id, userID);
                model.addObject("info", info);
                //浏览获得金豆
                this.shareInfoRecordService.browseAddJd(token, id);
            } else {
                info = this.allService.info(id);
                model.addObject("info", info);
            }
        } else {
            info = this.allService.info(id);
            model.addObject("info", info);
        }

        long create = info != null ? info.getCreateDate().getTime() : 0;

        //long time = new Date().getTime() - create;
        long time = System.currentTimeMillis() - create;

        Long min = time / 60000;
        String time3;
        if (min < 5) {
            time3 = "刚刚";
        } else if (min < 60) {
            time3 = min + "分钟前";
        } else if (min < 1440) {
            time3 = min / 60 + "小时前";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
            time3 = simpleDateFormat.format(create);
        }
        model.addObject("createTime", time3);
        return model;
    }
    
    /**
     * 详情页
     */
    @RequestMapping("info1/{id}")
    public ModelAndView info1(@PathVariable Long id, String token) {
        ModelAndView model = new ModelAndView("informationall/info1");
        InformationAll info;
        if (token != null) {
            Users users = usersService.findByToken(token);
            if (users != null) {
                Long userID = users.getId();
                info = this.allService.info(id, userID);
                model.addObject("info", info);
                //浏览获得金豆
                this.shareInfoRecordService.browseAddJd(token, id);
            } else {
                info = this.allService.info(id);
                model.addObject("info", info);
            }
        } else {
            info = this.allService.info(id);
            model.addObject("info", info);
        }

        long create = info != null ? info.getCreateDate().getTime() : 0;

        //long time = new Date().getTime() - create;
        long time = System.currentTimeMillis() - create;

        Long min = time / 60000;
        String time3;
        if (min < 5) {
            time3 = "刚刚";
        } else if (min < 60) {
            time3 = min + "分钟前";
        } else if (min < 1440) {
            time3 = min / 60 + "小时前";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
            time3 = simpleDateFormat.format(create);
        }
        model.addObject("createTime", time3);
        return model;
    }

    /**
     * 删除
     */
    @RequiresPermissions("informationall:del")
    @RequestMapping("del")
    @ResponseBody
    public ResultUtil del(Long id) {
        //可以改为下架 不删除
        return new ResultUtil(200, "ok", this.allService.delById(id));
    }

    /**
     * 获取文章的礼物榜
     */
    @Resource
    private GiftRecordService giftRecordService;

    @RequestMapping("giftRecord/{id}")
    @ResponseBody
    public ResultUtil giftRecord(@PathVariable Long id) {
        List<Map<String, Object>> list = giftRecordService.findGiftRecordByToInformation(id);
        return new ResultUtil(200, "ok", list);
    }


    
	@RequestMapping("zhiding")
	@ResponseBody
	public ResultUtil zhiDing(Long id) {
		int i=this.informationAllMapper.selectStick(id);
		if(i==1){
			return new ResultUtil(200, "ok", "已置顶!");
		}
		this.allService.zhiding(id);
		
		return new ResultUtil(200, "ok", "置顶成功!");
	}
	
	@RequestMapping("zhidingup")
	@ResponseBody
	public ResultUtil zhiDingup(Long id) {
		int i=this.informationAllMapper.selectStick(id);
		if(i==0){
			return new ResultUtil(200, "ok", "未置顶!");
		}
		this.allService.zhidingup(id);
		
		return new ResultUtil(200, "ok", "取消置顶成功!");
	}
	
	
	 @RequestMapping("findStick")
	 @ResponseBody
	    public ResultUtil findStick(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
	                                    @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize,
	                                     Integer type,String token) {
	        PageInfo<BaseShow> info = this.allService.findStick(pageNum, pageSize, type,token);
	        return new ResultUtil(200, "ok", info);
	    }
	
	
}
