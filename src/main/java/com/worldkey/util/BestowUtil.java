package com.worldkey.util;

public class BestowUtil {

    private String message;
    private Integer seat;
    private Integer toSeat;
    private String giftImg;
    private Integer catagory;

    public BestowUtil(Integer catagory) {
        this.catagory = catagory;
    }

    public BestowUtil(String message, Integer seat, Integer toSeat, String giftImg, Integer catagory) {
        this.message = message;
        this.seat = seat;
        this.toSeat = toSeat;
        this.giftImg = giftImg;
        this.catagory = catagory;
    }

    public BestowUtil(String message, Integer seat, Integer toSeat, String giftImg) {
        this.message = message;
        this.seat = seat;
        this.toSeat = toSeat;
        this.giftImg = giftImg;
    }

    public Integer getCatagory() {
        return catagory;
    }

    public void setCatagory(Integer catagory) {
        this.catagory = catagory;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public Integer getToSeat() {
        return toSeat;
    }

    public void setToSeat(Integer toSeat) {
        this.toSeat = toSeat;
    }

    public String getGiftImg() {
        return giftImg;
    }

    public void setGiftImg(String giftImg) {
        this.giftImg = giftImg;
    }

}
