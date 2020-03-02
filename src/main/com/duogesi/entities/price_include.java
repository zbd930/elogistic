package com.duogesi.entities;

import java.sql.Date;

public class price_include {
    private int id;
    private String area;
    private Date valid_date_start;
    private Date valid_date_end;
    private int user_id;
    private String way;
    private float one;
    private float three;
    private float five;
    private float ten;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public float getOne() {
        return one;
    }

    public void setOne(float one) {
        this.one = one;
    }

    public float getThree() {
        return three;
    }

    public void setThree(float three) {
        this.three = three;
    }

    public float getFive() {
        return five;
    }

    public void setFive(float five) {
        this.five = five;
    }

    public float getTen() {
        return ten;
    }

    public void setTen(float ten) {
        this.ten = ten;
    }
}
