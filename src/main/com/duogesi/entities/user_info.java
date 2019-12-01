package com.duogesi.entities;

public class user_info {

    private int id;
    private int username;
    private int password;
    private int type;
    private int Post_like;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsername() {
        return username;
    }

    public void setUsername(int username) {
        this.username = username;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPost_like() {
        return Post_like;
    }

    public void setPost_like(int post_like) {
        Post_like = post_like;
    }
}
