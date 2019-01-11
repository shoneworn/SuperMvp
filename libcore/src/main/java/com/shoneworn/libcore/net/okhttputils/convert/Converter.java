package com.shoneworn.libcore.net.okhttputils.convert;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */
import okhttp3.Response;

public interface Converter<T> {
    T convertResponse(Response var1) throws Throwable;
}
