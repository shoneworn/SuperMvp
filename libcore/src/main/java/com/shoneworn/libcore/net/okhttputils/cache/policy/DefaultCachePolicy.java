package com.shoneworn.libcore.net.okhttputils.cache.policy;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import com.shoneworn.libcore.net.okhttputils.cache.CacheEntity;
import com.shoneworn.libcore.net.okhttputils.callback.Callback;
import com.shoneworn.libcore.net.okhttputils.exception.CacheException;
import com.shoneworn.libcore.net.okhttputils.model.Response;
import com.shoneworn.libcore.net.okhttputils.request.base.Request;
import okhttp3.Call;

public class DefaultCachePolicy<T> extends BaseCachePolicy<T> {
    public DefaultCachePolicy(Request<? extends Request> request) {
        super(request);
    }

    public void onSuccess(final Response<T> success) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                DefaultCachePolicy.this.mCallback.onSuccess(success);
                DefaultCachePolicy.this.mCallback.onFinish();
            }
        });
    }

    public void onError(final Response<T> error) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                DefaultCachePolicy.this.mCallback.onError(error);
                DefaultCachePolicy.this.mCallback.onFinish();
            }
        });
    }

    public boolean onAnalysisResponse(Call call, okhttp3.Response response) {
        if(response.code() != 304) {
            return false;
        } else {
            final Response error;
            if(this.cacheEntity == null) {
                error = Response.error(true, call, response, CacheException.NON_AND_304(this.request.getCacheKey()));
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        DefaultCachePolicy.this.mCallback.onError(error);
                        DefaultCachePolicy.this.mCallback.onFinish();
                    }
                });
            } else {
                error = Response.success(true, this.cacheEntity.getData(), call, response);
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        DefaultCachePolicy.this.mCallback.onCacheSuccess(error);
                        DefaultCachePolicy.this.mCallback.onFinish();
                    }
                });
            }

            return true;
        }
    }

    public Response<T> requestSync(CacheEntity<T> cacheEntity) {
        try {
            this.prepareRawCall();
        } catch (Throwable var3) {
            return Response.error(false, this.rawCall, (okhttp3.Response)null, var3);
        }

        Response<T> response = this.requestNetworkSync();
        if(response.isSuccessful() && response.code() == 304) {
            if(cacheEntity == null) {
                response = Response.error(true, this.rawCall, response.getRawResponse(), CacheException.NON_AND_304(this.request.getCacheKey()));
            } else {
                response = Response.success(true, cacheEntity.getData(), this.rawCall, response.getRawResponse());
            }
        }

        return response;
    }

    public void requestAsync(CacheEntity<T> cacheEntity, Callback<T> callback) {
        this.mCallback = callback;
        this.runOnUiThread(new Runnable() {
            public void run() {
                DefaultCachePolicy.this.mCallback.onStart(DefaultCachePolicy.this.request);

                try {
                    DefaultCachePolicy.this.prepareRawCall();
                } catch (Throwable var3) {
                    Response<T> error = Response.error(false, DefaultCachePolicy.this.rawCall, (okhttp3.Response)null, var3);
                    DefaultCachePolicy.this.mCallback.onError(error);
                    return;
                }

                DefaultCachePolicy.this.requestNetworkAsync();
            }
        });
    }
}
