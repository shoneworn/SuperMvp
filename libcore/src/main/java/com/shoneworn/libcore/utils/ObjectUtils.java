package com.shoneworn.libcore.utils;

/**
 * Created by chenxiangxiang on 2018/11/2.
 */


public class ObjectUtils {
    public ObjectUtils() {
    }

    /** @deprecated */
    @Deprecated
    public static String toString(Object obj) {
        return obj == null?"":obj.toString();
    }

    /** @deprecated */
    @Deprecated
    public static String toString(Object obj, String nullStr) {
        return obj == null?nullStr:obj.toString();
    }
}