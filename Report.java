package com.tms.govt.champcash.home.report;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by govt on 19-04-2017.
 */

public class Report implements Serializable {



    private String status;
    private String message;
    private String report;
    private int err_code;
    private JSONObject jsonObject;

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }


}
