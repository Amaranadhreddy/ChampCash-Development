package com.tms.govt.champcash.home.challenge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.tms.govt.champcash.R;

/**
 * Created by govt on 24-04-2017.
 */

public class PlayActivity extends Activity {

    MediaPlayer mp1,jump,takecoin;
    gameloop gameLoopThread;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //phone state
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyMgr.listen(new TeleListener(), PhoneStateListener.LISTEN_CALL_STATE);
        //for no title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GameView(this));
    }

    public class GameView extends SurfaceView {
        Bitmap bmp,pause;
        Bitmap background,kinfe,note1,powerimg,note2;
        Bitmap run1;
        Bitmap run2;
        Bitmap run3;
        Bitmap coin;
        Bitmap exit;

        private SurfaceHolder holder;
        private int x = 0,y=0,z=0,delay=0,getx,gety,sound=1;
        int show=0,sx,sy;
        int cspeed=0,kspeed=0,gameover=0;
        int score=0,health=100,reset=0;
        int pausecount=0,volume,power=0,powerrun=0,shieldrun=0;


        @SuppressWarnings("deprecation")
        @SuppressLint("NewApi")
        public GameView(Context context)
        {
            super(context);

            gameLoopThread = new gameloop(this);
            holder = getHolder();

            holder.addCallback(new SurfaceHolder.Callback() {
                @SuppressWarnings("deprecation")
                @Override
                public void surfaceDestroyed(SurfaceHolder holder)
                {
                    //for stoping the game
                    gameLoopThread.setRunning(false);
                    gameLoopThread.getThreadGroup().interrupt();
                }

                @SuppressLint("WrongCall")
                @Override
                public void surfaceCreated(SurfaceHolder holder)
                {
                    gameLoopThread.setRunning(true);
                    gameLoopThread.start();

                }
                @Override
                public void surfaceChanged(SurfaceHolder holder, int format,int width, int height)
                {
                }
            });

            //getting the screen size
            Display display = getWindowManager().getDefaultDisplay();

            sx = display.getWidth();
            sy = display.getHeight();;
            cspeed=sx/2;
            kspeed=sx/2;
            powerrun=(3*sx/4);
            shieldrun=sx/8;
            background = BitmapFactory.decodeResource(getResources(), R.drawable.back);
            run1=BitmapFactory.decodeResource(getResources(), R.drawable.run1);
            run2=BitmapFactory.decodeResource(getResources(), R.drawable.run2);
            run3=BitmapFactory.decodeResource(getResources(), R.drawable.run3);
            coin=BitmapFactory.decodeResource(getResources(), R.drawable.coin);
            exit=BitmapFactory.decodeResource(getResources(), R.drawable.exit);
            kinfe=BitmapFactory.decodeResource(getResources(), R.drawable.kinfe);
            note1=BitmapFactory.decodeResource(getResources(), R.drawable.note1);
            pause=BitmapFactory.decodeResource(getResources(), R.drawable.pause);
            powerimg=BitmapFactory.decodeResource(getResources(), R.drawable.power);
            note2=BitmapFactory.decodeResource(getResources(), R.drawable.note2);

            exit=Bitmap.createScaledBitmap(exit, 25,25, true);
            pause=Bitmap.createScaledBitmap(pause, 25,25, true);
            powerimg=Bitmap.createScaledBitmap(powerimg, 25,25, true);
            note2=Bitmap.createScaledBitmap(note2, sx,sy, true);
            run1=Bitmap.createScaledBitmap(run1, sx/9,sy/7, true);
            run2=Bitmap.createScaledBitmap(run2, sx/9,sy/7, true);
            run3=Bitmap.createScaledBitmap(run3, sx/9,sy/7, true);
            coin=Bitmap.createScaledBitmap(coin, sx/16,sy/24, true);
            background=Bitmap.createScaledBitmap(background, 2*sx,sy, true);
            //health dec
            note1=Bitmap.createScaledBitmap(note1, sx,sy, true);

            mp1=MediaPlayer.create(PlayActivity.this,R.raw.game);
            jump=MediaPlayer.create(PlayActivity.this,R.raw.jump);
            takecoin=MediaPlayer.create(PlayActivity.this,R.raw.cointake);
        }

        // on touch method

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            if(event.getAction()==MotionEvent.ACTION_DOWN)
            {
                show=1;

                getx=(int) event.getX();
                gety=(int) event.getY();
                //exit
                if(getx<25&&gety<25)
                {
                    //high score
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("higher", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("score", score);
                    editor.commit();
                    System.exit(0);

                }
                // restart game
                if(getx>91&&gety<25)
                {
                    if(health<=0)
                    {
                        gameLoopThread.setPause(0);
                        health=100;
                        score=0;

                    }
                }
                //pause game
                if((getx>(sx-25)&&gety<25&&pausecount==0))
                {

                    gameLoopThread.setPause(1);
                    mp1.stop();
                    pausecount=1;
                }
                else if(getx>(sx-25)&&gety<25&&pausecount==1)
                {
                    gameLoopThread.setPause(0);
                    mp1.start();
                    pausecount=0;
                }
            }

            return true;
        }


        @SuppressLint("WrongCall")
        @Override
        protected void onDraw(Canvas canvas)
        {

            //volume
            SharedPreferences pref = getApplicationContext().getSharedPreferences("higher", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            volume=pref.getInt("vloume", 0);
            if(volume==0)
            {
                sound=0;
            }

            canvas.drawColor(Color.BLACK);

            //background moving
            z=z-10;
            if(z==-sx)
            {
                z=0;
                canvas.drawBitmap(background, z, 0, null);

            }
            else
            {
                canvas.drawBitmap(background, z, 0, null);
            }

            //running player

            x+=5;
            if(x==20)
            {
                x=5;
            }

            if(show==0)
            {
                if(x%2==0)
                {
                    canvas.drawBitmap(run3, sx/16, 15*sy/18, null);

                }
                else
                {
                    canvas.drawBitmap(run1, sx/16, 15*sy/18, null);

                }

                //kinfe hit
                if(kspeed==20)
                {
                    kspeed=sx;
                    health-=25;
                    canvas.drawBitmap(note1, 0, 0, null);
                }

                //power take
                if(powerrun==30)
                {
                    powerrun=3*sx;
                    health+=25;
                    canvas.drawBitmap(note2, 0, 0, null);
                }
            }
            //power
            powerrun=powerrun-10;
            canvas.drawBitmap(powerimg, powerrun, 15*sy/18, null);

            if(powerrun<0)
            {
                powerrun=3*sx/4;
            }

            //kinfe
            kspeed=kspeed-20;
            canvas.drawBitmap(kinfe, kspeed, 15*sy/18, null);
            if(kspeed<0)
            {
                kspeed=sx;
            }

            // for jump
            if(show==1)
            {
                if(sound==1)
                {
                    jump.start();
                }

                canvas.drawBitmap(run2, sx/16, 3*sy/4, null);
                //score
                if(cspeed<=sx/8&&cspeed>=sx/16)
                {
                    if(sound==1)
                    {
                        takecoin.start();
                    }
                    cspeed=sx/2;
                    score+=10;

                }

                // jump-hold
                delay+=1;
                if(delay==3)
                {
                    show=0;
                    delay=0;
                }
            }

            //for coins
            cspeed=cspeed-5;
            if(cspeed==-sx/2)
            {
                cspeed=sx/2;
                canvas.drawBitmap(coin, cspeed, 3*sy/4, null);

            }
            else
            {
                canvas.drawBitmap(coin, cspeed, 3*sy/4, null);
            }




            //score
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setAntiAlias(true);
            paint.setFakeBoldText(true);
            paint.setTextSize(15);
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Score :"+score, 3*sx/4, 20, paint);
            //exit
            canvas.drawBitmap(exit, 0, 0, null);
            if(sound==1)
            {
                mp1.start();
                mp1.setLooping(true);
            }
            else
            {
                mp1.stop();
            }
            //health
            Paint myPaint = new Paint();
            myPaint.setColor(Color.RED);
            myPaint.setStrokeWidth(10);
            myPaint.setAntiAlias(true);
            myPaint.setFakeBoldText(true);
            canvas.drawText("Health :"+health, 0, (sy/8)-5, myPaint);
            canvas.drawRect(0, sy/8, health, sy/8+10, myPaint);

            //game over
            if(health<=0)
            {
                gameover=1;
                mp1.stop();

                //high score
                editor.putInt("score", score);
                editor.commit();

                canvas.drawText("GAMEOVER OVER", sx/2, sy/2, myPaint);
                canvas.drawText("YOUR SCORE : "+score, sx/2, sy/4, myPaint);
                canvas.drawText("Restart", 91, 25, myPaint);
                gameLoopThread.setPause(1);
                canvas.drawBitmap(background, sx, sy, null);
            }
            // restart

            if(reset==1)
            {
                gameLoopThread.setPause(0);
                health=100;
                score=0;
            }

            canvas.drawBitmap(pause, (sx-25), 0, null);
        }

    }

    //phone state
    public class TeleListener extends PhoneStateListener
    {
        public void onCallStateChanged(int state,String incomingNumber)
        {
            if(state==TelephonyManager.CALL_STATE_RINGING)
            {
                mp1.stop();
                System.exit(0);
            }
        }

    }

}
