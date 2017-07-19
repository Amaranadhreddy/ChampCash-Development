package com.tms.govt.champcash.home.scanner;

import android.os.Handler;

import java.lang.ref.WeakReference;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by govt on 09-05-2017.
 */

public class FocusHandler implements Runnable {


    private final int FOCUS_OFF_TIME = 1000;
    private final int FOCUS_ON_TIME = 1000;
    private boolean flag = false;
    private boolean state = false;
    private Handler handler;
    private WeakReference<ZXingScannerView> scannerView;

    public FocusHandler(Handler handler, ZXingScannerView scannerView){
        this.handler = handler;
        this.flag = false;
        this.scannerView = new WeakReference<>(scannerView);
    }

    public void start(){
        state = true;
        this.handler.post(this);
    }

    public void stop(){
        state = false;
        scannerView.clear();
    }

    @Override
    public void run() {
        if (!state || this.scannerView.get() == null){
            return;
        }

        int time;
        if (!flag){
            this.scannerView.get().setAutoFocus(flag);
            time = FOCUS_OFF_TIME;
        }
        else{
            this.scannerView.get().setAutoFocus(flag);
            time = FOCUS_ON_TIME;
        }

        flag = !flag;
        handler.postDelayed(this, time);
    }
}
