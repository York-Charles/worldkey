package com.worldkey.entity;

import lombok.Data;

@Data
public class Portrait {

	private Integer reqSeat;
	private String reqLabel;
	private Integer isreqSeat;
	private String icon;

	public Portrait(Integer reqSeat, String reqLabel, Integer isreqSeat, String icon) {
		super();
		this.reqSeat = reqSeat;
		this.reqLabel = reqLabel;
		this.isreqSeat = isreqSeat;
		this.icon = icon;
	}

}
