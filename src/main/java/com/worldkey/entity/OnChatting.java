package com.worldkey.entity;

import lombok.Data;

@Data
public class OnChatting {

	private String label1;
	private String label2;
	private Integer userId1;
	private Integer userId2;

	public OnChatting(String label1, String label2, Integer userId1, Integer userId2) {
		super();
		this.label1 = label1;
		this.label2 = label2;
		this.userId1 = userId1;
		this.userId2 = userId2;
	}

	public OnChatting() {
		super();
	}

	
}
