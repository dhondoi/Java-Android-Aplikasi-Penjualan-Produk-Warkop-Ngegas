package com.dhondoi.nonaseblak.entity;

public class NumberReceipt {

    private Integer id;
    private Integer receiptId;
    private String note;

    public NumberReceipt(Integer id, Integer receiptId, String note) {
        this.id = id;
        this.receiptId = receiptId;
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Integer receiptId) {
        this.receiptId = receiptId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "NumberReceipt{" +
                "id=" + id +
                ", receiptId=" + receiptId +
                ", note='" + note + '\'' +
                '}';
    }
}
