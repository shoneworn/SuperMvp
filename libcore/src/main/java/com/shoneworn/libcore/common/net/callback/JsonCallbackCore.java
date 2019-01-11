package com.shoneworn.libcore.common.net.callback;

import android.content.Context;

import com.shoneworn.libcore.common.net.help.CommonCallback;
import com.shoneworn.libcore.net.okhttputils.OkHttpUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */
public abstract class JsonCallbackCore<T> extends CommonCallback<T> {
    public JsonCallbackCore(Context ctx) {
        super(ctx);
    }

    public JsonCallbackCore() {
        super(OkHttpUtils.getInstance().getContext());
    }

    public Type getResponseType() {
        Type genType = this.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        return params[0];
    }

    public Class<T> getResponseClass() {
        return null;
    }
}
