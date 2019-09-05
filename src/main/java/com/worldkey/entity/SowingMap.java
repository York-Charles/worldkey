package com.worldkey.entity;

import lombok.Data;

@Data
public class SowingMap {
	
	private Integer id;
	private String sowingImg;
	private Integer oneType;
	private Integer twoType;
	
	public SowingMap() {
		super();
	}
	public SowingMap(Integer id, String sowingImg, Integer oneType, Integer twoType) {
		super();
		this.id = id;
		this.sowingImg = sowingImg;
		this.oneType = oneType;
		this.twoType = twoType;
	}

	
}
