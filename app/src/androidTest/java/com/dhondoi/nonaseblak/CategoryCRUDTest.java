package com.dhondoi.nonaseblak;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.dhondoi.nonaseblak.entity.Category;
import com.dhondoi.nonaseblak.repository.CategoryRepository;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CategoryCRUDTest {
    @Test
    public void readCategoryData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        List<Category> data = new CategoryRepository(appContext).readData();
        for (Category category : data) {
            Log.i("Category-" + category.getId(), category.getName());
        }
    }

    @Test
    public void addCategoryData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Long result = new CategoryRepository(appContext).saveData(new Category(null, "a"));
        result = new CategoryRepository(appContext).saveData(new Category(null, "b"));
        Log.i("Category", String.valueOf(result));
        readCategoryData();
        Assert.assertTrue(result > 0);
    }

    @Test
    public void updateCategoryData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Long result = new CategoryRepository(appContext).updateData(new Category(1, "dhondoi"));
        Log.i("Category", String.valueOf(result));
        readCategoryData();
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