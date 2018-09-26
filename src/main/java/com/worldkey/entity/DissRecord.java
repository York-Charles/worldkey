package com.worldkey.entity;

import lombok.Data;

@Data
public class DissRecord {

	private Integer id;
	private Long userId;
	private Integer dissId;

	public DissRecord(Long userId, Integer dissId) {
		super();
		this.userId = userId;
		this.dissId = dissId;
	}

}
