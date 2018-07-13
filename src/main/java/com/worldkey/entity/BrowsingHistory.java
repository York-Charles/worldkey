package com.worldkey.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class BrowsingHistory implements Serializable{
    @NotNull
    private Long user;
    @NotNull
    private Long item;

    private Float pf;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time;

    public BrowsingHistory(Long item,Long user,Float pf, Date time) {
        this.user = user;
        this.item = item;
        this.time = time;
        this.pf=pf;
    }

    public BrowsingHistory() {
    }

    public BrowsingHistory(Long user, Long item) {
        this.user = user;
        this.item = item;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getItem() {
        return item;
    }

    public void setItem(Long item) {
        this.item = item;
    }

    public Float getPf() {
        return pf;
    }

    public void setPf(Float pf) {
        this.pf = pf;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "BrowsingHistory{" +
                "user=" + user +
                ", item=" + item +
                ", pf=" + pf +
                ", time=" + time +
                '}';
    }
}
