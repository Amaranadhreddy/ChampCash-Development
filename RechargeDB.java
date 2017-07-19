package com.tms.govt.champcash.home.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tms.govt.champcash.home.report.ChampcashRecharge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by govt on 11-05-2017.
 */

public class RechargeDB extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "RechargeLocalDB";
    // ChampCash table name
    private static final String TABLE_RECHARGE = "recharge";

    private static final String USER_MOBILE_NUMBER_PREPAID = "user_mobile_number_prepaid";
    private static final String USER_PREPAID = "user_prepaid";
    private static final String USER_AMOUNT_PREPAID = "user_amount_prepaid";

    public RechargeDB(Context applicationContext) {
        super(applicationContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TEMPLES_TABLE = "CREATE TABLE " + TABLE_RECHARGE + "("
                + USER_MOBILE_NUMBER_PREPAID + " TEXT,"
                + USER_PREPAID + " TEXT,"
                + USER_AMOUNT_PREPAID + " TEXT " + ")";

        db.execSQL(CREATE_TEMPLES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECHARGE);
        // Creating tables again
        onCreate(db);
    }

    public void addRecharge(ChampcashRecharge champcashRecharge) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_MOBILE_NUMBER_PREPAID, champcashRecharge.getUserMobileNumberPrepaid());
        values.put(USER_PREPAID, champcashRecharge.getUserOperatorPrepaid());
        values.put(USER_AMOUNT_PREPAID, champcashRecharge.getUserAmountPrepaid());

        // Inserting Row
        long result = db.insert(TABLE_RECHARGE, null, values);
        System.out.println(result);
        db.close(); // Closing database connection
    }

    public List<ChampcashRecharge> getAllRechargeDetails() {

        List<ChampcashRecharge> champcashRechargeList = new ArrayList<ChampcashRecharge>();

        try {
            // Select All Query
            String query = "SELECT * FROM " + TABLE_RECHARGE;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {

                do {

                    ChampcashRecharge champcashRecharge = new ChampcashRecharge();
                    champcashRecharge.setUserMobileNumberPrepaid(cursor.getString(0));
                    champcashRecharge.setUserOperatorPrepaid(cursor.getString(1));
                    champcashRecharge.setUserAmountPrepaid(cursor.getString(2));

                    champcashRechargeList.add(champcashRecharge);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return champcashRechargeList;
    }


}
