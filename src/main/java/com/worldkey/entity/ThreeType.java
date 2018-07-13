package com.worldkey.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;
/**
 * 三级标签实体
 */
@Data
//@ToString
//@TableName("three_type")
public class ThreeType  {
//	@TableId
	private Integer id;
	private String  typeName;
	private String  typeImg;
	private Integer twoType;
	private String headImg;
	private String bgImg;
	private String content;
	private Integer amount;
	private Long userId;
	private	Integer checked;
	private Integer isJoin;
	private String petName;
	
	private List<UserGroup> userGroup;
	
	public  ThreeType (Integer id,String typeName) {
		super();
		this.id=id;
		this.typeName=typeName;

	}

	public ThreeType() {
		super();
	}

	public ThreeType(Integer id, String typeName, String typeImg, Integer twoType, String headImg, String bgImg,
			String content,Integer amount,Long userId,Integer checked,String petName) {
		super();
		this.id = id;
		this.typeName = typeName;
		this.typeImg = typeImg;
		this.twoType = twoType;
		this.headImg = headImg;
		this.bgImg = bgImg;
		this.content = content;
		this.amount = amount;
		this.userId = userId;
		this.checked = checked;
		this.petName = petName;
	}
	
	
}
