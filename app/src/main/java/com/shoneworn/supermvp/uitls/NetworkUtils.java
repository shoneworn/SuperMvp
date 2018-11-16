package com.shoneworn.supermvp.uitls;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NetworkUtils {

    public final static int NONE = 0;// 无网络
    public final static int WIFI = 1;// Wi-Fi
    public final static int MOBILE = 2;// 3G,GPRS

    public static final String NETWORK_CLASS_UNKNOWN = null;
    public static final String NETWORK_WIFI = "wifi";
    public static final String NETWORK_CLASS_2_G = "2g";
    public static final String NETWORK_CLASS_3_G = "3g";
    public static final String NETWORK_CLASS_4_G = "4g";

    /**
     * 获取当前网络状态(wifi,3G)
     *
     * @param context
     * @return
     */
    public static int getNetworkState(Context context) {
        if (context == null) {
            return NONE;
        }
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager == null ||
                connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) == null ||
                connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) == null) {
            return NONE;
        }
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return MOBILE;
        }
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return WIFI;
        }
        return NONE;

    }


    /**
     * 检测是否有网络
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo network : info) {
                    if (network.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 三种状态，分别为网络错误，数据为空，没有网络
     *
     * @author yjp
     */
    public enum NetworkFialEnum {
        NETWORK_ERROR, DATA_NULL, NO_NETWORK
    }

    public interface NetworkResult<T> {
        /**
         * 开始访问
         */
        public void onStart(String url);

        /**
         * 结束访问
         */
        public void onStop(String url);

        /**
         * 数据获取成功
         */
        /**
         * @param data 返回的字符串
         * @param url
         */

        public void onSuccess(T data, String url);

        /**
         * @param nenum   网络错误，或者数据为空
         * @param message 网络错误返回的信息
         */
        public void onFail(NetworkFialEnum nenum, String message, String url);
    }

    public static boolean isWifiAvailable(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }


    public static String getNetworkTypeName(Context context) {
        if (context != null) {
            ConnectivityManager connectMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectMgr != null) {
                NetworkInfo info = connectMgr.getActiveNetworkInfo();
                if (info != null) {
                    switch (info.getType()) {
                        case ConnectivityManager.TYPE_WIFI:
                            return "WIFI";
                        case ConnectivityManager.TYPE_MOBILE:
                            return getNetworkTypeName(info.getSubtype());
                    }
                }
            }
        }
        return getNetworkTypeName(TelephonyManager.NETWORK_TYPE_UNKNOWN);
    }

    public static String getNetworkTypeName(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "CDMA - EvDo rev. 0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "CDMA - EvDo rev. A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "CDMA - EvDo rev. B";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "CDMA - 1xRTT";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "CDMA - eHRPD";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "iDEN";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPA+";
            default:
                return "UNKNOWN";
        }
    }

    public static String getURL(String url, Map<String, ?> map) {
        if (map == null || map.size() == 0) {
            return url;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(url);
        Set set = map.keySet();
        Iterator iterator = set.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Object key = iterator.next();
            Object value = map.get(key);
            if (index == 0) {
                sb.append("?" + key + "=" + (value == null ? "" : value.toString()));
            } else {
                sb.append("&" + key + "=" + (value == null ? "" : value.toString()));
            }
            index++;
        }
        return sb.toString();
    }


    /**
     * 获取网络类型
     */
    public static String getNetWorkStatus(Context context) {
        String netWorkType = NETWORK_CLASS_UNKNOWN;

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            int type = networkInfo.getType();

            if (type == ConnectivityManager.TYPE_WIFI) {
                netWorkType = NETWORK_WIFI;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                netWorkType = getNetWorkClass(context);
            }
        }

        return netWorkType;
    }

    public static String getNetWorkClass(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;

            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;

            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;

            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }


}
