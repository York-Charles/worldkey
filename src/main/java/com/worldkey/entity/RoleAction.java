package com.worldkey.entity;

import java.io.Serializable;

public class RoleAction implements Serializable {
    private Integer role;

    private Integer action;

    private static final long serialVersionUID = 1L;

    
    public RoleAction() {
		super();
	}

	public RoleAction(Integer role, Integer action) {
		this.role = role;
		this.action = action;
	}

	public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "RoleAction{" +
                "role=" + role +
                ", action=" + action +
                '}';
    }
}