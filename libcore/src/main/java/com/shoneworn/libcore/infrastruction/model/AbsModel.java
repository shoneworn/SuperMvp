package com.shoneworn.libcore.infrastruction.model;

import android.content.Context;

/**
 * Created by chenxiangxiang on 2018/11/19.
 */

public abstract class AbsModel {
    public AbsModel() {
    }

    public static final <T extends AbsModel> T getInstance(Class<T> clazz) {
        return ModelManager.getInstance(clazz);
    }

    protected void onAppCreate(Context ctx) {
    }

    protected void onAppCreateOnBackThread(Context ctx) {
    }

    protected final void runOnBackThread(Runnable runnable) {
        ModelManager.runOnBackThread(runnable);
    }
}
