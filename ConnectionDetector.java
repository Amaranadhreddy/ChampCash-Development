package com.tms.govt.champcash.home.network;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by govt on 15-04-2017.
 */

public class ConnectionDetector {


    private Context _context;

    public ConnectionDetector(Context context) {
        this._context = context;
    }

    /* Check internet connection*/
    public boolean isConnectionAvailable() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            return false;
        }
        return false;
    }
}
