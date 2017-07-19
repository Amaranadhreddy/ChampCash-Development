package com.tms.govt.champcash.home.slidemenu;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.adapter.RechargeAdapter;
import com.tms.govt.champcash.home.localdatabase.RechargeDB;
import com.tms.govt.champcash.home.network.ConnectionDetector;
import com.tms.govt.champcash.home.report.ChampcashRecharge;

import java.util.List;

/**
 * Created by govt on 05-05-2017.
 */

public class TransactionsActivity extends Activity {

    RechargeDB RECHARGEDB;
    private List<ChampcashRecharge> champcashRechargeList;
    private RecyclerView recyclerView;
    RechargeAdapter rechargeAdapter;
    private TextView activityTitle;
    private ImageView activityBack;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_prepaid_rcg_history);
        activityTitle = (TextView) findViewById(R.id.header_activity_title);
        activityBack = (ImageView) findViewById(R.id.header_back);
        activityTitle.setText("Recharge History");
//        activityTitle.setTextSize(16);

        activityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cd = new ConnectionDetector(TransactionsActivity.this);
        RECHARGEDB = new RechargeDB(TransactionsActivity.this);

        champcashRechargeList = RECHARGEDB.getAllRechargeDetails();
        rechargeAdapter = new RechargeAdapter(TransactionsActivity.this, champcashRechargeList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(TransactionsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rechargeAdapter);
        rechargeAdapter.notifyDataSetChanged();

    }
}
