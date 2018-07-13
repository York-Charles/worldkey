package com.worldkey.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.worldkey.entity.Album;
import com.worldkey.mapper.AlbumMapper;
import com.worldkey.service.AlbumService;

@Service
public class AlbumServiceImpl implements AlbumService {
	
	@Resource
	AlbumMapper albumMapper;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer addAlbum(Album album) {
		// TODO Auto-generated method stub
		return this.albumMapper.addAlbum(album);
	}

	@Override
	public String getAlbum(Integer userId) {
		// TODO Auto-generated method stub
		List<String> albums = this.albumMapper.getAlbum(userId);
		return String.join(",", albums);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer delAlbum(String url) {
		// TODO Auto-generated method stub
		return this.albumMapper.delAlbum(url);
	}

}
