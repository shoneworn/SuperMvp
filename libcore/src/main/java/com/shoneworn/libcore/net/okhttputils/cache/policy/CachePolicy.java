package com.shoneworn.libcore.net.okhttputils.cache.policy;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import com.shoneworn.libcore.net.okhttputils.cache.CacheEntity;
import com.shoneworn.libcore.net.okhttputils.callback.Callback;
import com.shoneworn.libcore.net.okhttputils.model.Response;
import okhttp3.Call;

public interface CachePolicy<T> {
    void onSuccess(Response<T> var1);

    void onError(Response<T> var1);

    boolean onAnalysisResponse(Call var1, okhttp3.Response var2);

    CacheEntity<T> prepareCache();

    Call prepareRawCall() throws Throwable;

    Response<T> requestSync(CacheEntity<T> var1);

    void requestAsync(CacheEntity<T> var1, Callback<T> var2);

    boolean isExecuted();

    void cancel();

    boolean isCanceled();
}
