package com.tms.govt.champcash.home.recharge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.slidemenu.RechargeActivity;

/**
 * Created by govt on 05-05-2017.
 */

public class SuccessPageActivity extends Activity {

    private TextView textMobileNumber_prepaid, textOperator_prepaid, textAmount_prepaid;

    private TextView activityTitle;
    private ImageView activityBack;

    private String mobile_prepaid, operator_prepaid, amount_prepaid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        textMobileNumber_prepaid = (TextView) findViewById(R.id.textViewMobileNumber);
        textOperator_prepaid = (TextView) findViewById(R.id.textViewOperator);
        textAmount_prepaid = (TextView) findViewById(R.id.textViewAmount);

        activityTitle = (TextView) findViewById(R.id.header_activity_title);
        activityBack = (ImageView) findViewById(R.id.header_back);

        activityTitle.setText("Recharge Details");

        mobile_prepaid = getIntent().getStringExtra("mobile_number_prepaid");
        textMobileNumber_prepaid.setText(mobile_prepaid);
        operator_prepaid = getIntent().getStringExtra("operator_prepaid");
        textOperator_prepaid.setText(operator_prepaid);
        amount_prepaid = getIntent().getStringExtra("amount_prepaid");
        textAmount_prepaid.setText(amount_prepaid);

        activityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onBackPressed(){
        Intent tabIntent = new Intent(SuccessPageActivity.this, RechargeActivity.class);
        tabIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        tabIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(tabIntent);
    }
}
