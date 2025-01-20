package com.dhondoi.nonaseblak.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dhondoi.nonaseblak.entity.NumberReceipt;
import com.dhondoi.nonaseblak.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NumberReceiptRepository implements Repository<NumberReceipt> {

    private DatabaseUtil databaseUtil;

    public NumberReceiptRepository(Context context) {
        this.databaseUtil = DatabaseUtil.getInstance(context);
    }

    @Override
    public List<NumberReceipt> readData() {

        List<NumberReceipt> numberReceipts = new ArrayList<>();

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getReadableDatabase();
             Cursor cursor = sqLiteDatabase.query(DatabaseUtil.TABLE_NUMBER_RECEIPTS, null, null, null, null, null, null)) {

            int iId = cursor.getColumnIndex(DatabaseUtil.KEY_ID);
            int iReceiptId = cursor.getColumnIndex(DatabaseUtil.KEY_RECEIPT_ID);
            int iNote = cursor.getColumnIndex(DatabaseUtil.KEY_NOTE);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Integer id = cursor.getInt(iId);
                Integer receiptId = cursor.getInt(iReceiptId);
                String note = cursor.getString(iNote);

                numberReceipts.add(new NumberReceipt(id, receiptId, note));
            }
        }

        return numberReceipts;
    }

    @Override
    public long saveData(NumberReceipt numberReceipt) {
        long result;

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase()) {


            ContentValues contentValues = new ContentValues();

            contentValues.put(DatabaseUtil.KEY_RECEIPT_ID, numberReceipt.getReceiptId());
            contentValues.put(DatabaseUtil.KEY_NOTE, numberReceipt.getNote());

            result = sqLiteDatabase.insert(DatabaseUtil.TABLE_NUMBER_RECEIPTS, null, contentValues);
            Log.i(getClass().getSimpleName(), "saveData(" + numberReceipt + "): " + result);
        }

        return result;
    }

    @Override
    public long updateData(NumberReceipt numberReceipt) {
        long result;

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase()) {

            ContentValues contentValues = new ContentValues();

            contentValues.put(DatabaseUtil.KEY_NOTE, numberReceipt.getNote());

            String whereClause = String.format("%s = ?", DatabaseUtil.KEY_ID);
            String[] whereArgs = {numberReceipt.getId().toString()};

            result = sqLiteDatabase.update(DatabaseUtil.TABLE_NUMBER_RECEIPTS, contentValues, whereClause, whereArgs);
            Log.i(getClass().getSimpleName(), "updateData(" + numberReceipt + "): " + result);
        }

        return result;
    }

    public int deleteData() {
        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase()) {
            return sqLiteDatabase.delete(DatabaseUtil.TABLE_NUMBER_RECEIPTS, null, null);
        }
    }

    public List<NumberReceipt> readDataByIdReceipt(Integer receiptId) {
        List<NumberReceipt> numberReceipts = new ArrayList<>();
        String whereClause = String.format("%s = ?", DatabaseUtil.KEY_RECEIPT_ID);
        String[] whereArgs = {receiptId.toString()};
        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getReadableDatabase();
             Cursor cursor = sqLiteDatabase.query(DatabaseUtil.TABLE_NUMBER_RECEIPTS, null, whereClause, whereArgs, null, null, null)) {

            int iId = cursor.getColumnIndex(DatabaseUtil.KEY_ID);
            int iReceiptId = cursor.getColumnIndex(DatabaseUtil.KEY_RECEIPT_ID);
            int iNote = cursor.getColumnIndex(DatabaseUtil.KEY_NOTE);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Integer id = cursor.getInt(iId);
                Integer receiptId2 = cursor.getInt(iReceiptId);
                String note = cursor.getString(iNote);

                numberReceipts.add(new NumberReceipt(id, receiptId2, note));
            }
        }

        return numberReceipts;
    }
}
