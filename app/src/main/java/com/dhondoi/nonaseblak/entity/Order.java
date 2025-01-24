package com.dhondoi.nonaseblak.entity;

public class Order {
    private Product product;
    private Integer quantity;
    private Long total;

    public Order(Product product, Integer quantity, Long total) {
        this.product = product;
        this.quantity = quantity;
        this.total = total;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Order{" +
                "product=" + product +
                ", quantity=" + quantity +
                ", total=" + total +
                '}';
    }
}