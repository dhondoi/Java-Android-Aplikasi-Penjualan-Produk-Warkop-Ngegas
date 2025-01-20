package com.dhondoi.nonaseblak;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.dhondoi.nonaseblak.entity.Receipt;
import com.dhondoi.nonaseblak.repository.NumberReceiptRepository;
import com.dhondoi.nonaseblak.repository.ReceiptRepository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ReceiptCRUDTest {
    @Test
    public void addReceiptData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // all null
//        Receipt receipt = new Receipt(null, null, null, null);
        // name,status null
//        Receipt receipt = new Receipt(null, new Date().toString(), null, null);
        // status null
//        Receipt receipt = new Receipt(null, new Date().toString(), "noni", null);
        // normal test
        Receipt receipt = new Receipt(null, new Date().toString(), "doni", "0");
        ReceiptRepository receiptRepository = new ReceiptRepository(appContext);

        // return id
        long result = receiptRepository.saveData(receipt);
        Log.i(getClass().getSimpleName(), "addReceiptData: " + result);
        Assert.assertTrue(result > 0);
    }

    @Test
    public void readReceiptData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        long start = System.currentTimeMillis();
        List<Receipt> receipts = new ReceiptRepository(appContext).readData();
//        for (Receipt receipt : receipts) {
//            Log.i(getClass().getSimpleName(), receipt.toString());
//        }
        long timeResult = (System.currentTimeMillis() - start) / 1000;
        Log.i(getClass().getSimpleName(), "readReceiptData: Time Execution = " + timeResult + "s");
    }
    @Test
    public void readReceiptDataByDate() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        long start = System.currentTimeMillis();
//        List<Receipt> receipts = new ReceiptRepository(appContext).readDataByDate();
//        for (Receipt receipt : receipts) {
//            Log.i(getClass().getSimpleName(), receipt.toString());
//        }
        long timeResult = (System.currentTimeMillis() - start) / 1000;
        Log.i(getClass().getSimpleName(), "readReceiptData: Time Execution = " + timeResult + "s");
    }

    @Test
    public void updateReceiptData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // all null
//        Receipt receipt = new Receipt(null, null, null, null);
        // name,status null
//        Receipt receipt = new Receipt(null, new Date().toString(), null, null);
        // status null
//        Receipt receipt = new Receipt(null, new Date().toString(), "noni", null);
        // normal test
        Receipt receipt = new Receipt(1, null, null, "1");
        ReceiptRepository receiptRepository = new ReceiptRepository(appContext);

        // return id
        long result = receiptRepository.updateData(receipt);
        Log.i(getClass().getSimpleName(), "updateReceiptData: " + result);
        Assert.assertTrue(result > 0);
    }

    @Test
    public void deleteAllData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        new ReceiptRepository(appContext).deleteData();
    }
}