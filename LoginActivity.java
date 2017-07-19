package com.tms.govt.champcash.home.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.HomeActivity;
import com.tms.govt.champcash.home.localdatabase.ChampCashDB;
import com.tms.govt.champcash.home.network.ConnectionDetector;
import com.tms.govt.champcash.home.report.Champcash;
import com.tms.govt.champcash.home.session.Cache;
import com.tms.govt.champcash.home.session.CatchValue;

import java.util.List;

/**
 * Created by govt on 19-04-2017.
 */

public class LoginActivity extends Activity {

    ChampCashDB CHAMPCASHDB;

    private EditText mobileNumber, password;
    private Button loginBtn;
    private TextView activityName;
    private ImageView activityBack;

    private String userMobileNumber, userPassword,keyID;

    ConnectionDetector cd;
    Boolean isInternetPresent = false;

    private String userID;

    ProgressDialog progress_dialog;

//    String email, user_name, user_password, user_authid, user_authmode,
//            user_id, user_type, screen, profile_pic, profile_name;
//    private String appID;
//    private String versionID;
//    private String deviceID;
//
//    private JSONObject jsonObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mobileNumber = (EditText) findViewById(R.id.editText_mobileNumber);
        password = (EditText) findViewById(R.id.editText_password);
        loginBtn = (Button) findViewById(R.id.btn_login);
        activityName = (TextView) findViewById(R.id.header_activity_title);
        activityBack = (ImageView) findViewById(R.id.header_back);

//        userID = (String) Cache.getData(CatchValue.USER_ID, LoginActivity.this);

//        appID = (String) Cache.getData(CatchValue.APP_ID, getApplicationContext());
//        deviceID = (String) Cache.getData(CatchValue.DEVICE_ID, getApplicationContext());
//        versionID = (String) Cache.getData(CatchValue.VERSION_ID, getApplicationContext());

        activityName.setText("LOGIN");
        mobileNumber.setText("9581856860");
        password.setText("123456");

        CHAMPCASHDB = new ChampCashDB(LoginActivity.this);

        cd = new ConnectionDetector(LoginActivity.this);
        List<Champcash> champcashList =CHAMPCASHDB.getAllDetails();
        for(int i=0;i<champcashList.size();i++) {
            keyID=champcashList.get(i).getIndexID();

        }
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isInternetPresent = cd.isConnectionAvailable();

                if (isInternetPresent) {

                    userMobileNumber = mobileNumber.getText().toString().trim();
                    userPassword = password.getText().toString().trim();
                    List<Champcash> champcashList =CHAMPCASHDB.getAllChampcash(userMobileNumber,keyID);
                        if(champcashList.size()!=0){
                            for(int i=0;i<champcashList.size();i++){
                                if (userMobileNumber.length() == 0){
                                    mobileNumber.setError("Please Enter Mobile Number");
                                    mobileNumber.requestFocus();
                                }else if (userPassword.length() == 0){
                                    password.setError("Please Enter Password");
                                    password.requestFocus();
                                } else if(!(champcashList.get(i).getMobileNumber().equals(userMobileNumber))){
                                    Toast.makeText(LoginActivity.this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                                } else if (!(champcashList.get(i).getPasword().equals(userPassword))){
                                    Toast.makeText(LoginActivity.this, "Please Enter Valid Password", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (isInternetPresent){

                                        Cache.putData(CatchValue.USER_ID, LoginActivity.this, userID, Cache.CACHE_LOCATION_DISK);
                                        Intent tabIntent = new Intent(LoginActivity.this, TabActivity1.class);
                                        startActivity(tabIntent);

                                    } else {
                                        ShowNoInternetDialog();
                                    }
                                }
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"InValid Details",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                    ShowNoInternetDialog();
                }

//                Intent tabIntent1 = new Intent(LoginActivity.this, TabActivity1.class);
//                startActivity(tabIntent1);
            }
        });

        activityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {

        NavigateToHomeActivity();
    }

    public void NavigateToHomeActivity() {

//        String goStatus = (String) Cache.getData(CatchValue.CART_BACK_STATUS, LoginActivity.this);

        if (!TextUtils.isEmpty(keyID)) {

            if (keyID.equalsIgnoreCase("0")) {
                LoginActivity.this.finish();
            } else if (keyID.equalsIgnoreCase("2")) {
                LoginActivity.this.finish();
            } else {

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra(EXTRA_MESSAGE_SCREEN, screen);
                intent.putExtra(userMobileNumber, keyID);
                startActivity(intent);
            }
        } else {

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra(EXTRA_MESSAGE_SCREEN, screen);
            intent.putExtra(userMobileNumber, keyID);
            startActivity(intent);
        }
    }


    //used to show no internet connection dialog
    public void ShowNoInternetDialog() {
        showAlertDialog(LoginActivity.this, getResources().getString(R.string.text_no_internet_connection),
                getResources().getString(R.string.text_please_check_your_network), false);
    }

    //show alert
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon((status) ? R.mipmap.ic_action_checked : R.mipmap.ic_action_warning);
        alertDialog.setButton(getResources().getString(R.string.text_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(16);
    }

/*
    private void getLoginResult(Report result) {
        try {

            if (result.getStatus().equalsIgnoreCase("true")) {
                JSONObject resultJsonObject = result.getJsonObject();
                if (resultJsonObject.getString("ResponseStatus").equalsIgnoreCase("true")) {
                    if (resultJsonObject.getString("ResponseCode").equalsIgnoreCase("CI100")) {
                        JSONObject jsonObject = resultJsonObject.getJSONObject("PilgrimInfo");
                        if (jsonObject.length() != 0) {
                            user_id = jsonObject.getString("user_id");
                            profile_pic = jsonObject.getString("profile_pic");
                            profile_name = jsonObject.getString("user_name");

                            Cache.putData(CatchValue.USER_ID, getApplicationContext(), user_id, Cache.CACHE_LOCATION_DISK);
                            // Cache.putData(CatchValue.PROFILE_PIC, getApplicationContext(), profile_pic, Cache.CACHE_LOCATION_DISK);

//                            new SaveProfileImageTask().execute(profile_pic);
//                            Cache.putData(CatchValue.PROFILE_NAME, getApplicationContext(), profile_name, Cache.CACHE_LOCATION_DISK);

                        }
                    } else {
                        Toast.makeText(this, resultJsonObject.getString("ResponseMsg"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, resultJsonObject.getString("ResponseMsg"), Toast.LENGTH_SHORT).show();
                }

            } else if (result.getStatus().equalsIgnoreCase("false")) {
                if (result.getErr_code() == 401) {
                    Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (result.getErr_code() == 500) {
                    Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException ex) {
            Toast.makeText(this, getResources().getString(R.string.text_something_went_wrong_pls_try_again), Toast.LENGTH_SHORT).show();
        }
    }
*/


/*
    private class TMSUserLoginTask extends AsyncTask<String, Void, Report> {

        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        Utility utility = new Utility(LoginActivity.this);
        ListOfURLs urLs = new ListOfURLs(LoginActivity.this);
        Report report = new Report();

        @Override
        protected void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getResources().getString(R.string.text_processing));
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected Report doInBackground(String... args) {

            try {
                jsonObject = new JSONObject();
                jsonObject.put("emailormobile", args[0]);
                jsonObject.put("user_password", args[1]);
                jsonObject.put("user_authmode", args[2]);
                jsonObject.put("user_authid", args[3]);
                jsonObject.put("user_type", args[4]);
                jsonObject.put("DeviceId", args[5]);
                jsonObject.put("Appid", args[6]);
                jsonObject.put("AppVersion", args[7]);
                jsonObject.put("DeviceIp", args[8]);

                report = utility.serviceResponse(jsonObject, urLs.getPilgrimLogInURL());

            } catch (JSONException e) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.text_something_went_wrong_pls_try_again), Toast.LENGTH_SHORT).show();
            }

            return report;
        }

        protected void onPostExecute(Report result) {
            progressDialog.dismiss();
            if (result != null) {
                getLoginResult(result);
            } else {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.text_something_went_wrong_pls_try_again), Toast.LENGTH_SHORT).show();
            }
        }

    }
*/

}
