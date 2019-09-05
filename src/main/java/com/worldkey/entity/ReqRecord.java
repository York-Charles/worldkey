package com.worldkey.entity;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class ReqRecord {

	private Integer id;
	private Timestamp createDate;
	private Integer barId;
	private Integer reqId;
	private Integer isReqId;

	public ReqRecord(Integer barId, Integer reqId, Integer isReqId) {
		super();
		this.barId = barId;
		this.reqId = reqId;
		this.isReqId = isReqId;
	}

	public ReqRecord(Integer id, Timestamp createDate, Integer barId, Integer reqId, Integer isReqId) {
		super();
		this.id = id;
		this.createDate = createDate;
		this.barId = barId;
		this.reqId = reqId;
		this.isReqId = isReqId;
	}

	public ReqRecord(Integer id, Integer barId, Integer reqId, Integer isReqId) {
		super();
		this.id = id;
		this.barId = barId;
		this.reqId = reqId;
		this.isReqId = isReqId;
	}

	public ReqRecord() {
		super();
	}

	
}
