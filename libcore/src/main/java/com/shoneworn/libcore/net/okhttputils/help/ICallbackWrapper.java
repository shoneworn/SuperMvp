package com.shoneworn.libcore.net.okhttputils.help;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */
public interface ICallbackWrapper<T> {
    void onError(String code, String msg, Call call, Response response, Exception exception);

    void onSuccess(T serverData, Call call, Response response);
}
