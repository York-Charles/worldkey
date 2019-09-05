package com.worldkey.entity;

import java.io.Serializable;

public class Friend implements Serializable {
    private String users;

    private String friend;

    private String note;

    private static final long serialVersionUID = 1L;


	public Friend() {
		super();
	}

	public Friend(String users, String friend) {
		super();
		this.users = users;
		this.friend = friend;
	}

	public Friend(String users, String friend, String note) {
		super();
		this.users = users;
		this.friend = friend;
		this.note = note;
	}

	public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users == null ? null : users.trim();
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend == null ? null : friend.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    @Override
    public String toString() {
        return "Friend{" +
                "users='" + users + '\'' +
                ", friend='" + friend + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}