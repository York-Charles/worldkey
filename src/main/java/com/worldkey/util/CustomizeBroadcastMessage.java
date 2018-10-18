package com.worldkey.util;

import java.util.Map;

import com.worldkey.entity.CoffeeBarUser;

import io.rong.messages.BaseMessage;
import io.rong.util.GsonUtil;

public class CustomizeBroadcastMessage extends BaseMessage {

	private CoffeeBarUser requestingUser;
	private CoffeeBarUser requestedUser;
	private String content;
	private String icon;
	private String iconRot;
	private Map<Integer, Integer> map = null;
	private transient static final String TYPE = "DB:broadcastRequest";

	public CustomizeBroadcastMessage(CoffeeBarUser requestingUser, CoffeeBarUser requestedUser) {
		super();
		this.requestingUser = requestingUser;
		this.requestedUser = requestedUser;
	}

	public String getIconRot() {
		return iconRot;
	}

	public void setIconRot(String iconRot) {
		this.iconRot = iconRot;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public CoffeeBarUser getRequestingUser() {
		return requestingUser;
	}

	public void setRequestingUser(CoffeeBarUser requestingUser) {
		this.requestingUser = requestingUser;
	}

	public CoffeeBarUser getRequestedUser() {
		return requestedUser;
	}

	public void setRequestedUser(CoffeeBarUser requestedUser) {
		this.requestedUser = requestedUser;
	}

	public Map<Integer, Integer> getMap() {
		return map;
	}

	public void setMap(Map<Integer, Integer> map) {
		this.map = map;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public String toString() {
		return GsonUtil.toJson(this, CustomizeBroadcastMessage.class);
	}

}
