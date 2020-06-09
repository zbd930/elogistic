package com.duogesi.beans;

public class comments {
    private int id;
    private String comments_text;
    private int user_id;
    private int address_id;
    private int star;
    private com.duogesi.beans.address address;

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public com.duogesi.beans.address getAddress() {
        return address;
    }

    public void setAddress(com.duogesi.beans.address address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComments_text() {
        return comments_text;
    }

    public void setComments_text(String comments_text) {
        this.comments_text = comments_text;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }
}
