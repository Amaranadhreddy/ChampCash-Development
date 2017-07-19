package com.tms.govt.champcash.home.shopping;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.tms.govt.champcash.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by govt on 21-04-2017.
 */

public class CountryList extends ListFragment {

    // Array of strings storing country names
    String[] countries = new String[]{
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
            "Shoppers Stop"
    };

    // Array of integers points to images stored in /res/drawable/
    int[] flags = new int[]{
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
            R.drawable.ic_shoppers_top
    };

/*
    // Array of strings to store currencies
    String[] currency = new String[]{
            "Indian Rupee",
            "Pakistani Rupee",
            "Sri Lankan Rupee",
            "Renminbi",
            "Bangladeshi Taka",
            "Nepalese Rupee",
            "Afghani",
            "North Korean Won",
            "South Korean Won",
            "Japanese Yen"
    };
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Each row in the list stores country name, currency and flag
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 15; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("txt", "Country : " + countries[i]);
//            hm.put("cur","Currency : " + currency[i]);
            hm.put("flag", Integer.toString(flags[i]));
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {"flag", "txt", "cur"};

        // Ids of views in listview_layout
        int[] to = {R.id.flag, R.id.txt, R.id.cur};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout, from, to);

        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}