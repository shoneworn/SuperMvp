package com.shoneworn.libcore.net.okhttputils.cache.policy;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import com.shoneworn.libcore.net.okhttputils.cache.CacheEntity;
import com.shoneworn.libcore.net.okhttputils.callback.Callback;
import com.shoneworn.libcore.net.okhttputils.model.Response;
import com.shoneworn.libcore.net.okhttputils.request.base.Request;

public class NoCachePolicy<T> extends BaseCachePolicy<T> {
    public NoCachePolicy(Request<? extends Request> request) {
        super(request);
    }

    public void onSuccess(final Response<T> success) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                NoCachePolicy.this.mCallback.onSuccess(success);
                NoCachePolicy.this.mCallback.onFinish();
            }
        });
    }

    public void onError(final Response<T> error) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                NoCachePolicy.this.mCallback.onError(error);
                NoCachePolicy.this.mCallback.onFinish();
            }
        });
    }

    public Response<T> requestSync(CacheEntity<T> cacheEntity) {
        try {
            this.prepareRawCall();
        } catch (Throwable var3) {
            return Response.error(false, this.rawCall, (okhttp3.Response)null, var3);
        }

        return this.requestNetworkSync();
    }

    public void requestAsync(CacheEntity<T> cacheEntity, Callback<T> callback) {
        this.mCallback = callback;
        this.runOnUiThread(new Runnable() {
            public void run() {
                NoCachePolicy.this.mCallback.onStart(NoCachePolicy.this.request);

                try {
                    NoCachePolicy.this.prepareRawCall();
                } catch (Throwable var3) {
                    Response<T> error = Response.error(false, NoCachePolicy.this.rawCall, (okhttp3.Response)null, var3);
                    NoCachePolicy.this.mCallback.onError(error);
                    return;
                }

                NoCachePolicy.this.requestNetworkAsync();
            }
        });
    }
}
