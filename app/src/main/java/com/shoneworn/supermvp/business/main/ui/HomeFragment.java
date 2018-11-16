package com.shoneworn.supermvp.business.main.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;


import com.shoneworn.libcore.infrastruction.base.PresenterTyper;
import com.shoneworn.supermvp.R;
import com.shoneworn.supermvp.business.main.presenter.HomePresenter;
import com.shoneworn.supermvp.common.base.fragment.UILazyFragment;


/**
 * Created by chenxiangxiang on 2018/11/13.
 */

@PresenterTyper(HomePresenter.class)
public class HomeFragment extends UILazyFragment<HomePresenter> {

    public static HomeFragment newInstance(){
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }


}
