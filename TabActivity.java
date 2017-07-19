package com.tms.govt.champcash.home.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.HomeActivity;
import com.tms.govt.champcash.home.adapter.DrawerMenuAdapter;
import com.tms.govt.champcash.home.network.ConnectionDetector;
import com.tms.govt.champcash.home.report.SideMenu;

import java.util.ArrayList;

/**
 * Created by govt on 19-04-2017.
 */

public class TabActivity extends Activity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ImageView sideMenuBar;
    private DrawerLayout mDrawerLayout;
    private DrawerMenuAdapter mAdapter;
    private ListView mDrawerList;
    LinearLayout sideLayout;
    ArrayList<SideMenu> menuItems;

    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    Context context;

    AlertDialog.Builder builder;

    private TextView textEarnCash, textChallengeCash, textShoppingCash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        sideMenuBar = (ImageView) findViewById(R.id.home_page_side_menu_icon);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sideLayout = (LinearLayout) findViewById(R.id.side_menu_layout);
        mDrawerList = (ListView) findViewById(R.id.side_menu_list);

        cd = new ConnectionDetector(TabActivity.this);

        menuItems = new ArrayList<SideMenu>();
        menuItems.add(new SideMenu("My Profile", R.mipmap.ic_my_profile));
        menuItems.add(new SideMenu("Earn More", R.mipmap.ic_booking_history));
        menuItems.add(new SideMenu("My Transactions", R.mipmap.ic_my_account));
        menuItems.add(new SideMenu("Redeem", R.mipmap.ic_settings));
        menuItems.add(new SideMenu("Share", R.mipmap.ic_share));
        menuItems.add(new SideMenu("Rate Us", R.mipmap.ic_rate_this_app));
        menuItems.add(new SideMenu("Help & Faq", R.mipmap.ic_help_faq));
        menuItems.add(new SideMenu("My Network", R.mipmap.ic_near_by));
        menuItems.add(new SideMenu("Sign Out", R.mipmap.ic_signout));
        mAdapter = new DrawerMenuAdapter(TabActivity.this, menuItems);

        textEarnCash = (TextView) findViewById(R.id.select_earn_champcash);
        textChallengeCash = (TextView) findViewById(R.id.select_challenge_champcash);
        textShoppingCash = (TextView) findViewById(R.id.select_shopping_champcash);

        textEarnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TabActivity.this, "Earn Cash", Toast.LENGTH_SHORT).show();
            }
        });

        textChallengeCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TabActivity.this, "Challenge Cash", Toast.LENGTH_SHORT).show();
            }
        });

        textShoppingCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectionShopping();
            }
        });


        mDrawerList.setAdapter(mAdapter);

        sideMenuBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(sideLayout);
            }
        });

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
    }

    private void setSelectionShopping() {
        textEarnCash.setBackgroundColor(getResources().getColor(R.color.colorButtonDark));
        textChallengeCash.setBackgroundColor(getResources().getColor(R.color.colorButtonDark));
        textShoppingCash.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        isInternetPresent = cd.isConnectionAvailable();
        if (isInternetPresent){
//            Intent shoppingIntent = new Intent(TabActivity.this, ShoppingActivity.class);
//            startActivity(shoppingIntent);

//            Intent shoppingIntent = new Intent(TabActivity.this, ShoppingActivity.class);
////            replaceContentView("activity_shopping", shoppingIntent);
//            shoppingIntent.putExtra(String.valueOf(R.layout.activity_shopping), 4);
        }

    }

/*
    public void replaceContentView(String id, Intent newIntent) {
        View view = ((ActivityGroup) context)
                .getLocalActivityManager()
                .startActivity(id,
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                .getDecorView();
        ((Activity) context).setContentView(view);

    }
*/

    private class SlideMenuClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // set side menu position
            displayView(position);
        }
    }

    private void displayView(int position) {

        switch (position){

            case 0:
                Toast.makeText(TabActivity.this, "My Profile", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(TabActivity.this, "Earn More", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(TabActivity.this, "My Transactions", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(TabActivity.this, "Redeem", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(TabActivity.this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(TabActivity.this, "Rate Us", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                Toast.makeText(TabActivity.this, "Help & FAQs", Toast.LENGTH_SHORT).show();
                break;
            case 7:
                Toast.makeText(TabActivity.this, "My Network", Toast.LENGTH_SHORT).show();
                break;
            case 8:
                builder = new AlertDialog.Builder(TabActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                builder.setTitle("Logout");
                builder.setMessage("Do you want to exit?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavigateToHomePageActivity();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });


                builder.show();

                break;

            default:
                break;
        }
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);

        // Close Side menu
        mDrawerLayout.closeDrawer(sideLayout);
    }

    private void NavigateToHomePageActivity() {

        Intent homeIntent = new Intent(TabActivity.this, HomeActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
