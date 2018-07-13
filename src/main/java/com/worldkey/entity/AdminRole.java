package com.worldkey.entity;

import java.io.Serializable;

public class AdminRole implements Serializable {
    private Integer admin;
	
    private Integer role;

    private static final long serialVersionUID = 1L;

    public AdminRole(Integer admin, Integer role) {
		super();
		this.admin = admin;
		this.role = role;
	}

	public AdminRole() {
		super();
	}

	public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}