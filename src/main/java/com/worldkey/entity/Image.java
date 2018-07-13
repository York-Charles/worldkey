package com.worldkey.entity;

import java.io.Serializable;

public class Image implements Serializable {
    private Integer id;

    private String url;

    private String location;

    private Integer used;

    private String table;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public Image() {
		super();
	}

	public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table == null ? null : table.trim();
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", location='" + location + '\'' +
                ", used=" + used +
                ", table='" + table + '\'' +
                '}';
    }
}