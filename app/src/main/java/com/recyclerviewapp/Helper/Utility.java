package com.recyclerviewapp.Helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by BookMEds on 01-02-2018.
 */

public class Utility {

    private static final Utility instance = new Utility();

    //private constructor to avoid client applications to use constructor
    private Utility() {
    }

    public static Utility getInstance() {
        return instance;
    }

    public boolean IsInternetConnected(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        isConnected = true;
                    }
                }
            }
        }
        return isConnected;
    }

}
