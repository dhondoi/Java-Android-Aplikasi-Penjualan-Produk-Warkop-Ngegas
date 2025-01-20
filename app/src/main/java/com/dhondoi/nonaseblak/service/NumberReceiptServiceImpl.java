package com.dhondoi.nonaseblak.service;

import android.content.Context;

import com.dhondoi.nonaseblak.entity.NumberReceipt;
import com.dhondoi.nonaseblak.repository.NumberReceiptRepository;
import com.dhondoi.nonaseblak.util.IntegerCheckerUtil;
import com.dhondoi.nonaseblak.util.StringCheckerUtil;

import java.util.List;

public class NumberReceiptServiceImpl implements NumberReceiptService {

    private NumberReceiptRepository numberReceiptRepository;

    public NumberReceiptServiceImpl(Context context) {
        this.numberReceiptRepository = new NumberReceiptRepository(context);
    }

    @Override
    public List<NumberReceipt> getData() {
        return numberReceiptRepository.readData();
    }

    public List<NumberReceipt> getDataByIdReceipt(Integer receiptId) {
        return numberReceiptRepository.readDataByIdReceipt(receiptId);
    }

    @Override
    public long save(Integer idReceipt, String note) throws Exception {

        StringCheckerUtil.checkedEmpty(note);
        note = note.trim().toLowerCase();
        IntegerCheckerUtil.checkLessThan1(Long.valueOf(idReceipt));

        NumberReceipt numberReceipt = new NumberReceipt(null, idReceipt, note);
        return numberReceiptRepository.saveData(numberReceipt);
    }

    @Override
    public long edit(Integer id, String note) throws Exception {

        StringCheckerUtil.checkedEmpty(note);
        note = note.trim().toLowerCase();

        NumberReceipt numberReceipt = new NumberReceipt(id, null, note);
        return numberReceiptRepository.updateData(numberReceipt);
    }

}
