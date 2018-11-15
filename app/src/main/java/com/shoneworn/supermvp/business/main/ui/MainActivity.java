package com.shoneworn.supermvp.business.main.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.shoneworn.libcore.infrastruction.base.PresenterTyper;
import com.shoneworn.supermvp.business.main.adapter.HomeViewPagerAdapter;
import com.shoneworn.supermvp.business.main.presenter.MainPresenter;
import com.shoneworn.supermvp.R;
import com.shoneworn.supermvp.common.base._activity.CommonActivity;
import com.shoneworn.supermvp.common.widget.toastcompat.utils.PromptUtils;
import com.shoneworn.supermvp.uitls.OnClickUtils;

import butterknife.BindView;

/**
 * 看看布局方式，这么布局，可以避免软键盘弹起后，把底部导航栏给顶起来
 */
@PresenterTyper(MainPresenter.class)
public class MainActivity extends CommonActivity<MainPresenter> implements  ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.bv_home_navigation)
    BottomNavigationView nvbottom;
    @BindView(R.id.vp_main)
    ViewPager vpMain;

    private HomeViewPagerAdapter mAdapter;
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
        vpMain.addOnPageChangeListener(this);

        // 不使用图标默认变色
        nvbottom.setItemIconTintList(null);
        nvbottom.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mAdapter = new HomeViewPagerAdapter(this);
        vpMain.setAdapter(mAdapter);

        // 限制页面数量
        vpMain.setOffscreenPageLimit(mAdapter.getCount());
    }

    @Override
    protected void setViewListener() {

    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                nvbottom.setSelectedItemId(R.id.menu_home);
                break;
            case 1:
                nvbottom.setSelectedItemId(R.id.home_found);
                break;
            case 2:
                nvbottom.setSelectedItemId(R.id.home_message);
                break;
            case 3:
                nvbottom.setSelectedItemId(R.id.home_me);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                vpMain.setCurrentItem(0);
                return true;
            case R.id.home_found:
                vpMain.setCurrentItem(1);
                return true;
            case R.id.home_message:
                vpMain.setCurrentItem(2);
                return true;
            case R.id.home_me:
                vpMain.setCurrentItem(3);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (OnClickUtils.isOnDoubleClick()) {
            //移动到上一个任务栈，避免侧滑引起的不良反应
            moveTaskToBack(false);
            getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //销毁掉当前界面
                    finish();
                }
            }, 300);
        } else {
            PromptUtils.showToast(this,getResources().getString(R.string.home_exit_hint));
        }
    }

    @Override
    protected void onDestroy() {
        vpMain.removeOnPageChangeListener(this);
        vpMain.setAdapter(null);
        nvbottom.setOnNavigationItemSelectedListener(null);
        super.onDestroy();
    }

    @Override
    public boolean isSupportSwipeBack() {
        // 不使用侧滑功能
        return !super.isSupportSwipeBack();
    }

}
