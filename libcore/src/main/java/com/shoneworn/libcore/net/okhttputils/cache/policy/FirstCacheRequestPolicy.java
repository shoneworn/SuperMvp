package com.shoneworn.libcore.net.okhttputils.cache.policy;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import com.shoneworn.libcore.net.okhttputils.cache.CacheEntity;
import com.shoneworn.libcore.net.okhttputils.callback.Callback;
import com.shoneworn.libcore.net.okhttputils.model.Response;
import com.shoneworn.libcore.net.okhttputils.request.base.Request;

public class FirstCacheRequestPolicy<T> extends BaseCachePolicy<T> {
    public FirstCacheRequestPolicy(Request<? extends Request> request) {
        super(request);
    }

    public void onSuccess(final Response<T> success) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                FirstCacheRequestPolicy.this.mCallback.onSuccess(success);
                FirstCacheRequestPolicy.this.mCallback.onFinish();
            }
        });
    }

    public void onError(final Response<T> error) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                FirstCacheRequestPolicy.this.mCallback.onError(error);
                FirstCacheRequestPolicy.this.mCallback.onFinish();
            }
        });
    }

    public Response<T> requestSync(CacheEntity<T> cacheEntity) {
        try {
            this.prepareRawCall();
        } catch (Throwable var3) {
            return Response.error(false, this.rawCall, (okhttp3.Response)null, var3);
        }

        Response response;
        if(cacheEntity != null) {
            response = Response.success(true, cacheEntity.getData(), this.rawCall, (okhttp3.Response)null);
        }

        response = this.requestNetworkSync();
        if(!response.isSuccessful() && cacheEntity != null) {
            response = Response.success(true, cacheEntity.getData(), this.rawCall, response.getRawResponse());
        }

        return response;
    }

    public void requestAsync(final CacheEntity<T> cacheEntity, Callback<T> callback) {
        this.mCallback = callback;
        this.runOnUiThread(new Runnable() {
            public void run() {
                FirstCacheRequestPolicy.this.mCallback.onStart(FirstCacheRequestPolicy.this.request);

                try {
                    FirstCacheRequestPolicy.this.prepareRawCall();
                } catch (Throwable var3) {
                    Response<T> error = Response.error(false, FirstCacheRequestPolicy.this.rawCall, (okhttp3.Response)null, var3);
                    FirstCacheRequestPolicy.this.mCallback.onError(error);
                    return;
                }

                if(cacheEntity != null) {
                    Response<T> success = Response.success(true, cacheEntity.getData(), FirstCacheRequestPolicy.this.rawCall, (okhttp3.Response)null);
                    FirstCacheRequestPolicy.this.mCallback.onCacheSuccess(success);
                }

                FirstCacheRequestPolicy.this.requestNetworkAsync();
            }
        });
    }
}
