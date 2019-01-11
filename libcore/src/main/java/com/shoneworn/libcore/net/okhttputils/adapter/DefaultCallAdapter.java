package com.shoneworn.libcore.net.okhttputils.adapter;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


public class DefaultCallAdapter<T> implements CallAdapter<T, Call<T>> {
    public DefaultCallAdapter() {
    }

    public Call<T> adapt(Call<T> call, AdapterParam param) {
        return call;
    }
}
