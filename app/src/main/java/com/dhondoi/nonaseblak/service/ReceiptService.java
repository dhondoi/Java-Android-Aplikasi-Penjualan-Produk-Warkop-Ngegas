package com.dhondoi.nonaseblak.service;

import com.dhondoi.nonaseblak.entity.Receipt;

import java.util.List;

public interface ReceiptService {
    List<Receipt> getData();

    long save(String name) throws Exception;

    long edit(Integer id, String status);
}
