package com.tms.govt.champcash.home.slidemenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;
import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.localdatabase.CaptureDB;
import com.tms.govt.champcash.home.network.ConnectionDetector;
import com.tms.govt.champcash.home.report.CaptureData;
import com.tms.govt.champcash.home.utility.CommonMethod;
import com.tms.govt.champcash.home.utility.FCVar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by govt on 10-05-2017.
 */

public class FingerCaptureActivity extends Activity implements MFS100Event {

    MFS100 mfs100 = null;
    int minQuality = 40;
    int timeout = 15000;
    int mfsVer = 41;
    byte[] Enroll_Template;
    byte[] Verify_Template;
    private ProgressDialog progressDialog;
    private ImageView imgFinger;
    private ImageView lblCapturedImg;
    private TextView lblCaptured;
    CommonMethod.ScannerAction scannerAction = CommonMethod.ScannerAction.Capture;
    SharedPreferences settings;
    MediaPlayer mediaPlayer;
    Button btnCapture;
    Thread updateThread;
    public boolean running;
    ConnectionDetector cd;
    private TextView titleTextView,textWeek,textDate,textCount;
    private String userName,userID,appID,deviceID,templeID,templeCode,deviceIP,captureCount;
    ArrayList<String> countList = new ArrayList<String>();

    private static int counter = 0;
    private String stringVal;
    private TextView activityTitle;
    private ImageView activityBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_capture);

        activityTitle = (TextView) findViewById(R.id.header_activity_title);
        activityBack = (ImageView) findViewById(R.id.header_back);
        activityTitle.setText("FingerPrint Verification");

        cd = new ConnectionDetector(this);

        initActivityElements();

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        mfsVer = Integer.parseInt(settings.getString("MFSVer", String.valueOf(mfsVer)));

//        titleTextView = (TextView) findViewById(R.id.tilteTV);
        if(!TextUtils.isEmpty(userName)){
            titleTextView.setText("Welcome "+userName);
        }

        CommonMethod.DeleteDirectory();
        CommonMethod.CreateDirectory();

        FCVar.sharedPrefernceDeviceMode = (SharedPreferences)this.getSharedPreferences(FCVar.strSpDeviceKey, Context.MODE_PRIVATE);
        mfs100 = new MFS100(this, mfsVer);
        mfs100.SetApplicationContext(this);

        startInit();

        btnCapture = (Button) findViewById(R.id.btn_capture_finger);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallCaptureMethod();
            }
        });

        activityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FingerCaptureActivity.this.finish();
            }
        });
    }

    private void initActivityElements() {

//        getCacheData();

        textDate = (TextView) findViewById(R.id.text_DateOfMonth);
        textCount = (TextView) findViewById(R.id.text_todayCount);
        lblCapturedImg = (ImageView) findViewById(R.id.lblCapturedImg);
        imgFinger = (ImageView) findViewById(R.id.imgFinger);
        lblCaptured = (TextView) findViewById(R.id.lblCaptured);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        textDate.setText("Date : "+df.format(cal.getTime()));

        List<CaptureData> data = new CaptureDB(this).getCapturedFingerList(userID,getCurrentDate());
        textCount.setText(String.valueOf(data.size()));
        //startRunningThread();
    }

   /* private void getCacheData() {

        userID = (String) Cache.getData(CatchValue.USER_ID, this);
        templeCode = (String) Cache.getData(CatchValue.TEMPLE_CODE, this);
        templeID = (String) Cache.getData(CatchValue.TEMPLE_ID, this);
        appID = (String) Cache.getData(CatchValue.APP_ID, this);
        deviceID = (String) Cache.getData(CatchValue.DEVICE_ID, this);
        deviceIP = (String) Cache.getData(CatchValue.DEVICE_IP, this);

    }*/

    private void CallCaptureMethod(){

        if (cd.isConnectionAvailable()) {
            scannerAction = CommonMethod.ScannerAction.Capture;
            StartAsyncCapture();

        } else {
            btnCapture.setClickable(true);
            ShowNoInternetDialog();
        }
    }

    private void startInit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InitScanner();
            }
        }).start();
    }

    public void ShowNoInternetDialog() {
        showAlertDialog(FingerCaptureActivity.this, "No Internet Connection", "Please check your network", false);
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon((status) ? R.mipmap.ic_action_checked : R.mipmap.ic_action_warning);
        alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });
        alertDialog.show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(16);
    }

    private void InitScanner() {
        try {
            int ret = mfs100.Init();
            if (ret != 0) {
                SetTextOnUIThread(lblCapturedImg, lblCaptured, "Device init failed, Please try again", Color.RED);
            } else {
                soundDeviceConnection();
            }
        } catch (Exception ex) {
            SetTextOnUIThread(lblCapturedImg, lblCaptured, "Init failed, Please try again", Color.RED);
        }
    }

    private void SetLogOnUIThread(final String str) {
    }

    private void StartAsyncCapture() {

        try {
            int ret = mfs100.StartCapture(minQuality, timeout, true);
            if (ret != 0) {
                SetTextOnUIThread(lblCapturedImg, lblCaptured, "No device connected", Color.RED);
            } else {
                btnCapture.setClickable(false);
                btnCapture.setBackgroundColor(Color.parseColor("#808080"));
                SetTextOnUIThread(lblCapturedImg, lblCaptured, "Place finger on scanner device", Color.parseColor("#808080D"));
            }
        } catch (Exception ex) {

        }
    }

    private void SetTextOnUIThread(final ImageView lblCapturedImg, final TextView lblCaptured, final String string, final int color) {

        lblCaptured.post(new Runnable() {
            public void run() {
                lblCaptured.setTextColor(color);
                lblCaptured.setText(string);
            }
        });

        lblCapturedImg.post(new Runnable() {
            public void run() {
                if (color == Color.RED) {
                    lblCapturedImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_already_verified));
                } else if (color == Color.parseColor("#308A0C")) {
                    lblCapturedImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_verified_success));
                } else if (color == Color.parseColor("#808080")) {
                    lblCapturedImg.setImageDrawable(null);
                }
            }
        });
    }

    private void playCaptureSoundError() {
        mediaPlayer = MediaPlayer.create(this, R.raw.capture_success);
        mediaPlayer.start();
    }

    private void playCaptureSoundSuccess() {
        mediaPlayer = MediaPlayer.create(this, R.raw.capture_success);
        mediaPlayer.start();
    }

    private void soundDeviceConnection() {
        mediaPlayer = MediaPlayer.create(this, R.raw.capture_device_info);
        mediaPlayer.start();
    }

    protected void onStop() {
        UnInitScanner();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mfs100 != null) {
            mfs100.Dispose();
        }
        super.onDestroy();
    }

    private void UnInitScanner() {
        try {
            int ret = mfs100.UnInit();
            if (ret != 0) {
                SetTextOnUIThread(lblCapturedImg, lblCaptured, "Disconnected, Please connect it again...", Color.RED);
            } else {
                SetLogOnUIThread("Un-Init Success");
            }
        } catch (Exception e) {
            Log.e("UnInitScanner.EX", e.toString());
        }
    }

    private void showSuccessLog() {
        soundDeviceConnection();
    }


    @Override
    public void OnDeviceAttached(int vid, int pid, boolean hasPermission) {
        int ret = 0;
        if (!hasPermission) {
            SetTextOnUIThread(lblCapturedImg, lblCaptured, "Permission Denied", Color.RED);
            return;
        }
        if (vid == 1204 || vid == 11279) {
            if (pid == 34323) {
                ret = mfs100.LoadFirmware();
                if (ret != 0) {
                    playCaptureSoundError();
                    SetTextOnUIThread(lblCapturedImg, lblCaptured, mfs100.GetErrorMsg(ret), Color.RED);
                } else {
                    SetTextOnUIThread(lblCapturedImg, lblCaptured, "Load Firmware Success", Color.parseColor("#808080"));
                }
            } else if (pid == 4101) {
                SetTextOnUIThread(lblCapturedImg, lblCaptured, "Connected", Color.parseColor("#308A0C"));

                String strDeviceMode = FCVar.sharedPrefernceDeviceMode.getString(FCVar.strSpDeviceKey, "public");
                if (strDeviceMode.toLowerCase().equalsIgnoreCase("public")) {
                    ret = mfs100.Init("");
                    if (ret == -1322) {
                        ret = mfs100.Init(testKey());
                        if (ret == 0) {
                            FCVar.sharedPrefernceDeviceMode.edit().putString(FCVar.strSpDeviceKey, "protected").apply();
                            showSuccessLog();
                        }
                    } else if (ret == 0) {
                        FCVar.sharedPrefernceDeviceMode.edit().putString(FCVar.strSpDeviceKey, "public").apply();
                        showSuccessLog();
                    }
                } else {
                    ret = mfs100.Init(testKey());
                    if (ret == -1322) {
                        ret = mfs100.Init("");
                        if (ret == 0) {
                            FCVar.sharedPrefernceDeviceMode.edit().putString(FCVar.strSpDeviceKey, "public").apply();
                            showSuccessLog();
                        }
                    } else if (ret == 0) {
                        FCVar.sharedPrefernceDeviceMode.edit().putString(FCVar.strSpDeviceKey, "protected").apply();
                        showSuccessLog();
                    }
                }
                if (ret != 0) {
                    SetTextOnUIThread(lblCapturedImg, lblCaptured, mfs100.GetErrorMsg(ret), Color.RED);
                }
            }
        }

    }

    private String testKey() {
        return "t7L8wTG/iv02t+pgYrMQ7tt8qvU1z42nXpJDfAfsW592N4sKUHLd8A0MEV0GRxH+f4RgefEaMZALj7mgm/Thc0jNhR2CW9BZCTgeDPjC6q0W";
    }


    @Override
    public void OnPreview(FingerData fingerData) {
        final Bitmap bitmap = BitmapFactory.decodeByteArray(fingerData.FingerImage(), 0, fingerData.FingerImage().length);
        imgFinger.post(new Runnable() {
            @Override
            public void run() {
                imgFinger.setImageBitmap(bitmap);
                imgFinger.refreshDrawableState();

            }
        });
        SetTextOnUIThread(lblCapturedImg, lblCaptured, "Quality: " + fingerData.Quality(), Color.parseColor("#808080"));
    }

    private void convertToFingerBitmap(Bitmap bitmap) {

        String result = "";

        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if (bitmap == null) {
                Toast.makeText(this, "Captured failed", Toast.LENGTH_SHORT).show();
            } else {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                result = Base64.encodeToString(byteArray, Base64.DEFAULT);

                CaptureDB db = new CaptureDB(this);
                db.insertCaptureImg(new CaptureData(userID,getCurrentDate(),result));

            }
        } catch (Exception e) {
            Toast.makeText(this, "Exception type : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        getCapturedCountList(userID,getCurrentDate());

    }

    private void getCapturedCountList(String userID, String currentDate) {

        List<CaptureData> data = new CaptureDB(this).getCapturedFingerList(userID,currentDate);

        if ((Integer.parseInt(textCount.getText().toString().trim()))<(data.size())){
            textCount.setText(String.valueOf(data.size()));
            playCaptureSoundSuccess();
            SetTextOnUIThread(lblCapturedImg, lblCaptured, "Success", Color.parseColor("#308A0C"));
        }
        else {
//            playCaptureSoundError();
//            SetTextOnUIThread(lblCapturedImg, lblCaptured, "Please try again", Color.RED);
            counter++;
            stringVal = Integer.toString(counter);
            textCount.setText(stringVal);
            playCaptureSoundSuccess();
            SetTextOnUIThread(lblCapturedImg, lblCaptured, "Success", Color.parseColor("#308A0C"));
        }

        btnCapture.setClickable(false);
        btnCapture.setBackgroundResource(R.color.colorPrimaryDark);
        scannerAction = CommonMethod.ScannerAction.Capture;
        StartAsyncCapture();

    }

    @Override
    public void OnCaptureCompleted(boolean status, int errorCode, String errorMsg, FingerData fingerData) {
        if (status) {
            final Bitmap bitmap = BitmapFactory.decodeByteArray(fingerData.FingerImage(), 0, fingerData.FingerImage().length);

            imgFinger.post(new Runnable() {
                @Override
                public void run() {

                    imgFinger.setImageBitmap(bitmap);
                    imgFinger.refreshDrawableState();
                    convertToFingerBitmap(bitmap);
                }
            });

            String log = "\nQuality: " + fingerData.Quality() + "\nNFIQ: "
                    + fingerData.Nfiq() + "\nWSQ Compress Ratio: "
                    + fingerData.WSQCompressRatio()
                    + "\nImage Dimensions (inch): " + fingerData.InWidth()
                    + "\" X " + fingerData.InHeight() + "\""
                    + "\nImage Area (inch): " + fingerData.InArea() + "\""
                    + "\nResolution (dpi/ppi): " + fingerData.Resolution()
                    + "\nGray Scale: " + fingerData.GrayScale()
                    + "\nBits Per Pixal: " + fingerData.Bpp() + "\nWSQ DeviceInfo: "
                    + fingerData.WSQInfo();

            SetLogOnUIThread(log);
        } else {

            playCaptureSoundError();
            SetTextOnUIThread(lblCapturedImg, lblCaptured, "Please try again", Color.RED);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnCapture.setClickable(true);
                    btnCapture.setBackgroundResource(R.color.colorPrimaryDark);
                }
            });

        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        progressDialog.setMessage("Processing Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public void SetData(FingerData fingerData) {
        if (scannerAction.equals(CommonMethod.ScannerAction.Capture)) {
            Enroll_Template = new byte[fingerData.ISOTemplate().length];
            System.arraycopy(fingerData.ISOTemplate(), 0, Enroll_Template, 0,
                    fingerData.ISOTemplate().length);
        } else if (scannerAction.equals(CommonMethod.ScannerAction.Verify)) {
            if (Enroll_Template == null) {
                SetTextOnUIThread(lblCapturedImg, lblCaptured, "Enrolled template not found.", Color.parseColor("#808080"));
                return;
            }
            if (Enroll_Template.length <= 0) {
                SetTextOnUIThread(lblCapturedImg, lblCaptured, "Enrolled template not found.", Color.parseColor("#808080"));
                return;
            }
            Verify_Template = new byte[fingerData.ISOTemplate().length];
            System.arraycopy(fingerData.ISOTemplate(), 0, Verify_Template, 0, fingerData.ISOTemplate().length);
            int ret = mfs100.MatchISO(Enroll_Template, Verify_Template);
            if (ret < 0) {
                SetTextOnUIThread(lblCapturedImg, lblCaptured, "Error: " + ret + "("
                        + mfs100.GetErrorMsg(ret) + ")", Color.RED);
            } else {
                if (ret >= 1400) {
                    SetTextOnUIThread(lblCapturedImg, lblCaptured, "Finger matched with score: " + ret, Color.parseColor("#808080"));
                } else {
                    SetTextOnUIThread(lblCapturedImg, lblCaptured, "Finger not matched, score: " + ret, Color.parseColor("#808080"));
                }
            }
        }

        WriteFile("Raw.raw", fingerData.RawData());
        WriteFile("Bitmap.bmp", fingerData.FingerImage());
        WriteFile("ISOTemplate.iso", fingerData.ISOTemplate());
        WriteFile("ANSITemplate.ansi", fingerData.ANSITemplate());
        WriteFile("ISOImage.iso", fingerData.ISOImage());
        WriteFile("WSQ.wsq", fingerData.WSQImage());

    }

    private void WriteFile(String filename, byte[] bytes) {
        try {
            String path = Environment.getExternalStorageDirectory() + "//FingerData";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            path = path + "//" + filename;
            file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(path);
            stream.write(bytes);
            stream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void OnDeviceDetached() {
        UnInitScanner();
        soundDeviceConnection();
        SetTextOnUIThread(lblCapturedImg, lblCaptured, "Device removed", Color.RED);
    }

    @Override
    public void OnHostCheckFailed(String err) {
        try {
            SetLogOnUIThread(err);
            Toast.makeText(this, err, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {

        }
    }


/*
    private class VerifyFingerTask extends AsyncTask<String, Void, FingerCaptureResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        FingerCaptureResponse fingerCaptureResponse = new FingerCaptureResponse();

        @Override
        protected FingerCaptureResponse doInBackground(String... params) {

            try {

                jsonObject = new JSONObject();
                jsonObject.put("Base64Pic", params[0]);
                jsonObject.put("venue_id", "1");
                jsonObject.put("user_id", userID);
                jsonObject.put("temple_code", templeCode);
                jsonObject.put("booking_date", getCurrentDate());
                jsonObject.put("DeviceId", deviceID);
                jsonObject.put("Appid", appID);
                jsonObject.put("DeviceIp", deviceIP);
                fingerCaptureResponse = new ServiceResponse().getURLConnection(jsonObject, new ListOfUrls().saveFingerVerification());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return fingerCaptureResponse;
        }

        @Override
        protected void onPostExecute(FingerCaptureResponse fingerCaptureResponse) {
            dismissProgressDialog();
            if (fingerCaptureResponse != null) {
                setVerifiedResponse(fingerCaptureResponse);
            }
        }
    }
*/


/*
    private void setVerifiedResponse(FingerCaptureResponse fingerCaptureResponse) {

        try {
            if (fingerCaptureResponse.getStatus().equalsIgnoreCase("True")) {
                JSONObject resultJsonObject = fingerCaptureResponse.getJsonObject();

                if (resultJsonObject.getString("ResponseStatus").equalsIgnoreCase("True")) {
                    if (resultJsonObject.getString("ResponseCode").equalsIgnoreCase("PI100")) {
                        btnCapture.setClickable(true);
                        btnCapture.setBackgroundResource(R.color.colorPrimaryDark);
                        playCaptureSoundSuccess();
                        SetTextOnUIThread(lblCapturedImg, lblCaptured, "Success", Color.parseColor("#308A0C"));
                        Toast.makeText(this, "Captured successfully", Toast.LENGTH_SHORT).show();

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            SetTextOnUIThread(lblCapturedImg, lblCaptured, "Try ag", Color.parseColor("#308A0C"));
                        }

                        if ( networkDetector.isConnectingToInternet()) {
                            scannerAction = CommonMethod.ScannerAction.Capture;
                            StartAsyncCapture();
                        } else {
                            btnCapture.setClickable(true);
                            btnCapture.setBackgroundResource(R.color.colorPrimaryDark);
                            ShowNoInternetDialog();
                        }

                    }
                } else {

                    btnCapture.setClickable(true);
                    btnCapture.setBackgroundResource(R.color.colorPrimaryDark);
                    playCaptureSoundError();

                }

            } else if (fingerCaptureResponse.getStatus().equalsIgnoreCase("false")) {
                btnCapture.setBackgroundResource(R.color.colorPrimaryDark);

                if (fingerCaptureResponse.getErrorCode() == 401) {
                    btnCapture.setClickable(true);
                    showToastMessage(fingerCaptureResponse.getMessage());
                }

                else if (fingerCaptureResponse.getErrorCode() == 500) {
                    btnCapture.setClickable(true);
                    showToastMessage(fingerCaptureResponse.getMessage());
                }

                else {
                    btnCapture.setClickable(true);
                    showToastMessage(fingerCaptureResponse.getMessage());
                }

            }

            else {

                btnCapture.setBackgroundResource(R.color.colorPrimaryDark);
                btnCapture.setClickable(true);
                showToastMessage(fingerCaptureResponse.getMessage());
            }


        } catch (JSONException ex) {

            btnCapture.setBackgroundResource(R.color.colorPrimaryDark);
            btnCapture.setClickable(true);
            showToastMessage(ex.getMessage());

        }

    }
*/

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    private String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        String date = df.format(cal.getTime());
        return date;
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

/*
    private void showLogOutAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Logout");
        builder.setMessage("Do you want to exit from TMS Finger Capture?");
        builder.setIcon((false) ? R.mipmap.ic_action_checked : R.mipmap.ic_launcher);
        builder.setCancelable(true);
        builder.setPositiveButton("Logout",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Cache.putData(CatchValue.USER_ID, FingerCaptureActivity.this, "", Cache.CACHE_LOCATION_DISK);
                        Cache.putData(CatchValue.TEMPLE_CODE, FingerCaptureActivity.this, "", Cache.CACHE_LOCATION_DISK);
                        Cache.putData(CatchValue.TEMPLE_ID, FingerCaptureActivity.this, "", Cache.CACHE_LOCATION_DISK);
                        Cache.putData(CatchValue.USER_NAME, FingerCaptureActivity.this, "", Cache.CACHE_LOCATION_DISK);
                        FingerCaptureActivity.this.finish();

                        //  stopRunningThread();
                    }
                });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }
*/

    private void stopRunningThread() {
        running = false;
        if (!Thread.currentThread().isInterrupted()){
            Thread.currentThread().isInterrupted();
        }
    }

/*
    private class SendFingerCapturedCount extends AsyncTask<Void,Void,Void>{

        String count;

        public SendFingerCapturedCount(String presentCount) {
            this.count = presentCount;
        }

        @Override
        protected Void doInBackground(Void... params) {
//            sendCountTask(count);
            return null;
        }
    }
*/

/*
    private void sendCountTask(String count) {

        if (networkDetector.isConnectingToInternet()){

            try {

                JSONObject jsonObject = new JSONObject();;
                jsonObject.put("venue_id", "1");
                jsonObject.put("user_id", userID);
                jsonObject.put("temple_id",templeID);
                jsonObject.put("temple_code", templeCode);
                jsonObject.put("day_count",count);
*/
/*                jsonObject.put("booking_date", getCurrentDate());
                jsonObject.put("DeviceId", deviceID);
                jsonObject.put("Appid", appID);*//*

                jsonObject.put("device_id", deviceIP);
                JSONArray jsonArray = new JSONArray();
                JSONObject object = new JSONObject();
                jsonArray.put(jsonObject);
                object.put("lstBiometric",jsonArray);
                //System.out.println("############################################################# "+object);
                new ServiceResponse().getURLConnection(object, new ListOfUrls().saveFingerCaptureCount());
                // System.out.println("#############################################################");

            }catch (JSONException ex){
                ex.printStackTrace();
            }

        }
    }
*/

    @Override
    protected void onResume() {
        running = true;
        startRunningThread();
        super.onResume();

    }

    private void startRunningThread() {

        Runnable myRunnable = new Runnable(){
            public void run(){

                while(running)
                {
                    try {
                        Thread.sleep(5*60*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    new SendFingerCapturedCount(textCount.getText().toString().trim()).execute();
                }
            }
        };

        updateThread = new Thread(myRunnable);
        updateThread.start();
    }

    @Override
    protected void onPause() {
        running = false;
        stopRunningThread();
        super.onPause();
    }
}
