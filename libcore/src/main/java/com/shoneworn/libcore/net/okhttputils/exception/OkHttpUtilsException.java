package com.shoneworn.libcore.net.okhttputils.exception;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */
public class OkHttpUtilsException extends Exception {
    private static final long serialVersionUID = -8641198158155821498L;

    public OkHttpUtilsException(String detailMessage) {
        super(detailMessage);
    }

    public static OkHttpUtilsException UNKNOWN() {
        return new OkHttpUtilsException("unknown exception!");
    }

    public static OkHttpUtilsException BREAKPOINT_NOT_EXIST() {
        return new OkHttpUtilsException("breakpoint file does not exist!");
    }

    public static OkHttpUtilsException BREAKPOINT_EXPIRED() {
        return new OkHttpUtilsException("breakpoint file has expired!");
    }
}