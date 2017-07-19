package com.tms.govt.champcash.home.login;


import android.support.v4.app.Fragment;

/**
 * Created by govt on 20-04-2017.
 */

public class ShoppingFragment extends Fragment {

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping, container, false);

    }*/


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_shopping);
//    }


/*

    ShoppingAdapter shoppingAdapter;
    private View view;
    private String[] itemname ={
            "Amazon",
            "Flipcart",
            "Snapdeal",
            "eBay",
    };

    private Integer[] imgid={
            R.drawable.ic_amazon,
            R.drawable.ic_flipcart,
            R.drawable.ic_snapdeal,
            R.drawable.ic_ebay,
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Each row in the list stores country name, currency and flag
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 10; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("txt", "Country : " +  itemname[i]);
            hm.put("flag", Integer.toString(imgid[i]));
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {"flag", "txt"};

        // Ids of views in listview_layout
        int[] to = {R.id.flag, R.id.txt};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.fragment_shopping, from, to);

        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
*/
}
