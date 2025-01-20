package com.dhondoi.nonaseblak;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.dhondoi.nonaseblak.entity.NumberReceipt;
import com.dhondoi.nonaseblak.entity.OrderHistory;
import com.dhondoi.nonaseblak.repository.NumberReceiptRepository;
import com.dhondoi.nonaseblak.repository.OrderHistoryRepository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class OrderHistoryCRUDTest {
    @Test
    public void addOrderHistoryData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // normal test
        OrderHistory orderHistory = new OrderHistory(1, 1, 1, 20000L);
        // null receipt id test
//        NumberReceipt orderHistory = new NumberReceipt(null, null, "pedas");
        // null note test
//        NumberReceipt orderHistory = new NumberReceipt(null, 1, null);
        OrderHistoryRepository orderHistoryRepository = new OrderHistoryRepository(appContext);

        // return id
        long result = orderHistoryRepository.saveData(orderHistory);

        Log.i(getClass().getSimpleName(), "addOrderHistoryData: " + result);
        Assert.assertTrue(result > 0);
    }

    @Test
    public void readOrderHistoryData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        List<OrderHistory> orderHistories = new OrderHistoryRepository(appContext).readData();
        for (OrderHistory orderHistory : orderHistories) {
            Log.i(getClass().getSimpleName(), orderHistory.toString());
        }
    }

    @Test
    public void deleteAllOrderHistoryData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        new OrderHistoryRepository(appContext).deleteData();
    }
//
//    @Test
//    public void updateOrderHistoryData() {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//
//        // normal test
//        NumberReceipt numberReceipt = new NumberReceipt(1, null, "pedas");
//        NumberReceiptRepository numberReceiptRepository = new NumberReceiptRepository(appContext);
//
//        // return id
//        long result = numberReceiptRepository.updateData(numberReceipt);
//        Log.i(getClass().getSimpleName(), "updateOrderHistoryData: " + result);
//        Assert.assertTrue(result > 0);
//        readNumberReceiptData();
//    }
}