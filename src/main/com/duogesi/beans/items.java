package com.duogesi.beans;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Component
public class items implements Serializable {

    private int id;
    private String qiyungang;
    private String mudigang;
    private String method;
    private String country;
    private Date etd;
    private Date eta;
    private String desc;
    private details details;
    private int status;
    private String statu;
    private int user_id;
    private float discount;
    private List<order> orders;
    private List<supplier_company> supplier_companies;
    private price_include myprices;
    private addition addition;
    private float price;
    private int like;
    private amount amount;
    private order_details order_details;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    public List<supplier_company> getSupplier_companies() {
        return supplier_companies;
    }

    public void setSupplier_companies(List<supplier_company> supplier_companies) {
        this.supplier_companies = supplier_companies;
    }

    public com.duogesi.beans.addition getAddition() {
        return addition;
    }

    public void setAddition(com.duogesi.beans.addition addition) {
        this.addition = addition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public com.duogesi.beans.details getDetails() {
        return details;
    }

    public void setDetails(com.duogesi.beans.details details) {
        this.details = details;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public List<order> getOrders() {
        return orders;
    }

    public void setOrders(List<order> orders) {
        this.orders = orders;
    }


    public price_include getMyprices() {
        return myprices;
    }

    public void setMyprices(price_include myprices) {
        this.myprices = myprices;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public com.duogesi.beans.amount getAmount() {
        return amount;
    }

    public void setAmount(com.duogesi.beans.amount amount) {
        this.amount = amount;
    }

    public com.duogesi.beans.order_details getOrder_details() {
        return order_details;
    }

    public void setOrder_details(com.duogesi.beans.order_details order_details) {
        this.order_details = order_details;
    }

    @Override
    public String toString() {
        return "items{" +
                "id=" + id +
                ", qiyungang='" + qiyungang + '\'' +
                ", mudigang='" + mudigang + '\'' +
                ", method='" + method + '\'' +
                ", country='" + country + '\'' +
                ", etd=" + etd +
                ", eta=" + eta +
                ", desc='" + desc + '\'' +
                ", details=" + details +
                ", status=" + status +
                ", statu='" + statu + '\'' +
                ", user_id=" + user_id +
                ", discount=" + discount +
                ", orders=" + orders +
                ", supplier_companies=" + supplier_companies +
                ", myprices=" + myprices +
                ", addition=" + addition +
                ", price=" + price +
                ", like=" + like +
                ", amount=" + amount +
                ", order_details=" + order_details +
                '}';
    }
}
