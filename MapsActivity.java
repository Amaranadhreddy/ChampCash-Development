package com.tms.govt.champcash.home.slidemenu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.network.ConnectionDetector;
import com.tms.govt.champcash.home.report.PlaceJSONParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by govt on 26-04-2017.
 */

public class MapsActivity extends FragmentActivity implements LocationListener {

    GoogleMap mGoogleMap;
    Spinner mSprPlaceType;
    private ProgressDialog progressDialog;
    String[] mPlaceType = null;
    String[] mPlaceTypeName = null;
    private int PROXIMITY_RADIUS = 5000;

    double mLatitude = 0;
    double mLongitude = 0;
    private LocationManager locationManager;
    private String type;
    HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();
    private TextView activityName;
    private RelativeLayout activityCart;
    private ImageView activityBack;
    private ImageView activityHome;
    private TextView textCartCount;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        cd = new ConnectionDetector(MapsActivity.this);

        activityName = (TextView) findViewById(R.id.header_activity_title);
        activityBack = (ImageView) findViewById(R.id.header_back);
        activityName.setText(getResources().getString(R.string.text_nearby));

        isInternetPresent = cd.isConnectionAvailable();
        if (isInternetPresent) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
                showGPSDisabledAlertToUser();
            }
        } else {
            ShowNoInternetDialog();
        }
        // Array of place types
        mPlaceType = getResources().getStringArray(R.array.place_type);
        // Array of place type names
        mPlaceTypeName = getResources().getStringArray(R.array.place_type_name);
        // Creating an array adapter with an array of Place types
        // to populate the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, mPlaceTypeName);
        // Getting reference to the Spinner
        mSprPlaceType = (Spinner) findViewById(R.id.spr_place_type);
        // Setting adapter on Spinner to set place types
        mSprPlaceType.setAdapter(adapter);

        // Getting reference to Find Button


        activityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mSprPlaceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {

                    isInternetPresent = cd.isConnectionAvailable();
                    if (isInternetPresent) {
                        int selectedPosition = position;
                        type = mPlaceType[selectedPosition];
                        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                        sb.append("location=" + mLatitude + "," + mLongitude);
                        sb.append("&radius=" + PROXIMITY_RADIUS);
                        sb.append("&types=" + type);
                        sb.append("&sensor=true");
                        sb.append("&key=" + getResources().getString(R.string.map_key));
                        // Creating a new non-ui thread task to download Google place json data
                        showProgressDialog();
                        PlacesTask placesTask = new PlacesTask();
                        // Invokes the "doInBackground()" method of the class PlaceTask
                        placesTask.execute(sb.toString());
                    }else{
                        ShowNoInternetDialog();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void ShowNoInternetDialog() {
        showAlertDialog(MapsActivity.this, getResources().getString(R.string.text_no_internet_connection),
                getResources().getString(R.string.text_please_check_your_network), false);
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Info");
        alertDialogBuilder.setIcon(R.mipmap.ic_action_info);
        alertDialogBuilder.setMessage("GPS is disabled. Please enable it")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                                MapsActivity.this.finish();
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MapsActivity.this.finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception while ", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        MarkerOptions mp = new MarkerOptions();
        mp.position(latLng);
        Geocoder gcd = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(mLatitude, mLongitude, 1);
            if (addresses.size() > 0) {
                mp.title("You are at " + addresses.get(0).getAddressLine(1));
                mp.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_current_loc));
                mGoogleMap.addMarker(mp);
            }
        } catch (IOException e) {

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * A class, to download Google Places
     */
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();
            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {
            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();
            try {
                jObject = new JSONObject(jsonData[0]);
                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);
                if (places.size() == 0) {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            showNoLocAlertDialog(MapsActivity.this, "Info",
                                    "Sorry, no " + type + " locations found", false);

                        }
                    });
                }

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {
            // Clears all the existing markers
            dismissProgressDialog();
            mGoogleMap.clear();

            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();
                    // Getting a place from the places list
                    HashMap<String, String> hmPlace = list.get(i);
                    // Getting latitude of the place
                    double lat = Double.parseDouble(hmPlace.get("lat"));
                    // Getting longitude of the place
                    double lng = Double.parseDouble(hmPlace.get("lng"));
                    // Getting name
                    String name = hmPlace.get("place_name");
                    // Getting vicinity
                    String vicinity = hmPlace.get("vicinity");
                    LatLng latLng = new LatLng(lat, lng);
                    // Setting the position for the marker
                    markerOptions.position(latLng);
                    // Setting the title for the marker.
                    //This will be displayed on taping the marker
                    markerOptions.title(name + " : " + vicinity);
                    // Placing a marker on the touched position
                    Marker m = mGoogleMap.addMarker(markerOptions);
                    // Linking Marker id and place reference
                    mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));
                }

                Location location = getLastKnownLocation();
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                LatLng latLng = new LatLng(mLatitude, mLongitude);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                MarkerOptions mp = new MarkerOptions();
                mp.position(latLng);
                Geocoder gcd = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = gcd.getFromLocation(mLatitude, mLongitude, 1);
                    if (addresses.size() > 0) {
                        mp.title("You are at " + addresses.get(0).getAddressLine(1));
                        mp.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_current_loc));
                        mGoogleMap.addMarker(mp);
                    }
                } catch (IOException e) {

                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        MapsActivity.this.finish();
    }

    @Override
    protected void onResume() {

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else { // Google Play Services are available
            // Getting reference to the SupportMapFragment
            SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            // Getting Google Map
            mGoogleMap = fragment.getMap();
            // Enabling MyLocation in Google Map
            mGoogleMap.setMyLocationEnabled(true);
            // Getting LocationManager object from System Service LOCATION_SERVICE
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();
            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

            // Getting Current Location From GPS
            Location location = getLastKnownLocation();
            if (location != null) {
                showProgressDialog();
                onLocationChanged(location);
                dismissProgressDialog();
            }
            if (location == null) {
                showProgressDialog();
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
                MapsActivity.this.finish();
                dismissProgressDialog();
            }
            locationManager.requestLocationUpdates(provider, 20000, 0, this);
            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker arg0) {
                    Intent intent = new Intent(getBaseContext(), PlaceDetailsActivity.class);
                    String reference = mMarkerPlaceLink.get(arg0.getId());
                    intent.putExtra("reference", reference);
                    // Starting the Place Details Activity
                    startActivity(intent);
                }
            });
            // Setting click event lister for the find button
        }

        super.onResume();
    }

    private void showNoLocAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon((status) ? R.mipmap.ic_action_checked : R.mipmap.ic_current_loc);
        alertDialog.setButton(getResources().getString(R.string.text_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(16);
    }

    private void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setIcon((status) ? R.mipmap.ic_action_checked : R.mipmap.ic_current_loc);
        alertDialog.setButton(getResources().getString(R.string.text_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MapsActivity.this.finish();
            }
        });
        alertDialog.show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(16);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.text_processing));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

    }
}

