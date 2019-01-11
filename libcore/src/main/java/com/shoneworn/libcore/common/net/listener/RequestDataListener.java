package com.shoneworn.libcore.common.net.listener;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import android.support.annotation.Nullable;

import okhttp3.Call;
import okhttp3.Response;

public abstract class RequestDataListener<ServerDataType, DataType> implements IRequestDataListener<ServerDataType, DataType> {
    public RequestDataListener() {
    }

    public void onError(String code, String msg, @Nullable Response response, @Nullable Exception e) {
    }

    public void onCacheSuccess(DataType dataType, ServerDataType serverModel, Call call) {
    }

    public void onPersonalSuccess(Object... objs) {
    }

    public void onCacheError(Call call, Exception e) {
    }

    public void onCallBackJsonData(Response response, String jsonData) {
    }

    public void onCallBackJsonData(String jsonData) {
    }
}