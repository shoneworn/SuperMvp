package com.shoneworn.supermvp;

import android.app.Application;

import com.shoneworn.libcore.net.okhttputils.OkHttpUtils;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created by chenxiangxiang on 2018/11/12.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BGASwipeBackHelper.init(this, null);
        OkHttpUtils.getInstance().init(this);
    }
}
