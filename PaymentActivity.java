package com.tms.govt.champcash.home.payment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.network.ConnectionDetector;

/**
 * Created by govt on 29-04-2017.
 */

public class PaymentActivity extends Activity {

    Boolean isInternetPresent = false;
    ConnectionDetector cd;



    private ImageView backButton;
    private TextView textTitle;
    private ProgressDialog progressDialog;
    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cd = new ConnectionDetector(PaymentActivity.this);

        backButton = (ImageView) findViewById(R.id.header_back);
        textTitle = (TextView) findViewById(R.id.header_activity_title);
        textTitle.setText("Champ Cash");

        mWebView = (WebView) findViewById(R.id.webViewPayment);
        mWebView.clearCache(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setBuiltInZoomControls(true);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());

        isInternetPresent = cd.isConnectionAvailable();
        if (isInternetPresent) {

        } else {
            ShowNoInternetDialog();
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void ShowNoInternetDialog() {
        showAlertDialog(PaymentActivity.this, getResources().getString(R.string.text_no_internet_connection),
                getResources().getString(R.string.text_please_check_your_network), false);
    }

    private void showAlertDialog(Context context, String title, String message, Boolean status) {
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

    private void showCancelAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon((status) ? R.mipmap.ic_action_checked : R.mipmap.ic_action_warning);
        alertDialog.setButton2(getResources().getString(R.string.text_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                NavigateToPaymentOrderActivity();
            }
        });
        alertDialog.setButton(getResources().getString(R.string.text_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(16);
    }

    public void NavigateToPaymentOrderActivity() {
        Intent intent = new Intent(PaymentActivity.this, OrderDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(PaymentActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.text_processing));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
