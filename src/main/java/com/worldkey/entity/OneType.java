package com.worldkey.entity;

import io.rong.util.GsonUtil;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

public class OneType implements Serializable {
    private Integer id;
    @Length(min=1,message="类型的最小长度为一")
    private String typeName;

    private List<TwoType> twoTypes;
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public OneType() {
		super();
	}

	public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public List<TwoType> getTwoTypes() {
        return twoTypes;
    }

    public void setTwoTypes(List<TwoType> twoTypes) {
        this.twoTypes = twoTypes;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this,this.getClass());
    }
}