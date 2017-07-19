package com.tms.govt.champcash.home.slidemenu;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.scanner.FocusHandler;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by govt on 09-05-2017.
 */

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private static final String FORMAT = "format";
    private static final String CONTENT = "content";
    private FocusHandler focusHandler;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    private Switch flashSwitch;
    private boolean hasFlash;
    private static final String FLASH_STATE = "FLASH_STATE";
    private boolean mFlash;
    private Camera camera;
    Camera.Parameters params;
    ViewGroup contentFrame;
    private boolean permission;
    private TextView activityTitle;
    private ImageView activityBack;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        scannerView = new ZXingScannerView(this);
        focusHandler = new FocusHandler(new Handler(), scannerView);
        setContentView(R.layout.activity_scanner);
        contentFrame = (ViewGroup) findViewById(R.id.capture_layout);
        contentFrame.addView(scannerView);

        activityTitle = (TextView) findViewById(R.id.header_activity_title);
        activityBack = (ImageView) findViewById(R.id.header_back);
        activityTitle.setText("QR Scanner and BarCode Scanner");

        activityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP){
            permission=checkPermissionForCamera();
        } else{
            // do something for phones running an SDK before lollipop
        }
        flashSwitch = (Switch) findViewById(R.id.flashSwitch);

        flashSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!hasFlash) {
                    // device doesn't support flash
                    // Show alert message and close the application
                    AlertDialog alert = new AlertDialog.Builder(ScannerActivity.this).create();
                    alert.setTitle("Info");
                    alert.setMessage("Sorry, This device doesn't support flash light!");
                    alert.setIcon(R.mipmap.ic_action_warning);
                    alert.setCancelable(false);
                    alert.setCanceledOnTouchOutside(false);
                    alert.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // closing the application
                            dialog.dismiss();
                            flashSwitch.setChecked(false);
                        }
                    });
                    alert.show();
                    return;
                }
                else {
                    mFlash = !mFlash;
                    scannerView.setFlash(mFlash);
                }

            }
        });

        Bundle extras = getIntent().getExtras();
        try {
            if (extras != null) {
                mFlash = extras.getBoolean(ScannerActivity.FLASH_STATE);
                if(mFlash) {
                    flashSwitch.setChecked(true);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onBackPressed(){
        ScannerActivity.this.finish();
    }
    private boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            requestPermissionForCamera();
            return false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
    }

    private void turnOffFlash() {
        if (mFlash) {
            if (camera == null || params == null) {
                return;
            }
            // playSound();
            camera = Camera.open();
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            mFlash = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.setAutoFocus(true);
        scannerView.requestFocus();
        scannerView.startCamera();
        focusHandler.start();
        scannerView.setFlash(mFlash);
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
        focusHandler.stop();
        turnOffFlash();
    }

    @Override
    public void handleResult(Result rawResult) {

        showDetailVerificationAlertDialog();

    }

    public void requestPermissionForCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(ScannerActivity.this, Manifest.permission.CAMERA)){
            Toast.makeText(ScannerActivity.this, "Camera permission needed. Please allow camera for further processing.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(ScannerActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);
        }
        else {
            ActivityCompat.requestPermissions(ScannerActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);
            scannerView.setResultHandler(this);
            scannerView.setAutoFocus(true);
            scannerView.startCamera();
            focusHandler.start();
        }

    }

    private void showDetailVerificationAlertDialog() {

        final AlertDialog alertDialog = new AlertDialog.Builder(ScannerActivity.this).create();
        alertDialog.setTitle("Scanner Verification");
        alertDialog.setMessage("Success!");
        alertDialog.setIcon(R.mipmap.ic_action_checked);
        alertDialog.setButton(getResources().getString(R.string.text_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                ScannerActivity.this.finish();
            }
        });

        alertDialog.show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(16);
    }

}
