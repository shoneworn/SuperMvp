package com.shoneworn.libcore.net.okhttputils.callback;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */
import com.shoneworn.libcore.net.okhttputils.convert.Converter;
import com.shoneworn.libcore.net.okhttputils.model.Progress;
import com.shoneworn.libcore.net.okhttputils.model.Response;
import com.shoneworn.libcore.net.okhttputils.request.base.Request;

public interface Callback<T> extends Converter<T> {
    void onStart(Request<? extends Request> var1);

    void onSuccess(Response<T> var1);

    void onCacheSuccess(Response<T> var1);

    void onError(Response<T> var1);

    void onFinish();

    void uploadProgress(Progress var1);

    void downloadProgress(Progress var1);

    boolean isForceCancel();

    void onForceCancel();
}