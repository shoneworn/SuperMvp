package com.shoneworn.libcore.net.okhttputils.cache.policy;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import com.shoneworn.libcore.net.okhttputils.cache.CacheEntity;
import com.shoneworn.libcore.net.okhttputils.callback.Callback;
import com.shoneworn.libcore.net.okhttputils.model.Response;
import com.shoneworn.libcore.net.okhttputils.request.base.Request;

public class RequestFailedCachePolicy<T> extends BaseCachePolicy<T> {
    public RequestFailedCachePolicy(Request<? extends Request> request) {
        super(request);
    }

    public void onSuccess(final Response<T> success) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                RequestFailedCachePolicy.this.mCallback.onSuccess(success);
                RequestFailedCachePolicy.this.mCallback.onFinish();
            }
        });
    }

    public void onError(final Response<T> error) {
        if(this.cacheEntity != null) {
            final Response<T> cacheSuccess = Response.success(true, this.cacheEntity.getData(), error.getRawCall(), error.getRawResponse());
            this.runOnUiThread(new Runnable() {
                public void run() {
                    RequestFailedCachePolicy.this.mCallback.onCacheSuccess(cacheSuccess);
                    RequestFailedCachePolicy.this.mCallback.onFinish();
                }
            });
        } else {
            this.runOnUiThread(new Runnable() {
                public void run() {
                    RequestFailedCachePolicy.this.mCallback.onError(error);
                    RequestFailedCachePolicy.this.mCallback.onFinish();
                }
            });
        }

    }

    public Response<T> requestSync(CacheEntity<T> cacheEntity) {
        try {
            this.prepareRawCall();
        } catch (Throwable var3) {
            return Response.error(false, this.rawCall, (okhttp3.Response)null, var3);
        }

        Response<T> response = this.requestNetworkSync();
        if(!response.isSuccessful() && cacheEntity != null) {
            response = Response.success(true, cacheEntity.getData(), this.rawCall, response.getRawResponse());
        }

        return response;
    }

    public void requestAsync(CacheEntity<T> cacheEntity, Callback<T> callback) {
        this.mCallback = callback;
        this.runOnUiThread(new Runnable() {
            public void run() {
                RequestFailedCachePolicy.this.mCallback.onStart(RequestFailedCachePolicy.this.request);

                try {
                    RequestFailedCachePolicy.this.prepareRawCall();
                } catch (Throwable var3) {
                    Response<T> error = Response.error(false, RequestFailedCachePolicy.this.rawCall, (okhttp3.Response)null, var3);
                    RequestFailedCachePolicy.this.mCallback.onError(error);
                    return;
                }

                RequestFailedCachePolicy.this.requestNetworkAsync();
            }
        });
    }
}
