package com.worldkey.entity;

import java.io.Serializable;

public class TwoType implements Serializable {
    private Integer id;

    private String typeName;

    private String typeImg;

    private Integer oneType;

    private static final long serialVersionUID = 1L;


    public TwoType(Integer id, String typeName) {
        super();
        this.id = id;
        this.typeName = typeName;
    }

    public TwoType() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public String getTypeImg() {
        return typeImg;
    }

    public void setTypeImg(String typeImg) {
        this.typeImg = typeImg == null ? null : typeImg.trim();
    }

    public Integer getOneType() {
        return oneType;
    }

    public void setOneType(Integer oneType) {
        this.oneType = oneType;
    }

    @Override
    public String toString() {
        return "TwoType{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                ", typeImg='" + typeImg + '\'' +
                ", oneType=" + oneType +
                '}';
    }
}