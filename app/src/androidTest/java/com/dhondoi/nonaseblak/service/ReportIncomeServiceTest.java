package com.dhondoi.nonaseblak.service;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.dhondoi.nonaseblak.entity.Category;
import com.dhondoi.nonaseblak.entity.NumberReceipt;
import com.dhondoi.nonaseblak.entity.OrderHistory;
import com.dhondoi.nonaseblak.entity.Receipt;
import com.dhondoi.nonaseblak.entity.Variant;
import com.dhondoi.nonaseblak.repository.CategoryRepository;
import com.dhondoi.nonaseblak.repository.VariantRepository;
import com.dhondoi.nonaseblak.util.DateUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ReportIncomeServiceTest {

    @Test
    public void createObject() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        long start = System.currentTimeMillis();
        ReportIncomeService reportIncomeService = new ReportIncomeService(appContext);
        long timeResult = (System.currentTimeMillis() - start) / 1000;
        Log.i(getClass().getSimpleName(), "createObject: Time Execution = " + timeResult + "s");
    }

    @Test
    public void getData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        long start = System.currentTimeMillis();
        ReportIncomeService reportIncomeService = new ReportIncomeService(appContext);
        reportIncomeService.getListReportIncome();
        long timeResult = (System.currentTimeMillis() - start) / 1000;
        Log.i(getClass().getSimpleName(), "getData: Time Execution = " + timeResult + "s");
    }

    @Test
    public void generateListReportIncome() throws ParseException {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        long start = System.currentTimeMillis();
//        reportIncomes = new ArrayList<>();
//        productService.getData();
        List<Receipt> receipts = new ReceiptServiceImpl(appContext).getData();
        List<NumberReceipt> numberReceipts = new NumberReceiptServiceImpl(appContext).getData();
        OrderHistoryServiceImpl orderHistoryService = new OrderHistoryServiceImpl(appContext);
        for (Receipt receipt : receipts) {
            String date = DateUtil.getStringDateForReport(receipt.getDate());
            for (NumberReceipt numberReceipt : numberReceipts) {
                if (numberReceipt.getReceiptId().equals(receipt.getId())) {
                    List<OrderHistory> orderHistories = orderHistoryService.getDataByIdNumberReceipt(numberReceipt.getId());
                    for (OrderHistory orderHistory : orderHistories) {
//                        if (orderHistory.getNumberReceiptId().equals(numberReceipt.getId())) {
////                            addToTheListReportIncome(date, orderHistory);
//                        }
                    }
                    break;
                }
            }
        }
        long timeResult = (System.currentTimeMillis() - start) / 1000;
        Log.i(getClass().getSimpleName(), "generateListReportIncome: Time Execution = " + timeResult + "s");
    }
}
