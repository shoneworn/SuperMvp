package com.shoneworn.libcore.net.okhttputils.exception;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import com.shoneworn.libcore.net.okhttputils.model.Response;
import com.shoneworn.libcore.net.okhttputils.utils.HttpUtils;

public class HttpException extends RuntimeException {
    private static final long serialVersionUID = 8773734741709178425L;
    private int code;
    private String message;
    private transient Response<?> response;

    public HttpException(String message) {
        super(message);
    }

    public HttpException(Response<?> response) {
        super(getMessage(response));
        this.code = response.code();
        this.message = response.message();
        this.response = response;
    }

    public HttpException(int code, String msg) {
        super("HTTP RESPONSE " + code + " " + msg);
        this.code = code;
        this.message = msg;
        this.response = null;
    }

    private static String getMessage(Response<?> response) {
        HttpUtils.checkNotNull(response, "response == null");
        return "HTTP " + response.code() + " " + response.message();
    }

    public int code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public Response<?> response() {
        return this.response;
    }

    public static HttpException NET_ERROR() {
        return new HttpException("network error! http response code is 404 or 5xx!");
    }

    public static HttpException COMMON(String message) {
        return new HttpException(message);
    }
}
