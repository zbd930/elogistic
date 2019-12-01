package com.duogesi.entities;

import java.sql.Date;

public class details {
        private int id;
        private int ship_id;
        private float weight;
        private float volume;
        private Date cut_time;
        private Date cut_end;


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
