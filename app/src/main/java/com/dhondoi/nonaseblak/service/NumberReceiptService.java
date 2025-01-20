package com.dhondoi.nonaseblak.service;

import com.dhondoi.nonaseblak.entity.NumberReceipt;

import java.util.List;

public interface NumberReceiptService {
    List<NumberReceipt> getData();

    long save(Integer idReceipt, String note) throws Exception;

    long edit(Integer id, String note) throws Exception;
}
