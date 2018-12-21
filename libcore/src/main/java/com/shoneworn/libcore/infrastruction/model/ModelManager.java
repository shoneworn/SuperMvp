package com.shoneworn.libcore.infrastruction.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Created by chenxiangxiang on 2018/11/19.
 */

public class ModelManager {
    private static final HashMap<Class<?>, AbsModel> mModelMap = new HashMap();
    private static final BackThread mBackThread = new BackThread();
    private static Context mApplication;

    public ModelManager() {
    }

    public static void init(Context ctx) {
        mBackThread.start();
        mApplication = ctx;
        Class[] models = null;

        try {
            ApplicationInfo appInfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            if(appInfo.metaData == null || appInfo.metaData.getString("MODEL") == null || appInfo.metaData.getString("MODEL").isEmpty()) {
                return;
            }

            String modelStr = appInfo.metaData.getString("MODEL").trim();
            String[] modelStrs = modelStr.split(",");
            models = new Class[modelStrs.length];

            for(int i = 0; i < modelStrs.length; ++i) {
                try {
                    models[i] = Class.forName(modelStrs[i].trim());
                } catch (ClassNotFoundException var7) {
                    Log.e("Beam", modelStrs[i].trim() + " Class No Found!");
                }
            }
        } catch (PackageManager.NameNotFoundException var8) {
            return;
        }

        Class[] var9 = models;
        int var11 = models.length;

        for(int var13 = 0; var13 < var11; ++var13) {
            Class m = var9[var13];
            createModel(m);
        }

        Iterator var10 = mModelMap.values().iterator();

        while(var10.hasNext()) {
            AbsModel absModel = (AbsModel)var10.next();
            launchModel(absModel);
        }

    }

    private static <T extends AbsModel> T createModel(Class<T> clazz) {
        if(clazz != null && AbsModel.class.isAssignableFrom(clazz)) {
            try {
                AbsModel instance = (AbsModel)clazz.newInstance();
                mModelMap.put(clazz, instance);
                return (T)instance;
            } catch (InstantiationException var2) {
                var2.printStackTrace();
            } catch (IllegalAccessException var3) {
                var3.printStackTrace();
            }

            throw new IllegalArgumentException("your model must extends AbsModel");
        } else {
            throw new IllegalArgumentException("your model must extends AbsModel");
        }
    }

    private static void launchModel(@NonNull final AbsModel model) {
        model.onAppCreate(mApplication);
        mBackThread.execute(new Runnable() {
            public void run() {
                model.onAppCreateOnBackThread(ModelManager.mApplication);
            }
        });
    }

    public static <T extends AbsModel> T getInstance(Class<T> clazz) {
        if(mModelMap.get(clazz) == null) {
            synchronized(clazz) {
                launchModel(createModel(clazz));
            }
        }

        return (T)mModelMap.get(clazz);
    }

    static void runOnBackThread(Runnable runnable) {
        mBackThread.execute(runnable);
    }
}
