package com.worldkey.util;

import java.io.Serializable;

/**
 * @author HP
 */
public class ResultUtil implements Serializable{

	public static final Integer OK=200;
	public static final Integer NO=400;
	public static final Integer NOT_FOUNT=404;
	public static final Integer SYSTEM_ERROR=500;
	public static final Integer PARAMETER_ERROR=406;


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer code;
	private String msg;
	private Object result;
	
	
	public ResultUtil() {
		super();
	}
	public ResultUtil(Integer code, String msg, Object result) {
		super();
		this.code = code;
		this.msg = msg;
		this.result = result;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		result = prime * result
				+ ((this.result == null) ? 0 : this.result.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ResultUtil other = (ResultUtil) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		if (msg == null) {
			if (other.msg != null) {
				return false;
			}
		} else if (!msg.equals(other.msg)) {
			return false;
		}
		if (result == null) {
			if (other.result != null) {
				return false;
			}
		} else if (!result.equals(other.result)) {
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return "ResultUtil [code=" + code + ", msg=" + msg + ", result="
				+ result + "]";
	}
	
	
}
