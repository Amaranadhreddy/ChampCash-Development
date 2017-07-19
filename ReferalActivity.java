package com.tms.govt.champcash.home.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.HomeActivity;
import com.tms.govt.champcash.home.network.ConnectionDetector;
import com.tms.govt.champcash.home.report.Report;
import com.tms.govt.champcash.home.services.ListOfURLs;
import com.tms.govt.champcash.home.services.Utility;
import com.tms.govt.champcash.home.session.Cache;
import com.tms.govt.champcash.home.session.CatchValue;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by govt on 17-04-2017.
 */

public class ReferalActivity extends Activity {

    public final static String EXTRA_MESSAGE_EMAIL = "email";
    public final static String EXTRA_MESSAGE_MOBILE = "mobile";
    public final static String EXTRA_MESSAGE_M_OTP = "m_otp";
    public final static String EXTRA_MESSAGE_E_OTP = "e_otp";
    public final static String EXTRA_MESSAGE_USER_DATA = "user_data";
    public final static String EXTRA_MESSAGE_AUTH_MODE = "auth_mode";
    public final static String EXTRA_MESSAGE_SCREEN = "screen";

    private EditText referalCode;
    private Button btnSubmit;
    private TextView resendTextView;

    private String userReferalCode;

    ConnectionDetector cd;
    Boolean isInternetPresent = false;

    int count = 0;

    private String user_data, user_authmode, user_type, m_otp, screen;

    private String appID;
    private String versionID;
    private String deviceID;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referal);

        referalCode = (EditText) findViewById(R.id.referal_code);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        resendTextView = (TextView) findViewById(R.id.resendTextView);

        appID = (String) Cache.getData(CatchValue.APP_ID, getApplicationContext());
        deviceID = (String) Cache.getData(CatchValue.DEVICE_ID, getApplicationContext());
        versionID = (String) Cache.getData(CatchValue.VERSION_ID, getApplicationContext());

        cd = new ConnectionDetector(ReferalActivity.this);

        //Get Previous activity data
        try {
            Intent intent = getIntent();
            user_data = intent.getStringExtra(EXTRA_MESSAGE_USER_DATA);
            m_otp = intent.getStringExtra(EXTRA_MESSAGE_M_OTP);
            user_authmode = intent.getStringExtra(EXTRA_MESSAGE_AUTH_MODE);
            screen = intent.getStringExtra(EXTRA_MESSAGE_SCREEN);
//            e_otp = intent.getStringExtra(EXTRA_MESSAGE_E_OTP);
//            otpEditText.setText(m_otp);
        } catch (NullPointerException ex) {
            Toast.makeText(ReferalActivity.this, getResources().getString(R.string.text_something_went_wrong_pls_try_again), Toast.LENGTH_SHORT).show();
        }

        resendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectionAvailable();
                if (isInternetPresent){
                    count++;
                    new ResendOTPTask().execute();
                } else {
                    ShowNoInternetDialog();
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call service to check otp authentication
                m_otp = referalCode.getText().toString().trim();
                if (TextUtils.isEmpty(m_otp)) {
                    referalCode.setError(getResources().getString(R.string.text_please_enter_otp));
                    referalCode.requestFocus();
                } else {
                    isInternetPresent = cd.isConnectionAvailable();
                    if (isInternetPresent){
                        final String[] args = new String[6];
                        user_type = "P";
                        args[0] = m_otp;
                        args[1] = user_data;
                        args[2] = user_type;
                        args[3] = deviceID;
                        args[4] = appID;
                        args[5] = versionID;
                        new CheckOTPForRegistrationTask().execute(args);
                    } else {
                        ShowNoInternetDialog();
                    }
                }
            }
        });

/*
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectionAvailable();
                if (isInternetPresent){

                    userReferalCode = referalCode.getText().toString().trim();

                    if (TextUtils.isEmpty(userReferalCode)){
                        referalCode.setError("Please Enter Referal Code");
                        referalCode.requestFocus();
                    } else if (!userReferalCode.matches("123456")){
                        showNumberAlertDialog();
                    } else {
                        showDetailVerificationAlertDialog();
                    }
                } else {
                    ShowNoInternetDialog();
                }

            }
        });
*/
    }

    private void showDetailVerificationAlertDialog() {

        final AlertDialog alertDialog = new AlertDialog.Builder(ReferalActivity.this).create();
        alertDialog.setTitle("Detail Verification");
        alertDialog.setMessage("Name: Champion");
        alertDialog.setMessage("Refer Id: 123456");
        alertDialog.setButton(getResources().getString(R.string.text_verify), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent challengeIntent = new Intent(ReferalActivity.this, ChallengeActivity.class);
                startActivity(challengeIntent);

            }
        });

        alertDialog.setButton2(getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(16);
    }

    private void showNumberAlertDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(ReferalActivity.this).create();
        alertDialog.setMessage("If you don't have any Referal Code, Please Enter 123456");
        alertDialog.setButton(getResources().getString(R.string.text_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(16);
    }

    private void ShowNoInternetDialog() {
        showAlertDialog(this, getResources().getString(R.string.text_no_internet_connection),
                getResources().getString(R.string.text_please_check_your_network), false);
    }

    private void showAlertDialog(Context context, String title, String message, boolean status) {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon((status) ? R.mipmap.ic_action_checked : R.mipmap.ic_action_warning);
        alertDialog.setButton(getResources().getString(R.string.text_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(16);
    }

    private void getResultFromOTPTask(Report result) {
        try {
            if (result.getStatus().equalsIgnoreCase("true")) {
                JSONObject resultJsonObject = result.getJsonObject();
                if (resultJsonObject.getString("ResponseCode").equalsIgnoreCase("PI100")) {
                    Toast.makeText(this, getResources().getString(R.string.text_registration_successfully_completed), Toast.LENGTH_SHORT).show();
                    NavigateToLoginActivity();

                }
                if (!(resultJsonObject.getString("ResponseCode").equalsIgnoreCase("PI100"))) {
                    showAlertDialog(ReferalActivity.this, getResources().getString(R.string.text_verification), getResources().getString(R.string.text_invalid_otp), false);
                    referalCode.setText("");
                    referalCode.requestFocus();
                }
            } else if (result.getStatus().equalsIgnoreCase("false")) {
                if (result.getErr_code() == 401) {
                    Toast.makeText(ReferalActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (result.getErr_code() == 500) {
                    Toast.makeText(ReferalActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReferalActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException ex) {
            Toast.makeText(this, getResources().getString(R.string.text_something_went_wrong_pls_try_again), Toast.LENGTH_SHORT).show();
        }
    }

    public void NavigateToLoginActivity() {
        Intent intent = new Intent(ReferalActivity.this, HomeActivity.class);
        startActivity(intent);
        ReferalActivity.this.finish();
    }

    private class CheckOTPForRegistrationTask extends AsyncTask<String, Void, Report>{

        ProgressDialog progressDialog = new ProgressDialog(ReferalActivity.this);
        Utility utility = new Utility(ReferalActivity.this);
        ListOfURLs urLs = new ListOfURLs(ReferalActivity.this);
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
                jsonObject.put("otp", args[0]);
                jsonObject.put("emailormobile", args[1]);
                jsonObject.put("user_type", args[2]);
                jsonObject.put("DeviceId", args[3]);
                jsonObject.put("AppId", args[4]);
                jsonObject.put("AppVersion", args[5]);

//                AesEncryption aesEncryption = new AesEncryption(OTPActivityForRegistration.this, new JSONObject());
//                String encryptedString = aesEncryption.getJsonObject();
                report = utility.serviceResponse(jsonObject, urLs.getPilgrimVerificationURL());


            } catch (JSONException e) {
                Toast.makeText(ReferalActivity.this, getResources().getString(R.string.text_something_went_wrong_pls_try_again), Toast.LENGTH_SHORT).show();
            }

            return report;
        }

        @Override
        protected void onPostExecute(Report result) {
            progressDialog.dismiss();
            if (result != null) {
                getResultFromOTPTask(result);
            } else {
                Toast.makeText(ReferalActivity.this, getResources().getString(R.string.text_something_went_wrong_pls_try_again), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getResultFromResendOTPTask(Report result) {
        try {
            if (result.getStatus().equalsIgnoreCase("true")) {
                JSONObject resultJsonObject = result.getJsonObject();
                if (resultJsonObject.getString("ResponseCode").equalsIgnoreCase("PI100")) {

                    Toast.makeText(ReferalActivity.this, getResources().getString(R.string.text_otp_sent), Toast.LENGTH_SHORT).show();
                }
                if (!(resultJsonObject.getString("ResponseCode").equalsIgnoreCase("PI100"))) {
                    Toast.makeText(ReferalActivity.this, resultJsonObject.getString("ResponseMsg"), Toast.LENGTH_SHORT).show();
                    referalCode.setText("");
                    referalCode.requestFocus();
                }
            } else if (result.getStatus().equals("false")) {
                if (result.getErr_code() == 401) {
                    Toast.makeText(ReferalActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (result.getErr_code() == 500) {
                    Toast.makeText(ReferalActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReferalActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException ex) {
            Toast.makeText(this, getResources().getString(R.string.text_something_went_wrong_pls_try_again), Toast.LENGTH_SHORT).show();
        }
    }


    private class ResendOTPTask extends AsyncTask<String, Void, Report>{

        ProgressDialog progressDialog = new ProgressDialog(ReferalActivity.this);
        Utility utility = new Utility(ReferalActivity.this);
        ListOfURLs urLs = new ListOfURLs(ReferalActivity.this);
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
                jsonObject.put("emailormobile", user_data);
                jsonObject.put("user_type", "P");
                jsonObject.put("DeviceId", deviceID);
                jsonObject.put("AppId", appID);
                jsonObject.put("AppVersion", versionID);

                report = utility.serviceResponse(jsonObject, urLs.getPilgrimPilgrimResendOTPURL());

            } catch (JSONException e) {
                Toast.makeText(ReferalActivity.this, getResources().getString(R.string.text_something_went_wrong_pls_try_again), Toast.LENGTH_SHORT).show();
            }

            return report;
        }

        @Override
        protected void onPostExecute(Report result) {
            progressDialog.dismiss();
            if (result != null) {
                getResultFromResendOTPTask(result);
            } else {
                Toast.makeText(ReferalActivity.this, getResources().getString(R.string.text_something_went_wrong_pls_try_again), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
