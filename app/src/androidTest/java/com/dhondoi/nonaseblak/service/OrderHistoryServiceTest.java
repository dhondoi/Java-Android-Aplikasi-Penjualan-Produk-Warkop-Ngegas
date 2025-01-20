package com.dhondoi.nonaseblak.service;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.dhondoi.nonaseblak.entity.OrderHistory;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class OrderHistoryServiceTest {

    @Test
    public void createObject() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        long start = System.currentTimeMillis();
        OrderHistoryServiceImpl orderHistoryService = new OrderHistoryServiceImpl(appContext);
        long timeResult = (System.currentTimeMillis() - start) / 1000;
        Log.i(getClass().getSimpleName(), "createObject: Time Execution = " + timeResult + "s");
    }

    @Test
    public void getData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        long start = System.currentTimeMillis();
        OrderHistoryServiceImpl orderHistoryService = new OrderHistoryServiceImpl(appContext);
        List<OrderHistory> orderHistories = orderHistoryService.getData();
        for (OrderHistory orderHistory : orderHistories) {
            Log.i(getClass().getSimpleName(), orderHistory.toString());
        }
        long timeResult = (System.currentTimeMillis() - start) / 1000;
        Log.i(getClass().getSimpleName(), "getData: Time Execution = " + timeResult + "s");
    }
}
