package com.tms.govt.champcash.home.report;

/**
 * Created by govt on 10-05-2017.
 */

public class CaptureData {

    private String keyID;
    private String userID;
    private String date;
    private String fingerImg;

    public CaptureData(){

    }

    public CaptureData(String userID, String currentDate, String resultImg) {
        this.userID = userID;
        this.date = currentDate;
        this.fingerImg = resultImg;
    }


    public String getKeyID() {
        return keyID;
    }

    public String getUserID() {
        return userID;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return fingerImg;
    }

    public void setKeyID(String keyID) {
        this.keyID = keyID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImage(String fingerImg) {
        this.fingerImg = fingerImg;
    }

}
