package com.worldkey.entity;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Admin implements Serializable {
    private Integer id;
    @Length(min=4,max=10,message="用户名的长度在4-10之间")
    private String name;
    @Length(min=6,max=12,message="密码长度在6-12之间")
    private String password;
    @Length(max=10,message="昵称在10个字之内")
    private String petname;

    private Set<Role>roles=new HashSet<>();
    
    
    
    private static final long serialVersionUID = 1L;

    public Admin( ) {
    	super();
    }

    
    
    public Set<Role> getRoles() {
		return roles;
	}



	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}



	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPetname() {
        return petname;
    }

    public void setPetname(String petname) {
        this.petname = petname == null ? null : petname.trim();
    }



	@Override
	public String toString() {
		return "Admin [id=" + id + ", name=" + name + ", password=" + password + ", petname=" + petname + ", roles="
				+ roles + "]";
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Admin admin = (Admin) o;

        if (id != null ? !id.equals(admin.id) : admin.id != null) {
            return false;
        }
        if (name != null ? !name.equals(admin.name) : admin.name != null) {
            return false;
        }
        if (password != null ? !password.equals(admin.password) : admin.password != null) {
            return false;
        }
        return petname != null ? petname.equals(admin.petname) : admin.petname == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (petname != null ? petname.hashCode() : 0);
        return result;
    }
}