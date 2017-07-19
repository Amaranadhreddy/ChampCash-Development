package com.tms.govt.champcash.home.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.HomeActivity;
import com.tms.govt.champcash.home.adapter.DrawerMenuAdapter;
import com.tms.govt.champcash.home.adapter.ShoppingAdapter;
import com.tms.govt.champcash.home.challenge.HighscoreActivity;
import com.tms.govt.champcash.home.challenge.PlayActivity;
import com.tms.govt.champcash.home.challenge.SettingActivity;
import com.tms.govt.champcash.home.dashboard.ColorTemplate1;
import com.tms.govt.champcash.home.localdatabase.ChampCashDB;
import com.tms.govt.champcash.home.network.ConnectionDetector;
import com.tms.govt.champcash.home.report.Champcash;
import com.tms.govt.champcash.home.report.SideMenu;
import com.tms.govt.champcash.home.shopping.WebLinkActivity;
import com.tms.govt.champcash.home.slidemenu.FingerCaptureActivity;
import com.tms.govt.champcash.home.slidemenu.MapsActivity;
import com.tms.govt.champcash.home.slidemenu.MyProfileActivity;
import com.tms.govt.champcash.home.slidemenu.RechargeActivity;
import com.tms.govt.champcash.home.slidemenu.ScannerActivity;
import com.tms.govt.champcash.home.slidemenu.TransactionsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by govt on 20-04-2017.
 */

public class TabActivity1 extends AppCompatActivity {

    ChampCashDB CHMPCASHDB;

    RelativeLayout piechartLayout;
    RelativeLayout gameLayout;

    PieChart pieChart ;
    ArrayList<Entry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;

    ListView listView;
    ShoppingAdapter shoppingAdapter;

    private String keyID;
    private String img;

    private ImageView profileImage;

    String[] itemname ={
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

    Integer[] imgid={
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_tab1);

        sideMenuBar = (ImageView) findViewById(R.id.home_page_side_menu_icon);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sideLayout = (LinearLayout) findViewById(R.id.side_menu_layout);
        mDrawerList = (ListView) findViewById(R.id.side_menu_list);
        profileImage = (ImageView) findViewById(R.id.profile_Pic);

        gameLayout = (RelativeLayout) findViewById(R.id.game_layout);
        piechartLayout = (RelativeLayout) findViewById(R.id.layout_piechart);

        CHMPCASHDB = new ChampCashDB(TabActivity1.this);

        //Piechart
            pieChart = (PieChart) findViewById(R.id.chart1);
            entries = new ArrayList<>();
            PieEntryLabels = new ArrayList<String>();
            AddValuesToPIEENTRY();
            AddValuesToPieEntryLabels();
            pieDataSet = new PieDataSet(entries, "");
            pieData = new PieData(PieEntryLabels, pieDataSet);
            pieDataSet.setColors(ColorTemplate1.COLORFUL_COLORS);
            pieChart.setData(pieData);
            pieChart.animateY(5000);

            piechartLayout.setVisibility(View.VISIBLE);


            cd = new ConnectionDetector(TabActivity1.this);

//        shoppingArrayList = new ArrayList<>();

            menuItems = new ArrayList<SideMenu>();
            menuItems.add(new SideMenu("My Profile", R.mipmap.ic_my_profile));
            menuItems.add(new SideMenu("Recharge", R.mipmap.ic_booking_history));
            menuItems.add(new SideMenu("My Transactions", R.mipmap.ic_my_account));
            menuItems.add(new SideMenu("Scanner", R.mipmap.ic_scanning_barcode));
            menuItems.add(new SideMenu("Finger Capture", R.mipmap.ic_finger_print));
            menuItems.add(new SideMenu("Nearby", R.mipmap.ic_near_by));
            menuItems.add(new SideMenu("Share", R.mipmap.ic_share));
            menuItems.add(new SideMenu("Rate Us", R.mipmap.ic_rate_this_app));
            menuItems.add(new SideMenu("About Us", R.mipmap.ic_help_faq));
            menuItems.add(new SideMenu("Sign Out", R.mipmap.ic_signout));
            mAdapter = new DrawerMenuAdapter(TabActivity1.this, menuItems);

            listView = (ListView) findViewById(R.id.list);

            textEarnCash = (TextView) findViewById(R.id.select_earn_champcash);
            textChallengeCash = (TextView) findViewById(R.id.select_challenge_champcash);
            textShoppingCash = (TextView) findViewById(R.id.select_shopping_champcash);

        List<Champcash> champcashList = CHMPCASHDB.getAllDetails();
        for(int i=0;i<champcashList.size();i++) {
            keyID=champcashList.get(i).getIndexID();

        }

            textEarnCash.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    String Slecteditem = itemname[+position];
//                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

                    switch (position) {
                        case 0:
//                        Intent webPageIntent = new Intent(Intent.ACTION_VIEW);
//                        webPageIntent.setData(Uri.parse("http://www.amazon.in/"));
//                        startActivity(webPageIntent);
                            Intent amazonIntent = new Intent(TabActivity1.this, WebLinkActivity.class);
                            amazonIntent.putExtra("key", 0);
                            startActivity(amazonIntent);
                            break;
                        case 1:
                            Intent flipcartIntent = new Intent(TabActivity1.this, WebLinkActivity.class);
                            flipcartIntent.putExtra("key", 1);
                            startActivity(flipcartIntent);
                            break;
                        case 2:
                            Intent snapdealIntent = new Intent(TabActivity1.this, WebLinkActivity.class);
                            snapdealIntent.putExtra("key", 2);
                            startActivity(snapdealIntent);
                            break;
                        case 3:
                            Intent ebayIntent = new Intent(TabActivity1.this, WebLinkActivity.class);
                            ebayIntent.putExtra("key", 3);
                            startActivity(ebayIntent);
                            break;
                        case 4:
                            Intent paytmIntent = new Intent(TabActivity1.this, WebLinkActivity.class);
                            paytmIntent.putExtra("key", 4);
                            startActivity(paytmIntent);
                            break;
                        case 5:
                            Intent myntraIntent = new Intent(TabActivity1.this, WebLinkActivity.class);
                            myntraIntent.putExtra("key", 5);
                            startActivity(myntraIntent);
                            break;
                        case 6:
                            Intent jabongIntent = new Intent(TabActivity1.this, WebLinkActivity.class);
                            jabongIntent.putExtra("key", 6);
                            startActivity(jabongIntent);
                            break;
                        case 7:
                            Intent shopcluesIntent = new Intent(TabActivity1.this, WebLinkActivity.class);
                            shopcluesIntent.putExtra("key", 7);
                            startActivity(shopcluesIntent);
                            break;
                        case 8:
                            Intent pepperfryIntent = new Intent(TabActivity1.this, WebLinkActivity.class);
                            pepperfryIntent.putExtra("key", 8);
                            startActivity(pepperfryIntent);
                            break;
                        case 9:
                            Intent homeshop18Intent = new Intent(TabActivity1.this, WebLinkActivity.class);
                            homeshop18Intent.putExtra("key", 9);
                            startActivity(homeshop18Intent);
                            break;
                        case 10:
                            Intent kraftlyIntent = new Intent(TabActivity1.this, WebLinkActivity.class);
                            kraftlyIntent.putExtra("key", 10);
                            startActivity(kraftlyIntent);
                            break;
                        case 11:
                            Intent shoppersstopIntent = new Intent(TabActivity1.this, WebLinkActivity.class);
                            shoppersstopIntent.putExtra("key", 11);
                            startActivity(shoppersstopIntent);
                            break;
                        case 12:
                            Intent bigbasketIntent = new Intent(TabActivity1.this, WebLinkActivity.class);
                            bigbasketIntent.putExtra("key", 12);
                            startActivity(bigbasketIntent);
                            break;
                        default:
                            break;
                    }
                }
            });


            textEarnCash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isInternetPresent = cd.isConnectionAvailable();
                    if (isInternetPresent) {
                        setSelectionEarning();
                    } else {
                        ShowNoInternetDialog();
                    }
                }
            });

            textChallengeCash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isInternetPresent = cd.isConnectionAvailable();
                    if (isInternetPresent) {
                        setSelectionChallenging();
                    } else {
                        ShowNoInternetDialog();
                    }
                }
            });

            textShoppingCash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isInternetPresent = cd.isConnectionAvailable();
                    if (isInternetPresent) {

                        setSelectionShopping();

                    } else {
                        ShowNoInternetDialog();
                    }
                }
            });


            mDrawerList.setAdapter(mAdapter);

            sideMenuBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(sideLayout);
                }
            });

            mDrawerList.setOnItemClickListener(new TabActivity1.SlideMenuClickListener());

    }


    @Override
    protected void onResume() {

        List<Champcash> champcashList = CHMPCASHDB.getAllDetails();
        if (champcashList.size() != 0) {
            for (int i = 0; i < champcashList.size(); i++) {
                if (champcashList.get(i).getImage() != null) {
                    Bitmap bm = StringToBitMap(champcashList.get(i).getImage());
                    profileImage.setImageBitmap(bm);
                }
            }
        }
        super.onResume();
    }

    public void ShowNoInternetDialog() {
        showAlertDialog(TabActivity1.this, getResources().getString(R.string.text_no_internet_connection),
                TabActivity1.this.getResources().getString(R.string.text_please_check_your_network), false);
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


    private void setSelectionShopping() {
        textEarnCash.setBackgroundColor(getResources().getColor(R.color.colorButtonDark));
        textChallengeCash.setBackgroundColor(getResources().getColor(R.color.colorButtonDark));
        textShoppingCash.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        gameLayout.setVisibility(View.GONE);
        piechartLayout.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        shoppingAdapter=new ShoppingAdapter(TabActivity1.this, itemname, imgid);
        listView.setAdapter(shoppingAdapter);

    }

    private void setSelectionChallenging(){
        textEarnCash.setBackgroundColor(getResources().getColor(R.color.colorButtonDark));
        textChallengeCash.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        textShoppingCash.setBackgroundColor(getResources().getColor(R.color.colorButtonDark));
        listView.setVisibility(View.GONE);
        piechartLayout.setVisibility(View.GONE);
        gameLayout.setVisibility(View.VISIBLE);

    }

    private void setSelectionEarning(){
        textEarnCash.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        textChallengeCash.setBackgroundColor(getResources().getColor(R.color.colorButtonDark));
        textShoppingCash.setBackgroundColor(getResources().getColor(R.color.colorButtonDark));
        listView.setVisibility(View.GONE);
        gameLayout.setVisibility(View.GONE);
        piechartLayout.setVisibility(View.VISIBLE);
    }


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
                Intent profileIntent = new Intent(TabActivity1.this, MyProfileActivity.class);
                startActivity(profileIntent);
                break;
            case 1:
                Intent rechargeIntent = new Intent(TabActivity1.this, RechargeActivity.class);
                startActivity(rechargeIntent);
//                Toast.makeText(TabActivity1.this, "Under Process!", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Intent transactionIntent = new Intent(TabActivity1.this, TransactionsActivity.class);
                startActivity(transactionIntent);
//                Toast.makeText(TabActivity1.this, "Under Process!", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Intent scannerIntent = new Intent(TabActivity1.this, ScannerActivity.class);
                startActivity(scannerIntent);
                break;
            case 4:
                Intent fingerPrintIntent = new Intent(TabActivity1.this, FingerCaptureActivity.class);
                startActivity(fingerPrintIntent);
                break;
            case 5:
                Intent mapsIntent = new Intent(TabActivity1.this, MapsActivity.class);
                startActivity(mapsIntent);
//                Toast.makeText(TabActivity1.this, "Under Process!", Toast.LENGTH_SHORT).show();
                break;
            case 6:
//                Toast.makeText(TabActivity1.this, "Under Process!", Toast.LENGTH_SHORT).show();

                String shareBody = "https://play.google.com/store/apps/details?id=************************";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AP TMS");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case 7:
                Intent rateIntent = new Intent(TabActivity1.this, WebLinkActivity.class);
                rateIntent.putExtra("key", 14);
                startActivity(rateIntent);
//                Toast.makeText(TabActivity1.this, "Under Process!", Toast.LENGTH_SHORT).show();
                break;
            case 8:
                Intent aboutIntent = new Intent(TabActivity1.this, WebLinkActivity.class);
                aboutIntent.putExtra("key", 13);
                startActivity(aboutIntent);
//                Toast.makeText(TabActivity1.this, "Help & FAQs", Toast.LENGTH_SHORT).show();
                break;

            case 9:
                builder = new AlertDialog.Builder(TabActivity1.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                builder.setTitle("Logout");
                builder.setMessage("Do you want to exit?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavigateToHomePageActivity();
                        //                                if (authMode.equalsIgnoreCase("G")) {
//                                    if (mGoogleApiClient.isConnected()) {
//                                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//                                        mGoogleApiClient.disconnect();
//                                        mGoogleApiClient.connect();
//                                    }
//                                } else if (authMode.equalsIgnoreCase("F")) {
//                                    LoginManager.getInstance().logOut();
//                                }else{
//                        new SignOutTask().execute();
//                    }


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

        Intent homeIntent = new Intent(TabActivity1.this, HomeActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        CHMPCASHDB.signOutChampcash(new Champcash(""));
        startActivity(homeIntent);
    }

    //Game
    public void play(View v)
    {
        Intent i=new Intent(this,PlayActivity.class);
        startActivity(i);
    }

    public void highscore(View v)
    {
        Intent i=new Intent(this,HighscoreActivity.class);
        startActivity(i);
    }

    public void setting(View v)
    {
        Intent i=new Intent(this,SettingActivity.class);
        startActivity(i);
    }

    public void exit(View v)
    {
        System.exit(0);
    }

    //piechart
    public void AddValuesToPIEENTRY(){

        entries.add(new BarEntry(2f, 0));
        entries.add(new BarEntry(5f, 1));
        entries.add(new BarEntry(5f, 2));
        entries.add(new BarEntry(8f, 3));
        entries.add(new BarEntry(1f, 4));
        entries.add(new BarEntry(3f, 5));
        entries.add(new BarEntry(5f, 6));
        entries.add(new BarEntry(6f, 7));
        entries.add(new BarEntry(2f, 8));
        entries.add(new BarEntry(5f, 9));
        entries.add(new BarEntry(4f, 10));
        entries.add(new BarEntry(3f, 11));

    }

    public void AddValuesToPieEntryLabels(){

        PieEntryLabels.add("Ja");
        PieEntryLabels.add("Fe");
        PieEntryLabels.add("Mr");
        PieEntryLabels.add("Ap");
        PieEntryLabels.add("Ma");
        PieEntryLabels.add("Jn");
        PieEntryLabels.add("Jl");
        PieEntryLabels.add("Au");
        PieEntryLabels.add("Se");
        PieEntryLabels.add("Oc");
        PieEntryLabels.add("No");
        PieEntryLabels.add("De");

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