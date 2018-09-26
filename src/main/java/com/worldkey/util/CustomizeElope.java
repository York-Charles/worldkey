package com.worldkey.util;

import java.util.Map;

import io.rong.messages.BaseMessage;
import io.rong.util.GsonUtil;

public class CustomizeElope extends BaseMessage {

	private String label;
	private String userId;
	private String operation;
	private String content;
	private Integer toSeat;
	private Integer fromSeat;
	private String channel;
	private Map<Integer, Integer> map;

	public CustomizeElope(String label, String operation, String userId) {
		super();
		this.label = label;
		this.userId = userId;
		this.operation = operation;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Integer getToSeat() {
		return toSeat;
	}

	public void setToSeat(Integer toSeat) {
		this.toSeat = toSeat;
	}

	public Integer getFromSeat() {
		return fromSeat;
	}

	public void setFromSeat(Integer fromSeat) {
		this.fromSeat = fromSeat;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	private transient static final String TYPE = "DB:elope";

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public String toString() {
		return GsonUtil.toJson(this, CustomizeElope.class);
	}
}
