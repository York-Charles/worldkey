package com.worldkey.entity;

import java.sql.Timestamp;

public class Video {

    private Integer id;
    private String name;
    private Timestamp createDate;
    private Integer userId;
    private String url;
    private String cover;

    public Video(String name,Integer userId, String url,String cover) {
        this.userId = userId;
        this.name = name;
        this.url = url;
        this.cover = cover;
    }

    public Video(String name, String url,String cover) {
        this.name = name;
        this.url = url;
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Video(Integer id, String name, Timestamp createDate, Integer userId, String url, String cover) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.userId = userId;
        this.url = url;
        this.cover = cover;
    }

    public Video() {
    }
}
