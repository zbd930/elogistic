package com.duogesi.entities;

import java.math.BigDecimal;

public class amount {
    private int id;
    private int order_id;
    private BigDecimal paid;
    private BigDecimal total;
    private BigDecimal tax;
    private BigDecimal customer;
    private BigDecimal inspect;
    private BigDecimal local;
    private BigDecimal additional;
    private int item_id;
    private String unionId;
    private com.duogesi.entities.order order;
    private com.duogesi.entities.items items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getCustomer() {
        return customer;
    }

    public void setCustomer(BigDecimal customer) {
        this.customer = customer;
    }

    public BigDecimal getInspect() {
        return inspect;
    }

    public void setInspect(BigDecimal inspect) {
        this.inspect = inspect;
    }

    public BigDecimal getLocal() {
        return local;
    }

    public void setLocal(BigDecimal local) {
        this.local = local;
    }

    public BigDecimal getAdditional() {
        return additional;
    }

    public void setAdditional(BigDecimal additional) {
        this.additional = additional;
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

    public com.duogesi.entities.order getOrder() {
        return order;
    }

    public void setOrder(com.duogesi.entities.order order) {
        this.order = order;
    }

    public com.duogesi.entities.items getItems() {
        return items;
    }

    public void setItems(com.duogesi.entities.items items) {
        this.items = items;
    }
}
