package com.tms.govt.champcash.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.localdatabase.ChampCashDB;
import com.tms.govt.champcash.home.login.LoginActivity;
import com.tms.govt.champcash.home.login.SignupActivity;
import com.tms.govt.champcash.home.login.TabActivity1;
import com.tms.govt.champcash.home.network.ConnectionDetector;
import com.tms.govt.champcash.home.report.Champcash;
import com.tms.govt.champcash.home.session.Cache;
import com.tms.govt.champcash.home.session.CatchValue;

import java.util.HashMap;
import java.util.List;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * Created by govt on 15-04-2017.
 */

public class HomeActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    SliderLayout sliderLayout;
    HashMap<String, Integer> Hash_file_maps ;

    ConnectionDetector cd;
    Boolean isInternetPresent = false;

    private Button btnSignUp, btnGoogleSignUP, btnFacebookSignup;
    private TextView textChangeDevice, textAbout, textNeedHelp;
    private ImageView champCashLogo;

    ProgressDialog progressDialog;

    //Signing Options
    private GoogleSignInOptions gso;

    //google api client
    private GoogleApiClient mGoogleApiClient;

    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;

    private NetworkImageView profilePhoto;

    //Image Loader
    private ImageLoader imageLoader;

//    private ImageView image;

    private String userID;

    ChampCashDB CHAMPCASHDB;
    private String keyID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignUp = (Button) findViewById(R.id.btn_signup);
        btnGoogleSignUP = (Button) findViewById(R.id.btn_signup_google);
        btnFacebookSignup = (Button) findViewById(R.id.btn_signup_facebook);
        textChangeDevice = (TextView) findViewById(R.id.textChangeDevice);
        textAbout = (TextView) findViewById(R.id.text_about_champ);
        textNeedHelp = (TextView) findViewById(R.id.text_need_help);
//        champCashLogo = (ImageView) findViewById(R.id.champCash_logo);
        progressDialog = new ProgressDialog(HomeActivity.this);
//        image = (ImageView) findViewById(R.id.imageViewSlider);
        progressDialog.setMessage("Signing in....");
//        profilePhoto = (NetworkImageView) findViewById(R.id.profileImage);

        cd = new ConnectionDetector(HomeActivity.this);

        isInternetPresent = cd.isConnectionAvailable();

        userID = (String) Cache.getData(CatchValue.USER_ID, HomeActivity.this);

        CHAMPCASHDB = new ChampCashDB(HomeActivity.this);
        List<Champcash> champcashList =CHAMPCASHDB.getAllDetails();
        for(int i=0;i<champcashList.size();i++) {
            keyID=champcashList.get(i).getIndexID();
            Cache.putData(CatchValue.USER_ID,getApplicationContext(),keyID,Cache.CACHE_LOCATION_DISK);
        }

//        Imageslide();

        //Initializing google signin option
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

//        signInButton.setSize(SignInButton.SIZE_WIDE);
//        signInButton.setScopes(gso.getScopeArray());

        //Initializing google api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectionAvailable();
                if (isInternetPresent){

                    Intent signupIntent = new Intent(HomeActivity.this, SignupActivity.class);
                    startActivity(signupIntent);
                } else {
                    ShowNoInternetDialog();
                }
            }
        });

        btnGoogleSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectionAvailable();
                if (isInternetPresent){

                    progressDialog.show();

                    //Creating an intent
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

                    //Starting intent for result
                    startActivityForResult(signInIntent, RC_SIGN_IN);

                } else {
                    ShowNoInternetDialog();
                }
            }
        });

        btnFacebookSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(HomeActivity.this, "Under Process!", Toast.LENGTH_SHORT).show();

            }
        });

        textChangeDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (keyID == null){
                    Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                } else {
                    Intent tabIntent = new Intent(HomeActivity.this, TabActivity1.class);
                    startActivity(tabIntent);
                }
            }
        });

        textAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Under Process!", Toast.LENGTH_SHORT).show();
            }
        });

        textNeedHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Under Process!", Toast.LENGTH_SHORT).show();
            }
        });

        Hash_file_maps = new HashMap<>();

        sliderLayout = (SliderLayout)findViewById(R.id.slider);

        Hash_file_maps.put("Champ Cash Logo", R.drawable.champcash_logo);
        Hash_file_maps.put("Champ Cash Splash", R.drawable.splash_screen);
        Hash_file_maps.put("Big Basket", R.drawable.ic_big_basket);
        Hash_file_maps.put("Home Shop", R.drawable.ic_homeshop18);
        Hash_file_maps.put("Rihanna", R.drawable.rihanna);

        for(String name : Hash_file_maps.keySet()) {

            TextSliderView textSliderView = new TextSliderView(HomeActivity.this);
            textSliderView
                    .description(name)
                    .image(Hash_file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);
            sliderLayout.addSlider(textSliderView);
        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(this);

    }

/*
    public void Imageslide(){
        image.postDelayed(new Runnable(){
                              @Override
                              public void run() {
                                  image.setImageResource(R.drawable.champcash_logo);
                              }}
                ,0);

        image.postDelayed(new Runnable(){
                              @Override
                              public void run() {
                                  image.setImageResource(R.drawable.splash_screen);
                              }}
                ,3000);

        image.postDelayed(new Runnable(){
                              @Override
                              public void run() {
                                  image.setImageResource(R.drawable.ic_big_basket);
                              }}
                ,6000);

        image.postDelayed(new Runnable(){
                              @Override
                              public void run() {
                                  image.setImageResource(R.drawable.ic_homeshop18);
                              }}
                ,9000);
        image.postDelayed(new Runnable(){
                              @Override
                              public void run() {
                                  image.setImageResource(R.drawable.rihanna);
                              }}
                ,12000);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Imageslide();
                    }
                },
                15000
        );
    }
*/

    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);

            progressDialog.dismiss();
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {

        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            /*//Displaying name and email
            textViewName.setText(acct.getDisplayName());
            textViewEmail.setText(acct.getEmail());

            //Initializing image loader

            imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
                    .getImageLoader();
*/

//            //Loading image
//            profilePhoto.setImageUrl(acct.getPhotoUrl().toString(), imageLoader);

//            profilePhoto.setImageUrl(acct.getPhotoUrl().toString(), imageLoader);

            Intent tabIntent = new Intent(HomeActivity.this, TabActivity1.class);
            startActivity(tabIntent);

        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    private void ShowNoInternetDialog() {

        showAlertDialog(HomeActivity.this, getResources().getString(R.string.text_no_internet_connection),
                getResources().getString(R.string.text_please_check_your_network), false);
    }

    private void showAlertDialog(HomeActivity homePageActivity, String title, String message, Boolean status) {

        AlertDialog alertDialog = new AlertDialog.Builder(homePageActivity).create();
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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
