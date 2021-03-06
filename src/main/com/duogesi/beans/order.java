package com.duogesi.beans;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Date;

@Component
public class order implements Serializable {
    private int item_id;
    private String unionId;
    private String numbers;
    private int id;
    private int address_id;
    private int status;
    private String statu;
    private com.duogesi.beans.items items;
    private Date picking;
    private String dest;
    private String songhuo;
    private Boolean tihuo;
    private order_details order_details;

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    public String getSonghuo() {
        return songhuo;
    }

    public void setSonghuo(String songhuo) {
        this.songhuo = songhuo;
    }

    public com.duogesi.beans.order_details getOrder_details() {
        return order_details;
    }

    public void setOrder_details(com.duogesi.beans.order_details order_details) {
        this.order_details = order_details;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public Boolean getTihuo() {
        return tihuo;
    }

    public void setTihuo(Boolean tihuo) {
        this.tihuo = tihuo;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public com.duogesi.beans.items getItems() {
        return items;
    }

    public void setItems(com.duogesi.beans.items items) {
        this.items = items;
    }

    public Date getPicking() {
        return picking;
    }

    public void setPicking(Date picking) {
        this.picking = picking;
    }

    @Override
    public String toString() {
        return "order{" +
                "item_id=" + item_id +
                ", unionId='" + unionId + '\'' +
                ", numbers='" + numbers + '\'' +
                ", id=" + id +
                ", address_id=" + address_id +
                ", status=" + status +
                ", statu='" + statu + '\'' +
                ", items=" + items +
                ", picking=" + picking +
                ", dest='" + dest + '\'' +
                ", songhuo='" + songhuo + '\'' +
                ", tihuo=" + tihuo +
                ", order_details=" + order_details +
                '}';
    }
}
