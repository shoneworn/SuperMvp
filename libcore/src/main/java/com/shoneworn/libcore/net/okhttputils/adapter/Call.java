package com.shoneworn.libcore.net.okhttputils.adapter;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import com.shoneworn.libcore.net.okhttputils.callback.Callback;
import com.shoneworn.libcore.net.okhttputils.model.Response;
import com.shoneworn.libcore.net.okhttputils.request.base.Request;

public interface Call<T> {
    Response<T> execute() throws Exception;

    void execute(Callback<T> var1);

    boolean isExecuted();

    void cancel();

    boolean isCanceled();

    Call<T> clone();

    Request getRequest();
}
