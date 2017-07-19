package com.tms.govt.champcash.home.slidemenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.localdatabase.ChampCashDB;
import com.tms.govt.champcash.home.network.ConnectionDetector;
import com.tms.govt.champcash.home.report.Champcash;
import com.tms.govt.champcash.home.session.Cache;
import com.tms.govt.champcash.home.session.CatchValue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by govt on 25-04-2017.
 */

public class MyProfileActivity extends Activity {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MyProfileActivity";

    private String keyID;
    List<Champcash> champcashList;

    private String name, emailID, dateofbirth, mobileNumber, country, state, city;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChooseTask;
    private String user_id, user_type;

//    private ProgressDialog progressDialog;
    private EditText userName, userMobile, userDateOfBirth, userEmail,userState, userCity;
    private EditText userCountry;
    private List<String> countryList;
    private ArrayAdapter<String> countryAdapter;

    private Button resetButton, updateButton;
    private ImageView profilePicImageView;
    private String userProfilePic;
    private String img;
    public static int i = 1;
    private TextView activityName;
    private ImageView activityBack;
    private String profileImg,userpassword="123456";
    ChampCashDB CHMPCASHDB;
    String GetChampCash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        CHMPCASHDB = new ChampCashDB(MyProfileActivity.this);

        activityName = (TextView) findViewById(R.id.header_activity_title);
        resetButton = (Button) findViewById(R.id.button_profile_reset);
        updateButton = (Button) findViewById(R.id.button_profile_update);
        profilePicImageView = (ImageView) findViewById(R.id.My_Profile_Pic);
        activityBack = (ImageView) findViewById(R.id.header_back);
        userCountry = (EditText) findViewById(R.id.edit_country);
        userName = (EditText) findViewById(R.id.profile_name);
        userEmail = (EditText) findViewById(R.id.profile_emailID);
        userDateOfBirth = (EditText) findViewById(R.id.profile_dateofdirth);
        userMobile = (EditText) findViewById(R.id.profile_mobileNo);
        userState = (EditText) findViewById(R.id.edit_state);
        userCity = (EditText) findViewById(R.id.edit_city);
        activityName.setText("MY PROFILE");

        champcashList = new ArrayList<>();

        cd = new ConnectionDetector(MyProfileActivity.this);

        List<Champcash> champcashList =CHMPCASHDB.getAllDetails();
        if (champcashList.size() != 0) {
            for (int i = 0; i < champcashList.size(); i++) {
                userName.setText(champcashList.get(i).getUserName());
                userEmail.setText(champcashList.get(i).getEmailID());
                userDateOfBirth.setText(champcashList.get(i).getDateOfBirth());
                userMobile.setText(champcashList.get(i).getMobileNumber());
                userCountry.setText(champcashList.get(i).getCountry());
                userState.setText(champcashList.get(i).getState());
                userCity.setText(champcashList.get(i).getCity());
                if (champcashList.get(i).getImage() != null) {

                    Bitmap bm = StringToBitMap(champcashList.get(i).getImage());
                    profilePicImageView.setImageBitmap(bm);
                    Cache.putData(CatchValue.IMAGE, MyProfileActivity.this, bm, Cache.CACHE_LOCATION_DISK);

                }
            }
        }

           /* countryList = new ArrayList<String>();
            updateCountries();
            spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        userCountry = countryList.get(position);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/

            profilePicImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isInternetPresent = cd.isConnectionAvailable();
                    if (isInternetPresent) {

                        final CharSequence[] items = {getResources().getString(R.string.text_take_photo), getResources().getString(R.string.text_choose_from_gallery)};
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
                        builder.setTitle(getResources().getString(R.string.text_add_photo));
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                boolean result = ProfileCapture.checkPermission(MyProfileActivity.this);

                                if (items[item].equals(getResources().getString(R.string.text_take_photo))) {
                                    userChooseTask = getResources().getString(R.string.text_take_photo);
                                    if (result)
                                        cameraIntent();
                                } else if (items[item].equals(getResources().getString(R.string.text_choose_from_gallery))) {
                                    userChooseTask = getResources().getString(R.string.text_choose_from_gallery);
                                    if (result)
                                        galleryIntent();
                                }
                            }
                        });
                        builder.show();
                    } else {
                        ShowNoInternetDialog();
                    }
                }


            });

            resetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isInternetPresent = cd.isConnectionAvailable();
                    if (isInternetPresent) {
                        showResetAlertDialog(MyProfileActivity.this, "RESET", "Do you want reset your profile ?", true);
                    } else {
                        ShowNoInternetDialog();
                    }
                }
            });


            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    name = userName.getText().toString().trim();
                    emailID = userEmail.getText().toString().trim();
                    mobileNumber = userMobile.getText().toString().trim();
                    dateofbirth = userDateOfBirth.getText().toString().trim();
                    country = userCountry.getText().toString().trim();
                    state = userState.getText().toString().trim();
                    city = userCity.getText().toString().trim();
                    profileImg = profilePicImageView.toString();

                    if (TextUtils.isEmpty(name)) {
                        userName.setError("Please Enter Name");
                        userName.requestFocus();
                    } else if (TextUtils.isEmpty(emailID)) {
                        userEmail.setError("Please Enter Email");
                        userEmail.requestFocus();
                    } else if (TextUtils.isEmpty(mobileNumber)) {
                        userMobile.setError("Please Enter Mobile Number");
                        userMobile.requestFocus();
                    } else if (TextUtils.isEmpty(dateofbirth)) {
                        Toast.makeText(MyProfileActivity.this, "Please Select Date!", Toast.LENGTH_SHORT).show();
                        userDateOfBirth.requestFocus();
                    } else if (TextUtils.isEmpty(country)) {
                        userCountry.setError("Please Enter Country");
                        userCountry.requestFocus();
                    } else if (TextUtils.isEmpty(state)) {
                        userState.setError("Please Enter State");
                        userState.requestFocus();
                    } else if (TextUtils.isEmpty(city)) {
                        userCity.setError("Please Enter City");
                        userCity.requestFocus();
                    } else {
                        isInternetPresent = cd.isConnectionAvailable();
                        if (isInternetPresent) {

                            img = updateProfile();
                            CHMPCASHDB.updateChampcash(new Champcash(name, emailID, userpassword, dateofbirth, mobileNumber, country, state, city, img));
                            finish();

                        } else {
                            ShowNoInternetDialog();
                        }
                    }
                }
            });

            activityBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

    }

    // When click back button on device
    @Override
    public void onBackPressed() {
        this.finish();
        //super.onBackPressed();
    }

    public String updateProfile(int profile){
        return "";
    }

    public String updateProfile(){

        try {

            profilePicImageView.buildDrawingCache();
            Bitmap bitmap = profilePicImageView.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            profileImg = Base64.encodeToString(b, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return profileImg;
    }


/*
    private void updateCountries() {
        countryList= Arrays.asList(getResources().getStringArray(R.array.select_country));
        countryAdapter = new ArrayAdapter<String>(MyProfileActivity.this, R.layout.spinner_text_view, countryList){

            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return false;
                } else {
                    return true;
                }
            }
        };
        spinnerCountry.setAdapter(countryAdapter);
        countryAdapter.setDropDownViewResource(R.layout.spinner_text_view);
    }
*/


    private void showResetAlertDialog(Context context, String title, String message, boolean status) {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon((status) ? R.mipmap.ic_action_info : R.mipmap.ic_action_warning);
        alertDialog.setButton(getResources().getString(R.string.text_button_reset), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent homeIntent = new Intent(MyProfileActivity.this, MyProfileActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                CHMPCASHDB.signOutChampcash(new Champcash("","","","","","","","",""));
                startActivity(homeIntent);
                MyProfileActivity.this.finish();
//                Intent intent = new Intent(MyProfileActivity.this, MyProfileActivity.class);
//                startActivity(intent);
//                MyProfileActivity.this.finish();
            }
        });
        alertDialog.setButton2(getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(16);
    }

    public void ShowNoInternetDialog() {
        showAlertDialog(MyProfileActivity.this, getResources().getString(R.string.text_no_internet_connection),
                MyProfileActivity.this.getResources().getString(R.string.text_please_check_your_network), false);
    }

    //show alert
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
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

    private void galleryIntent() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.text_select_file)), SELECT_FILE);

    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                profilePicImageView.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        profilePicImageView.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        profilePicImageView.setImageBitmap(thumbnail);

    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
