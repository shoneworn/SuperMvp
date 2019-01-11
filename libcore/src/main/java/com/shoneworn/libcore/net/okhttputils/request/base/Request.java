package com.shoneworn.libcore.net.okhttputils.request.base;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.text.TextUtils;

import com.shoneworn.libcore.net.okhttputils.OkHttpUtils;
import com.shoneworn.libcore.net.okhttputils.adapter.AdapterParam;
import com.shoneworn.libcore.net.okhttputils.adapter.CacheCall;
import com.shoneworn.libcore.net.okhttputils.adapter.Call;
import com.shoneworn.libcore.net.okhttputils.adapter.CallAdapter;
import com.shoneworn.libcore.net.okhttputils.cache.CacheMode;
import com.shoneworn.libcore.net.okhttputils.cache.policy.CachePolicy;
import com.shoneworn.libcore.net.okhttputils.callback.Callback;
import com.shoneworn.libcore.net.okhttputils.convert.Converter;
import com.shoneworn.libcore.net.okhttputils.https.HttpsUtils.SSLParams;
import com.shoneworn.libcore.net.okhttputils.model.HttpHeaders;
import com.shoneworn.libcore.net.okhttputils.model.HttpMethod;
import com.shoneworn.libcore.net.okhttputils.model.HttpParams;
import com.shoneworn.libcore.net.okhttputils.model.HttpParams.FileWrapper;
import com.shoneworn.libcore.net.okhttputils.request.base.ProgressRequestBody.UploadInterceptor;
import com.shoneworn.libcore.net.okhttputils.utils.HttpUtils;
import com.shoneworn.libcore.utils.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.net.Proxy;
import java.net.ProxySelector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.OkHttpClient.Builder;

public abstract class Request<R extends Request> implements Serializable {
    private static final long serialVersionUID = -7174118653689916252L;
    protected String url;
    protected String baseUrl;
    protected transient OkHttpClient client;
    protected transient Object tag;
    protected int retryCount;
    protected long readTimeOut;
    protected long writeTimeOut;
    protected long connectTimeout;
    protected CacheMode cacheMode;
    protected String cacheKey;
    protected long cacheTime;
    protected HttpParams params = new HttpParams();
    protected HttpHeaders headers = new HttpHeaders();
    protected ProxySelector proxySelector;
    protected Proxy proxy;
    private SSLParams sslParams;
    private HostnameVerifier hostnameVerifier;
    protected transient okhttp3.Request mRequest;
    protected transient Call call;
    protected transient Callback callback;
    protected transient Converter converter;
    protected transient CachePolicy cachePolicy;
    protected transient UploadInterceptor uploadInterceptor;
    protected List<Interceptor> interceptors = new ArrayList();
    protected OkHttpUtils okHttpUtils;
    protected boolean isReGenerateRequestBody = true;
    private boolean isAppendCommonParams = true;
    private List<String> removedCommonParamKeyList = new ArrayList();
    private boolean isAppendCommonHeaders = true;
    private List<String> removedCommonHeaderKeyList = new ArrayList();

    public Request(String url) {
        this.url = url;
        this.baseUrl = url;
        this.isAppendCommonParams = true;
        this.removedCommonParamKeyList.clear();
        this.isAppendCommonHeaders = true;
        this.removedCommonHeaderKeyList.clear();
        this.okHttpUtils = OkHttpUtils.getInstance();
        this.interceptors.clear();
        String acceptLanguage = HttpHeaders.getAcceptLanguage();
        if (!TextUtils.isEmpty(acceptLanguage)) {
            this.headers("Accept-Language", acceptLanguage);
        }

        String userAgent = HttpHeaders.getUserAgent();
        if (!TextUtils.isEmpty(userAgent)) {
            this.headers("User-Agent", userAgent);
        }

        if (this.okHttpUtils.getCommonParams() != null) {
            this.putAllCommonParams(this.okHttpUtils.getCommonParams());
        }

        if (this.okHttpUtils.getCommonHeaders() != null) {
            this.putAllCommonHeaders(this.okHttpUtils.getCommonHeaders());
        }

        this.retryCount = this.okHttpUtils.getRetryCount();
        this.cacheMode = this.okHttpUtils.getCacheMode();
        this.cacheTime = this.okHttpUtils.getCacheTime();
    }

    private void putAllCommonParams(HttpParams commonParams) {
        if (!Utils.isEmpty(commonParams)) {
            if (this.okHttpUtils.isAddCommonPHDeep()) {
                HttpParams deepHttpParams = commonParams.deepClone();
                this.params(deepHttpParams);
            } else {
                this.params(commonParams);
            }

        }
    }

    private void putAllCommonHeaders(HttpHeaders commonHeaders) {
        if (!Utils.isEmpty(commonHeaders)) {
            if (this.okHttpUtils.isAddCommonPHDeep()) {
                HttpHeaders deepHttpHeaders = commonHeaders.deepClone();
                this.headers(deepHttpHeaders);
            } else {
                this.headers(commonHeaders);
            }

        }
    }

    public R tag(Object tag) {
        this.tag = tag;
        return (R) this;
    }

    public R readTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return (R) this;
    }

    public R writeTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return (R) this;
    }

    public R connTimeOut(long connTimeOut) {
        this.connectTimeout = connTimeOut;
        return (R) this;
    }

    public R retryCount(int retryCount) {
        if (retryCount < 0) {
            throw new IllegalArgumentException("retryCount must > 0");
        } else {
            this.retryCount = retryCount;
            return (R) this;
        }
    }

    public R setProxySelector(ProxySelector proxySelector) {
        this.proxySelector = proxySelector;
        return (R) this;
    }

    public R setProxy(Proxy proxy) {
        this.proxy = proxy;
        return (R) this;
    }

    public R setSslParams(SSLParams sslParams) {
        this.sslParams = sslParams;
        return (R) this;
    }

    public R setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return (R) this;
    }

    public R client(OkHttpClient client) {
        HttpUtils.checkNotNull(client, "OkHttpClient == null");
        this.client = client;
        return (R) this;
    }

    public <T> R call(Call<T> call) {
        HttpUtils.checkNotNull(call, "call == null");
        this.call = call;
        return (R) this;
    }

    public <T> R converter(Converter<T> converter) {
        HttpUtils.checkNotNull(converter, "converter == null");
        this.converter = converter;
        return (R) this;
    }

    public R cacheMode(CacheMode cacheMode) {
        HttpUtils.checkNotNull(cacheMode, "cacheMode == null");
        this.cacheMode = cacheMode;
        return (R) this;
    }

    public <T> R cachePolicy(CachePolicy<T> cachePolicy) {
        HttpUtils.checkNotNull(cachePolicy, "cachePolicy == null");
        this.cachePolicy = cachePolicy;
        return (R) this;
    }

    public R cacheKey(String cacheKey) {
        HttpUtils.checkNotNull(cacheKey, "cacheKey == null");
        this.cacheKey = cacheKey;
        return (R) this;
    }

    public R cacheTime(long cacheTime) {
        if (cacheTime <= -1L) {
            cacheTime = -1L;
        }

        this.cacheTime = cacheTime;
        return (R) this;
    }

    public R headers(HttpHeaders headers) {
        this.headers.put(headers);
        return (R) this;
    }

    public R headers(String key, String value) {
        this.headers.put(key, value);
        return (R) this;
    }

    public R params(HttpParams params) {
        this.params.put(params);
        return (R) this;
    }

    public R params(String key, String value, boolean... isReplace) {
        this.params.put(key, value, isReplace);
        return (R) this;
    }

    public R params(String key, int value, boolean... isReplace) {
        this.params.put(key, value, isReplace);
        return (R) this;
    }

    public R params(String key, float value, boolean... isReplace) {
        this.params.put(key, value, isReplace);
        return (R) this;
    }

    public R params(String key, double value, boolean... isReplace) {
        this.params.put(key, value, isReplace);
        return (R) this;
    }

    public R params(String key, long value, boolean... isReplace) {
        this.params.put(key, value, isReplace);
        return (R) this;
    }

    public R params(String key, char value, boolean... isReplace) {
        this.params.put(key, value, isReplace);
        return (R) this;
    }

    public R params(String key, boolean value, boolean... isReplace) {
        this.params.put(key, value, isReplace);
        return (R) this;
    }

    public R paramsStringMap(Map<String, String> params, boolean... isReplace) {
        this.params.putStringMap(params, isReplace);
        return (R) this;
    }

    public R paramsStringList(String key, List<String> values, boolean... isReplace) {
        this.params.putStringList(key, values, isReplace);
        return (R) this;
    }

    public R paramsPath(String key, String value) {
        this.params.putPath(key, value);
        return (R) this;
    }

    public R removeHeader(String key) {
        this.headers.remove(key);
        return (R) this;
    }

    public R removeAllHeaders() {
        this.headers.clear();
        return (R) this;
    }

    public R removeParam(String key) {
        this.params.remove(key);
        return (R) this;
    }

    public R removeAllParams() {
        this.params.clear();
        return (R) this;
    }

    public R uploadInterceptor(UploadInterceptor uploadInterceptor) {
        this.uploadInterceptor = uploadInterceptor;
        return (R) this;
    }

    public R addInterceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
        return (R) this;
    }

    public R removeCommonParamKeys() {
        return this.removeCommonParamKeys(false, (String[]) null);
    }

    public R removeCommonParamKeys(boolean appendCommonParams, String... removedCommonParamKeys) {
        List<String> tempList = new ArrayList();
        if (!HttpUtils.isEmpty(removedCommonParamKeys)) {
            String[] var4 = removedCommonParamKeys;
            int var5 = removedCommonParamKeys.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                String removedCommonParamKey = var4[var6];
                tempList.add(removedCommonParamKey);
            }
        }

        return this.removeCommonParamKeyList(appendCommonParams, tempList);
    }

    public R removeCommonParamKeyList() {
        return (R) this.removeCommonParamKeyList(false, (List) null);
    }

    public R removeCommonParamKeyList(boolean appendCommonParams, List<String> removedCommonParamKeyList) {
        this.isAppendCommonParams = appendCommonParams;
        this.removedCommonParamKeyList.clear();
        if (!appendCommonParams && !HttpUtils.isEmpty(this.okHttpUtils) && !HttpUtils.isEmpty(this.params)) {
            if (removedCommonParamKeyList == null) {
                removedCommonParamKeyList = new ArrayList();
            }

            HttpParams commonParams = this.okHttpUtils.getCommonParams();
            if (HttpUtils.isEmpty(commonParams)) {
                return (R) this;
            } else {
                List<String> commonParamsKeyList = commonParams.getKeyList();
                if (HttpUtils.isEmpty((Collection) removedCommonParamKeyList) && !HttpUtils.isEmpty(commonParamsKeyList)) {
                    ((List) removedCommonParamKeyList).addAll(commonParamsKeyList);
                }

                if (HttpUtils.isEmpty((Collection) removedCommonParamKeyList)) {
                    return (R) this;
                } else {
                    this.removedCommonParamKeyList.addAll((Collection) removedCommonParamKeyList);
                    Iterator var5 = this.removedCommonParamKeyList.iterator();

                    while (var5.hasNext()) {
                        String key = (String) var5.next();
                        this.params.remove(key);
                    }

                    return (R) this;
                }
            }
        } else {
            return (R) this;
        }
    }

    public R removeCommonHeaderKeys() {
        return this.removeCommonHeaderKeys(false, (String[]) null);
    }

    public R removeCommonHeaderKeys(boolean appendCommonHeaders, String... removedCommonHeaderKeys) {
        List<String> tempList = new ArrayList();
        if (!HttpUtils.isEmpty(removedCommonHeaderKeys)) {
            String[] var4 = removedCommonHeaderKeys;
            int var5 = removedCommonHeaderKeys.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                String removedCommonHeaderKey = var4[var6];
                tempList.add(removedCommonHeaderKey);
            }
        }

        return this.removeCommonHeaderKeyList(appendCommonHeaders, tempList);
    }

    public R removeCommonHeaderKeyList() {
        return (R) this.removeCommonHeaderKeyList(false, (List) null);
    }

    public R removeCommonHeaderKeyList(boolean appendCommonHeaders, List<String> removedCommonHeaderKeyList) {
        this.isAppendCommonHeaders = appendCommonHeaders;
        this.removedCommonHeaderKeyList.clear();
        if (!appendCommonHeaders && !HttpUtils.isEmpty(this.okHttpUtils) && !HttpUtils.isEmpty(this.headers)) {
            if (removedCommonHeaderKeyList == null) {
                removedCommonHeaderKeyList = new ArrayList();
            }

            HttpHeaders commonHeaders = this.okHttpUtils.getCommonHeaders();
            if (HttpUtils.isEmpty(commonHeaders)) {
                return (R) this;
            } else {
                List<String> commonHeaderKeyList = commonHeaders.getKeyList();
                if (HttpUtils.isEmpty((Collection) removedCommonHeaderKeyList) && !HttpUtils.isEmpty(commonHeaderKeyList)) {
                    ((List) removedCommonHeaderKeyList).addAll(commonHeaderKeyList);
                }

                if (HttpUtils.isEmpty((Collection) removedCommonHeaderKeyList)) {
                    return (R) this;
                } else {
                    this.removedCommonHeaderKeyList.addAll((Collection) removedCommonHeaderKeyList);
                    Iterator var5 = this.removedCommonHeaderKeyList.iterator();

                    while (var5.hasNext()) {
                        String key = (String) var5.next();
                        this.headers.remove(key);
                    }

                    return (R) this;
                }
            }
        } else {
            return (R) this;
        }
    }

    public String getUrlParam(String key) {
        List<String> values = (List) this.params.stringParamsMap.get(key);
        return values != null && values.size() > 0 ? (String) values.get(0) : null;
    }

    public FileWrapper getFileParam(String key) {
        List<FileWrapper> values = (List) this.params.fileParamsMap.get(key);
        return values != null && values.size() > 0 ? (FileWrapper) values.get(0) : null;
    }

    public boolean isReGenerateRequestBody() {
        return this.isReGenerateRequestBody;
    }

    public boolean isAppendCommonParams() {
        return this.isAppendCommonParams;
    }

    public List<String> getRemovedCommonParamKeyList() {
        return this.removedCommonParamKeyList;
    }

    public boolean isAppendCommonHeaders() {
        return this.isAppendCommonHeaders;
    }

    public List<String> getRemovedCommonHeaderKeyList() {
        return this.removedCommonHeaderKeyList;
    }

    public HttpParams getParams() {
        return this.params;
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public String getUrl() {
        return this.url;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public Object getTag() {
        return this.tag;
    }

    public CacheMode getCacheMode() {
        return this.cacheMode;
    }

    public <T> CachePolicy<T> getCachePolicy() {
        return this.cachePolicy;
    }

    public String getCacheKey() {
        return this.cacheKey;
    }

    public long getCacheTime() {
        return this.cacheTime;
    }

    public int getRetryCount() {
        return this.retryCount;
    }

    public ProxySelector getProxySelector() {
        return this.proxySelector;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public okhttp3.Request getRequest() {
        return this.mRequest;
    }

    public <T> void setCallback(Callback<T> callback) {
        this.callback = callback;
    }

    public <T> Converter<T> getConverter() {
        if (this.converter == null) {
            this.converter = this.callback;
        }

        HttpUtils.checkNotNull(this.converter, "converter == null, do you forget to call Request#converter(Converter<T>) ?");
        return this.converter;
    }

    public abstract HttpMethod getMethod();

    protected abstract RequestBody generateRequestBody();

    public abstract okhttp3.Request generateRequest(RequestBody var1);

    public okhttp3.Call getRawCall() {
        RequestBody requestBody = this.generateRequestBody();
        if (requestBody != null) {
            if (this.isReGenerateRequestBody) {
                ProgressRequestBody progressRequestBody = new ProgressRequestBody(requestBody, this.callback);
                progressRequestBody.setInterceptor(this.uploadInterceptor);
                this.mRequest = this.generateRequest(progressRequestBody);
            } else {
                this.mRequest = this.generateRequest(requestBody);
            }
        } else {
            this.mRequest = this.generateRequest((RequestBody) null);
        }

        if (this.client == null) {
            if (this.readTimeOut <= 0L && this.writeTimeOut <= 0L && this.connectTimeout <= 0L && Utils.isEmpty(this.interceptors) && Utils.isEmpty(this.proxy) && Utils.isEmpty(this.proxySelector) && Utils.isEmpty(this.sslParams) && Utils.isEmpty(this.hostnameVerifier)) {
                this.client = OkHttpUtils.getInstance().getOkHttpClient();
            } else {
                Builder newClientBuilder = OkHttpUtils.getInstance().getOkHttpClient().newBuilder();
                if (this.readTimeOut > 0L) {
                    newClientBuilder.readTimeout(this.readTimeOut, TimeUnit.MILLISECONDS);
                }

                if (this.writeTimeOut > 0L) {
                    newClientBuilder.writeTimeout(this.writeTimeOut, TimeUnit.MILLISECONDS);
                }

                if (this.connectTimeout > 0L) {
                    newClientBuilder.connectTimeout(this.connectTimeout, TimeUnit.MILLISECONDS);
                }

                if (!Utils.isEmpty(this.interceptors)) {
                    Iterator var3 = this.interceptors.iterator();

                    while (var3.hasNext()) {
                        Interceptor interceptor = (Interceptor) var3.next();
                        newClientBuilder.addInterceptor(interceptor);
                    }
                }

                if (!Utils.isEmpty(this.proxy)) {
                    newClientBuilder.proxy(this.proxy);
                }

                if (!Utils.isEmpty(this.proxySelector)) {
                    newClientBuilder.proxySelector(this.proxySelector);
                }

                if (!Utils.isEmpty(this.sslParams)) {
                    newClientBuilder.sslSocketFactory(this.sslParams.sSLSocketFactory, this.sslParams.trustManager);
                }

                if (!Utils.isEmpty(this.hostnameVerifier)) {
                    newClientBuilder.hostnameVerifier(this.hostnameVerifier);
                }

                this.client = newClientBuilder.build();
            }
        }

        return this.client.newCall(this.mRequest);
    }

    public R setReGenerateRequestBody(boolean reGenerateRequestBody) {
        this.isReGenerateRequestBody = reGenerateRequestBody;
        return (R) this;
    }

    public <T> Call<T> adapt() {
        return (Call) (this.call == null ? new CacheCall(this) : this.call);
    }

    public <T, E> E adapt(CallAdapter<T, E> adapter) {
        Call<T> innerCall = this.call;
        if (innerCall == null) {
            innerCall = new CacheCall(this);
        }

        return (E) adapter.adapt((Call) innerCall, (AdapterParam) null);
    }

    public <T, E> E adapt(AdapterParam param, CallAdapter<T, E> adapter) {
        Call<T> innerCall = this.call;
        if (innerCall == null) {
            innerCall = new CacheCall(this);
        }

        return (E) adapter.adapt((Call) innerCall, param);
    }

    public Response execute() throws IOException {
        return this.getRawCall().execute();
    }

    public <T> void execute(Callback<T> callback) {
        HttpUtils.checkNotNull(callback, "callback == null");
        this.callback = callback;
        Call<T> call = this.adapt();
        call.execute(callback);
    }
}
