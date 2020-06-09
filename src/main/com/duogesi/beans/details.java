package com.duogesi.beans;

import java.sql.Date;

public class details {
    private int id;
    private int ship_id;
    private int ctn;
    private float weight;
    private float volume;
    private Date cut_time;
    private Date cut_end;
    private int compensate;
    private Date dead_date;
    private int money;
    private String kill_start;
    private String kill_end;
    private int discount;
    private String ups;
    private String countDownHour;
    private String countDownMinute;
    private String countDownSecond;
    private int user_id;


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCountDownHour() {
        return countDownHour;
    }

    public void setCountDownHour(String countDownHour) {
        this.countDownHour = countDownHour;
    }

    public String getCountDownMinute() {
        return countDownMinute;
    }

    public void setCountDownMinute(String countDownMinute) {
        this.countDownMinute = countDownMinute;
    }

    public String getCountDownSecond() {
        return countDownSecond;
    }

    public void setCountDownSecond(String countDownSecond) {
        this.countDownSecond = countDownSecond;
    }

    public String getKill_end() {
        return kill_end;
    }

    public void setKill_end(String kill_end) {
        this.kill_end = kill_end;
    }

    public String getKill_start() {
        return kill_start;
    }

    public void setKill_start(String kill_start) {
        this.kill_start = kill_start;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getCtn() {
        return ctn;
    }

    public void setCtn(int ctn) {
        this.ctn = ctn;
    }

    public String getUps() {
        return ups;
    }

    public void setUps(String ups) {
        this.ups = ups;
    }

    public int getCompensate() {
        return compensate;
    }

    public void setCompensate(int compensate) {
        this.compensate = compensate;
    }

    public Date getDead_date() {
        return dead_date;
    }

    public void setDead_date(Date dead_date) {
        this.dead_date = dead_date;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Date getCut_time() {
        return cut_time;
    }

    public void setCut_time(Date cut_time) {
        this.cut_time = cut_time;
    }

    public Date getCut_end() {
        return cut_end;
    }

    public void setCut_end(Date cut_end) {
        this.cut_end = cut_end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShip_id() {
        return ship_id;
    }

    public void setShip_id(int ship_id) {
        this.ship_id = ship_id;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }


}
