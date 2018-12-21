package com.shoneworn.supermvp.common.base.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.shoneworn.libcore.infrastruction._activity_fragment.BeamFragment;
import com.shoneworn.libcore.infrastruction._activity_fragment.PresenterWrapper;

import java.lang.reflect.Field;

/**
 * Created by chenxiangxiang on 2018/11/13.
 */

public abstract class BaseLazyFragment<T extends PresenterWrapper> extends BeamFragment<T> {

    private boolean isLazyLoad = false;//是否已经懒加载
    private boolean isVisibleToUser = false;

    protected boolean isLazyLoad() {
        return isLazyLoad;
    }

    /**
     * 是否在Fragment使用沉浸式
     */
    protected boolean isStatusBarEnabled() {
        return false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && !isLazyLoad() && getView() != null) {
            isLazyLoad = true;
            init();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isVisibleToUser && !isLazyLoad() && getView() != null) {
            isLazyLoad = true;
            init();
        }
    }

    protected void init() {
        initView();
        initData();
        setViewListener();
    }

    public boolean isVisibleToUser() {
        return isVisibleToUser;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //解决java.lang.IllegalStateException: Activity has been destroyed 的错误
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    protected abstract void initView();
    /**
     * 初始化数据
     */
    protected abstract void initData();

    protected abstract void setViewListener();

    //标题栏id
    protected abstract int getTitleBarId();
}
