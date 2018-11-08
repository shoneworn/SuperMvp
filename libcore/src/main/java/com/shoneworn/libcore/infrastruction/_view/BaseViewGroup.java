package com.shoneworn.libcore.infrastruction._view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shoneworn.libcore.infrastruction.base.AnnotationUtil;
import com.shoneworn.libcore.infrastruction.base.BaseView;

/**
 * Created by chenxiangxiang on 2018/11/5.
 */

public abstract class BaseViewGroup<PresenterType extends PresenterWrapper> extends ViewGroup implements BaseView<PresenterType> {

    private PresenterType presenter;
    protected Context ctx;
    protected LayoutInflater layoutInflater;

    public BaseViewGroup(Context context) {
        this(context, (AttributeSet) null);
    }

    public BaseViewGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.beamLayout(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.beamLayout(context, attrs, defStyleAttr, defStyleRes);
    }

    private final void beamLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (this.presenter == null) {
            presenter = (PresenterType) AnnotationUtil.getAnnotationValue(this.getClass());
        }
        if (presenter != null) {
            presenter.onCreateView(this);
        }
        this.ctx = context;
        presenter.onConstructCalled(context, attrs, defStyleAttr, defStyleRes);
        onConstructorCalled();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (presenter != null)
            presenter.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (presenter != null)
            presenter.onDetachedFromWindow();
    }

    @NonNull
    protected final <E extends View> E $(@NonNull View view, @IdRes int id) {
        return view.findViewById(id);
    }

    @NonNull
    protected final <E extends View> E viewId(@NonNull View view, @IdRes int id) {
        return view.findViewById(id);
    }

    @NonNull
    protected final <E extends View> E $(@IdRes int id) {
        return this.findViewById(id);
    }

    @NonNull
    protected final <E extends View> E viewId(@IdRes int id) {
        return this.findViewById(id);
    }


    public Activity getActivity() {
        return getActivityFromView(this.getContext());
    }

    private Activity getActivityFromView(Context ctx) {
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

    @Override
    public void setPresenter(PresenterType presenter) {
        this.presenter = presenter;
    }

    @Override
    public PresenterType getPresenter() {
        return presenter;
    }

    protected void onConstructorCalled() {
        this.layoutInflater = LayoutInflater.from(ctx);
        init();
    }

    protected void init() {
        initView();
        initData();
        setViewListener();
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void setViewListener();
}
