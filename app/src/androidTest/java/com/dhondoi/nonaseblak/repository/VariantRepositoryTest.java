package com.dhondoi.nonaseblak.repository;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.dhondoi.nonaseblak.entity.Category;
import com.dhondoi.nonaseblak.entity.Variant;

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
public class VariantRepositoryTest {

    @Test
    public void readData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        List<Variant> variants = new VariantRepository(appContext).readData();
        for (Variant variant : variants) {
            Log.i(getClass().getSimpleName(), variant.toString());
        }
    }

    @Test
    public void addData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Variant variant = new Variant(null, "pedas");

        Long result = new VariantRepository(appContext).saveData(variant);

        Log.i(getClass().getSimpleName(), String.valueOf(result));
        readData();
        Assert.assertTrue(result > 0);
    }

    @Test
    public void updateCategoryData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Long result = new CategoryRepository(appContext).updateData(new Category(1, "dhondoi"));
        Log.i("Category", String.valueOf(result));
        readData();
    }
//    @Test
//    public void removeCategoryData() {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        Long result = new CategoryRepository(appContext).remove(new Category(3, "dhondoi"));
//        Log.i("Category", String.valueOf(result));
//        readCategoryData();
//    }
}