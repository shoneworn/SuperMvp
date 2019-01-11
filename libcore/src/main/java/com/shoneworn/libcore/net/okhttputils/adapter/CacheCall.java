package com.shoneworn.libcore.net.okhttputils.adapter;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import com.shoneworn.libcore.net.okhttputils.cache.CacheEntity;
import com.shoneworn.libcore.net.okhttputils.cache.policy.CachePolicy;
import com.shoneworn.libcore.net.okhttputils.cache.policy.DefaultCachePolicy;
import com.shoneworn.libcore.net.okhttputils.cache.policy.FirstCacheRequestPolicy;
import com.shoneworn.libcore.net.okhttputils.cache.policy.NoCachePolicy;
import com.shoneworn.libcore.net.okhttputils.cache.policy.NoneCacheRequestPolicy;
import com.shoneworn.libcore.net.okhttputils.cache.policy.RequestFailedCachePolicy;
import com.shoneworn.libcore.net.okhttputils.callback.Callback;
import com.shoneworn.libcore.net.okhttputils.model.Response;
import com.shoneworn.libcore.net.okhttputils.request.base.Request;
import com.shoneworn.libcore.net.okhttputils.utils.HttpUtils;

public class CacheCall<T> implements Call<T> {
    private CachePolicy<T> policy = null;
    private Request<? extends Request> request;

    public CacheCall(Request<? extends Request> request) {
        this.request = request;
        this.policy = this.preparePolicy();
    }

    public Response<T> execute() {
        CacheEntity<T> cacheEntity = this.policy.prepareCache();
        return this.policy.requestSync(cacheEntity);
    }

    public void execute(Callback<T> callback) {
        HttpUtils.checkNotNull(callback, "callback == null");
        CacheEntity<T> cacheEntity = this.policy.prepareCache();
        if(!HttpUtils.isEmpty(callback) && callback.isForceCancel()) {
            callback.onForceCancel();
        } else {
            this.policy.requestAsync(cacheEntity, callback);
        }
    }

    private CachePolicy<T> preparePolicy() {
        switch(this.request.getCacheMode().ordinal()) {
            case 1:
                this.policy = new DefaultCachePolicy(this.request);
                break;
            case 2:
                this.policy = new NoCachePolicy(this.request);
                break;
            case 3:
                this.policy = new NoneCacheRequestPolicy(this.request);
                break;
            case 4:
                this.policy = new FirstCacheRequestPolicy(this.request);
                break;
            case 5:
                this.policy = new RequestFailedCachePolicy(this.request);
        }

        if(this.request.getCachePolicy() != null) {
            this.policy = this.request.getCachePolicy();
        }

        HttpUtils.checkNotNull(this.policy, "policy == null");
        return this.policy;
    }

    public boolean isExecuted() {
        return this.policy.isExecuted();
    }

    public void cancel() {
        this.policy.cancel();
    }

    public boolean isCanceled() {
        return this.policy.isCanceled();
    }

    public Call<T> clone() {
        return new CacheCall(this.request);
    }

    public Request getRequest() {
        return this.request;
    }
}
