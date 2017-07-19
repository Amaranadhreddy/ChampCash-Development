package com.tms.govt.champcash.home.challenge;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.tms.govt.champcash.R;

/**
 * Created by govt on 24-04-2017.
 */

public class HighscoreActivity extends Activity {

    TextView t1;
    int score,hscore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("higher", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        score=pref.getInt("score", 0);
        hscore=pref.getInt("hscore", 0);

        if(score>hscore)
        {
            editor.putInt("hscore", score);
            editor.commit();
        }
        hscore=pref.getInt("hscore", 0);

        t1=(TextView) findViewById(R.id.textView1);
        t1.setText("Highscore :"+hscore);
    }
}
