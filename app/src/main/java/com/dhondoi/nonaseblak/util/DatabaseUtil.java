package com.dhondoi.nonaseblak.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseUtil extends SQLiteOpenHelper {

    public static final String TABLE_VARIANTS = "variants";

    public static final String TABLE_CATEGORIES = "categories";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";

    public static final String TABLE_PRODUCTS = "products";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_PRICE = "price";
    public static final String KEY_DESCRIPTION = "description";

    public static final String TABLE_RECEIPTS = "receipts";
    public static final String KEY_DATE = "date";
    public static final String KEY_STATUS = "status";

    public static final String TABLE_NUMBER_RECEIPTS = "number_receipts";
    public static final String KEY_RECEIPT_ID = "receipt_id";
    public static final String KEY_NOTE = "note";

    public static final String TABLE_ORDER_HISTORY = "order_history";
    public static final String KEY_NUMBER_RECEIPT_ID = "number_receipt_id";
    public static final String KEY_PRODUCT_ID = "product_id";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_TOTAL = "total";

    private static final String DB_NAME = "db_nona_seblak";
    private static final int DB_VER = 1;

    private static DatabaseUtil databaseUtil;

    public static synchronized DatabaseUtil getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (databaseUtil == null) {
            databaseUtil = new DatabaseUtil(context.getApplicationContext());
        }
        return databaseUtil;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DatabaseUtil(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table Category
        String sql = "CREATE TABLE  " + TABLE_CATEGORIES
                + "( "
                + KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME
                + " TEXT NOT NULL UNIQUE);";
        db.execSQL(sql);
        // create table product
        sql = "CREATE TABLE " +
                TABLE_PRODUCTS +
                " ( " +
                KEY_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_CATEGORY_ID +
                " INTEGER REFERENCES " +
                TABLE_CATEGORIES +
                ", " +
                KEY_NAME +
                " TEXT NOT NULL, " +
                KEY_PRICE +
                " INTEGER NOT NULL, " +
                KEY_DESCRIPTION +
                " TEXT );";
        db.execSQL(sql);
        // create table receipt
        sql = "CREATE TABLE " +
                TABLE_RECEIPTS +
                " ( " +
                KEY_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_DATE +
                " TEXT NOT NULL, " +
                KEY_NAME +
                " TEXT NOT NULL, " +
                KEY_STATUS +
                " TEXT NOT NULL );";
        db.execSQL(sql);
        // create table number receipt
        sql = "CREATE TABLE " +
                TABLE_NUMBER_RECEIPTS +
                " ( " +
                KEY_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_RECEIPT_ID +
                " INTEGER NOT NULL, " +
                KEY_NOTE +
                " TEXT NOT NULL, FOREIGN KEY (" +
                KEY_RECEIPT_ID +
                ") REFERENCES " +
                TABLE_RECEIPTS +
                "(" +
                KEY_ID +
                ") ON DELETE RESTRICT ON UPDATE RESTRICT);";
        db.execSQL(sql);
        // create table order history
        sql = "CREATE TABLE " +
                TABLE_ORDER_HISTORY +
                " ( " +
                KEY_NUMBER_RECEIPT_ID +
                " INTEGER NOT NULL, " +
                KEY_PRODUCT_ID +
                " INTEGER NOT NULL, " +
                KEY_QUANTITY +
                " INTEGER NOT NULL, " +
                KEY_TOTAL +
                " INTEGER NOT NULL, PRIMARY KEY (" +
                KEY_NUMBER_RECEIPT_ID +
                ", " +
                KEY_PRODUCT_ID +
                "), FOREIGN KEY (" +
                KEY_NUMBER_RECEIPT_ID +
                ") REFERENCES " +
                TABLE_NUMBER_RECEIPTS +
                "(" +
                KEY_ID +
                ") ON DELETE RESTRICT ON UPDATE RESTRICT, FOREIGN KEY (" +
                KEY_PRODUCT_ID +
                ") REFERENCES " +
                TABLE_PRODUCTS +
                " (" +
                KEY_ID +
                ") ON DELETE RESTRICT ON UPDATE RESTRICT);";
        db.execSQL(sql);
        // create table Category
        sql = "CREATE TABLE  " + TABLE_VARIANTS
                + "( "
                + KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME
                + " TEXT NOT NULL UNIQUE);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_CATEGORIES;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + TABLE_PRODUCTS;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + TABLE_RECEIPTS;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + TABLE_ORDER_HISTORY;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + TABLE_VARIANTS;
        db.execSQL(sql);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
