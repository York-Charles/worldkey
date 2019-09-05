package com.worldkey.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class InformationAll implements Serializable {
	private Long id;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	//5.10修改 可以为空
//	@NotNull(message = "标题不能为空")
//	@Size(min = 1, message = "标题不能为空")  
	private String title;

	private String titleImg;

	private Integer type;

	private String weburl;
	private String abstracte;
	private String auther;

	private Integer pointNumber;
	
	private Integer classify;
	private Users users;
	private Integer stick;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date stickDate;
	/**
	 * 值与对应的状态 0 ->未审核 1 ->已审核 2 ->草稿 3 ->已下架
	 */
	private Integer checked;
	/**
	 * 判定是否已经推送了
	 */
	private Integer showPush = 0;

	private String info;
	/**
	 * 橱窗展示competitive
	 */
	private Integer competitive = 0;
	/**
	 * 个人品牌展示文章
	 */
	private Integer userBrand;
	//bug反馈1待处理 2已答复 3处理中 4请补充 5已收录 6已解决
	private Integer solve;
	
	//删除状态0存在1已删除
	private Integer state;
	
	//草稿 0 草稿 1非草稿
		private Integer draft;
		
		//判断乐赠xianzhi
		private Integer xl;

	private static final long serialVersionUID = 1L;

	public InformationAll(String title, String titleImg, Integer type, String auther, Integer pointNumber, Users users,
			Integer checked, String info) {
		this.title = title;
		this.titleImg = titleImg;
		this.type = type;
		this.auther = auther;
		this.pointNumber = pointNumber;
		this.users = users;
		this.checked = checked;
		this.info = info;
	}

	public InformationAll(String title, String titleImg, Integer type, String auther, Integer pointNumber, Users users,
			Integer checked, String info,Integer userBrand) {
		this.title = title;
		this.titleImg = titleImg;
		this.type = type;
		this.auther = auther;
		this.pointNumber = pointNumber;
		this.users = users;
		this.checked = checked;
		this.info = info;
		this.userBrand = userBrand;
	}
	public InformationAll() {
		super();
	}

	public InformationAll(String title) {
		this.title = title;
	}


	/**
	 * 更改后添加的构造方法
	 * @param id
	 * @param createDate
	 * @param title
	 * @param titleImg
	 * @param type
	 * @param weburl
	 * @param abstracte
	 * @param auther
	 * @param pointNumber
	 * @param classify
	 * @param users
	 * @param checked
	 * @param showPush
	 * @param info
	 */
	public InformationAll(Long id, Date createDate, String title, String titleImg, Integer type, String weburl,
			String abstracte, String auther, Integer pointNumber, Integer classify, Users users, Integer checked,
			Integer showPush, String info) {
		super();
		this.id = id;
		this.createDate = createDate;
		this.title = title;
		this.titleImg = titleImg;
		this.type = type;
		this.weburl = weburl;
		this.abstracte = abstracte;
		this.auther = auther;
		this.pointNumber = pointNumber;
		this.classify = classify;
		this.users = users;
		this.checked = checked;
		this.showPush = showPush;
		this.info = info;
	}
}