package com.tms.govt.champcash.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.report.SideMenu;

import java.util.ArrayList;

/**
 * Created by govt on 19-04-2017.
 */

public class DrawerMenuAdapter extends BaseAdapter {

    // variable initialization
    private Context context;
    private ArrayList<SideMenu> menuItems;

    public DrawerMenuAdapter(Context applicationContext, ArrayList<SideMenu> menuItems){
        this.context = applicationContext;
        this.menuItems = menuItems;
    }
    @Override
    public int getCount() {
        // return menu list size
        return menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        // return menu list position
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        // item position
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.sidemenu_list_row, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.menu_icon);
        // Side menu list item name
        TextView txtTitle = (TextView) convertView.findViewById(R.id.menu_title);

        imgIcon.setImageResource(menuItems.get(position).getIcon());
        txtTitle.setText(menuItems.get(position).getName());
        return convertView;
    }
}
