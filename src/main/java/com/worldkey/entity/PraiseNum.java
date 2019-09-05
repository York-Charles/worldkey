package com.worldkey.entity;

import java.io.Serializable;

/**
 * @author w
 */
public class PraiseNum implements Serializable {
    private Long praiseNumId;

    private Integer information;

    private Integer praiseNum;

    private static final long serialVersionUID = 1L;
    @Override
    public String toString(){
        return " PraiseNum [id=" + praiseNumId +",information=" + information + ",praiseNum=" + praiseNum +"]";}

    public Long getPraiseNumId() {
        return praiseNumId;
    }

    public void setPraiseNumId(Long praiseNumId) {
        this.praiseNumId = praiseNumId;
    }

    public Integer getInformation() {
        return information;
    }

    public void setInformation(Integer information) {
        this.information = information;
    }

    public Integer getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(Integer praiseNum) {
        this.praiseNum = praiseNum;
    }
}