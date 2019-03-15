package com.worldkey.mapper;

import com.worldkey.entity.Video;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ShortVideoMapper {

    /**
     * sc 删除 1未删除 0已删除 kf 审核 1未审核 0已审核
     * @param video
     * @return
     */
    @Insert("insert into short_video values (null,#{name},now(),#{userId},#{url},#{cover},1,0)")
    Integer addVideo(Video video);

    @Select("SELECT id,name,create_date,user_id,url,cover FROM short_video where sc=1 and kf=0 and id>= (SELECT (RAND() * (SELECT MAX(id) FROM short_video))) order by id LIMIT 1")
    Video getRandomVideo();

    @Select("select video from short_video where user_id=#{userId} " +
            "where sc=1 and kf=1 order by create_date desc")
    List<Video> getUserVideo(Long userId);

    @Update("update short_video set sc=0 where id=#{id}")
    boolean truncVideo(Integer id);

    @Select("select name,create_date,url from short_video where name like '%#{keyword}%")
    List<Video> getAlliedVideo(String keyword);
}
