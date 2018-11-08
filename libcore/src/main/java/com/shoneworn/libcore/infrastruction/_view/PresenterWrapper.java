package com.shoneworn.libcore.infrastruction._view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.view.View;

import com.shoneworn.libcore.infrastruction.base.BasePresenter;

/**
 * Created by chenxiangxiang on 2018/11/5.
 */

public class PresenterWrapper<ViewType extends View> extends BasePresenter<ViewType> {

    protected Context ctx;
    private Activity activity = null;

    protected void onConstructCalled(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.ctx = context;
    }

    public Context getCtx() {
        return ctx;
    }

    public Activity getActivity() {
        if (activity != null) return activity;

        if (ctx != null) {
            activity = getActivityFromView(ctx);
        }

        if (activity == null) {
            activity = getActivityFromView((View) this.getView());
        }
        return activity;
    }

    private static Activity getActivityFromView(View view) {
        return view == null ? null : getActivityFromView(view.getContext());
    }

    private static Activity getActivityFromView(Context ctx) {
        if (ctx == null) {
            return null;
        } else {
            while (ctx instanceof ContextWrapper) {
                if (ctx instanceof Activity) {
                    return (Activity) ctx;
                }

                ctx = ((ContextWrapper) ctx).getBaseContext();
            }

            return null;
        }
    }

}
