package com.dhondoi.nonaseblak.service;

import com.dhondoi.nonaseblak.entity.OrderHistory;

import java.util.List;

public interface OrderHistoryService {
    List<OrderHistory> getData();

    long save(OrderHistory orderHistory) throws Exception;

//    long edit(Integer id, String status);
}
