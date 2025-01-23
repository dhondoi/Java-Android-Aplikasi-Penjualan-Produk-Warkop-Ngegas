package com.dhondoi.nonaseblak.service;

import android.content.Context;
import android.util.Log;

import com.dhondoi.nonaseblak.entity.Receipt;
import com.dhondoi.nonaseblak.repository.ReceiptRepository;
import com.dhondoi.nonaseblak.util.DateUtil;
import com.dhondoi.nonaseblak.util.StringCheckerUtil;

import java.util.List;

public class ReceiptServiceImpl implements ReceiptService {

    private ReceiptRepository receiptRepository;

    public static final String FINISH = "1";
    public static final String START = "0";

    public ReceiptServiceImpl(Context context) {
        this.receiptRepository = new ReceiptRepository(context);
    }

    @Override
    public List<Receipt> getData() {
        return receiptRepository.readData();
    }

    @Override
    public long save(String name) throws Exception {

        StringCheckerUtil.checkedEmpty(name = name.trim().toLowerCase());
        String date = DateUtil.getStringDateNow();
        Receipt receipt = new Receipt(null, date, name, START);
        return receiptRepository.saveData(receipt);
    }

    @Override
    public long edit(Integer id, String status) {

        Receipt receipt = new Receipt(id, null, null, status);
        return receiptRepository.updateData(receipt);
    }

    public List<Receipt> getDataByDate(String startDate, String endDate) {
//        List<Receipt> receipts = receiptRepository.readDataByDate(startDate, endDate);

//        for (Receipt receipt : receipts) {
//            Log.i(getClass().getSimpleName(), receipt.toString());
//        }
        return receiptRepository.readDataByDate(startDate, endDate);
    }

    public List<Receipt> getDataByStatus(String status) {
        return receiptRepository.readDataByStatus(status);
    }
}
