package com.dhondoi.nonaseblak.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dhondoi.nonaseblak.entity.OrderHistory;
import com.dhondoi.nonaseblak.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OrderHistoryRepository implements Repository<OrderHistory> {

    private DatabaseUtil databaseUtil;

    public OrderHistoryRepository(Context context) {
        this.databaseUtil = DatabaseUtil.getInstance(context);
    }

    @Override
    public List<OrderHistory> readData() {
        List<OrderHistory> orderHistories = new ArrayList<>();

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getReadableDatabase();
             Cursor cursor = sqLiteDatabase.query(DatabaseUtil.TABLE_ORDER_HISTORY, null, null, null, null, null, null)) {

            int iNumberReceiptId = cursor.getColumnIndex(DatabaseUtil.KEY_NUMBER_RECEIPT_ID);
            int iProductId = cursor.getColumnIndex(DatabaseUtil.KEY_PRODUCT_ID);
            int iQuantity = cursor.getColumnIndex(DatabaseUtil.KEY_QUANTITY);
            int iTotal = cursor.getColumnIndex(DatabaseUtil.KEY_TOTAL);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Integer numberReceiptId = cursor.getInt(iNumberReceiptId);
                Integer productId = cursor.getInt(iProductId);
                Integer quantity = cursor.getInt(iQuantity);
                Long total = cursor.getLong(iTotal);

                orderHistories.add(new OrderHistory(numberReceiptId, productId, quantity, total));
            }
        }

        return orderHistories;
    }

    public List<OrderHistory> readDataByIdNumberReceipt(Integer numberReceiptId) {
        List<OrderHistory> orderHistories = new ArrayList<>();
        String whereClause = String.format("%s = ?", DatabaseUtil.KEY_NUMBER_RECEIPT_ID);
        String[] whereArgs = {numberReceiptId.toString()};
        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getReadableDatabase();
             Cursor cursor = sqLiteDatabase.query(DatabaseUtil.TABLE_ORDER_HISTORY, null, whereClause, whereArgs, null, null, null)) {

            int iNumberReceiptId = cursor.getColumnIndex(DatabaseUtil.KEY_NUMBER_RECEIPT_ID);
            int iProductId = cursor.getColumnIndex(DatabaseUtil.KEY_PRODUCT_ID);
            int iQuantity = cursor.getColumnIndex(DatabaseUtil.KEY_QUANTITY);
            int iTotal = cursor.getColumnIndex(DatabaseUtil.KEY_TOTAL);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Integer numberReceiptId2 = cursor.getInt(iNumberReceiptId);
                Integer productId = cursor.getInt(iProductId);
                Integer quantity = cursor.getInt(iQuantity);
                Long total = cursor.getLong(iTotal);

                orderHistories.add(new OrderHistory(numberReceiptId2, productId, quantity, total));
            }
        }

        return orderHistories;
    }

    @Override
    public long saveData(OrderHistory orderHistory) {
        long result;

        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase()) {


            ContentValues contentValues = new ContentValues();

            contentValues.put(DatabaseUtil.KEY_NUMBER_RECEIPT_ID, orderHistory.getNumberReceiptId());
            contentValues.put(DatabaseUtil.KEY_PRODUCT_ID, orderHistory.getProductId());
            contentValues.put(DatabaseUtil.KEY_QUANTITY, orderHistory.getQuantity());
            contentValues.put(DatabaseUtil.KEY_TOTAL, orderHistory.getTotal());

            result = sqLiteDatabase.insert(DatabaseUtil.TABLE_ORDER_HISTORY, null, contentValues);
            Log.i(getClass().getSimpleName(), "saveData(" + orderHistory + "): " + result);
        }

        return result;
    }

    @Override
    public long updateData(OrderHistory orderHistory) {
//        long result;
//
//        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase()) {
//
//            ContentValues contentValues = new ContentValues();
//
//            contentValues.put(DatabaseUtil.KEY_NOTE, orderHistory.);
//
//            String whereClause = String.format("%s = ?", DatabaseUtil.KEY_ID);
//            String[] whereArgs = {numberReceipt.getId().toString()};
//
//            result = sqLiteDatabase.update(DatabaseUtil.TABLE_NUMBER_RECEIPTS, contentValues, whereClause, whereArgs);
//            Log.i(getClass().getSimpleName(), "updateData(" + numberReceipt + "): " + result);
//        }
//
//        return result;
        return -1;
    }

    public int deleteData() {
        try (SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase()) {
            return sqLiteDatabase.delete(DatabaseUtil.TABLE_ORDER_HISTORY, null, null);
        }
    }


}
