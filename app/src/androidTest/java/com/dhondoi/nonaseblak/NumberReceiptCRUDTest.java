package com.dhondoi.nonaseblak;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.dhondoi.nonaseblak.entity.NumberReceipt;
import com.dhondoi.nonaseblak.entity.Receipt;
import com.dhondoi.nonaseblak.repository.NumberReceiptRepository;
import com.dhondoi.nonaseblak.repository.OrderHistoryRepository;
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
public class NumberReceiptCRUDTest {
    @Test
    public void addNumberReceiptData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // normal test
        NumberReceipt numberReceipt = new NumberReceipt(null, 1, "pedas");
        // null receipt id test
//        NumberReceipt numberReceipt = new NumberReceipt(null, null, "pedas");
        // null note test
//        NumberReceipt numberReceipt = new NumberReceipt(null, 1, null);
        NumberReceiptRepository numberReceiptRepository = new NumberReceiptRepository(appContext);

        // return id
        long result = numberReceiptRepository.saveData(numberReceipt);
        Log.i(getClass().getSimpleName(), "addNumberReceiptData: " + result);
        Assert.assertTrue(result > 0);
    }

    @Test
    public void readNumberReceiptData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        List<NumberReceipt> numberReceipts = new NumberReceiptRepository(appContext).readData();
        for (NumberReceipt numberReceipt : numberReceipts) {
            Log.i(getClass().getSimpleName(), numberReceipt.toString());
        }
    }

    @Test
    public void updateNumberReceiptData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // normal test
        NumberReceipt numberReceipt = new NumberReceipt(1, null, "pedas");
        NumberReceiptRepository numberReceiptRepository = new NumberReceiptRepository(appContext);

        // return id
        long result = numberReceiptRepository.updateData(numberReceipt);
        Log.i(getClass().getSimpleName(), "updateNumberReceiptData: " + result);
        Assert.assertTrue(result > 0);
        readNumberReceiptData();
    }

    @Test
    public void deleteAllData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        new NumberReceiptRepository(appContext).deleteData();
    }
}