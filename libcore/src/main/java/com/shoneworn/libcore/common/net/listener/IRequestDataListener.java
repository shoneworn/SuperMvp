package com.shoneworn.libcore.common.net.listener;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import android.support.annotation.Nullable;

import okhttp3.Response;

public interface IRequestDataListener<ServerDataType, DataType> {
    void onSuccess(DataType data, ServerDataType serverData, @Nullable Response response);
}
