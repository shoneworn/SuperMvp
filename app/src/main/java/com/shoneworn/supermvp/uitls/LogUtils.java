package com.shoneworn.supermvp.uitls;

import android.util.Log;

/**
 * Created by chenxiangxiang on 2018/11/16.
 */

public class LogUtils {
    private static boolean debug = false;

    public static void e(String TAG, String msg) {
        if (debug)
            Log.e(TAG, msg);
    }

    public static void d(String TAG, String msg) {
        if (debug)
            Log.d(TAG, msg);
    }

    public static void i(String TAG, String msg) {
        if (debug)
            Log.i(TAG, msg);
    }

    public static void w(String TAG, String msg) {
        if (debug)
            Log.w(TAG, msg);
    }

    public static void e(String msg) {
        if (debug)
            System.out.println(msg);
    }


    public static void d(String msg) {
        if (debug)
            System.out.println(msg);
    }

    public static void w(String msg) {
        if (debug)
            System.out.println(msg);
    }

    public static void i(String msg) {
        if (debug)
            System.out.println(msg);
    }

}
