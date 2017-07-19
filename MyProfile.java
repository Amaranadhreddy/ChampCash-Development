package com.tms.govt.champcash.home.report;

import java.io.Serializable;

/**
 * Created by govt on 25-04-2017.
 */

public class MyProfile implements Serializable {

    private String profileID;
    private String user_id;
    private String user_type;
    private String name;
    private String email;
    private String mobile_num;
    private String member_date_of_birth;
    private String profileImg;
    private String city;
    private String country;
    private String state;

    public MyProfile() {
    }

    public MyProfile(String user_id, String user_type, String name, String email, String mobile_num,
                     String member_date_of_birth, String profileImg, String city, String country,
                     String state) {

        this.user_id = user_id;
        this.user_type = user_type;
        this.name = name;
        this.email = email;
        this.mobile_num = mobile_num;
        this.member_date_of_birth = member_date_of_birth;
        this.profileImg = profileImg;
        this.city = city;
        this.country = country;
        this.state = state;

    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_num() {
        return mobile_num;
    }

    public void setMobile_num(String mobile_num) {
        this.mobile_num = mobile_num;
    }

    public String getMember_date_of_birth() {
        return member_date_of_birth;
    }

    public void setMember_date_of_birth(String date_of_birth) {
        this.member_date_of_birth = date_of_birth;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
