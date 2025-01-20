package com.dhondoi.nonaseblak.repository;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhondoi.nonaseblak.entity.Variant;
import com.dhondoi.nonaseblak.util.DatabaseUtil;

import java.util.LinkedList;
import java.util.List;

public class VariantRepository implements Repository<Variant> {

    private DatabaseUtil databaseUtil;

    public VariantRepository(Context context) {
        this.databaseUtil = DatabaseUtil.getInstance(context);
    }

    @Override
    public List<Variant> readData() {
        List<Variant> variants;

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getReadableDatabase();
             Cursor cursor = sqLiteDatabase.query(DatabaseUtil.TABLE_VARIANTS, null, null, null, null, null, null)) {

            variants = new LinkedList<>();
            int iId = cursor.getColumnIndex(DatabaseUtil.KEY_ID);
            int iName = cursor.getColumnIndex(DatabaseUtil.KEY_NAME);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Integer id = cursor.getInt(iId);
                String name = cursor.getString(iName);
                variants.add(new Variant(id, name));
            }
        }

        return variants;
    }

    @Override
    public long saveData(Variant variant) {
        long result;

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase()) {

            ContentValues contentValues = new ContentValues();
            String name = variant.getName().toLowerCase();
            contentValues.put(DatabaseUtil.KEY_NAME, name);

            result = sqLiteDatabase.insert(DatabaseUtil.TABLE_VARIANTS, null, contentValues);
        }

        return result;
    }

    @Override
    public long updateData(Variant variant) {
        long result;

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase()) {

            ContentValues contentValues = new ContentValues();
            String name = variant.getName().toLowerCase();
            contentValues.put(DatabaseUtil.KEY_NAME, name);

            String whereClause = String.format("%s = ?", DatabaseUtil.KEY_ID);
            String[] whereArgs = {variant.getId().toString()};

//            Log.i("Category Repository", "edit: "+contentValues);
            result = sqLiteDatabase.update(DatabaseUtil.TABLE_VARIANTS, contentValues, whereClause, whereArgs);
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
