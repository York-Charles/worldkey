package com.worldkey.entity;

import java.io.Serializable;

public class PraiseCommentNum implements Serializable {
	 private Long praiseNumId;

	    private Integer comment;

	    private Integer praiseNum;

	    private static final long serialVersionUID = 1L;

		public PraiseCommentNum(Long praiseNumId, Integer comment, Integer praiseNum) {
			super();
			this.praiseNumId = praiseNumId;
			this.comment = comment;
			this.praiseNum = praiseNum;
		}

		public PraiseCommentNum() {
			super();
		}

		public Long getPraiseNumId() {
			return praiseNumId;
		}

		public void setPraiseNumId(Long praiseNumId) {
			this.praiseNumId = praiseNumId;
		}

		public Integer getComment() {
			return comment;
		}

		public void setComment(Integer comment) {
			this.comment = comment;
		}

		public Integer getPraiseNum() {
			return praiseNum;
		}

		public void setPraiseNum(Integer praiseNum) {
			this.praiseNum = praiseNum;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}
	    
}
