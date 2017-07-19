package com.tms.govt.champcash.home.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tms.govt.champcash.home.report.CaptureData;
import com.tms.govt.champcash.home.slidemenu.FingerCaptureActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by govt on 10-05-2017.
 */

public class CaptureDB extends SQLiteOpenHelper {

    FingerCaptureActivity captureActivity;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TMSFingerDB";
    private static final String TABLE_FINGER_CAPTURE = "fingers";

    private static final String KEY_ID = "keyID";
    private static final String USER_ID = "userID";
    private static final String CAPTURE_DATE = "date";
    private static final String CAPTURED_IMAGE = "fingerImage";


    public CaptureDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TEMPLES_TABLE = "CREATE TABLE " + TABLE_FINGER_CAPTURE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_ID + " TEXT,"
                + CAPTURE_DATE + " TEXT,"
                + CAPTURED_IMAGE + " TEXT " + ")";

        db.execSQL(CREATE_TEMPLES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINGER_CAPTURE);
        // Creating tables again
        onCreate(db);
    }

    public void insertCaptureImg(CaptureData data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, data.getKeyID());
        values.put(USER_ID, data.getUserID());
        values.put(CAPTURE_DATE, data.getDate());
        values.put(CAPTURED_IMAGE, data.getImage());

        db.insert(TABLE_FINGER_CAPTURE, null, values);
        db.close();

    }

    public List<CaptureData> getCapturedFingerList(String userID, String currentDate) {

        List<CaptureData> dataList = new ArrayList<CaptureData>();

        try {

            // Select All Query
            String query = "SELECT * FROM " + TABLE_FINGER_CAPTURE + " WHERE " + USER_ID +"='"+ userID +"'"+ " AND " + CAPTURE_DATE +"='"+ currentDate +"'"+" ORDER BY " + KEY_ID + " DESC";
            // String query = "SELECT * FROM " + TABLE_FINGER_CAPTURE + " ORDER BY " + KEY_ID + " DESC";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {

                do {

                    CaptureData data = new CaptureData();
                    data.setKeyID(cursor.getString(0));
                    data.setUserID(cursor.getString(1));
                    data.setDate(cursor.getString(2));
                    data.setImage(cursor.getString(3));
                    dataList.add(data);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }
}
