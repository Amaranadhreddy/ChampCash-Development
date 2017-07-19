package com.tms.govt.champcash.home.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.localdatabase.ChampCashDB;
import com.tms.govt.champcash.home.network.ConnectionDetector;
import com.tms.govt.champcash.home.report.Champcash;
import com.tms.govt.champcash.home.report.Report;
import com.tms.govt.champcash.home.services.ListOfURLs;
import com.tms.govt.champcash.home.services.Utility;
import com.tms.govt.champcash.home.session.Cache;
import com.tms.govt.champcash.home.session.CatchValue;
import com.tms.govt.champcash.home.slidemenu.ProfileCapture;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by govt on 17-04-2017.
 */

public class SignupActivity extends Activity {

    public final static String EXTRA_MESSAGE_EMAIL = "email";
    public final static String EXTRA_MESSAGE_MOBILE = "mobile";
    public final static String EXTRA_MESSAGE_M_OTP = "m_otp";
    public final static String EXTRA_MESSAGE_E_OTP = "e_otp";
    public final static String EXTRA_MESSAGE_AUTH_MODE = "auth_mode";
    public final static String EXTRA_MESSAGE_USER_DATA = "user_data";
    public final static String EXTRA_MESSAGE_SCREEN = "screen";

    ChampCashDB CHMPCASHDB;

    private EditText name, email, password, dateofbirth, mobilenumber, state, city;
    private ImageView image;
    private Button proceed;
    private Spinner spinnerCountry;
    private String userName, userEmail, userPassword, userDateOfBirth, userMobileNumber, userCountry, userState, userCity, userImage;
    private Calendar calendar;
    private CheckBox checkBox;
    private String img;
    private TextView termsCond;
    private LinearLayout linearLayout;
    private List<String> countryList;
    private ArrayAdapter<String> countryAdapter;

    private Button captchaBtn;
    private EditText captchaEditText;
    private ImageView captchaImgBtn;
    private String userCaptchaValue, randomStr;
    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String user_authmode, user_authid;
    private String m_otp, e_otp;

    private String appID;
    private String versionID;
    private String deviceID;
    private JSONObject jsonObject;

    ConnectionDetector cd;
    Boolean isInternetPresent = false;

    ProgressDialog progressDialog;

    private TextView activityTitle;
    private ImageView activityBack;

    private String userChooseTask;
    private String user_id, user_type;

    private String userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        CHMPCASHDB = new ChampCashDB(SignupActivity.this);
//        CHMPCASHDB.removeChampcash();

        name = (EditText) findViewById(R.id.text_name);
        email = (EditText) findViewById(R.id.text_email);
        password = (EditText) findViewById(R.id.text_password);
        dateofbirth = (EditText) findViewById(R.id.text_date_of_birth);
        mobilenumber = (EditText) findViewById(R.id.text_mobile_number);
        spinnerCountry = (Spinner) findViewById(R.id.spinner_country);
        state = (EditText) findViewById(R.id.text_state);
        city = (EditText) findViewById(R.id.text_city);
        proceed = (Button) findViewById(R.id.btn_proceed);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        termsCond = (TextView) findViewById(R.id.terms_and_cond);
        linearLayout = (LinearLayout) findViewById(R.id.signupLL);
        activityTitle = (TextView) findViewById(R.id.header_activity_title);
        activityBack = (ImageView) findViewById(R.id.header_back);
        progressDialog = new ProgressDialog(SignupActivity.this);
        image = (ImageView) findViewById(R.id.My_Profile_Pic);
        activityTitle.setText("Register");

        captchaBtn = (Button) findViewById(R.id.captchaBtn);
        captchaImgBtn = (ImageView) findViewById(R.id.captchaImgBtn);
        captchaEditText = (EditText) findViewById(R.id.captchaET);

        appID = (String) Cache.getData(CatchValue.APP_ID, getApplicationContext());
        deviceID = (String) Cache.getData(CatchValue.DEVICE_ID, getApplicationContext());
        versionID = (String) Cache.getData(CatchValue.VERSION_ID, getApplicationContext());

        randomStr = getRandomString(4);
        captchaBtn.setText(randomStr);
        captchaBtn.setPaintFlags(captchaBtn.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        dateofbirth.setTextSize(16);
        dateofbirth.setTextColor(Color.GRAY);

//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        try {
//            if (bundle != null) {
//                String name1 = intent.getStringExtra("user_name");
//                if (!TextUtils.isEmpty(name1)) {
//                    name.setText(name1);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });

        calendar = Calendar.getInstance();

        cd = new ConnectionDetector(SignupActivity.this);

        countryList=new ArrayList<String>();
        updateCountries();
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
//                   userCountry = String.valueOf(position);
                    userCountry = countryList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignupActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        captchaImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomStr = getRandomString(4);
                captchaBtn.setText(randomStr);
                captchaBtn.setPaintFlags(captchaBtn.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                captchaEditText.setText("");
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isInternetPresent = cd.isConnectionAvailable();
                if (isInternetPresent) {

                    final CharSequence[] items = {getResources().getString(R.string.text_take_photo), getResources().getString(R.string.text_choose_from_gallery)};
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setTitle(getResources().getString(R.string.text_add_photo));
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            boolean result = ProfileCapture.checkPermission(SignupActivity.this);

                            if (items[item].equals(getResources().getString(R.string.text_take_photo))) {
                                userChooseTask = getResources().getString(R.string.text_take_photo);
                                if (result)
                                    cameraIntent();
                            } else if (items[item].equals(getResources().getString(R.string.text_choose_from_gallery))) {
                                userChooseTask = getResources().getString(R.string.text_choose_from_gallery);
                                if (result)
                                    galleryIntent();
                            }
                        }
                    });
                    builder.show();
                } else {
                    ShowNoInternetDialog();
                }
            }
        });

       /* dateofbirth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new DatePickerDialog(SignupActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                return true;
            }
        });*/

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    isInternetPresent = cd.isConnectionAvailable();
                    if (isInternetPresent) {
                        userName = name.getText().toString().trim();
                        userEmail = email.getText().toString().trim();
                        userPassword = password.getText().toString().trim();
                        userDateOfBirth = dateofbirth.getText().toString();
                        userMobileNumber = mobilenumber.getText().toString().trim();
//                        userCountry = spinnerCountry.getSelectedItem().toString();
                        userState = state.getText().toString().trim();
                        userCity = city.getText().toString().trim();
                        userCaptchaValue = captchaEditText.getText().toString().trim();

                        if (TextUtils.isEmpty(userName)) {
                            name.setError("Please Enter Name");
                            name.requestFocus();
                        } else if (TextUtils.isEmpty(userEmail)) {
                            email.setError("Please Enter Email");
                            email.requestFocus();
                        } else if (TextUtils.isEmpty(userPassword)) {
                            password.setError("Please Enter Password");
                            password.requestFocus();
                        } else if (TextUtils.isEmpty(userDateOfBirth)) {
                            Toast.makeText(SignupActivity.this, "Please Select Date!", Toast.LENGTH_SHORT).show();
                            dateofbirth.requestFocus();
                        } else if (TextUtils.isEmpty(userMobileNumber)) {
                            mobilenumber.setError("Please Enter Mobile Number");
                            mobilenumber.requestFocus();
                        } else if (mobilenumber.getText().length() != 10) {
                            mobilenumber.setError("Please enter valid mobile number");
                            mobilenumber.requestFocus();
                        }else if (TextUtils.isEmpty(userCountry)) {
                            Toast.makeText(SignupActivity.this, "Please Select Country", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(userState)) {
                            state.setError("Please Enter State");
                            state.requestFocus();
                        } else if (TextUtils.isEmpty(userCity)) {
                            city.setError("Please Enter City");
                            city.requestFocus();
                        } else if (TextUtils.isEmpty(userCaptchaValue)){
                            captchaEditText.setError("Please Enter Captcha");
                            captchaEditText.requestFocus();
                        } else if (!(userCaptchaValue.equals(randomStr))){
                            captchaEditText.requestFocus();
                            captchaEditText.setText("");
                            randomStr = getRandomString(4);
                            captchaBtn.setText(randomStr);
                            captchaBtn.setPaintFlags(captchaBtn.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        } else if (!checkBox.isChecked()) {
                            Toast.makeText(SignupActivity.this, getResources().getString(R.string.accept_terms_and_conditions), Toast.LENGTH_SHORT).show();
                        } else {

                           /* if (isInternetPresent) {
                                progressDialog.show();
                                CHMPCASHDB.addChampcash(new Champcash(userName,userEmail,userPassword,userDateOfBirth,userMobileNumber,userCountry,userState,userCity));

                                userMobileNumber = mobilenumber.getText().toString().trim();
                                user_authmode = "T";
                                user_type = "P";
                                user_authid = userPassword;

                                String[] args = new String[14];
                                args[0] = userName;
                                args[1] = userEmail;
                                args[2] = userPassword;
                                args[3] = userDateOfBirth;
                                args[4] = userMobileNumber;
                                args[5] = userCountry;
                                args[6] = userState;
                                args[7] = userCity;
                                args[8] = user_authmode;
                                args[9] = user_authid;
                                args[10] = user_type;
                                args[11] = deviceID;
                                args[12] = appID;
                                args[13] = versionID;
                                new RegisterTask().execute(args);*/

                            img=updateProfile();
                            Cache.putData(CatchValue.IMAGE,SignupActivity.this, img, Cache.CACHE_LOCATION_DISK);
                            CHMPCASHDB.addChampcash(new Champcash(userName,userEmail,userPassword,userDateOfBirth,userMobileNumber,userCountry,userState,userCity,img));
                            Cache.putData(CatchValue.USER_ID, SignupActivity.this, user_id, Cache.CACHE_LOCATION_DISK);
                            Toast.makeText(SignupActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                /*Intent registerIntent = new Intent(SignupActivity.this, ReferalActivity.class);
                                startActivity(registerIntent);*/
                            Intent registerIntent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(registerIntent);
                            }
//                        }
                    }
                    else {
                            ShowNoInternetDialog();
                        }
            }
        });

        activityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void galleryIntent() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.text_select_file)), SELECT_FILE);

    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                image.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        image.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        image.setImageBitmap(thumbnail);

    }
    public String updateProfile(){

        try {

            image.buildDrawingCache();
            Bitmap bitmap = image.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            userImage = Base64.encodeToString(b, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userImage;
    }
    public void onBackPressed(){
        finish();
    }

    private String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; i++)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void updateCountries() {
        countryList= Arrays.asList(getResources().getStringArray(R.array.select_country));
        countryAdapter = new ArrayAdapter<String>(SignupActivity.this, R.layout.spinner_text_view, countryList){

            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return false;
                } else {
                    return true;
                }
            }
        };
        spinnerCountry.setAdapter(countryAdapter);
        countryAdapter.setDropDownViewResource(R.layout.spinner_text_view);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {

        String format = "MM/dd/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        dateofbirth.setText(simpleDateFormat.format(calendar.getTime()));
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

    private void getRegistrationResult(Report result) {
        try {

            if (result.getStatus().equalsIgnoreCase("true")) {
                JSONObject resultJsonObject = result.getJsonObject();
                if (resultJsonObject.getString("ResponseStatus").equalsIgnoreCase("true")) {
                    if (resultJsonObject.getString("ResponseCode").equalsIgnoreCase("PI100")) {
                        m_otp = resultJsonObject.getString("MobileOTP");
                        e_otp = resultJsonObject.getString("EmailOTP");
                        NavigateToReferalActivity(userMobileNumber, m_otp, e_otp);
                    }
                    if (resultJsonObject.getString("ResponseCode").equalsIgnoreCase("PI105")) {
                        showAlertDialog(SignupActivity.this, "Failed", resultJsonObject.getString("ResponseMsg"), false);
                    }
                    if (resultJsonObject.getString("ResponseCode").equals("PI106")) {
                        showAlertDialog(SignupActivity.this, "Failed", resultJsonObject.getString("ResponseMsg"), false);
                    }
                } else {
                    Toast.makeText(this, resultJsonObject.getString("ResponseMsg"), Toast.LENGTH_SHORT).show();
                }

            } else if (result.getStatus().equalsIgnoreCase("false")) {
                if (result.getErr_code() == 401) {
                    Toast.makeText(SignupActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (result.getErr_code() == 500) {
                    Toast.makeText(SignupActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignupActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException ex) {
            Toast.makeText(this, getResources().getString(R.string.text_something_went_wrong_pls_try_again), Toast.LENGTH_SHORT).show();
        }
    }

    public void NavigateToReferalActivity(String phoneNo, String m_otp, String e_otp) {
        Intent intent = new Intent(this, ReferalActivity.class);
        intent.putExtra(EXTRA_MESSAGE_EMAIL, userEmail);
        intent.putExtra(EXTRA_MESSAGE_MOBILE, userMobileNumber);
        intent.putExtra(EXTRA_MESSAGE_M_OTP, m_otp);
        intent.putExtra(EXTRA_MESSAGE_E_OTP, e_otp);
        intent.putExtra(EXTRA_MESSAGE_USER_DATA, userMobileNumber);
        intent.putExtra(EXTRA_MESSAGE_AUTH_MODE, user_authmode);
        intent.putExtra(EXTRA_MESSAGE_SCREEN, "1");
        startActivity(intent);
    }

    private class RegisterTask extends AsyncTask<String,Void, Report>{

        ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        Utility utility = new Utility(SignupActivity.this);
        ListOfURLs urLs = new ListOfURLs(SignupActivity.this);
        Report report = new Report();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SignupActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.text_processing));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Report doInBackground(String... args) {

            try {

                jsonObject = new JSONObject();
                jsonObject.put("member_fname", args[0]);
                jsonObject.put("member_lname", args[1]);
                jsonObject.put("user_password", args[2]);
                jsonObject.put("user_emailid", args[3]);
                jsonObject.put("user_mnumber", args[4]);
                jsonObject.put("user_authmode", args[5]);
                jsonObject.put("user_authid", args[6]);
                jsonObject.put("user_type", args[7]);
                jsonObject.put("DeviceId", args[8]);
                jsonObject.put("AppId", args[9]);
                jsonObject.put("AppVersion", args[10]);

                report = utility.serviceResponse(jsonObject, urLs.getPilgrimRegistrationURL());

            } catch (Exception e) {
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.text_something_went_wrong_pls_try_again), Toast.LENGTH_SHORT).show();
            }

            return report;
        }

        @Override
        protected void onPostExecute(Report result) {
            progressDialog.dismiss();
            if (result != null) {
                getRegistrationResult(result);
            } else {
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.text_something_went_wrong_pls_try_again), Toast.LENGTH_SHORT).show();
            }
        }
    }


}
