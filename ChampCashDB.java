package com.tms.govt.champcash.home.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.tms.govt.champcash.home.report.Champcash;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by govt on 18-04-2017.
 */

public class ChampCashDB extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ChampCashLocalDB";
    // ChampCash table name
    private static final String TABLE_CHAMPCASH = "champcash";

     Context con;

    public static final String IMAGE_ID = "id";
//    public static final String IMAGE = "image";

    // Temples Table Columns names
    private static final String KEY_ID = "id";
    private static final String USER_NAME = "user_name";
    private static final String EMAIL_ID = "email_id";
    private static final String PASSWORD = "password";
    private static final String DATE_OF_BIRTH = "date_of_birth";
    private static final String MOBILE_NUMBER = "mobile_number";
    private static final String COUNTRY = "country";
    private static final String STATE = "state";
    private static final String CITY = "city";
    private static final String IMAGE = "image";

    private static final String USER_MOBILE_NUMBER_PREPAID = "user_mobile_number_prepaid";
    private static final String USER_PREPAID = "user_prepaid";
    private static final String USER_AMOUNT_PREPAID = "user_amount_prepaid";

    public ChampCashDB(Context applicationContext) {

        super(applicationContext, DATABASE_NAME, null, DATABASE_VERSION);
        this.con=applicationContext;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TEMPLES_TABLE = "CREATE TABLE " + TABLE_CHAMPCASH + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_NAME + " TEXT,"
                + EMAIL_ID + " TEXT,"
                + PASSWORD + " TEXT,"
                + DATE_OF_BIRTH + " TEXT ,"
                + MOBILE_NUMBER + " TEXT,"
                + COUNTRY + " TEXT,"
                + STATE + " TEXT,"
                + CITY + " TEXT,"
                + IMAGE + " TEXT " + ")";

        db.execSQL(CREATE_TEMPLES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAMPCASH);

        // Creating tables again
        onCreate(db);
    }

    public void addChampcash(Champcash champcash) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_NAME, champcash.getUserName());
        values.put(EMAIL_ID, champcash.getEmailID());
        values.put(PASSWORD, champcash.getPasword());
        values.put(DATE_OF_BIRTH, champcash.getDateOfBirth());
        values.put(MOBILE_NUMBER, champcash.getMobileNumber());
        values.put(COUNTRY, champcash.getCountry());
        values.put(STATE, champcash.getState());
        values.put(CITY, champcash.getCity());
        values.put(IMAGE, champcash.getImage());

        // Inserting Row
        long result = db.insert(TABLE_CHAMPCASH, null, values);
        System.out.println(result);
        db.close(); // Closing database connection

    }

/*
    public void addRecharge(ChampcashRecharge champcashRecharge) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_MOBILE_NUMBER_PREPAID, champcashRecharge.getUserMobileNumberPrepaid());
        values.put(USER_PREPAID, champcashRecharge.getUserPrepaid());
        values.put(USER_AMOUNT_PREPAID, champcashRecharge.getUserAmountPrepaid());

        // Inserting Row
        long result = db.insert(TABLE_CHAMPCASH, null, values);
        System.out.println(result);
        db.close(); // Closing database connection
    }
*/


    public void signOutChampcash(Champcash champcash) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, champcash.getIndexID());
        values.put(USER_NAME, champcash.getUserName());
        values.put(PASSWORD, champcash.getUserName());
        values.put(DATE_OF_BIRTH, champcash.getUserName());
        values.put(MOBILE_NUMBER, champcash.getUserName());
        values.put(COUNTRY, champcash.getUserName());
        values.put(STATE, champcash.getUserName());
        values.put(CITY, champcash.getUserName());
        values.put(IMAGE, champcash.getImage());

        // Inserting Row
        long result = db.insert(TABLE_CHAMPCASH, null, values);
        System.out.println(result);
        db.close(); // Closing database connection
    }

    public void removeChampcash() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CHAMPCASH);
        db.close();
    }


    public List<Champcash> getAllChampcash(String selectSql,String keyID) {

        List<Champcash> champcashList = new ArrayList<Champcash>();

        try {
            // Select All Query
            String query = "SELECT "+MOBILE_NUMBER+","+PASSWORD+" FROM " + TABLE_CHAMPCASH + " WHERE "+MOBILE_NUMBER +"="+selectSql+ " AND "+ KEY_ID +"="+keyID ;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {

                do {

                    Champcash champcash = new Champcash();
                    champcash.setMobileNumber(cursor.getString(0));
                    champcash.setPasword(cursor.getString(1));


                    // Adding temples to list
                    champcashList.add(champcash);


                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return champcashList;
    }


    public void updateChampcash(Champcash champcash) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
      //  values.put(KEY_ID, champcash.getIndexID());
        values.put(USER_NAME, champcash.getUserName());
        values.put(EMAIL_ID, champcash.getEmailID());
        values.put(PASSWORD, champcash.getPasword());
        values.put(DATE_OF_BIRTH, champcash.getDateOfBirth());
        values.put(MOBILE_NUMBER, champcash.getMobileNumber());
        values.put(COUNTRY, champcash.getCountry());
        values.put(STATE, champcash.getState());
        values.put(CITY, champcash.getCity());
        values.put(IMAGE, champcash.getImage());
        // update Row
        db.update(TABLE_CHAMPCASH,values, "MOBILE_NUMBER = '"+champcash.getMobileNumber()+"'",null);
        db.close(); // Closing database connection
        Toast.makeText(con, "Profile updated sucessfully", Toast.LENGTH_SHORT).show();

    }

    public List<Champcash> getAllDetails() {

        List<Champcash> champcashList = new ArrayList<Champcash>();

        try {
            // Select All Query
            String query = "SELECT * FROM " + TABLE_CHAMPCASH;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {

                do {

                    Champcash champcash = new Champcash();
                    champcash.setIndexID(cursor.getString(0));
                    champcash.setUserName(cursor.getString(1));
                    champcash.setEmailID(cursor.getString(2));
                    champcash.setPasword(cursor.getString(3));
                    champcash.setDateOfBirth(cursor.getString(4));
                    champcash.setMobileNumber(cursor.getString(5));
                    champcash.setCountry(cursor.getString(6));
                    champcash.setState(cursor.getString(7));
                    champcash.setCity(cursor.getString(8));
                    champcash.setImage(cursor.getString(9));

                    // Adding temples to list
                    champcashList.add(champcash);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return champcashList;
    }

/*
    public List<ChampcashRecharge> getAllRechargeDetails() {

        List<ChampcashRecharge> champcashRechargeList = new ArrayList<ChampcashRecharge>();

        try {
            // Select All Query
            String query = "SELECT * FROM " + TABLE_CHAMPCASH;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {

                do {

                    ChampcashRecharge champcashRecharge = new ChampcashRecharge();
                    champcashRecharge.setUserMobileNumberPrepaid(cursor.getString(10));
                    champcashRecharge.setUserPrepaid(cursor.getString(11));
                    champcashRecharge.setUserAmountPrepaid(cursor.getString(12));

                    champcashRechargeList.add(champcashRecharge);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return champcashRechargeList;
    }
*/
}
