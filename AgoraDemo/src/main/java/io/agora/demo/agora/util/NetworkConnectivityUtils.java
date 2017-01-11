/**
 * Created by harry on Nov 24, 2011 Copyright : FOCUSONE Inc. All Rights
 * Reserved.
 */

package io.agora.demo.agora.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * This utils helps to do the following: 1) If device is connected to mobile
 * network 2) If device is connected to wifi 3) If device is connected, either
 * to mobile network or wifi.
 * 
 * @author harry
 */
public class NetworkConnectivityUtils {

    /**
     * Log
     */
    private static final String TAG = NetworkConnectivityUtils.class.getSimpleName ();

    public static boolean isConnectedToMobile (Context context) {

        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        // mobile
        State mobile = conMan.getNetworkInfo (0).getState ();
        LoggingUtils.debug (TAG,
                            "checking if device is connected to  mobile network");
        return mobile == State.CONNECTED;

    }

    public static boolean isConnectedToWifi (Context context) {

        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        // wifi
        State wifi = conMan.getNetworkInfo (1).getState ();
        LoggingUtils.debug (TAG, "checking if device is connected to wifi");

        return wifi == State.CONNECTED;
    }

    /**
     * This is a simple way to check if you are CONNECTED or is CONNECTING to
     * network. NOTE: you need to set <uses-permission
     * android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
     * in your AndroidManifest.xml
     * 
     * @param context a context used to getSystemInfo
     * @return
     */
    public static boolean isConnectedToNetwork (Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo ();
        boolean isConnected = netInfo != null && netInfo.isConnected ();
        LoggingUtils.debug (TAG, "device is connected to network :  "
                + isConnected);
        return isConnected;
    }

    private NetworkConnectivityUtils() {

    }
}
