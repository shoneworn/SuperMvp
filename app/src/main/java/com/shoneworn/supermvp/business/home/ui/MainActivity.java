package com.shoneworn.supermvp.business.home.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.shoneworn.libcore.infrastruction.base.PresenterTyper;
import com.shoneworn.supermvp.business.home.presenter.MainPresenter;
import com.shoneworn.supermvp.R;
import com.shoneworn.supermvp.common.CommonActivity;

import butterknife.BindView;

@PresenterTyper(MainPresenter.class)
public class MainActivity extends CommonActivity<MainPresenter> implements  BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.bv_home_navigation)
    BottomNavigationView mBottomNavigationView;

    @Override
    protected int getTitleBarId() {
        return super.getTitleBarId();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        // 不使用图标默认变色
        mBottomNavigationView.setItemIconTintList(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:

                return true;
            case R.id.home_found:

                return true;
            case R.id.home_message:

                return true;
            case R.id.home_me:

                return true;
        }
        return false;
    }



}
