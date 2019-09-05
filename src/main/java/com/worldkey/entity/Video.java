package com.worldkey.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Video {

    private Integer id;
    private String name;
    private Timestamp createDate;
    private Integer userId;
    private String url;
    private String cover;
    private Integer sc;
    private Integer kf;
    

    public Video(String url,String cover){
    	this.url=url;
    	this.cover=cover;
    }
    public Video(String name,Integer userId, String url) {
        this.userId = userId;
        this.name = name;
        this.url = url;
    }
    public Video(String name,Integer userId, String url,String cover) {
        this.userId = userId;
        this.name = name;
        this.url = url;
        this.cover = cover;
    }
    

    public Video(Integer id, String name, Timestamp createDate, Integer userId, String url) {
		super();
		this.id = id;
		this.name = name;
		this.createDate = createDate;
		this.userId = userId;
		this.url = url;
	}


	public Video() {
		super();
	}


	public Video(Integer id, String name, Timestamp createDate, Integer userId, String url, Integer sc, Integer kf) {
		super();
		this.id = id;
		this.name = name;
		this.createDate = createDate;
		this.userId = userId;
		this.url = url;
		this.sc = sc;
		this.kf = kf;
	}



    
}
