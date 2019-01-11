package com.shoneworn.libcore.net.okhttputils.callback;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import com.shoneworn.libcore.net.okhttputils.help.ICallbackWrapper;
import com.shoneworn.libcore.net.okhttputils.model.Progress;
import com.shoneworn.libcore.net.okhttputils.model.Response;
import com.shoneworn.libcore.net.okhttputils.request.base.Request;
import com.shoneworn.libcore.net.okhttputils.utils.OkLogger;
import okhttp3.Call;

public abstract class AbsCallbackWrapper<T> extends AbsCallback<T> implements ICallbackWrapper<T> {
    public AbsCallbackWrapper() {
    }

    public void onStart(Request<? extends Request> request) {
        super.onStart(request);
    }

    public void onCacheSuccess(Response<T> response) {
        if(response != null) {
            T body = response.body();
            Call rawCall = response.getRawCall();
            this.onCacheSuccess(body, rawCall);
        }
    }

    public void onSuccess(Response<T> response) {
        if(response != null) {
            T body = response.body();
            Call rawCall = response.getRawCall();
            okhttp3.Response rawResponse = response.getRawResponse();
            this.onSuccess(body, rawCall, rawResponse);
        }
    }

    public void onError(Response<T> response) {
        OkLogger.printStackTrace(response.getException());
        if(response != null) {
            String code = response.code() + "";
            String msg = response.message();
            Call call = response.getRawCall();
            okhttp3.Response rawResponse = response.getRawResponse();
            Exception e = new Exception(response.getException());
            if(response.isFromCache()) {
                this.onCacheError(call, e);
            } else {
                this.onError(code, msg, call, rawResponse, e);
            }

        }
    }

    public void onFinish() {
    }

    public void uploadProgress(Progress progress) {
    }

    public void downloadProgress(Progress progress) {
    }

    public boolean isForceCancel() {
        return false;
    }

    public void onForceCancel() {
    }

    public void onCacheSuccess(T t, Call call) {
    }

    public void onCacheError(Call call, Exception e) {
    }

    public void onCallBackJsonData(okhttp3.Response rawResponse, String jsonData) {
    }
}
