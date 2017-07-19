package com.tms.govt.champcash.home.shopping;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tms.govt.champcash.R;

/**
 * Created by govt on 24-04-2017.
 */

public class WebLinkActivity extends Activity {

    private WebView mWebView;
    private ImageView backButton;
    private TextView textTitle;

    private String title;
    private String link;

    private ProgressDialog progressDialog;
//    private MyWebChromeClient mWebChromeClient = null;

    private View mCustomView;
    private LinearLayout mContentView;
    private FrameLayout mCustomViewContainer;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        backButton = (ImageView) findViewById(R.id.activity_champcash_info_back);
        textTitle = (TextView) findViewById(R.id.text_activity_title);


        mWebView = (WebView) findViewById(R.id.shopping_webView);
        mWebView.clearCache(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new myWebClient());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        int position = getIntent().getIntExtra("key", 0);

        /*if (position == 0){
            mWebView.loadUrl("http://www.amazon.in/");
        } else if (position == 1){
            mWebView.loadUrl("https://www.flipkart.com/");
        } else if (position == 2) {
            mWebView.loadUrl("https://www.snapdeal.com/");
        } else if (position == 3){
            mWebView.loadUrl("http://www.ebay.in/");
        }*/

        switch (position) {
            case 0:
                textTitle.setText("Amazon");
                mWebView.loadUrl("http://www.amazon.in/");
                break;
            case 1:
                textTitle.setText("Fliipcart");
                mWebView.loadUrl("https://www.flipkart.com/");
                break;
            case 2:
                textTitle.setText("Snapdeal");
                mWebView.loadUrl("https://www.snapdeal.com/");
                break;
            case 3:
                textTitle.setText("eBay");
                mWebView.loadUrl("http://www.ebay.in/");
                break;
            case 4:
                textTitle.setText("Paytm");
                mWebView.loadUrl("https://paytm.com/");
                break;
            case 5:
                textTitle.setText("Myntra");
                mWebView.loadUrl("http://www.myntra.com/");
                break;
            case 6:
                textTitle.setText("Jabong");
                mWebView.loadUrl("http://www.jabong.com/");
                break;
            case 7:
                textTitle.setText("Shopclues");
                mWebView.loadUrl("http://www.shopclues.com/");
                break;
            case 8:
                textTitle.setText("Pepperfry");
                mWebView.loadUrl("https://www.pepperfry.com/");
                break;
            case 9:
                textTitle.setText("Homeshop18");
                mWebView.loadUrl("http://www.homeshop18.com/");
                break;
            case 10:
                textTitle.setText("Kraftly");
                mWebView.loadUrl("https://kraftly.com/");
                break;
            case 11:
                textTitle.setText("Shoppers Stop");
                mWebView.loadUrl("https://www.shoppersstop.com/");
                break;
            case 12:
                textTitle.setText("Big Basket");
                mWebView.loadUrl("https://www.bigbasket.com/");
                break;
            case 13:
                textTitle.setText("AboutUs");
                mWebView.loadUrl("https://champcash.com/Home/HowWeWork");
                break;
            case 14:
                textTitle.setText("Champ Cash");
                mWebView.loadUrl("https://play.google.com/store/apps/details?id=com.cash.champ&hl=en");
                break;

            default:
                break;
        }
    }


    public void showProgressDialog() {
        progressDialog = new ProgressDialog(WebLinkActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.text_processing));
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        WebLinkActivity.this.finish();
    }

    private class myWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            dismissProgressDialog();
            showProgressDialog();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dismissProgressDialog();
        }
    }
}
