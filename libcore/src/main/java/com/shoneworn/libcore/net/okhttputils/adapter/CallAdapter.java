package com.shoneworn.libcore.net.okhttputils.adapter;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

public interface CallAdapter<T, R> {
    R adapt(Call<T> var1, AdapterParam var2);
}
