package com.shoneworn.libcore.net.okhttputils.cache.policy;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import com.shoneworn.libcore.net.okhttputils.cache.CacheEntity;
import com.shoneworn.libcore.net.okhttputils.callback.Callback;
import com.shoneworn.libcore.net.okhttputils.model.Response;
import com.shoneworn.libcore.net.okhttputils.request.base.Request;

public class NoneCacheRequestPolicy<T> extends BaseCachePolicy<T> {
    public NoneCacheRequestPolicy(Request<? extends Request> request) {
        super(request);
    }

    public void onSuccess(final Response<T> success) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                NoneCacheRequestPolicy.this.mCallback.onSuccess(success);
                NoneCacheRequestPolicy.this.mCallback.onFinish();
            }
        });
    }

    public void onError(final Response<T> error) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                NoneCacheRequestPolicy.this.mCallback.onError(error);
                NoneCacheRequestPolicy.this.mCallback.onFinish();
            }
        });
    }

    public Response<T> requestSync(CacheEntity<T> cacheEntity) {
        try {
            this.prepareRawCall();
        } catch (Throwable var3) {
            return Response.error(false, this.rawCall, (okhttp3.Response)null, var3);
        }

        Response<T> response = null;
        if(cacheEntity != null) {
            response = Response.success(true, cacheEntity.getData(), this.rawCall, (okhttp3.Response)null);
        }

        if(response == null) {
            response = this.requestNetworkSync();
        }

        return response;
    }

    public void requestAsync(final CacheEntity<T> cacheEntity, Callback<T> callback) {
        this.mCallback = callback;
        this.runOnUiThread(new Runnable() {
            public void run() {
                NoneCacheRequestPolicy.this.mCallback.onStart(NoneCacheRequestPolicy.this.request);

                try {
                    NoneCacheRequestPolicy.this.prepareRawCall();
                } catch (Throwable var3) {
                    Response<T> error = Response.error(false, NoneCacheRequestPolicy.this.rawCall, (okhttp3.Response)null, var3);
                    NoneCacheRequestPolicy.this.mCallback.onError(error);
                    return;
                }

                if(cacheEntity != null) {
                    Response<T> success = Response.success(true, cacheEntity.getData(), NoneCacheRequestPolicy.this.rawCall, (okhttp3.Response)null);
                    NoneCacheRequestPolicy.this.mCallback.onCacheSuccess(success);
                    NoneCacheRequestPolicy.this.mCallback.onFinish();
                } else {
                    NoneCacheRequestPolicy.this.requestNetworkAsync();
                }
            }
        });
    }
}
