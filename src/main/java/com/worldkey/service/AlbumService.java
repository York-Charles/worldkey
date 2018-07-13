package com.worldkey.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.worldkey.entity.Album;

public interface AlbumService {

	Integer addAlbum(Album album);

	String getAlbum(Integer userId);
	
	Integer delAlbum(String url);
}
