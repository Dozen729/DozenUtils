package com.dozen.commonbase.utils;

import android.util.Log;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/2
 */
public class MyLog {

    private static final String TAG = "PaiPai_Test";
    private static boolean enableLog = true;

    public MyLog() {
    }

    public static void enableLog(boolean enable) {
        enableLog = enable;
    }

    public static void v(String msg) {
        if(enableLog) {
            Log.v(TAG," Log : " + msg);
            // LogUtil.writeLog("Streamer-->" + cls.getSimpleName() + " , Log : " + msg);
        }

    }


    public static void d(String msg) {
        if(enableLog) {
            Log.d(TAG , " Log : " + msg);
            //LogUtil.writeLog("Streamer-->" + cls.getSimpleName() + " , Log : " + msg);
        }

    }


    public static void i( String msg) {
        if(enableLog) {
            Log.i(TAG," Log : " + msg);
            //LogUtil.writeLog("Streamer-->" + cls.getSimpleName() + " , Log : " + msg);
        }

    }



    public static void w(String msg) {
        if(enableLog) {
            Log.w(TAG, " Log : " + msg);
            // LogUtil.writeLog("Streamer-->" + cls.getSimpleName() + " , Log : " + msg);
        }

    }

    public static void e( String msg) {
        if(enableLog) {
            Log.e(TAG , " Log : " + msg);
            // LogUtil.writeLog("Streamer-->" + cls.getSimpleName() + " , Log : " + msg);
        }

    }
}

