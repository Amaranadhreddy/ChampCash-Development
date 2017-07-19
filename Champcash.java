package com.tms.govt.champcash.home.report;

import java.io.Serializable;

/**
 * Created by govt on 18-04-2017.
 */

public class Champcash implements Serializable {

    private String indexID;
    private String userName;
    private String emailID;
    private String pasword;
    private String dateOfBirth;
    private String mobileNumber;
    private String country;
    private String state;
    private String city;
    private String image;

    public Champcash(String userName, String emailID,String pasword,String dateOfBirth,String mobileNumber,
                     String country, String state, String city, String img){

        this.userName = userName;
        this.emailID = emailID;
        this.pasword = pasword;
        this.dateOfBirth = dateOfBirth;
        this.mobileNumber = mobileNumber;
        this.country = country;
        this.state = state;
        this.city = city;
        this.image=img;
    }

    public Champcash(){

    }

    public String getIndexID(){
        return indexID;
    }
    public void setIndexID(String indexID){
        this.indexID = indexID;
    }

    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getEmailID(){
        return emailID;
    }
    public void setEmailID(String emailID){
        this.emailID = emailID;
    }

    public String getPasword(){
        return pasword;
    }
    public void setPasword(String pasword){
        this.pasword = pasword;
    }

    public String getDateOfBirth(){
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth){
        this.dateOfBirth = dateOfBirth;
    }

    public String getMobileNumber(){
        return mobileNumber;
    }
    public void setMobileNumber(String mobileNumber){
        this.mobileNumber = mobileNumber;
    }

    public String getCountry(){
        return country;
    }
    public void setCountry(String country){
        this.country = country;
    }

    public String getState(){
        return state;
    }
    public void setState(String state){
        this.state = state;
    }

    public String getCity(){
        return city;
    }
    public void setCity(String city){
        this.city = city;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
