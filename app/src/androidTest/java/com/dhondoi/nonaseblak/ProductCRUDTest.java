package com.dhondoi.nonaseblak;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.dhondoi.nonaseblak.entity.Product;
import com.dhondoi.nonaseblak.repository.ProductRepository;

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
public class ProductCRUDTest {
    @Test
    public void readProductData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        List<Product> data = new ProductRepository(appContext).readData();
        for (Product product : data) {
            Log.i("Product-" + product.getId(), product.toString());
        }
    }

    @Test
    public void addProductData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Long result = new ProductRepository(appContext).saveData(new Product(null, 1, "Tea Jus", 3_000L, ""));
        Log.i("Product", String.valueOf(result));
        readProductData();
        Assert.assertTrue(result > 0);
    }

    @Test
    public void updateProductData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Long result = new ProductRepository(appContext).updateData(new Product(1, 1, "Seblak", 10_000L, "Kerupuk"));
        Log.i("Product", String.valueOf(result));
        readProductData();
    }
}