package com.dhondoi.nonaseblak.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhondoi.nonaseblak.entity.Category;
import com.dhondoi.nonaseblak.util.DatabaseUtil;

import java.util.LinkedList;
import java.util.List;

public class CategoryRepository implements Repository<Category> {

    private DatabaseUtil databaseUtil;

    public CategoryRepository(Context context) {
        this.databaseUtil = DatabaseUtil.getInstance(context);
    }

    @Override
    public List<Category> readData() {
        List<Category> categories;

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getReadableDatabase();
             Cursor cursor = sqLiteDatabase.query(DatabaseUtil.TABLE_CATEGORIES, null, null, null, null, null, null)) {

            categories = new LinkedList<>();
            int iId = cursor.getColumnIndex(DatabaseUtil.KEY_ID);
            int iName = cursor.getColumnIndex(DatabaseUtil.KEY_NAME);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Integer id = cursor.getInt(iId);
                String name = cursor.getString(iName);
                categories.add(new Category(id, name));
            }
        }

        return categories;
    }

    @Override
    public long saveData(Category category) {
        long result;

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase()) {

            ContentValues contentValues = new ContentValues();
            String name = category.getName().toLowerCase();
            contentValues.put(DatabaseUtil.KEY_NAME, name);

            result = sqLiteDatabase.insert(DatabaseUtil.TABLE_CATEGORIES, null, contentValues);
        }

        return result;
    }

    @Override
    public long updateData(Category category) {
        long result;

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase()) {

            ContentValues contentValues = new ContentValues();
            String name = category.getName().toLowerCase();
            contentValues.put(DatabaseUtil.KEY_NAME, name);

            String whereClause = String.format("%s = ?", DatabaseUtil.KEY_ID);
            String[] whereArgs = {category.getId().toString()};

//            Log.i("Category Repository", "edit: "+contentValues);
            result = sqLiteDatabase.update(DatabaseUtil.TABLE_CATEGORIES, contentValues, whereClause, whereArgs);
        }

        return result;
    }


//    public long remove(Category category) {
//        long result;
//
//        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase()) {
//
//            String whereClause = String.format("%s = ?", DatabaseUtil.KEY_ID);
//            String[] whereArgs = {category.getId().toString()};
//
//            result = sqLiteDatabase.delete(DatabaseUtil.TABLE_CATEGORIES, whereClause, whereArgs);
//        }
//
//        return result;
//    }
}
