package com.dhondoi.nonaseblak.entity;

public class ReportIncome {

    private String date;
    private String name;
    private Long price;
    private Integer amount;
    private Long total;

    public ReportIncome(String date, String name, Long price, Integer amount, Long total) {
        this.date = date;
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Integer getAmount() {
        return amount;
    }

    public Long getTotal() {
        return total;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
