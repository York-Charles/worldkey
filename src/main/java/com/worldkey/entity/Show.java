package com.worldkey.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 展示的实体类
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class Show  extends BaseShow{
    private String typeName;
    private Integer type;
    private String info;
    //展示分类的状态ID
    private Integer check=4;
    private Users users;
    private String author;
    private Integer showPush;
}
