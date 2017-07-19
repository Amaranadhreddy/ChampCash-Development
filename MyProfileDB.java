package com.tms.govt.champcash.home.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tms.govt.champcash.home.report.MyProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by govt on 25-04-2017.
 */

public class MyProfileDB extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ChampCashLocalDB";
    // profile table name
    private static final String PROFILE_TABLE = "profile";

    // Temples Table Columns names
    private static final String PROFILE_ID = "profile_id";
    private static final String USER_ID = "user_id";
    private static final String USER_TYPE = "user_type";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String MOBILE = "mobile";
    private static final String DATE_OF_BIRTH = "date_of_birth";
    private static final String PIC = "profile_pic";
//    private static final String GENDER = "gender";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String STATE = "state";

    public MyProfileDB(Context applicationContext) {
        super(applicationContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_PROFILE_TABLE = "CREATE TABLE " + PROFILE_TABLE + "("
                + PROFILE_ID + " INTEGER PRIMARY KEY  AUTOINCREMENT,"
                + USER_ID + " TEXT,"
                + USER_TYPE + " TEXT,"
                + NAME + " TEXT,"
                + EMAIL + " TEXT,"
                + MOBILE + " TEXT,"
                + DATE_OF_BIRTH + " TEXT,"
                + PIC + " TEXT,"
                + CITY + " TEXT,"
                + COUNTRY + " TEXT,"
                + STATE + " TEXT" + ")";

        sqLiteDatabase.execSQL(CREATE_PROFILE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PROFILE_TABLE);
        // Creating tables again
        onCreate(sqLiteDatabase);

    }

    /*public void onUpdate(SQLiteDatabase sqLiteDatabase,int i, int i1){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, sqLiteDatabase);
    }*/

    public void addDetails(MyProfile details){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ID, details.getUser_id());
        values.put(USER_TYPE, details.getUser_type());
        values.put(NAME, details.getName());
        values.put(EMAIL, details.getEmail());
        values.put(MOBILE, details.getMobile_num());
        values.put(DATE_OF_BIRTH, details.getMember_date_of_birth());
        values.put(PIC, details.getProfileImg());
        values.put(COUNTRY, details.getCountry());
        values.put(STATE, details.getState());
        values.put(CITY, details.getCity());

        // Inserting Row
        long result = db.insert(PROFILE_TABLE, null, values);
        db.close(); // Closing database connection

    }

    public List<MyProfile> getProfile(String uID) {

        List<MyProfile> profileList = new ArrayList<MyProfile>();

        try {
            // Select All Query
            String query = "SELECT * FROM " + PROFILE_TABLE + " WHERE " + USER_ID + "='" + uID + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {

                do {
                    MyProfile profile = new MyProfile();

                    profile.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                    profile.setUser_type(cursor.getString(cursor.getColumnIndex("user_type")));
                    profile.setName(cursor.getString(cursor.getColumnIndex("name")));
                    profile.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                    profile.setMobile_num(cursor.getString(cursor.getColumnIndex("mobile")));
                    profile.setMember_date_of_birth(cursor.getString(cursor.getColumnIndex("date_of_birth")));
                    profile.setProfileImg(cursor.getString(cursor.getColumnIndex("profile_pic")));
                    profile.setCountry(cursor.getString(cursor.getColumnIndex("country")));
                    profile.setState(cursor.getString(cursor.getColumnIndex("state")));
                    profile.setCity(cursor.getString(cursor.getColumnIndex("city_id")));

                    profileList.add(profile);


                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return profileList;
    }

    public void removeProfile() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + PROFILE_TABLE);
        db.close();

    }


}
