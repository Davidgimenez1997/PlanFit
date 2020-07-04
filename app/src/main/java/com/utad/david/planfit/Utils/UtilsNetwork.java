package com.utad.david.planfit.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UtilsNetwork {

    private static int TYPE_WIFI = 1;
    private static int TYPE_MOBILE = 2;
    private static int TYPE_NOT_CONNECTED = 0;
    private static int NETWORK_STATUS_NOT_CONNECTED = 0;
    private static int NETWORK_STATUS_WIFI = 1;
    private static int NETWORK_STATUS_MOBILE = 2;

    public static boolean checkConnectionInternetDevice(Context context) {
        boolean isConnected = false;
        if (getConnectivityStatus(context) == NETWORK_STATUS_MOBILE || getConnectivityStatus(context) == NETWORK_STATUS_WIFI) {
            isConnected = true;
        } else if (getConnectivityStatusString(context) == NETWORK_STATUS_NOT_CONNECTED) {
            isConnected = false;
        }
        return isConnected;
    }

    private static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    private static int getConnectivityStatusString(Context context) {
        int conn = UtilsNetwork.getConnectivityStatus(context);
        int status = 0;
        if (conn == UtilsNetwork.TYPE_WIFI) {
            status = NETWORK_STATUS_WIFI;
        } else if (conn == UtilsNetwork.TYPE_MOBILE) {
            status = NETWORK_STATUS_MOBILE;
        } else if (conn == UtilsNetwork.TYPE_NOT_CONNECTED) {
            status = NETWORK_STATUS_NOT_CONNECTED;
        }
        return status;
    }
}
