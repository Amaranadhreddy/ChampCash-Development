package com.tms.govt.champcash.home.challenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tms.govt.champcash.R;

/**
 * Created by govt on 24-04-2017.
 */

public class GameActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    public void play(View v)
    {
        Intent i=new Intent(this,PlayActivity.class);
        startActivity(i);
    }

    public void highscore(View v)
    {
        Intent i=new Intent(this,HighscoreActivity.class);
        startActivity(i);
    }

    public void setting(View v)
    {
        Intent i=new Intent(this,SettingActivity.class);
        startActivity(i);
    }

    public void exit(View v)
    {
        System.exit(0);
    }

}
