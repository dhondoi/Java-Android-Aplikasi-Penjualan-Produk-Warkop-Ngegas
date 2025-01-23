package com.dhondoi.nonaseblak.entity;

public class OrderHistory {

    private Integer numberReceiptId;

    private Integer productId;

    private String productName;

    private Integer quantity;

    private Long total;

    public OrderHistory(Integer numberReceiptId, Integer productId, String productName, Integer quantity, Long total) {
        this.numberReceiptId = numberReceiptId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.total = total;
    }

    public Integer getNumberReceiptId() {
        return numberReceiptId;
    }

    public void setNumberReceiptId(Integer numberReceiptId) {
        this.numberReceiptId = numberReceiptId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
        return "OrderHistory{" +
                "numberReceiptId=" + numberReceiptId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", total=" + total +
                '}';
    }

}
