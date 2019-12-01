package com.duogesi.entities;

import java.sql.Date;

public class price_haika {

    private String mudigang;
    private String qiyungang;
    private float price;
    private int user_id;
    private Date valid_date_start;
    private Date valid_date_end;

    public String getQiyungang() {
        return qiyungang;
    }

    public void setQiyungang(String qiyungang) {
        this.qiyungang = qiyungang;
    }

    public String getMudigang() {
        return mudigang;
    }

    public void setMudigang(String mudigang) {
        this.mudigang = mudigang;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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
