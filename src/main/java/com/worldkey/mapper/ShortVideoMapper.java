package com.worldkey.mapper;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Video;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ShortVideoMapper {

    @Insert("insert into short_video values (null,#{name},now(),#{url},#{userId},#{cover},1,1)")
    Integer addVideo(Video video);

    @Select("SELECT id,name,create_date,user_id,url,cover FROM short_video where sc=1 and kf=1 and id>= (SELECT (RAND() * (SELECT MAX(id) FROM short_video))) order by id LIMIT 1")
    String getRandomVideo();

    @Select("select id,name,url,cover from short_video where user_id=#{userId} " +
            "and sc=1 and kf=1 order by create_date desc")
    List<Video> getUserVideo(Long userId);

    @Update("update short_video set sc=0 where id=#{id}")
    boolean truncVideo(Integer id);

    @Select("select name,create_date,url from short_video where name like '%#{keyword}%")
    List<Video> getAlliedVideo(String keyword);
    
    @Select("select id,name,create_date,url,user_id,sc,kf from short_video where sc=1 and (short_video.name LIKE concat('%',#{q},'%')) order by short_video.id desc")
    List<Video> getAll( @Param("q") String q);
    
    @Select("select id,name,create_date,url,user_id,sc,kf from short_video where sc=0 and (short_video.name LIKE concat('%',#{q},'%')) order by short_video.id desc")
    List<Video> getAllRecycle( @Param("q") String q);
    
    @Select("select id,name,create_date,url,user_id,sc,kf from short_video where id=#{id}")
    Video getP(Integer id);//根据id查
    
    @Update("update short_video set kf=#{kf} where id=#{id}")
    Integer checked(@Param("kf") Integer checked, @Param("id") Integer id);
    
    @Update("update short_video set sc=1 where id=#{id}")
    Integer stick(@Param("id") Integer id);
    
    @Update("update short_video set sc=0 where id=#{id}")
    Integer todel(Integer id);
    
    @Delete("delete from short_video where id=#{id}")
    Integer del(Integer id);
}
