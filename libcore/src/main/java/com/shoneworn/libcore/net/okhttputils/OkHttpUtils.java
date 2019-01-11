package com.shoneworn.libcore.net.okhttputils;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.Nullable;

import java.io.InputStream;
import java.net.Proxy;
import java.net.ProxySelector;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.X509TrustManager;


import com.shoneworn.libcore.net.okhttputils.cache.CacheMode;
import com.shoneworn.libcore.net.okhttputils.cookie.CookieJarImpl;
import com.shoneworn.libcore.net.okhttputils.cookie.store.CookieStore;
import com.shoneworn.libcore.net.okhttputils.https.HttpsUtils;
import com.shoneworn.libcore.net.okhttputils.https.HttpsUtils.SSLParams;
import com.shoneworn.libcore.net.okhttputils.interceptor.HttpLoggingInterceptor;
import com.shoneworn.libcore.net.okhttputils.interceptor.HttpLoggingInterceptor.Level;
import com.shoneworn.libcore.net.okhttputils.model.HttpHeaders;
import com.shoneworn.libcore.net.okhttputils.model.HttpParams;
import com.shoneworn.libcore.net.okhttputils.request.DeleteRequest;
import com.shoneworn.libcore.net.okhttputils.request.GetRequest;
import com.shoneworn.libcore.net.okhttputils.request.HeadRequest;
import com.shoneworn.libcore.net.okhttputils.request.OptionsRequest;
import com.shoneworn.libcore.net.okhttputils.request.PatchRequest;
import com.shoneworn.libcore.net.okhttputils.request.PostRequest;
import com.shoneworn.libcore.net.okhttputils.request.PutRequest;
import com.shoneworn.libcore.net.okhttputils.request.TraceRequest;
import com.shoneworn.libcore.net.okhttputils.utils.HttpUtils;
import com.shoneworn.libcore.net.okhttputils.utils.OkLogger;

import com.shoneworn.libcore.utils.Utils;
import com.shoneworn.libcore.utils.WeakHandler;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;

public class OkHttpUtils {
    public static final long DEFAULT_MILLISECONDS = 60000L;
    public static final int DEFAULT_RETRY_COUNT = 3;
    public static long REFRESH_TIME = 300L;
    private Application context;
    private WeakHandler mDelivery;
    private OkHttpClient okHttpClient;
    private Builder okHttpClientBuilder;
    private HttpParams mCommonParams;
    private HttpHeaders mCommonHeaders;
    private int mRetryCount;
    private CacheMode mCacheMode;
    private long mCacheTime;
    private CookieJarImpl cookieJar;
    private ProxySelector proxySelector;
    private Proxy proxy;
    private SSLParams sslParams;
    private HostnameVerifier hostnameVerifier;
    private boolean isAddCommonPHDeep;

    private OkHttpUtils() {
        this.isAddCommonPHDeep = false;
        this.mDelivery = new WeakHandler(Looper.getMainLooper());
        this.mRetryCount = 3;
        this.mCacheTime = -1L;
        this.mCacheMode = CacheMode.NO_CACHE;
        this.okHttpClientBuilder = new Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkHttpUtils");
        loggingInterceptor.setPrintLevel(Level.BODY);
        loggingInterceptor.setColorLevel(java.util.logging.Level.INFO);
        this.okHttpClientBuilder.addInterceptor(loggingInterceptor);
        this.okHttpClientBuilder.readTimeout(60000L, TimeUnit.MILLISECONDS);
        this.okHttpClientBuilder.writeTimeout(60000L, TimeUnit.MILLISECONDS);
        this.okHttpClientBuilder.connectTimeout(60000L, TimeUnit.MILLISECONDS);
        if(this.sslParams == null) {
            this.sslParams = HttpsUtils.getSslSocketFactory();
        }

        if(this.hostnameVerifier == null) {
            this.hostnameVerifier = HttpsUtils.UnSafeHostnameVerifier;
        }

        if(!Utils.isEmpty(this.sslParams)) {
            this.okHttpClientBuilder.sslSocketFactory(this.sslParams.sSLSocketFactory, this.sslParams.trustManager);
        }

        if(!Utils.isEmpty(this.hostnameVerifier)) {
            this.okHttpClientBuilder.hostnameVerifier(this.hostnameVerifier);
        }

        if(!Utils.isEmpty(this.proxy)) {
            this.okHttpClientBuilder.proxy(this.proxy);
        }

        if(!Utils.isEmpty(this.proxySelector)) {
            this.okHttpClientBuilder.proxySelector(this.proxySelector);
        }

        this.rebuildOkHttpClient();
    }

    private void rebuildOkHttpClient() {
        this.checkOkHttpClientBuilderNotNull();
        this.okHttpClient = this.okHttpClientBuilder.build();
    }

    private void checkOkHttpClientBuilderNotNull() {
        if(this.okHttpClientBuilder == null) {
            this.okHttpClientBuilder = new Builder();
        }

    }

    public static OkHttpUtils getInstance() {
        return OkHttpUtils.OkHttpUtilsHolder.holder;
    }

    public static GetRequest get(String url) {
        return new GetRequest(url);
    }

    public static PostRequest post(String url) {
        return new PostRequest(url);
    }

    public static PutRequest put(String url) {
        return new PutRequest(url);
    }

    public static HeadRequest head(String url) {
        return new HeadRequest(url);
    }

    public static DeleteRequest delete(String url) {
        return new DeleteRequest(url);
    }

    public static OptionsRequest options(String url) {
        return new OptionsRequest(url);
    }

    public static PatchRequest patch(String url) {
        return new PatchRequest(url);
    }

    public static TraceRequest trace(String url) {
        return new TraceRequest(url);
    }

    public OkHttpUtils init(Application app) {
        this.context = app;
        return this;
    }

    public Context getContext() {
        HttpUtils.checkNotNull(this.context, "please call OkHttpUtils.getInstance().init() first in application!");
        return this.context;
    }

    public WeakHandler getDelivery() {
        if(this.mDelivery == null) {
            this.mDelivery = new WeakHandler(Looper.getMainLooper());
        }

        return this.mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        HttpUtils.checkNotNull(this.okHttpClient, "please call OkHttpUtils.getInstance().setOkHttpClient() first in application!");
        return this.okHttpClient;
    }

    public OkHttpUtils setOkHttpClient(Builder okHttpClientBuilder, OkHttpClient okHttpClient) {
        HttpUtils.checkNotNull(okHttpClient, "okHttpClient == null");
        HttpUtils.checkNotNull(okHttpClientBuilder, "okHttpClientBuilder == null");
        this.okHttpClientBuilder = okHttpClientBuilder;
        this.okHttpClient = okHttpClient;
        this.cookieJar = (CookieJarImpl)okHttpClient.cookieJar();
        return this;
    }

    public OkHttpUtils debug(boolean isEnable) {
        this.debug(isEnable, OkLogger.tag, java.util.logging.Level.INFO);
        return this;
    }

    public OkHttpUtils debug(boolean isEnable, String tag) {
        this.debug(isEnable, tag, java.util.logging.Level.INFO);
        return this;
    }

    public OkHttpUtils debug(boolean isEnable, String tag, java.util.logging.Level level) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(tag);
        loggingInterceptor.setPrintLevel(Level.BODY);
        loggingInterceptor.setColorLevel(level);
        this.okHttpClientBuilder.addInterceptor(loggingInterceptor);
        OkLogger.debug(isEnable);
        return this;
    }

    public boolean isAddCommonPHDeep() {
        return this.isAddCommonPHDeep;
    }

    public OkHttpUtils setAddCommonPHDeep(boolean addCommonPHDeep) {
        this.isAddCommonPHDeep = addCommonPHDeep;
        return this;
    }

    public OkHttpUtils setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        this.checkOkHttpClientBuilderNotNull();
        this.okHttpClientBuilder.hostnameVerifier(this.hostnameVerifier);
        this.rebuildOkHttpClient();
        return this;
    }

    public HostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }

    public OkHttpUtils setCertificates(InputStream... certificates) {
        this.setCertificates((InputStream)null, (String)null, (InputStream[])certificates);
        return this;
    }

    public OkHttpUtils setCertificates(X509TrustManager trustManager) {
        this.setCertificates((InputStream)null, (String)null, (X509TrustManager)trustManager);
        return this;
    }

    public OkHttpUtils setCertificates(InputStream bksFile, String password, InputStream... certificates) {
        this.sslParams = HttpsUtils.getSslSocketFactory(bksFile, password, certificates);
        this.checkOkHttpClientBuilderNotNull();
        if(!Utils.isEmpty(this.sslParams)) {
            this.okHttpClientBuilder.sslSocketFactory(this.sslParams.sSLSocketFactory, this.sslParams.trustManager);
        }

        this.rebuildOkHttpClient();
        return this;
    }

    public OkHttpUtils setCertificates(InputStream bksFile, String password, X509TrustManager trustManager) {
        this.sslParams = HttpsUtils.getSslSocketFactory(bksFile, password, trustManager);
        this.checkOkHttpClientBuilderNotNull();
        if(!Utils.isEmpty(this.sslParams)) {
            this.okHttpClientBuilder.sslSocketFactory(this.sslParams.sSLSocketFactory, this.sslParams.trustManager);
        }

        this.rebuildOkHttpClient();
        return this;
    }

    public SSLParams getSslParams() {
        return this.sslParams;
    }

    public OkHttpUtils setCookieStore(CookieStore cookieStore) {
        this.checkOkHttpClientBuilderNotNull();
        this.cookieJar = new CookieJarImpl(cookieStore);
        this.okHttpClientBuilder.cookieJar(this.cookieJar);
        this.rebuildOkHttpClient();
        return this;
    }

    public CookieJarImpl getCookieJar() {
        return this.cookieJar;
    }

    public OkHttpUtils setProxySelector(@Nullable ProxySelector proxySelector) {
        this.checkOkHttpClientBuilderNotNull();
        this.proxySelector = proxySelector;
        if(!Utils.isEmpty(this.okHttpClientBuilder)) {
            this.okHttpClientBuilder.proxySelector(proxySelector);
        }

        this.rebuildOkHttpClient();
        return this;
    }

    public OkHttpUtils setProxy(@Nullable Proxy proxy) {
        this.checkOkHttpClientBuilderNotNull();
        this.proxy = proxy;
        if(!Utils.isEmpty(this.okHttpClientBuilder)) {
            this.okHttpClientBuilder.proxy(proxy);
        }

        this.rebuildOkHttpClient();
        return this;
    }

    public ProxySelector getProxySelector() {
        return this.proxySelector;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public OkHttpUtils setReadTimeOut(long readTimeOut) {
        this.checkOkHttpClientBuilderNotNull();
        this.okHttpClientBuilder.readTimeout(readTimeOut, TimeUnit.MILLISECONDS);
        this.rebuildOkHttpClient();
        return this;
    }

    public OkHttpUtils setWriteTimeOut(long writeTimeout) {
        this.checkOkHttpClientBuilderNotNull();
        this.okHttpClientBuilder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        this.rebuildOkHttpClient();
        return this;
    }

    public OkHttpUtils setConnectTimeout(long connectTimeout) {
        this.checkOkHttpClientBuilderNotNull();
        this.okHttpClientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        this.rebuildOkHttpClient();
        return this;
    }

    public OkHttpUtils setRetryCount(int retryCount) {
        if(retryCount < 0) {
            throw new IllegalArgumentException("retryCount must > 0");
        } else {
            this.mRetryCount = retryCount;
            return this;
        }
    }

    public int getRetryCount() {
        return this.mRetryCount;
    }

    public OkHttpUtils setCacheMode(CacheMode cacheMode) {
        this.mCacheMode = cacheMode;
        return this;
    }

    public CacheMode getCacheMode() {
        return this.mCacheMode;
    }

    public OkHttpUtils setCacheTime(long cacheTime) {
        if(cacheTime <= -1L) {
            cacheTime = -1L;
        }

        this.mCacheTime = cacheTime;
        return this;
    }

    public long getCacheTime() {
        return this.mCacheTime;
    }

    public HttpParams getCommonParams() {
        return this.mCommonParams;
    }

    public OkHttpUtils addCommonParams(HttpParams commonParams) {
        if(this.mCommonParams == null) {
            this.mCommonParams = new HttpParams();
        }

        this.mCommonParams.put(commonParams);
        return this;
    }

    public OkHttpUtils clearCommonParams() {
        if(this.mCommonParams != null) {
            this.mCommonParams.clear();
        }

        return this;
    }

    public HttpHeaders getCommonHeaders() {
        return this.mCommonHeaders;
    }

    public OkHttpUtils addCommonHeaders(HttpHeaders commonHeaders) {
        if(this.mCommonHeaders == null) {
            this.mCommonHeaders = new HttpHeaders();
        }

        this.mCommonHeaders.put(commonHeaders);
        return this;
    }

    public OkHttpUtils clearCommonHeaders() {
        if(this.mCommonHeaders != null) {
            this.mCommonHeaders.clear();
        }

        return this;
    }

    public void cancelTag(Object tag) {
        if(tag != null) {
            Iterator var2 = this.getOkHttpClient().dispatcher().queuedCalls().iterator();

            Call call;
            while(var2.hasNext()) {
                call = (Call)var2.next();
                if(tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }

            var2 = this.getOkHttpClient().dispatcher().runningCalls().iterator();

            while(var2.hasNext()) {
                call = (Call)var2.next();
                if(tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }

        }
    }

    public static void cancelTag(OkHttpClient client, Object tag) {
        if(client != null && tag != null) {
            Iterator var2 = client.dispatcher().queuedCalls().iterator();

            Call call;
            while(var2.hasNext()) {
                call = (Call)var2.next();
                if(tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }

            var2 = client.dispatcher().runningCalls().iterator();

            while(var2.hasNext()) {
                call = (Call)var2.next();
                if(tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }

        }
    }

    public void cancelAll() {
        Iterator var1 = this.getOkHttpClient().dispatcher().queuedCalls().iterator();

        Call call;
        while(var1.hasNext()) {
            call = (Call)var1.next();
            call.cancel();
        }

        var1 = this.getOkHttpClient().dispatcher().runningCalls().iterator();

        while(var1.hasNext()) {
            call = (Call)var1.next();
            call.cancel();
        }

    }

    public static void cancelAll(OkHttpClient client) {
        if(client != null) {
            Iterator var1 = client.dispatcher().queuedCalls().iterator();

            Call call;
            while(var1.hasNext()) {
                call = (Call)var1.next();
                call.cancel();
            }

            var1 = client.dispatcher().runningCalls().iterator();

            while(var1.hasNext()) {
                call = (Call)var1.next();
                call.cancel();
            }

        }
    }

    private static class OkHttpUtilsHolder {
        private static OkHttpUtils holder = new OkHttpUtils();

        private OkHttpUtilsHolder() {
        }
    }
}
