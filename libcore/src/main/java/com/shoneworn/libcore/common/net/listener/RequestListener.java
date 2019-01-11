package com.shoneworn.libcore.common.net.listener;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.support.annotation.Nullable;

import okhttp3.Request;
import okhttp3.Response;

public abstract class RequestListener {
    public RequestListener() {
    }

    public abstract void onSuccess(boolean var1, Request var2, @Nullable Response var3);

    public void onError(boolean isFromCache, @Nullable Response response, @Nullable Exception e) {
    }

    public void onErrrCodeMsg(String errorCode, String errMsg) {
    }
}