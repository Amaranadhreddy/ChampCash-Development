package com.tms.govt.champcash.home.splash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.TextView;

import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.HomeActivity;
import com.tms.govt.champcash.home.network.ConnectionDetector;
import com.tms.govt.champcash.home.session.Cache;
import com.tms.govt.champcash.home.session.CatchValue;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by govt on 18-04-2017.
 */

public class SplashActivity extends Activity {

    final static long DURATION = 3 * 1000;

    private String IPAddress;
//    private Locale locale;

    ConnectionDetector cd;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        cd = new ConnectionDetector(SplashActivity.this);

        isInternetPresent = cd.isConnectionAvailable();
        if (isInternetPresent) {

            NetworkDetect();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                String appLang = (String) Cache.getData(CatchValue.APP_LANGUAGE, SplashActivity.this);
//                setLanguage(appLang);

                    Intent homeIntent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    finish();

                }
            }, DURATION);
        } else {
            ShowNoInternetDialog();
        }
    }

    private void NetworkDetect() {

        boolean WIFI = false;

        boolean MOBILE = false;

        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))

                if (netInfo.isConnected())

                    WIFI = true;

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))

                if (netInfo.isConnected())

                    MOBILE = true;
        }

        if (WIFI == true)

        {
            IPAddress = GetDeviceIPWiFiData();
            Cache.putData(CatchValue.DEVICE_IP, SplashActivity.this, IPAddress, Cache.CACHE_LOCATION_DISK);
        }

        if (MOBILE == true) {
            IPAddress = GetDeviceIPMobileData();
            Cache.putData(CatchValue.DEVICE_IP, SplashActivity.this, IPAddress, Cache.CACHE_LOCATION_DISK);
        }
    }

    private String GetDeviceIPWiFiData() {

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        @SuppressWarnings("deprecation")
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;

    }

    private String GetDeviceIPMobileData() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Current IP", ex.toString());
        }

        return null;
    }

/*
    private void setLanguage(String appLanguage) {
        locale = getResources().getConfiguration().locale;

        if (!TextUtils.isEmpty(appLanguage)){

            if (appLanguage.equals("en")) {
                setLocaleLanguage("en");
                Cache.putData(CatchValue.APP_LANGUAGE, SplashActivity.this,"en", Cache.CACHE_LOCATION_DISK);

            }
            else if (appLanguage.equals("te")) {
                setLocaleLanguage("te");
                Cache.putData(CatchValue.APP_LANGUAGE, SplashActivity.this, "te", Cache.CACHE_LOCATION_DISK);
            }

            else {
                setLocaleLanguage("en");
                Cache.putData(CatchValue.APP_LANGUAGE, SplashActivity.this,"en", Cache.CACHE_LOCATION_DISK);
            }

        }

        else {
            setLocaleLanguage("en");
            Cache.putData(CatchValue.APP_LANGUAGE, SplashActivity.this,"en", Cache.CACHE_LOCATION_DISK);

        }
    }
*/

/*
    private void setLocaleLanguage(String language) {
        locale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }
*/

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
}
