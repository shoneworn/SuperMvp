package com.shoneworn.libcore.net.okhttputils.cache.policy;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.graphics.Bitmap;

import com.shoneworn.libcore.net.okhttputils.OkHttpUtils;
import com.shoneworn.libcore.net.okhttputils.cache.CacheEntity;
import com.shoneworn.libcore.net.okhttputils.cache.CacheMode;
import com.shoneworn.libcore.net.okhttputils.callback.Callback;
import com.shoneworn.libcore.net.okhttputils.db.CacheManager;
import com.shoneworn.libcore.net.okhttputils.exception.HttpException;
import com.shoneworn.libcore.net.okhttputils.request.base.Request;
import com.shoneworn.libcore.net.okhttputils.utils.HeaderParser;
import com.shoneworn.libcore.net.okhttputils.utils.HttpUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

public abstract class BaseCachePolicy<T> implements CachePolicy<T> {
    protected Request<? extends Request> request;
    protected volatile boolean canceled;
    protected volatile int currentRetryCount = 0;
    protected boolean executed;
    protected Call rawCall;
    protected Callback<T> mCallback;
    protected CacheEntity<T> cacheEntity;

    public BaseCachePolicy(Request<? extends Request> request) {
        this.request = request;
    }

    public boolean onAnalysisResponse(Call call, Response response) {
        return false;
    }

    public CacheEntity<T> prepareCache() {
        if (this.request.getCacheKey() == null) {
            this.request.cacheKey(HttpUtils.createUrlFromParams(this.request.getBaseUrl(), true, this.request.getParams()));
        }

        if (this.request.getCacheMode() == null) {
            this.request.cacheMode(CacheMode.NO_CACHE);
        }

        CacheMode cacheMode = this.request.getCacheMode();
        if (cacheMode != CacheMode.NO_CACHE) {
            this.cacheEntity = (CacheEntity<T>) CacheManager.getInstance().get(this.request.getCacheKey());
            HeaderParser.addCacheHeaders(this.request, this.cacheEntity, cacheMode);
            if (this.cacheEntity != null && this.cacheEntity.checkExpire(cacheMode, this.request.getCacheTime(), System.currentTimeMillis())) {
                this.cacheEntity.setExpire(true);
            }
        }

        if (this.cacheEntity == null || this.cacheEntity.isExpire() || this.cacheEntity.getData() == null || this.cacheEntity.getResponseHeaders() == null) {
            this.cacheEntity = null;
        }

        return this.cacheEntity;
    }

    public synchronized Call prepareRawCall() throws Throwable {
        if (this.executed) {
            throw HttpException.COMMON("Already executed!");
        } else {
            this.executed = true;
            this.rawCall = this.request.getRawCall();
            if (this.canceled) {
                this.rawCall.cancel();
            }

            return this.rawCall;
        }
    }

    protected com.shoneworn.libcore.net.okhttputils.model.Response<T> requestNetworkSync() {
        try {
            Response response = this.rawCall.execute();
            int responseCode = response.code();
            if (responseCode != 404 && responseCode < 500) {
                T body = (T) this.request.getConverter().convertResponse(response);
                this.saveCache(response.headers(), body);
                return com.shoneworn.libcore.net.okhttputils.model.Response.success(false, body, this.rawCall, response);
            } else {
                return com.shoneworn.libcore.net.okhttputils.model.Response.error(false, this.rawCall, response, HttpException.NET_ERROR());
            }
        } catch (Throwable var4) {
            if (var4 instanceof SocketTimeoutException && this.currentRetryCount < this.request.getRetryCount()) {
                ++this.currentRetryCount;
                this.rawCall = this.request.getRawCall();
                if (this.canceled) {
                    this.rawCall.cancel();
                } else {
                    this.requestNetworkSync();
                }
            }

            return com.shoneworn.libcore.net.okhttputils.model.Response.error(false, this.rawCall, (Response) null, var4);
        }
    }

    protected void requestNetworkAsync() {
        this.rawCall.enqueue(new okhttp3.Callback() {
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException && BaseCachePolicy.this.currentRetryCount < BaseCachePolicy.this.request.getRetryCount()) {
                    ++BaseCachePolicy.this.currentRetryCount;
                    BaseCachePolicy.this.rawCall = BaseCachePolicy.this.request.getRawCall();
                    if (BaseCachePolicy.this.canceled) {
                        BaseCachePolicy.this.rawCall.cancel();
                    } else {
                        BaseCachePolicy.this.rawCall.enqueue(this);
                    }
                } else if (!call.isCanceled()) {
                    com.shoneworn.libcore.net.okhttputils.model.Response<T> error = com.shoneworn.libcore.net.okhttputils.model.Response.error(false, call, (Response) null, e);
                    BaseCachePolicy.this.onError(error);
                }

            }

            public void onResponse(Call call, Response response) throws IOException {
                int responseCode = response.code();
                if (responseCode != 404 && responseCode < 500) {
                    if (!BaseCachePolicy.this.onAnalysisResponse(call, response)) {
                        com.shoneworn.libcore.net.okhttputils.model.Response errorx;
                        try {
                            T body = (T) BaseCachePolicy.this.request.getConverter().convertResponse(response);
                            BaseCachePolicy.this.saveCache(response.headers(), body);
                            errorx = com.shoneworn.libcore.net.okhttputils.model.Response.success(false, body, call, response);
                            BaseCachePolicy.this.onSuccess(errorx);
                        } catch (Throwable var6) {
                            errorx = com.shoneworn.libcore.net.okhttputils.model.Response.error(false, call, response, var6);
                            BaseCachePolicy.this.onError(errorx);
                        }

                    }
                } else {
                    com.shoneworn.libcore.net.okhttputils.model.Response<T> error = com.shoneworn.libcore.net.okhttputils.model.Response.error(false, call, response, HttpException.NET_ERROR());
                    BaseCachePolicy.this.onError(error);
                }
            }
        });
    }

    private void saveCache(Headers headers, T data) {
        if (this.request.getCacheMode() != CacheMode.NO_CACHE) {
            if (!(data instanceof Bitmap)) {
                CacheEntity<T> cache = HeaderParser.createCacheEntity(headers, data, this.request.getCacheMode(), this.request.getCacheKey());
                if (cache == null) {
                    CacheManager.getInstance().remove(this.request.getCacheKey());
                } else {
                    CacheManager.getInstance().replace(this.request.getCacheKey(), cache);
                }

            }
        }
    }

    protected void runOnUiThread(Runnable run) {
        OkHttpUtils.getInstance().getDelivery().post(run);
    }

    public boolean isExecuted() {
        return this.executed;
    }

    public void cancel() {
        this.canceled = true;
        if (this.rawCall != null) {
            this.rawCall.cancel();
        }

    }

    public boolean isCanceled() {
        if (this.canceled) {
            return true;
        } else {
            synchronized (this) {
                return this.rawCall != null && this.rawCall.isCanceled();
            }
        }
    }
}
