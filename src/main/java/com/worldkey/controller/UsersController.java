package com.worldkey.controller;

import com.worldkey.check.user.ChangeInfo;
import com.worldkey.check.user.UserReg;
import com.worldkey.entity.Users;
import com.worldkey.exception.Z406Exception;
import com.worldkey.service.FriendService;
import com.worldkey.service.GiftService;
import com.worldkey.service.UsersService;
import com.worldkey.service.impl.UsersServiceImpl;
import com.worldkey.util.FileUploadUtilAsync;
import com.worldkey.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author HP
 */
@RestController
@RequestMapping("/member")
public class UsersController {
    private static final Logger log = LoggerFactory.getLogger(UsersServiceImpl.class);
    @Resource
    private UsersService uService;
    @Resource
    private FriendService friendService;
    @Resource
    private GiftService giftService;

    /**
     * 通过用户token查询用户的金豆，钻石，K币的数量
     */
    @PostMapping("getJdAndZsAndKb/{token}")
    public ResultUtil getJdAndZsAndKb(@PathVariable String token) {
        Users byToken = this.uService.findByToken(token);
        if (byToken == null) {
            return new ResultUtil(406, "no", "未登录");
        }
        Users users = this.uService.getJdAndZsAndKb(byToken.getId());
        return new ResultUtil(200, "ok", users);
    }


    @PostMapping("praiseNum")
    public ResultUtil getPraiseNum(String token) {
        Users byToken = uService.findByToken(token);
        if (byToken == null) {
            //未登录
            return new ResultUtil(406, "no", "未登录");
        }
        Integer a = this.uService.getPraiseNum(byToken.getId());
        return new ResultUtil(200, "ok", a);
    }


    @PostMapping("balance/{token}")
    public ResultUtil findBalance(@PathVariable String token) {
        Users byToken = this.uService.findByToken(token);
        if (byToken != null) {
            BigDecimal balanceByID = this.uService.findBalanceByID(byToken.getId());
            return new ResultUtil(200, "ok", balanceByID);
        }
        return new ResultUtil(406, "no", "no login");
    }

    @PostMapping("setHead/{token}")
    public ResultUtil setHeadImg(@PathVariable String token, MultipartFile image, @RequestHeader(value = "host") String host) throws Exception {
        String headImg = this.friendService.updateHeadImg(token, host, image);
        if (Objects.equals(headImg, null)) {
            return new ResultUtil(1003, "no", "修改失败,请再次修改");
        }
        return new ResultUtil(200, "ok", headImg);
    }

    @GetMapping("/regHtml")
    public ModelAndView reg() {
        return new ModelAndView("manage/reg");
    }

    @PostMapping("/reg")
    public ResultUtil reg(@Validated({UserReg.class}) Users usersVo, BindingResult result,String recommendedCode) {

        if (result.hasErrors()) {
            Map<String, String> map = new HashMap<>(10);
            for (FieldError error : result.getFieldErrors()) {
                map.put(error.getField(), error.getDefaultMessage());
            }
            return new ResultUtil(406, "ok", map);
        }
        Users byLoginName = null;
        if (recommendedCode != null) {
            byLoginName = this.uService.findByLoginName(recommendedCode);
            if (byLoginName == null) {
                return new ResultUtil(406, "no", "推荐码错误");
            }
        }
        int ok = 1, loginNameIsExist = 2, containSensitiveWorld = 3;
        int a = this.uService.reg(usersVo, byLoginName);
        if (a == ok) {
            return new ResultUtil(200, "ok", "1");
        } else if (a == loginNameIsExist) {
            return new ResultUtil(406, "no", "loginNameIs Exist");
        } else if (a == containSensitiveWorld) {
            //用户名中包含敏感词
            return new ResultUtil(406, "no", "contain Sensitive World");
        }
        return new ResultUtil(406, "no", "error");
    }
    
    @PostMapping("reg1")
    public ResultUtil reg1(@Validated({UserReg.class}) Users usersVo, BindingResult result){
    	 if (result.hasErrors()) {
             Map<String, String> map = new HashMap<>(10);
             for (FieldError error : result.getFieldErrors()) {
                 map.put(error.getField(), error.getDefaultMessage());
             }
             return new ResultUtil(406, "ok", map);
         }
    	 int loginNameIsExist = 2, containSensitiveWorld = 3,petNameIsExist=4;
         Map<String,Object> a = this.uService.reg1(usersVo);
         if ((int)a.get("result") == loginNameIsExist) {
             return new ResultUtil(406, "no", "loginNameIs Exist");
         } else if ((int)a.get("result") == containSensitiveWorld) {
             //用户名中包含敏感词
             return new ResultUtil(406, "no", "contain Sensitive World");
         }else if ((int)a.get("result") == petNameIsExist) {
             //名称重复
             return new ResultUtil(406, "no", "petNameIs Exist");
         }

         Users u  = (Users)a.get("users");
         String  token=this.uService.login(u.getLoginName(), u.getPassword());
         u.setToken(token);
         return new ResultUtil(200, "ok", u) ;
    }

    @RequestMapping("/info")
    public ResultUtil findByToken(String token) {
        if (token == null) {
            return new ResultUtil(406, "no", "token is null");
        }
        Users s = this.uService.findByToken(token);
        if (s == null) {
            return new ResultUtil(406, "no", "token is error");
        }
        log.debug("token: " + token);
        return new ResultUtil(200, "ok", s);
    }

    @RequestMapping("checkname/{loginName}")
    public ResultUtil checkLoginName(@PathVariable String loginName) {
        int s = this.uService.checkName(loginName);
        return new ResultUtil(200, "ok", s);
    }
    
    @RequestMapping("checkpetname/{petName}")
    public ResultUtil checkPetName(@PathVariable String petName) {
        int s = this.uService.checkPetName(petName);
        return new ResultUtil(200, "ok", s);
    }


    /**
     * 修改用户信息的接口
     * @param host    服务器地址
     * @param usersVo 用户信息
     * @param result  校验结果
     */
    @RequestMapping("changeInfo/{token}")
    public ResultUtil changeInfo(@PathVariable String token, @RequestHeader("host") String host, @Validated({ChangeInfo.class}) Users usersVo, BindingResult result) {
        //数据校验
        if (result.hasErrors()) {
            Map<String, String> map = new HashMap<>(result.getFieldErrorCount());
            for (FieldError error : result.getFieldErrors()) {
                map.put(error.getField(), error.getDefaultMessage());
            }
            return new ResultUtil(406, "no", map);
        }

        Users user = uService.findByToken(token);
        if (user != null) {
            usersVo.setId(user.getId());
            Users userCo = this.uService.changeInfo(usersVo, host, token);
            return new ResultUtil(200, "ok", userCo);
        }
        return new ResultUtil(406, "no", "not login");

    }


    @RequestMapping("getUser")
    public ResultUtil getUsers(String loginName) {
        Users uvo = this.uService.findByLoginName(loginName);
        return new ResultUtil(200, "ok", uvo);
    }

    /**
     * 赠送礼物的接口
     * @param token 当前登录的用户的token
     * @param giftId 赠送的礼物的ID
     * @param toInformation 受赠的文章的ID
     * @return ResultUtil result的值为 礼物ID或文章不存在返回0  余额不足返回2 成功返回3，sql错误返回4
     */
    @PostMapping("giveGift/{token}")
    public ResultUtil giveGift(@PathVariable String token, Integer giftId, Long toInformation) {
        Users byToken = uService.findByToken(token);
        if (byToken == null) {
            return new ResultUtil(406, "no", "未登录");
        }
        Integer result = giftService.giveGift(byToken, giftId, toInformation);
        return new ResultUtil(200, "ok", result);
    }

    @PostMapping("kb2Zs/{token}")
    public ResultUtil kb2Zs(Integer kbNum, @PathVariable String token) throws Z406Exception {
        if (kbNum==null||kbNum<1){
           throw new Z406Exception("kbNum不能小于1");
        }
        Users byToken = uService.findByToken(token);
        if (byToken==null){
            return new ResultUtil(406,"no","未登录");
        }
       Integer i= this.uService.kb2Zs(kbNum,byToken.getId());
        if (i<1){
            return new ResultUtil(500,"no","余额不足");
        }
        return new ResultUtil(200,"ok","购买成功");
    }
    //5.11
    @RequestMapping(value="findUserByGroup",method=RequestMethod.GET)
    public ResultUtil findUserByGroup(Integer id,@RequestParam(value="pageNum",defaultValue="1")Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10")Integer pageSize){
    	return new ResultUtil(200 ,"ok", this.uService.findUserByGroup(id,pageNum,pageSize));
    }
    
    @RequestMapping(value="findGroupLeader",method=RequestMethod.GET)
    public ResultUtil findGroupleader(Integer groupId){
    	return new ResultUtil(200,"ok",this.uService.selectGroupLeader(groupId));
    }

    @RequestMapping(value="findUserByGroup1",method=RequestMethod.GET)
    public ResultUtil findUserByGroup1(Integer id,@RequestParam(value="pageNum",defaultValue="1")Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10")Integer pageSize){
    	return new ResultUtil(200 ,"ok", this.uService.findUserByGroup1(id,pageNum,pageSize));
    }
    
    @RequestMapping(value="updateBackground",method=RequestMethod.POST)
    public ResultUtil updateBackground(HttpServletRequest request,String bgContent,Long userId) throws Exception{
    	MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
    	MultipartFile bgImgFile = params.getFile("bgImg");
    	Users user = new Users();
    	String[] allowedType = { "image/bmp", "image/gif", "image/jpeg", "image/png", "image/jpg" };
    	if (bgImgFile != null) {
			log.debug(bgImgFile.isEmpty() + "");
			boolean allowed1 = Arrays.asList(allowedType).contains(bgImgFile.getContentType());
			if (!allowed1) {
				return new ResultUtil(500, "error", "不支持的文件类型!");
			}
			String bgImgrRealPath = new FileUploadUtilAsync().getFileName(request.getHeader("host"), bgImgFile);
			user.setBgImg(bgImgrRealPath);
		}
    	user.setId(userId);
    	user.setBgContent(bgContent);
    	this.uService.updateByPrimaryKeySelective(user);
    	return new ResultUtil(200,"ok","更改您的个性背景成功！");
    }
    
    @RequestMapping(value="userDetail")
    public ResultUtil getUserDetail(Long userId){
		return new ResultUtil(200,"ok",this.uService.userDetails(userId));
    }
    
    @RequestMapping("getFans")
    public ResultUtil getFans(Integer userId,@RequestParam(value="pageNum",defaultValue="1")Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10")Integer pageSize){
    	return new ResultUtil(200 ,"ok",this.uService.getFans(userId, pageNum, pageSize));
    }
    
    @RequestMapping("getStars")
    public ResultUtil getStars(Integer userId,@RequestParam(value="pageNum",defaultValue="1")Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10")Integer pageSize){
    	return new ResultUtil(200 ,"ok",this.uService.getStars(userId, pageNum, pageSize));
    }
    
    public ResultUtil bbb(){
    	return new ResultUtil();
    }
}
