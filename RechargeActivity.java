package com.tms.govt.champcash.home.slidemenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.localdatabase.RechargeDB;
import com.tms.govt.champcash.home.network.ConnectionDetector;
import com.tms.govt.champcash.home.recharge.FailurePageActivity;
import com.tms.govt.champcash.home.recharge.SuccessPageActivity;
import com.tms.govt.champcash.home.report.ChampcashRecharge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by govt on 28-04-2017.
 */

public class RechargeActivity extends Activity {

    RechargeDB RECHARGEDB;

    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    private TextView rechargeMobile, rechargeDTH;
    private TextView activityTitle;
    private ImageView activityBack;
    private EditText mobileNumberPrepaid, amountPrepaid, mobileNumberPostPaid, amountPostPaid, mobileNumberDTH, amountDTH;
    private Spinner operatorPrepaidSpinner, operatorPostPaidSpinner, operatorDTHSpinner;
    private List<String> operatorPrepaidList, operatorPostPaidList, operatorDTHList;
    private ArrayAdapter<String> operatorPrepaidAdapter, operatorPostPaidAdapter, operatorDTHAdapter;
    private LinearLayout prepaidLayout, postpaidLayout;
    private LinearLayout mobileLayout, dthLayout;
    private RadioGroup rechargeFilter;
    private RadioButton prepaidFilter, postpaidFilter;
    private Button prepaidButton, postpaidButton, dthButton;
    private Button prepaidAddToCart, postpaidAddToCart, dthAddToCart;
    private String userMobileNumberPrepaid, userAmountPrepaid, userMobileNumberPostpaid, userAmountPostpaid, userMobileNumberDTH, userAmountDTH;
    private String userPrepaid, userPostpaid, userdth;
    private TextView textCount;
    private static int counter = 0;
    private String stringVal;
    private ImageView cartImage;
    AlertDialog.Builder builder;

    private TextView textOperator;

    TelephonyManager telephonyManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        activityTitle = (TextView) findViewById(R.id.header_activity_title);
        activityBack = (ImageView) findViewById(R.id.header_back);
        rechargeMobile = (TextView) findViewById(R.id.recharge_mobile);
        rechargeDTH = (TextView) findViewById(R.id.recharge_dth);
        mobileNumberPrepaid = (EditText) findViewById(R.id.edit_mobile_number);
        amountPrepaid = (EditText) findViewById(R.id.edit_amount);
//        operatorPrepaidSpinner = (Spinner) findViewById(R.id.spinner_operator);
        mobileLayout = (LinearLayout) findViewById(R.id.layout_mobile);
        prepaidLayout = (LinearLayout) findViewById(R.id.layout_prepaid);
        mobileNumberPostPaid = (EditText) findViewById(R.id.edit_mobile_number2);
        amountPostPaid = (EditText) findViewById(R.id.edit_amount2);
        operatorPostPaidSpinner = (Spinner) findViewById(R.id.spinner_operator2);
        prepaidLayout = (LinearLayout) findViewById(R.id.layout_prepaid);
        postpaidLayout = (LinearLayout) findViewById(R.id.layout_postpaid);
        rechargeFilter = (RadioGroup) findViewById(R.id.groupRecharge_filter);
        prepaidFilter = (RadioButton) findViewById(R.id.prepaid_radio_button);
        postpaidFilter = (RadioButton) findViewById(R.id.postpaid_radio_button);
        dthLayout = (LinearLayout) findViewById(R.id.layout_dth);
        operatorDTHSpinner = (Spinner) findViewById(R.id.spinner_operator_dth);
        mobileNumberDTH = (EditText) findViewById(R.id.edit_mobile_number_dth);
        amountDTH = (EditText) findViewById(R.id.edit_amount_dth);
        prepaidButton = (Button) findViewById(R.id.btn_proceed_to_recharge);
        postpaidButton = (Button) findViewById(R.id.btn_proceed_to_recharge2);
        dthButton = (Button) findViewById(R.id.btn_proceed_to_recharge_dth);
        prepaidAddToCart = (Button) findViewById(R.id.prepaid_add_to_cart);
        postpaidAddToCart = (Button) findViewById(R.id.postpaid_add_to_cart);
        dthAddToCart = (Button) findViewById(R.id.dth_add_to_cart);
        textCount = (TextView) findViewById(R.id.tms_cart_count);
        cartImage = (ImageView) findViewById(R.id.cart_img);
        textOperator = (TextView) findViewById(R.id.text_operator);

        RECHARGEDB = new RechargeDB(RechargeActivity.this);

        prepaidFilter.setChecked(true);
        prepaidFilter.setTextColor(getResources().getColor(R.color.colorSlotAvailable));

        cd = new ConnectionDetector(RechargeActivity.this);
        activityTitle.setText(getResources().getString(R.string.text_recharge));

        operatorPostPaidList=new ArrayList<String>();
        operatorPostPaidList = new ArrayList<String>();
        operatorDTHList = new ArrayList<String>();

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        rechargeMobile.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        activityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mobileNumberPrepaid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence string, int start, int before, int count) {

                String mobileNo = String.valueOf(string).trim();
                if (!TextUtils.isEmpty(mobileNo)){
                    if (mobileNo.length() == 10){
                        getServiceProvider();
                    }
                    else {
                        textOperator.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        rechargeMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectionAvailable();
                if (isInternetPresent){
                    setSelectionMobile();
                } else {
                    ShowNoInternetDialog();
                }
            }
        });

        rechargeDTH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectionAvailable();
                if (isInternetPresent){
                    setSelectionDTH();
                } else {
                    ShowNoInternetDialog();
                }
            }
        });

        rechargeFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (prepaidFilter.isChecked()){
                    prepaidFilter.setTextColor(getResources().getColor(R.color.colorSlotAvailable));
                    postpaidFilter.setTextColor(getResources().getColor(R.color.textBlackColor));
                    prepaidLayout.setVisibility(View.VISIBLE);
                    postpaidLayout.setVisibility(View.GONE);
                }
                if (postpaidFilter.isChecked()){
                    postpaidFilter.setTextColor(getResources().getColor(R.color.colorSlotAvailable));
                    prepaidFilter.setTextColor(getResources().getColor(R.color.textBlackColor));
                    postpaidLayout.setVisibility(View.VISIBLE);
                    prepaidLayout.setVisibility(View.GONE);
                }
            }
        });

        operatorPostPaidList=new ArrayList<String>();
        updatePostpaid();
        operatorPostPaidSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {

                    userPostpaid = operatorPostPaidList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        operatorDTHList=new ArrayList<String>();
        updateDTH();
        operatorDTHSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {

                    userdth = operatorDTHList.get(position);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        prepaidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepaidRecharge();
            }
        });

        postpaidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postpaidRecharge();
            }
        });

        dthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dthRecharge();
            }
        });

        prepaidAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectionAvailable();
                if (isInternetPresent){
                    prepaidAddtoCart();
                } else {
                    ShowNoInternetDialog();
                }
            }
        });

        postpaidAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectionAvailable();
                if (isInternetPresent){
                    postpaidAddtoCart();
                } else {
                    ShowNoInternetDialog();
                }
            }
        });

        dthAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectionAvailable();
                if (isInternetPresent){
                    dthAddtoCart();
                } else {
                    ShowNoInternetDialog();
                }
            }
        });

        cartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void prepaidAddtoCart() {
        isInternetPresent = cd.isConnectionAvailable();
        if (isInternetPresent){

            userMobileNumberPrepaid = mobileNumberPrepaid.getText().toString().trim();
//            userPrepaid = operatorPrepaidSpinner.getSelectedItem().toString();
            userAmountPrepaid = amountPrepaid.getText().toString().trim();

            if (TextUtils.isEmpty(userMobileNumberPrepaid)){
                mobileNumberPrepaid.setError("Please Enter Mobile Number");
                mobileNumberPrepaid.requestFocus();
            } else if (mobileNumberPrepaid.getText().length() != 10) {
                mobileNumberPrepaid.setError("Please enter valid mobile number");
                mobileNumberPrepaid.requestFocus();
            } else if (TextUtils.isEmpty(userPrepaid)){
//                getServiceProvider();
                Toast.makeText(RechargeActivity.this, "Please select operator", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(userAmountPrepaid)){
                amountPrepaid.setError("Please enter the amount");
                amountPrepaid.requestFocus();
            } else {

                counter++;
                stringVal = Integer.toString(counter);
                textCount.setText(stringVal);
                Toast.makeText(RechargeActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                mobileNumberPrepaid.setText("");
                amountPrepaid.setText("");

            }

        } else {
            ShowNoInternetDialog();
        }
    }

    private void postpaidAddtoCart(){
        isInternetPresent = cd.isConnectionAvailable();
        if (isInternetPresent){

            userMobileNumberPostpaid = mobileNumberPostPaid.getText().toString().trim();
//            userPostpaid = operatorPostPaidSpinner.getSelectedItem().toString();
            userAmountPostpaid = amountPostPaid.getText().toString().trim();

            if (TextUtils.isEmpty(userMobileNumberPostpaid)){
                mobileNumberPostPaid.setError("Please Enter Mobile Number");
                mobileNumberPostPaid.requestFocus();
            } else if (mobileNumberPostPaid.getText().length() != 10) {
                mobileNumberPostPaid.setError("Please enter valid mobile number");
                mobileNumberPostPaid.requestFocus();
            } else if (TextUtils.isEmpty(userPostpaid)){
                Toast.makeText(RechargeActivity.this, "Please select operator", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(userAmountPostpaid)){
                amountPostPaid.setError("Please enter the amount");
                amountPostPaid.requestFocus();
            } else {
                counter++;
                stringVal = Integer.toString(counter);
                textCount.setText(stringVal);
                Toast.makeText(RechargeActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                mobileNumberPostPaid.setText("");
                amountPostPaid.setText("");
            }

        } else {
            ShowNoInternetDialog();
        }
    }

    private void dthAddtoCart(){
        isInternetPresent = cd.isConnectionAvailable();
        if (isInternetPresent){

            userMobileNumberDTH = mobileNumberDTH.getText().toString().trim();
//            userdth = operatorDTHSpinner.getSelectedItem().toString();
            userAmountDTH = amountDTH.getText().toString().trim();

            if (TextUtils.isEmpty(userdth)){
                Toast.makeText(RechargeActivity.this, "Please select operator", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(userMobileNumberDTH)){
                mobileNumberDTH.setError("Please Enter Mobile Number");
                mobileNumberDTH.requestFocus();
            } else if (mobileNumberDTH.getText().length() != 10) {
                mobileNumberDTH.setError("Please enter valid mobile number");
                mobileNumberDTH.requestFocus();
            }  else if (TextUtils.isEmpty(userAmountDTH)){
                amountDTH.setError("Please enter the amount");
                amountDTH.requestFocus();
            } else {

                counter++;
                stringVal = Integer.toString(counter);
                textCount.setText(stringVal);
                Toast.makeText(RechargeActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                mobileNumberDTH.setText("");
                amountDTH.setText("");

            }

        } else {
            ShowNoInternetDialog();
        }
    }

    // Returns the alphabetic name of current registered operator
    private void getServiceProvider() {

        String operatorName  = telephonyManager.getNetworkOperatorName();
        String operatorName1 = telephonyManager.getSimOperatorName();
        textOperator.setText(operatorName1);
//        String lineName = telephonyManager.getLine1Number();
        System.out.println("Operator Name : " + operatorName);
//        System.out.println("Operator Name : " + lineName);
        System.out.println("Operator Name1 : " + operatorName1);

    }

    private String getMyPhoneNumber(){
        return telephonyManager.getLine1Number();
    }

    private String getMy10DigitPhoneNumber(){
        String s = getMyPhoneNumber();
        return s != null && s.length() > 2 ? s.substring(2) : null;
    }

    private void prepaidRecharge(){
        isInternetPresent = cd.isConnectionAvailable();
        if (isInternetPresent){

            userMobileNumberPrepaid = mobileNumberPrepaid.getText().toString().trim();
//            userPrepaid = operatorPrepaidSpinner.getSelectedItem().toString();
            userPrepaid = textOperator.getText().toString();
            userAmountPrepaid = amountPrepaid.getText().toString().trim();

            if (TextUtils.isEmpty(userMobileNumberPrepaid)){
                mobileNumberPrepaid.setError("Please Enter Mobile Number");
                mobileNumberPrepaid.requestFocus();
            } else if (mobileNumberPrepaid.getText().length() != 10) {
                mobileNumberPrepaid.setError("Please enter valid mobile number");
                mobileNumberPrepaid.requestFocus();
            } else if (TextUtils.isEmpty(userPrepaid)){
//                getServiceProvider();

//                Toast.makeText(RechargeActivity.this, "Please select operator", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(userAmountPrepaid)){
                amountPrepaid.setError("Please enter the amount");
                amountPrepaid.requestFocus();
            } else {

//                RECHARGEDB.addRecharge(new ChampcashRecharge(userMobileNumberPrepaid, userPrepaid, userAmountPrepaid));
                builder = new AlertDialog.Builder(RechargeActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                builder.setTitle("Recharge");
                builder.setMessage("Recharge of Mobile Number?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RECHARGEDB.addRecharge(new ChampcashRecharge(userMobileNumberPrepaid, userPrepaid, userAmountPrepaid));
                        NavigateToSuccessPageActivityPrepaid();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavigateToFailurePageActivityPrepaid();
                    }
                });

                builder.show();
            }

        } else {
            ShowNoInternetDialog();
        }
    }

    private void postpaidRecharge(){
        isInternetPresent = cd.isConnectionAvailable();
        if (isInternetPresent){

            userMobileNumberPostpaid = mobileNumberPostPaid.getText().toString().trim();
//            userPostpaid = operatorPostPaidSpinner.getSelectedItem().toString();
            userAmountPostpaid = amountPostPaid.getText().toString().trim();

            if (TextUtils.isEmpty(userMobileNumberPostpaid)){
                mobileNumberPostPaid.setError("Please Enter Mobile Number");
                mobileNumberPostPaid.requestFocus();
            } else if (mobileNumberPostPaid.getText().length() != 10) {
                mobileNumberPostPaid.setError("Please enter valid mobile number");
                mobileNumberPostPaid.requestFocus();
            } else if (TextUtils.isEmpty(userPostpaid)){
                Toast.makeText(RechargeActivity.this, "Please select operator", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(userAmountPostpaid)){
                amountPostPaid.setError("Please enter the amount");
                amountPostPaid.requestFocus();
            } else {
//                Toast.makeText(RechargeActivity.this, "Success!", Toast.LENGTH_SHORT).show();
//                this.finish();

                builder = new AlertDialog.Builder(RechargeActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                builder.setTitle("Recharge");
                builder.setMessage("Recharge of Mobile Number?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RECHARGEDB.addRecharge(new ChampcashRecharge(userMobileNumberPostpaid, userPostpaid, userAmountPostpaid));
                        NavigateToSuccessPageActivityPostpaid();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavigateToFailurePageActivityPostpaid();
                    }
                });

                builder.show();
            }

        } else {
            ShowNoInternetDialog();
        }
    }

    private void dthRecharge(){
        isInternetPresent = cd.isConnectionAvailable();
        if (isInternetPresent){

            userMobileNumberDTH = mobileNumberDTH.getText().toString().trim();
//            userdth = operatorDTHSpinner.getSelectedItem().toString();
            userAmountDTH = amountDTH.getText().toString().trim();

            if (TextUtils.isEmpty(userdth)){
                Toast.makeText(RechargeActivity.this, "Please select operator", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(userMobileNumberDTH)){
                mobileNumberDTH.setError("Please Enter Mobile Number");
                mobileNumberDTH.requestFocus();
            } else if (mobileNumberDTH.getText().length() != 10) {
                mobileNumberDTH.setError("Please enter valid mobile number");
                mobileNumberDTH.requestFocus();
            }  else if (TextUtils.isEmpty(userAmountDTH)){
                amountDTH.setError("Please enter the amount");
                amountDTH.requestFocus();
            } else {
//                Toast.makeText(RechargeActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                builder = new AlertDialog.Builder(RechargeActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                builder.setTitle("Recharge");
                builder.setMessage("Recharge of DTH?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RECHARGEDB.addRecharge(new ChampcashRecharge(userdth, userMobileNumberDTH, userAmountDTH));
                        NavigateToSuccessPageActivityDTH();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavigateToFailurePageActivityDTH();
                    }
                });

                builder.show();
            }

        } else {
            ShowNoInternetDialog();
        }
    }



    private void NavigateToSuccessPageActivityPrepaid() {
        Intent successIntent = new Intent(RechargeActivity.this, SuccessPageActivity.class);
        successIntent.putExtra("mobile_number_prepaid", userMobileNumberPrepaid);
        successIntent.putExtra("operator_prepaid", userPrepaid);
        successIntent.putExtra("amount_prepaid", userAmountPrepaid);
        startActivity(successIntent);
    }

    private void NavigateToFailurePageActivityPrepaid() {
        Intent failIntent = new Intent(RechargeActivity.this, FailurePageActivity.class);
        failIntent.putExtra("mobile_number_prepaid", userMobileNumberPrepaid);
        failIntent.putExtra("operator_prepaid", userPrepaid);
        failIntent.putExtra("amount_prepaid", userAmountPrepaid);
        startActivity(failIntent);
    }

    private void NavigateToSuccessPageActivityPostpaid() {
        Intent successIntent = new Intent(RechargeActivity.this, SuccessPageActivity.class);
        successIntent.putExtra("mobile_number_prepaid", userMobileNumberPostpaid);
        successIntent.putExtra("operator_prepaid", userPostpaid);
        successIntent.putExtra("amount_prepaid", userAmountPostpaid);
        startActivity(successIntent);
    }

    private void NavigateToFailurePageActivityPostpaid() {
        Intent failIntent = new Intent(RechargeActivity.this, FailurePageActivity.class);
        failIntent.putExtra("mobile_number_prepaid", userMobileNumberPostpaid);
        failIntent.putExtra("operator_prepaid", userPostpaid);
        failIntent.putExtra("amount_prepaid", userAmountPostpaid);
        startActivity(failIntent);
    }

    private void NavigateToSuccessPageActivityDTH() {
        Intent successIntent = new Intent(RechargeActivity.this, SuccessPageActivity.class);
        successIntent.putExtra("operator_prepaid", userdth);
        successIntent.putExtra("mobile_number_prepaid", userMobileNumberDTH);
        successIntent.putExtra("amount_prepaid", userAmountDTH);
        startActivity(successIntent);
    }

    private void NavigateToFailurePageActivityDTH() {
        Intent failIntent = new Intent(RechargeActivity.this, FailurePageActivity.class);
        failIntent.putExtra("operator_prepaid", userdth);
        failIntent.putExtra("mobile_number_prepaid", userMobileNumberDTH);
        failIntent.putExtra("amount_prepaid", userAmountDTH);
        startActivity(failIntent);
    }


    private void updatePrepaid() {
        operatorPrepaidList= Arrays.asList(getResources().getStringArray(R.array.select_mobile_prepaid_operators));
        operatorPrepaidAdapter = new ArrayAdapter<String>(RechargeActivity.this, R.layout.spinner_text_view, operatorPrepaidList){

            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return false;
                } else {
                    return true;
                }
            }
        };
        operatorPrepaidSpinner.setAdapter(operatorPrepaidAdapter);
        operatorPrepaidAdapter.setDropDownViewResource(R.layout.spinner_text_view);
    }

    private void updatePostpaid() {
        operatorPostPaidList= Arrays.asList(getResources().getStringArray(R.array.select_mobile_postpaid_operators));
        operatorPostPaidAdapter = new ArrayAdapter<String>(RechargeActivity.this, R.layout.spinner_text_view, operatorPostPaidList){

            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return false;
                } else {
                    return true;
                }
            }
        };
        operatorPostPaidSpinner.setAdapter(operatorPostPaidAdapter);
        operatorPostPaidAdapter.setDropDownViewResource(R.layout.spinner_text_view);
    }

    private void updateDTH() {
        operatorDTHList= Arrays.asList(getResources().getStringArray(R.array.select_dth_operators));
        operatorDTHAdapter = new ArrayAdapter<String>(RechargeActivity.this, R.layout.spinner_text_view, operatorDTHList){

            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return false;
                } else {
                    return true;
                }
            }
        };
        operatorDTHSpinner.setAdapter(operatorDTHAdapter);
        operatorDTHAdapter.setDropDownViewResource(R.layout.spinner_text_view);
    }


    public void ShowNoInternetDialog() {
        showAlertDialog(RechargeActivity.this, getResources().getString(R.string.text_no_internet_connection),
                RechargeActivity.this.getResources().getString(R.string.text_please_check_your_network), false);
    }

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

    private void setSelectionMobile(){
        rechargeMobile.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        rechargeDTH.setBackgroundColor(getResources().getColor(R.color.colorButtonDark));
        mobileLayout.setVisibility(View.VISIBLE);
        dthLayout.setVisibility(View.GONE);
    }

    private void setSelectionDTH(){
        rechargeDTH.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        rechargeMobile.setBackgroundColor(getResources().getColor(R.color.colorButtonDark));
        dthLayout.setVisibility(View.VISIBLE);
        mobileLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
