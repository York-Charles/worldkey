package com.worldkey.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.worldkey.check.user.ChangeInfo;
import com.worldkey.check.user.Login;
import com.worldkey.check.user.UserReg;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author HP
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Users implements Serializable {
    private Long id;
//    @NotNull(message = "用户名不能为空", groups = {UserReg.class, Login.class})
    @Pattern(regexp = "^[A-Za-z0-9_-]{6,12}$", message = "用户名为6-12位字母数字和下划线的组合", groups = {UserReg.class, Login.class})
    private String loginName;
    @NotNull(message = "密码不能为空", groups = {UserReg.class, Login.class})
    @Size(min = 6, max = 16, message = "长度为6-16", groups = {UserReg.class, Login.class})
    private String password;
    @Pattern(regexp = "^[\\u4e00-\\u9fa5_a-zA-Z0-9]{1,16}$", groups = {ChangeInfo.class, UserReg.class})
    private String petName;
    //0为男，1为女
    @Min(value = 0, message = "只能为0(女)或1(男)", groups = {UserReg.class, ChangeInfo.class})
    @Max(value = 1, message = "只能为0(女)或1(男)", groups = {UserReg.class, ChangeInfo.class})
    private Integer sex;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "生日必须小于现在", groups = {ChangeInfo.class, UserReg.class})
    private Date birthday;
    private String birth;

    private String headImg;
    @Pattern(regexp = "^1[34578][0-9]{9}$", groups = {UserReg.class})
    private String telNum;

    private String token;

    private BigDecimal balance;

    private String rongyunToken;
    @Email(groups = {UserReg.class, ChangeInfo.class})
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /**
     * 金豆
     */
    private Integer jd;
    /**
     * 钻石
     */
    private Integer zs;
    /**
     * K币
     */
    private BigDecimal kb;

    /**
     * 新手礼包记录
     */
    private Integer noviceGiftBag;
    
    //6.7个人信息添加
    private String bgImg;
    private String bgContent;
    private String signature;
	private String emotional;
	private Double height;
	private Double weight;
	private String constellation;
	private String occupation;
	private String fond;
    private Integer age;
    //个人品牌
    private Integer personalBrand;
    
    private List<UserGroup> userGroup;

    private static final long serialVersionUID = 1L;
    

    
    public Users() {
        super();
    }

    public Users(Long id, BigDecimal balance) {
        this.balance = balance;
        this.id = id;
    }

    public Users(Long id, Integer jd) {
        this.id = id;
        this.jd = jd;
    }
    
    public Users(String bgImg,String bgContent){
    	this.bgImg = bgImg;
    	this.bgContent = bgContent;
    }
}