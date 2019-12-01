package com.duogesi.entities;

public class order_details {
    private int id;
    private int ctn;
    private float weight;
    private float volume;
    private String beizhu;
    private String ups;
    private order order;
    private int order_id;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public com.duogesi.entities.order getOrder() {
        return order;
    }

    public void setOrder(com.duogesi.entities.order order) {
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCtn() {
        return ctn;
    }

    public void setCtn(int ctn) {
        this.ctn = ctn;
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

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getUps() {
        return ups;
    }

    public void setUps(String ups) {
        this.ups = ups;
    }
}
