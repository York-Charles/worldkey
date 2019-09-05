package com.worldkey.entity;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wu
 */
public class Praise implements Serializable {
    /**
     * 点赞实体ID
     */
    private Long praiseId;

    /**
     * 点赞用户
     */
    private Users users;

    /**
     * 被点赞文章
     */
    private Integer information;

    /**
     * 点赞状态 0为未点赞，1为已点赞。默认为0
     */
    private Integer status;

    /**
     * 点赞数量
     */
    private Integer praiseNum;

    /**
     * 点赞时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

    @Override
    public  String toString(){
        return    "Praise [id=" + praiseId + ",users=" + users
                + ",information=" + information
                + ",status=" + status
                + ",praiseNum=" + praiseNum
                +",createTime" + createTime+"]";
    }


    public Long getPraiseId() {
        return praiseId;
    }

    public void setPraiseId(Long praiseId) {
        this.praiseId = praiseId;
    }

	public Users getUsers() {
		return users;
	}


	public void setUsers(Users users) {
		this.users = users;
	}


	public Integer getInformation() {
        return information;
    }

    public void setInformation(Integer information) {
        this.information = information;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(Integer praiseNum) {
        this.praiseNum = praiseNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}