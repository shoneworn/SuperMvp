package com.shoneworn.libcore.net.okhttputils.callback;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import com.shoneworn.libcore.net.okhttputils.model.Progress;
import com.shoneworn.libcore.net.okhttputils.model.Response;
import com.shoneworn.libcore.net.okhttputils.request.base.Request;
import com.shoneworn.libcore.net.okhttputils.utils.OkLogger;

public abstract class AbsCallback<T> implements Callback<T> {
    public AbsCallback() {
    }

    public void onStart(Request<? extends Request> request) {
    }

    public void onCacheSuccess(Response<T> response) {
    }

    public void onError(Response<T> response) {
        OkLogger.printStackTrace(response.getException());
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
}
