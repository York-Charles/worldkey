package com.worldkey.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Role implements Serializable {
    private Integer id;

    private String name;

    private  String value;

    
    private Set<Admin>admins=new HashSet<>();
    
    private Set<Action>actions=new HashSet<>();
    
    
    private static final long serialVersionUID = 1L;

    
    public Role() {
		super();
	}

	public Set<Admin> getAdmins() {
		return admins;
	}

	public void setAdmins(Set<Admin> admins) {
		this.admins = admins;
	}

	public Set<Action> getActions() {
		return actions;
	}

	public void setActions(Set<Action> actions) {
		this.actions = actions;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Role role = (Role) o;
		return id.equals(role.id) && (name != null ? name.equals(role.name) : role.name == null) && (value != null ? value.equals(role.value) : role.value == null);
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (value != null ? value.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Role{" +
				"id=" + id +
				", name='" + name + '\'' +
				", value='" + value + '\'' +
				", admins=" + admins +
				", actions=" + actions +
				'}';
	}
}