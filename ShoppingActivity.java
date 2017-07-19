package com.tms.govt.champcash.home.shopping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.adapter.ShoppingAdapter;
import com.tms.govt.champcash.home.network.ConnectionDetector;

/**
 * Created by govt on 20-04-2017.
 */

public class ShoppingActivity extends Activity {

    private ListView list;
    private ShoppingAdapter shoppingAdapter;

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    
    private String[] itemname ={
            "Amazon",
            "Flipcart",
            "Snapdeal",
            "eBay",
            "Paytm",
            "Myntra",
            "Jabong",
            "Shopclues",
            "Pepperfry",
            "Homeshop18",
            "Kraftly",
            "Shoppers Stop",
            "Big Basket"
    };

    private Integer[] imgid={
            R.drawable.ic_amazon,
            R.drawable.ic_flipcart,
            R.drawable.ic_snapdeal,
            R.drawable.ic_ebay,
            R.drawable.ic_paytm,
            R.drawable.ic_myntra,
            R.drawable.ic_jabong,
            R.drawable.ic_shopclues,
            R.drawable.ic_pepperfry,
            R.drawable.ic_homeshop18,
            R.drawable.ic_kraftly,
            R.drawable.ic_shoppers_top,
            R.drawable.ic_big_basket
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        
        cd = new ConnectionDetector(ShoppingActivity.this);

        Intent i = getIntent();
        int tabToOpen = i.getIntExtra(String.valueOf(R.layout.activity_shopping), -1);
        if (tabToOpen!=-1) {
            // Open the right tab
        }

      //  shoppingAdapter = new ShoppingAdapter(ShoppingActivity.this, itemname, imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(shoppingAdapter);

        isInternetPresent = cd.isConnectionAvailable();
        if (isInternetPresent) {
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    String Slecteditem = itemname[+position];
                    Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            ShowNoInternetDialog();
        }
    }

    private void ShowNoInternetDialog() {

        showAlertDialog(ShoppingActivity.this, getResources().getString(R.string.text_no_internet_connection),
                getResources().getString(R.string.text_please_check_your_network), false);
    }

    private void showAlertDialog(ShoppingActivity homePageActivity, String title, String message, Boolean status) {

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
}