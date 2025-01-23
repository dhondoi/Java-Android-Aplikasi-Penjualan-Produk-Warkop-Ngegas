package com.dhondoi.nonaseblak.service;

import android.content.Context;

import com.dhondoi.nonaseblak.entity.NumberReceipt;
import com.dhondoi.nonaseblak.entity.OrderHistory;
import com.dhondoi.nonaseblak.repository.OrderHistoryRepository;
import com.dhondoi.nonaseblak.util.IntegerCheckerUtil;

import java.util.LinkedList;
import java.util.List;

public class OrderHistoryServiceImpl implements OrderHistoryService {

    private OrderHistoryRepository orderHistoryRepository;

    public OrderHistoryServiceImpl(Context context) {
        this.orderHistoryRepository = new OrderHistoryRepository(context);
    }

    @Override
    public List<OrderHistory> getData() {
        return orderHistoryRepository.readData();
    }

    public List<OrderHistory> getDataByIdNumberReceipt(Integer numberReceiptId) {
        return orderHistoryRepository.readDataByIdNumberReceipt(numberReceiptId);
    }

    @Override
    public long save(OrderHistory orderHistory) throws Exception {

        Long numberReceiptId = Long.valueOf(orderHistory.getNumberReceiptId());
        Long productId = Long.valueOf(orderHistory.getProductId());
        Long quantity = Long.valueOf(orderHistory.getQuantity());
        Long total = Long.valueOf(orderHistory.getTotal());
        IntegerCheckerUtil.checkLessThan1(numberReceiptId, productId, quantity, total);

        return orderHistoryRepository.saveData(orderHistory);
    }


    public List<OrderHistory> getDataByListNumberReceipt(List<NumberReceipt> numberReceipts) {
        List<OrderHistory> orderHistories = new LinkedList<>();
        for (NumberReceipt numberReceipt : numberReceipts) {
            orderHistories.addAll(orderHistoryRepository.readDataByIdNumberReceipt(numberReceipt.getId()));
        }
        return orderHistories;
    }
}
