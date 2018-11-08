package com.shoneworn.libcore.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by chenxiangxiang on 2018/11/2.
 */

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

    public Utils() {
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetConnected(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if(infos != null) {
                NetworkInfo[] var3 = infos;
                int var4 = infos.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    NetworkInfo ni = var3[var5];
                    if(ni.isConnected()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isWifiConnected(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.getType() == 1) {
                return true;
            }
        }

        return false;
    }
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean is3gConnected(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.getType() == 0) {
                return true;
            }
        }

        return false;
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isConnectingToInternet(Context ctx) {
        ConnectivityManager connectivity = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if(info != null) {
                for(int i = 0; i < info.length; ++i) {
                    if(info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean isGpsEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled("gps");
    }

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    private static boolean isAppShowing(String packageName, Context context) {
        boolean result = false;
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();
        if(appProcesses != null) {
            Iterator var5 = appProcesses.iterator();

            while(true) {
                int status;
                do {
                    ActivityManager.RunningAppProcessInfo runningAppProcessInfo;
                    do {
                        if(!var5.hasNext()) {
                            return result;
                        }

                        runningAppProcessInfo = (ActivityManager.RunningAppProcessInfo)var5.next();
                    } while(!runningAppProcessInfo.processName.equals(packageName));

                    status = runningAppProcessInfo.importance;
                } while(status != 200 && status != 100);

                result = true;
            }
        } else {
            return result;
        }
    }

    public static boolean isForeground(Context ctx) {
        return isAppShowing(ctx.getPackageName(), ctx);
    }

    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isEmpty(Object obj) {
        return null == obj;
    }

    public static boolean isEmpty(Map map) {
        return null == map || map.isEmpty();
    }

    public static boolean isEmpty(Object[] objs) {
        return null == objs || objs.length <= 0;
    }

    public static boolean isEmpty(int[] objs) {
        return null == objs || objs.length <= 0;
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return null == charSequence || charSequence.length() <= 0;
    }

    public static boolean isNotEmpty(Collection collection) {
        return null != collection && !collection.isEmpty();
    }

    public static boolean isNotEmpty(Object obj) {
        return null != obj;
    }

    public static boolean isNotEmpty(Map map) {
        return null != map && !map.isEmpty();
    }

    public static boolean isNotEmpty(Object[] objs) {
        return null != objs && objs.length > 0;
    }

    public static boolean isNotEmpty(int[] objs) {
        return null != objs && objs.length > 0;
    }

    public static boolean isNotEmpty(CharSequence charSequence) {
        return null != charSequence && charSequence.length() > 0;
    }

    public static boolean hasPermission(Context ctx, String permission) {
        if(!isEmpty((Object)ctx) && !StringUtils.isEmpty(permission)) {
            int targetSdkVersion = ctx.getApplicationInfo().targetSdkVersion;
            return targetSdkVersion >= 23? ContextCompat.checkSelfPermission(ctx, permission) == 0: PermissionChecker.checkSelfPermission(ctx, permission) == 0;
        } else {
            return false;
        }
    }
}
