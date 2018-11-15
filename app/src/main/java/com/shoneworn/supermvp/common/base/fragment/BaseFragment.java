package com.shoneworn.supermvp.common.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.shoneworn.libcore.infrastruction._activity_fragment.BeamFragment;
import com.shoneworn.libcore.infrastruction.base.BeamPresenter;

/**
 * Created by chenxiangxiang on 2018/11/13.
 */

public abstract class BaseFragment<PresenterType extends BeamPresenter> extends BeamFragment<PresenterType> {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    protected void init() {
        initViews(contentView);
        initData();
        setViewListener();
    }

    protected abstract void initViews(View view);

    /**
     * 初始化数据
     */
    protected abstract void initData();

    protected abstract void setViewListener();

}
