package com.dhondoi.nonaseblak.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhondoi.nonaseblak.entity.Product;
import com.dhondoi.nonaseblak.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProductRepository implements Repository<Product> {


    private DatabaseUtil databaseUtil;

    public ProductRepository(Context context) {
        this.databaseUtil = DatabaseUtil.getInstance(context);
    }

    @Override
    public List<Product> readData() {
        List<Product> products;

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getReadableDatabase();
             Cursor cursor = sqLiteDatabase.query(DatabaseUtil.TABLE_PRODUCTS, null, null, null, null, null, DatabaseUtil.KEY_NAME)) {

            products = new ArrayList<>();

            int iId = cursor.getColumnIndex(DatabaseUtil.KEY_ID);
            int iCategoryId = cursor.getColumnIndex(DatabaseUtil.KEY_CATEGORY_ID);
            int iName = cursor.getColumnIndex(DatabaseUtil.KEY_NAME);
            int iPrice = cursor.getColumnIndex(DatabaseUtil.KEY_PRICE);
            int iDescription = cursor.getColumnIndex(DatabaseUtil.KEY_DESCRIPTION);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Integer id = cursor.getInt(iId);
                Integer categoryId = cursor.getInt(iCategoryId);
                String name = cursor.getString(iName);
                Long price = cursor.getLong(iPrice);
                String description = cursor.getString(iDescription);
                products.add(new Product(id, categoryId, name, price, description));
            }
        }

        return products;
    }

    @Override
    public long saveData(Product product) {
        long result;

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase()) {

            ContentValues contentValues = new ContentValues();

            Integer categoryId = product.getCategoryId();
            String name = product.getName().toLowerCase();
            Long price = product.getPrice();
            String description = product.getDescription().toLowerCase();

            contentValues.put(DatabaseUtil.KEY_CATEGORY_ID, categoryId);
            contentValues.put(DatabaseUtil.KEY_NAME, name);
            contentValues.put(DatabaseUtil.KEY_PRICE, price);
            contentValues.put(DatabaseUtil.KEY_DESCRIPTION, description);

            result = sqLiteDatabase.insert(DatabaseUtil.TABLE_PRODUCTS, null, contentValues);
        }

        return result;
    }

    @Override
    public long updateData(Product product) {
        long result;

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase()) {

            ContentValues contentValues = new ContentValues();

//            Integer categoryId = product.getCategoryId();
            String name = product.getName().toLowerCase();
            Long price = product.getPrice();
            String description = product.getDescription().toLowerCase();

//            contentValues.put(DatabaseUtil.KEY_CATEGORY_ID, categoryId);
            contentValues.put(DatabaseUtil.KEY_NAME, name);
            contentValues.put(DatabaseUtil.KEY_PRICE, price);
            contentValues.put(DatabaseUtil.KEY_DESCRIPTION, description);

            String whereClause = String.format("%s = ?", DatabaseUtil.KEY_ID);
            String[] whereArgs = {product.getId().toString()};

            result = sqLiteDatabase.update(DatabaseUtil.TABLE_PRODUCTS, contentValues, whereClause, whereArgs);
        }

        return result;
    }
}
