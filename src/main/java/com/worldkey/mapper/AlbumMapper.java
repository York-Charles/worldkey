package com.worldkey.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import com.worldkey.entity.Album;

@Repository
public interface AlbumMapper {
	
	@Insert("insert into album values (null,now(),#{userId},#{url})")
	Integer addAlbum(Album album);
	
	@Select("select url from album inner join users on users.id=album.user_id where users.id=#{userId} order by create_time")
	List<String> getAlbum(Integer userId);
	
	@Delete("delete from album where url=#{url}")
	Integer delAlbum(String url);
}
