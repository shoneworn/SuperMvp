package com.shoneworn.libcore.net.okhttputils.model;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */
public final class Result<T> {
    private final Response<T> response;
    private final Throwable error;

    public static <T> Result<T> error(Throwable error) {
        if(error == null) {
            throw new NullPointerException("error == null");
        } else {
            return new Result((Response)null, error);
        }
    }

    public static <T> Result<T> response(Response<T> response) {
        if(response == null) {
            throw new NullPointerException("response == null");
        } else {
            return new Result(response, (Throwable)null);
        }
    }

    private Result(Response<T> response, Throwable error) {
        this.response = response;
        this.error = error;
    }

    public Response<T> response() {
        return this.response;
    }

    public Throwable error() {
        return this.error;
    }

    public boolean isError() {
        return this.error != null;
    }

    public String toString() {
        return this.error != null?"Result{isError=true, error=\"" + this.error + "\"}":"Result{isError=false, response=" + this.response + '}';
    }
}
