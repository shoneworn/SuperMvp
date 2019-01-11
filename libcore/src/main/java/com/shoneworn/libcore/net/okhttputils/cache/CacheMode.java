package com.shoneworn.libcore.net.okhttputils.cache;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

public enum CacheMode {
    DEFAULT,
    NO_CACHE,
    REQUEST_FAILED_READ_CACHE,
    IF_NONE_CACHE_REQUEST,
    FIRST_CACHE_THEN_REQUEST;

    private CacheMode() {
    }
}
