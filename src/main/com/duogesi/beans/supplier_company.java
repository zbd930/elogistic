package com.duogesi.beans;

import org.springframework.stereotype.Component;

@Component
public class supplier_company {
    private int supplier_id;
    private int user_id;
    private String contact_name;
    private String contact_phone;
    private String contact_mail;
    private String contact_address;
    private String company_name;
    private String peichang;
    private com.duogesi.beans.items items;

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getPeichang() {
        return peichang;
    }

    public void setPeichang(String peichang) {
        this.peichang = peichang;
    }

    public com.duogesi.beans.items getItems() {
        return items;
    }

    public void setItems(com.duogesi.beans.items items) {
        this.items = items;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public int getId() {
        return supplier_id;
    }

    public void setId(int id) {
        this.supplier_id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getContact_mail() {
        return contact_mail;
    }

    public void setContact_mail(String contact_mail) {
        this.contact_mail = contact_mail;
    }

    public String getContact_address() {
        return contact_address;
    }

    public void setContact_address(String contact_address) {
        this.contact_address = contact_address;
    }
}
