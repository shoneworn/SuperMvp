package com.shoneworn.libcore.net.okhttputils.model;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import okhttp3.Call;
import okhttp3.Headers;

public final class Response<T> {
    private T body;
    private Throwable throwable;
    private boolean isFromCache;
    private Call rawCall;
    private okhttp3.Response rawResponse;

    public static <T> Response<T> success(boolean isFromCache, T body, Call rawCall, okhttp3.Response rawResponse) {
        Response<T> response = new Response();
        response.setFromCache(isFromCache);
        response.setBody(body);
        response.setRawCall(rawCall);
        response.setRawResponse(rawResponse);
        return response;
    }

    public static <T> Response<T> error(boolean isFromCache, Call rawCall, okhttp3.Response rawResponse, Throwable throwable) {
        Response<T> response = new Response();
        response.setFromCache(isFromCache);
        response.setRawCall(rawCall);
        response.setRawResponse(rawResponse);
        response.setException(throwable);
        return response;
    }

    public Response() {
    }

    public int code() {
        return this.rawResponse == null?-1:this.rawResponse.code();
    }

    public String message() {
        return this.rawResponse == null?null:this.rawResponse.message();
    }

    public Headers headers() {
        return this.rawResponse == null?null:this.rawResponse.headers();
    }

    public boolean isSuccessful() {
        return this.throwable == null;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public T body() {
        return this.body;
    }

    public Throwable getException() {
        return this.throwable;
    }

    public void setException(Throwable exception) {
        this.throwable = exception;
    }

    public Call getRawCall() {
        return this.rawCall;
    }

    public void setRawCall(Call rawCall) {
        this.rawCall = rawCall;
    }

    public okhttp3.Response getRawResponse() {
        return this.rawResponse;
    }

    public void setRawResponse(okhttp3.Response rawResponse) {
        this.rawResponse = rawResponse;
    }

    public boolean isFromCache() {
        return this.isFromCache;
    }

    public void setFromCache(boolean fromCache) {
        this.isFromCache = fromCache;
    }
}
