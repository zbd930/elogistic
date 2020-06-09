package com.duogesi.beans;

import java.sql.Date;

public class price {

    private float west;
    private float middle;
    private float east;
    private int user_id;
    private Date valid_date_start;
    private Date valid_date_end;


    public float getWest() {
        return west;
    }

    public void setWest(float west) {
        this.west = west;
    }

    public float getMiddle() {
        return middle;
    }

    public void setMiddle(float middle) {
        this.middle = middle;
    }

    public float getEast() {
        return east;
    }

    public void setEast(float east) {
        this.east = east;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getValid_date_start() {
        return valid_date_start;
    }

    public void setValid_date_start(Date valid_date_start) {
        this.valid_date_start = valid_date_start;
    }

    public Date getValid_date_end() {
        return valid_date_end;
    }

    public void setValid_date_end(Date valid_date_end) {
        this.valid_date_end = valid_date_end;
    }
}
