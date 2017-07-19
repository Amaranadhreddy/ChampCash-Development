package com.tms.govt.champcash.home.challenge;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.tms.govt.champcash.R;

/**
 * Created by govt on 24-04-2017.
 */

public class SettingActivity extends Activity {

    CheckBox ch1;
    int volume;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ch1=(CheckBox) findViewById(R.id.checkBox1);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("higher", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        volume=pref.getInt("vloume", 0);

        if(volume==1)
        {
            ch1.setChecked(true);
        }
    }
    public void volume(View v) {
        ch1 = (CheckBox)v;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("higher", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if(ch1.isChecked())
        {
            editor.putInt("vloume", 1);
            editor.commit();
            Toast.makeText(this,"volume on", Toast.LENGTH_LONG).show();
        }
        else
        {
            editor.putInt("vloume", 0);
            editor.commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }
}