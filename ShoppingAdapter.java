package com.tms.govt.champcash.home.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tms.govt.champcash.R;

/**
 * Created by govt on 20-04-2017.
 */

public class ShoppingAdapter extends ArrayAdapter<String> {

    String shoppingURL;

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public ShoppingAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.list_row, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_row, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.shopping_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.shopping_logo);

        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);
        return rowView;

    };
}