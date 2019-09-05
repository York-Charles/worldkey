package com.worldkey.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.worldkey.entity.ReqRecord;

@Repository
public interface ReqRecordMapper {
	
	@Insert("insert into req_record values (null,now(),#{barId},#{reqId},#{isReqId},0)")
	Integer addRecord(ReqRecord req);
	
	@Select("select id,bar_id,req_id,is_reqId from req_record where bar_id=#{barId} and ifsuc=0")
	List<ReqRecord>  selectByBarId(Integer barId);
	
	@Select("select id,bar_id,req_id,is_reqId from req_record where bar_id=#{barId} and ifsuc=1")
	List<ReqRecord>  selectByBarId1(Integer barId);

	@Delete("delete from req_record where id=#{id}")
	Integer delRecord(Integer id);
	
	@Delete("delete from req_record where req_id=#{toUserId} or is_reqId=#{toUserId}")
	Integer delRecord1(Integer toUserId);
	
	@Select("select id,create_date,bar_id,req_id,is_reqId from req_record where (is_reqId=#{isId} or req_id=#{isId}) and ifsuc=0")
	ReqRecord selectByIsReqId(Long isId);

	@Select("select count(id) from req_record where (is_reqId=#{isId} or req_id=#{isId}) and ifsuc=0")
	Integer selectByIsReqId2(Long isId);
	
	@Select("select id,create_date,bar_id,req_id,is_reqId from req_record where (is_reqId=#{isId} or req_id=#{isId}) and ifsuc=1")
	ReqRecord selectByIsReqId1(Integer isId);
	
	@Select("select create_date from req_record where id=#{id}")
	Long getChannel(Integer id);
	
	@Update("update req_record set ifsuc=1 where id=#{id}")
	Integer elopeSucceed(Integer id);
	
	@Update("update req_record set ifsuc=0 where id=#{id}")
	Integer elopeFailed(Integer id);
	
	@Select("select id,create_date,bar_id,req_id,is_reqId from req_record where create_date=FROM_UNIXTIME(#{channel})")
	ReqRecord SelectByChannel(String channel);
	
	@Select("select id from req_record where req_id=#{toUserId} and is_reqId=#{fromUserId}")
	Integer selectExist(@Param("toUserId")Long toUserId,@Param("fromUserId")Long fromUserId);
}
