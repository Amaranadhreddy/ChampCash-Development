package com.tms.govt.champcash.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.report.ChampcashRecharge;
import com.tms.govt.champcash.home.slidemenu.TransactionsActivity;

import java.util.List;

/**
 * Created by govt on 19-05-2017.
 */

public class RechargeAdapter extends RecyclerView.Adapter<RechargeAdapter.MyViewHolder> {

    private List<ChampcashRecharge> champcashRechargeList;

    public RechargeAdapter(TransactionsActivity transactionsActivity, List<ChampcashRecharge> champcashRechargeList){
        this.champcashRechargeList = champcashRechargeList;
    }

    @Override
    public RechargeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recharge_details_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ChampcashRecharge champcashRecharge = champcashRechargeList.get(position);
        holder.mobileNumber.setText(champcashRecharge.getUserMobileNumberPrepaid());
        holder.operator.setText(champcashRecharge.getUserOperatorPrepaid());
        holder.amount.setText(champcashRecharge.getUserAmountPrepaid());
    }

    @Override
    public int getItemCount() {
        return champcashRechargeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mobileNumber, operator, amount;

        public MyViewHolder(View itemView){
            super(itemView);
            mobileNumber = (TextView) itemView.findViewById(R.id.textViewMobileNumber1);
            operator = (TextView) itemView.findViewById(R.id.textViewOperator1);
            amount = (TextView) itemView.findViewById(R.id.textViewAmount1);

        }
    }
}
