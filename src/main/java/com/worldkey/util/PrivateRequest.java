package com.worldkey.util;

import io.rong.messages.BaseMessage;
import io.rong.util.GsonUtil;

public class PrivateRequest extends BaseMessage {

	private Integer message;
	private String l1;
	private String h1;
	private Long id1;
	private String l2;
	private String h2;
	private Long id2;
	private String content;

	public PrivateRequest() {
		super();
	}

	public PrivateRequest(String l1) {
		super();
		this.l1 = l1;
	}

	public PrivateRequest(String l1, String h1, Long id1, String l2, String h2, Long id2) {
		super();
		this.l1 = l1;
		this.h1 = h1;
		this.id1 = id1;
		this.l2 = l2;
		this.h2 = h2;
		this.id2 = id2;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getL1() {
		return l1;
	}

	public void setL1(String l1) {
		this.l1 = l1;
	}

	public String getH1() {
		return h1;
	}

	public void setH1(String h1) {
		this.h1 = h1;
	}

	public Long getId1() {
		return id1;
	}

	public void setId1(Long id1) {
		this.id1 = id1;
	}

	public String getL2() {
		return l2;
	}

	public void setL2(String l2) {
		this.l2 = l2;
	}

	public String getH2() {
		return h2;
	}

	public void setH2(String h2) {
		this.h2 = h2;
	}

	public Long getId2() {
		return id2;
	}

	public void setId2(Long id2) {
		this.id2 = id2;
	}

	public Integer getMessage() {
		return message;
	}

	public void setMessage(Integer message) {
		this.message = message;
	}

	private transient static final String TYPE = "DB:private";

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public String toString() {
		return GsonUtil.toJson(this, PrivateRequest.class);
	}

}
