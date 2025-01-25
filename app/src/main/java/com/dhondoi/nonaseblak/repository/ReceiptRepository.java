package com.dhondoi.nonaseblak.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dhondoi.nonaseblak.entity.Receipt;
import com.dhondoi.nonaseblak.service.ReceiptServiceImpl;
import com.dhondoi.nonaseblak.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ReceiptRepository implements Repository<Receipt> {

    private DatabaseUtil databaseUtil;

    public ReceiptRepository(Context context) {
        this.databaseUtil = DatabaseUtil.getInstance(context);
    }

    @Override
    public List<Receipt> readData() {

        List<Receipt> receipts = new ArrayList<>();

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getReadableDatabase();
             Cursor cursor = sqLiteDatabase.query(DatabaseUtil.TABLE_RECEIPTS, null, null, null, null, null, null)) {

            int iId = cursor.getColumnIndex(DatabaseUtil.KEY_ID);
            int iDate = cursor.getColumnIndex(DatabaseUtil.KEY_DATE);
            int iName = cursor.getColumnIndex(DatabaseUtil.KEY_NAME);
            int iStatus = cursor.getColumnIndex(DatabaseUtil.KEY_STATUS);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Integer id = cursor.getInt(iId);
                String date = cursor.getString(iDate);
                String name = cursor.getString(iName);
                String status = cursor.getString(iStatus);

                receipts.add(new Receipt(id, date, name, status));
            }
        }

        return receipts;
    }

    public List<Receipt> readDataByDate(String startDate,String endDate) {
        List<Receipt> receipts = new ArrayList<>();
        String whereClause = String.format("%s > ? AND %s < ? AND %s = ?", DatabaseUtil.KEY_DATE, DatabaseUtil.KEY_DATE,DatabaseUtil.KEY_STATUS);
        String[] whereArgs = {startDate, endDate, ReceiptServiceImpl.FINISH};
        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getReadableDatabase();
             Cursor cursor = sqLiteDatabase.query(DatabaseUtil.TABLE_RECEIPTS, null, whereClause, whereArgs, null, null, null)) {

            int iId = cursor.getColumnIndex(DatabaseUtil.KEY_ID);
            int iDate = cursor.getColumnIndex(DatabaseUtil.KEY_DATE);
            int iName = cursor.getColumnIndex(DatabaseUtil.KEY_NAME);
            int iStatus = cursor.getColumnIndex(DatabaseUtil.KEY_STATUS);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Integer id = cursor.getInt(iId);
                String date = cursor.getString(iDate);
                String name = cursor.getString(iName);
                String status = cursor.getString(iStatus);

                receipts.add(new Receipt(id, date, name, status));
            }
        }

        return receipts;
    }

    @Override
    public long saveData(Receipt receipt) {
        long result;

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase()) {

            ContentValues contentValues = new ContentValues();

            contentValues.put(DatabaseUtil.KEY_DATE, receipt.getDate());
            contentValues.put(DatabaseUtil.KEY_NAME, receipt.getName());
            contentValues.put(DatabaseUtil.KEY_STATUS, receipt.getStatus());

            result = sqLiteDatabase.insert(DatabaseUtil.TABLE_RECEIPTS, null, contentValues);
            Log.i(getClass().getSimpleName(), "saveData(" + receipt + "): " + result);
        }

        return result;
    }

    @Override
    public long updateData(Receipt receipt) {
        long result;

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase()) {

            ContentValues contentValues = new ContentValues();

//            contentValues.put(DatabaseUtil.KEY_DATE, receipt.getDate());
//            contentValues.put(DatabaseUtil.KEY_NAME, receipt.getName());
            contentValues.put(DatabaseUtil.KEY_STATUS, receipt.getStatus());

            String whereClause = String.format("%s = ?", DatabaseUtil.KEY_ID);
            String[] whereArgs = {receipt.getId().toString()};

            result = sqLiteDatabase.update(DatabaseUtil.TABLE_RECEIPTS, contentValues, whereClause, whereArgs);
            Log.i(getClass().getSimpleName(), "updateData(" + receipt + "): " + result);
        }

        return result;
    }

    public int deleteData() {
        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase()) {
            return sqLiteDatabase.delete(DatabaseUtil.TABLE_RECEIPTS, null, null);
        }
    }

    public List<Receipt> readDataByStatus(String status) {
        List<Receipt> receipts = new ArrayList<>();
        String whereClause = String.format("%s = ?", DatabaseUtil.KEY_STATUS);
        String[] whereArgs = {status};
        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getReadableDatabase();
             Cursor cursor = sqLiteDatabase.query(DatabaseUtil.TABLE_RECEIPTS, null, whereClause, whereArgs, null, null, null)) {

            int iId = cursor.getColumnIndex(DatabaseUtil.KEY_ID);
            int iDate = cursor.getColumnIndex(DatabaseUtil.KEY_DATE);
            int iName = cursor.getColumnIndex(DatabaseUtil.KEY_NAME);
            int iStatus = cursor.getColumnIndex(DatabaseUtil.KEY_STATUS);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Integer id = cursor.getInt(iId);
                String date = cursor.getString(iDate);
                String name = cursor.getString(iName);
                String statusCol = cursor.getString(iStatus);

                receipts.add(new Receipt(id, date, name, statusCol));
            }
        }

        return receipts;
    }
}
