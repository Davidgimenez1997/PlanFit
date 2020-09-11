package com.utad.david.planfit.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UtilsNetwork {

    private static int TYPE_WIFI = 1;
    private static int TYPE_MOBILE = 2;
    private static int TYPE_NOT_CONNECTED = 0;
    private static int NETWORK_STATUS_WIFI = 1;
    private static int NETWORK_STATUS_MOBILE = 2;
    private static int NETWORK_STATUS_NOT_CONNECTED = 0;

    /**
     * Check connection internet in device
     * @param context aplication
     * @return true or false
     */
    public static boolean checkConnectionInternetDevice(Context context) {
        boolean isConnected = false;
        if (getConnectivityStatus(context) == NETWORK_STATUS_MOBILE || getConnectivityStatus(context) == NETWORK_STATUS_WIFI) {
            isConnected = true;
        } else if (getConnectivityStatusString(context) == NETWORK_STATUS_NOT_CONNECTED) {
            isConnected = false;
        }
        return isConnected;
    }

    /**
     * Get connectivity status
     * TYPE_WIFI
     * TYPE_MOBILE
     * TYPE_NOT_CONNECTED
     * @param context aplication
     * @return type connectivity
     */
    private static int getConnectivityStatus(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (null != networkInfo) {
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return TYPE_WIFI;
            }
            if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return TYPE_MOBILE;
            }
        }
        return TYPE_NOT_CONNECTED;
    }

    /**
     * Get connectivity status in string format
     * @param context aplication
     * @return type connectivity in string format
     */
    private static int getConnectivityStatusString(Context context) {
        int conexion = UtilsNetwork.getConnectivityStatus(context);
        int status = 0;
        if (conexion == UtilsNetwork.TYPE_WIFI) {
            status = NETWORK_STATUS_WIFI;
        } else if (conexion == UtilsNetwork.TYPE_MOBILE) {
            status = NETWORK_STATUS_MOBILE;
        } else if (conexion == UtilsNetwork.TYPE_NOT_CONNECTED) {
            status = NETWORK_STATUS_NOT_CONNECTED;
        }
        return status;
    }
}
