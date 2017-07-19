package com.tms.govt.champcash.home.report;

import java.io.Serializable;

/**
 * Created by govt on 05-05-2017.
 */

public class ChampcashRecharge implements Serializable {

    private String userMobileNumberPrepaid, userOperatorPrepaid, userAmountPrepaid;

    public ChampcashRecharge(String userMobileNumberPrepaid, String userOperatorPrepaid, String userAmountPrepaid){
        this.userMobileNumberPrepaid = userMobileNumberPrepaid;
        this.userOperatorPrepaid = userOperatorPrepaid;
        this.userAmountPrepaid = userAmountPrepaid;
    }

    public ChampcashRecharge(){

    }

    public String getUserMobileNumberPrepaid(){
        return userMobileNumberPrepaid;
    }
    public void setUserMobileNumberPrepaid(String userMobileNumberPrepaid){
        this.userMobileNumberPrepaid = userMobileNumberPrepaid;
    }

    public String getUserOperatorPrepaid(){
        return userOperatorPrepaid;
    }
    public void setUserOperatorPrepaid(String userOperatorPrepaid){
        this.userOperatorPrepaid = userOperatorPrepaid;
    }

    public String getUserAmountPrepaid(){
        return userAmountPrepaid;
    }
    public void setUserAmountPrepaid(String userAmountPrepaid){
        this.userAmountPrepaid = userAmountPrepaid;
    }

}
