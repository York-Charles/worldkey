package com.worldkey.entity;

import io.rong.util.GsonUtil;

import java.io.Serializable;

public class InformationExample extends InformationAll implements Serializable{
	private static final long serialVersionUID = -4231227528801582843L;

	private Long user;
	private Integer onetype;
	public Long getUser() {
		return user;
	}

	public InformationExample() {
		super();
	}

	public InformationExample(String title) {
		super(title);
	}

	public void setUser(Long user) {
		this.user = user;
	}

	public Integer getOnetype() {
		return onetype;
	}

	public void setOnetype(Integer onetype) {
		this.onetype = onetype;
	}

	@Override
	public String toString() {
		return GsonUtil.toJson(this,this.getClass());
	}
}
