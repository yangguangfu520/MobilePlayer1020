/*-
 * Authors      : harry 
 *
 * Created Date : Jun 13, 2013
 *  
 * Beauty Bond Inc.  All rights reserved.
 *
 */

package io.agora.demo.agora.util;

import android.util.Log;

import io.agora.demo.agora.BuildConfig;


/**
 * This util offers flexible logging for development of Android application. We
 * will need log information when the application is in working process. But it
 * is not recommended to provide log information when the application is
 * released. Thus the wrapper of Log will help. The principal is to offer a
 * switch to turn on and off and any time to control if the log information is
 * visible.
 */
public class LoggingUtils {

    public static void debug (String tag, String msg) {

        if (BuildConfig.DEBUG) {
            msg = msg == null ? "" : msg;
            Log.d(tag, msg);
        }
    }

    public static void error (String tag, String msg) {

        if (BuildConfig.DEBUG) {
            msg = msg == null ? "" : msg;
            Log.e(tag, msg);
        }
    }

    public static void warning (String tag, String msg) {

        if (BuildConfig.DEBUG) {
            msg = msg == null ? "" : msg;
            Log.w(tag, msg);
        }
    }
}
