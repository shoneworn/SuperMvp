package com.shoneworn.supermvp.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.shoneworn.libcore.infrastruction._activity_fragment.PresenterWrapper;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chenxiangxiang on 2018/11/12.
 */

public abstract class CommonActivity<T extends PresenterWrapper> extends BaseActivity<T> {
    private Unbinder mButterKnife;//View注解

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();

        if (!(layoutId == View.NO_ID || layoutId == 0)) {
            setContentView(getLayoutId());
        }
        mButterKnife = ButterKnife.bind(this);
        initView(savedInstanceState);
        initData(savedInstanceState);
        setViewListener();
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    protected abstract int getLayoutId();


    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void setViewListener();

    @Override
    public boolean statusBarDarkFont() {
        //返回true表示黑色字体
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mButterKnife != null) mButterKnife.unbind();
    }
}
