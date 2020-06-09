package com.duogesi.beans;

public class order_details {
    private int id;
    private int ctn;
    private float weight;
    private float volume;
    private String beizhu;
    private String ups;
    private com.duogesi.beans.order order;
    private int order_id;
    private Boolean change;
    private String chaigui;
    private String category;
    private int user_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Boolean getChange() {
        return change;
    }

    public void setChange(Boolean change) {
        this.change = change;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getChaigui() {
        return chaigui;
    }

    public void setChaigui(String chaigui) {
        this.chaigui = chaigui;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public com.duogesi.beans.order getOrder() {
        return order;
    }

    public void setOrder(com.duogesi.beans.order order) {
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

    @Override
    public String toString() {
        return "order_details{" +
                "id=" + id +
                ", ctn=" + ctn +
                ", weight=" + weight +
                ", volume=" + volume +
                ", beizhu='" + beizhu + '\'' +
                ", ups='" + ups + '\'' +
                ", order=" + order +
                ", order_id=" + order_id +
                ", change=" + change +
                ", chaigui='" + chaigui + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
