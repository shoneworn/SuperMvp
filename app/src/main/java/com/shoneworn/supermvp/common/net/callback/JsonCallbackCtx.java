package com.shoneworn.supermvp.common.net.callback;

import com.google.gson.Gson;
import com.shoneworn.libcore.common.net.callback.JsonCallbackCore;
import com.shoneworn.libcore.net.okhttputils.model.HttpParams;
import com.shoneworn.libcore.net.okhttputils.request.base.Request;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */
public abstract class JsonCallbackCtx<T> extends JsonCallbackCore<T> {

    public static final String TAG = JsonCallbackCtx.class.getSimpleName() + "\n";

    private LinkedHashMap<String, String> partRequestParamsMap = new LinkedHashMap<String, String>();

    @Override
    public void onForceCancel() {
        onError("", "", null, null, null);
    }

    @Override
    protected void appendRequestParamsIfNeed(Request<? extends Request> request, HttpParams params) {
        super.appendRequestParamsIfNeed(request, params);
    }

    @Override
    protected void onCallBackJsonInner(Response response, String responseData) {
        super.onCallBackJsonInner(response, responseData);
    }

    @Override
    public T fromJson(String str, Class<T> clazz) {
        Gson jsonParseImpl = new Gson();
        if (jsonParseImpl == null) {
            return null;
        }
        return jsonParseImpl.fromJson(str, clazz);
    }

    @Override
    public T fromJson(String str, Type type) {
        Gson jsonParseImpl = new Gson();
        if (jsonParseImpl == null) {
            return null;
        }
        return jsonParseImpl.fromJson(str, type);
    }

    @Override
    public void onError(String code, String msg, Call call, Response response, Exception e) {
    }
}
