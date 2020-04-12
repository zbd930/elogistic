package com.duogesi.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class xiaobao {

    private int xiaobao_id;
    private String desc;
    private String country;
    private String timetable;
    private String detail;
    private price_xiaobao price_xiaobao;
    private String method;
    private supplier_company supplier_company;
    private int  user_id;


    public com.duogesi.entities.supplier_company getSupplier_company() {
        return supplier_company;
    }

    public void setSupplier_company(com.duogesi.entities.supplier_company supplier_company) {
        this.supplier_company = supplier_company;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public com.duogesi.entities.price_xiaobao getPrice_xiaobao() {
        return price_xiaobao;
    }

    public void setPrice_xiaobao(com.duogesi.entities.price_xiaobao price_xiaobao) {
        this.price_xiaobao = price_xiaobao;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getXiaobao_id() {
        return xiaobao_id;
    }

    public void setXiaobao_id(int xiaobao_id) {
        this.xiaobao_id = xiaobao_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTimetable() {
        return timetable;
    }

    public void setTimetable(String timetable) {
        this.timetable = timetable;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "xiaobao{" +
                "xiaobao_id=" + xiaobao_id +
                ", desc='" + desc + '\'' +
                ", country='" + country + '\'' +
                ", timetable='" + timetable + '\'' +
                ", detail='" + detail + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
