package com.shoneworn.supermvp.common.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.shoneworn.libcore.infrastruction._activity_fragment.PresenterWrapper;
import com.shoneworn.supermvp.common.base._activity.BaseActivity;
import com.shoneworn.supermvp.uitls.LogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chenxiangxiang on 2018/11/13.
 */

public abstract class UILazyFragment<PresenterType extends PresenterWrapper> extends BaseLazyFragment<PresenterType> {


    private ImmersionBar mImmersionBar;//状态栏沉浸
    private Unbinder mButterKnife;// View注解

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //初始化沉浸式状态栏
        if (isVisibleToUser() && isStatusBarEnabled() && isLazyLoad()) {
            statusBarConfig().init();
        }

        //设置标题栏
        if (getTitleBarId() > 0) {
            ImmersionBar.setTitleBar(ctx, contentView.findViewById(getTitleBarId()));
        }

        mButterKnife = ButterKnife.bind(this, view);
    }

    /**
     * 是否在Fragment使用沉浸式
     */
    public boolean isStatusBarEnabled() {
        return false;
    }

    /**
     * 获取状态栏沉浸的配置对象
     */
    protected ImmersionBar getStatusBarConfig() {
        return mImmersionBar;
    }

    /**
     * 初始化沉浸式
     */
    private ImmersionBar statusBarConfig() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(getActivity())
                .statusBarDarkFont(statusBarDarkFont())    //默认状态栏字体颜色为黑色
                .keyboardEnable(true);  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
        return mImmersionBar;
    }

    /**
     * 获取状态栏字体颜色
     */
    protected boolean statusBarDarkFont() {
        //返回true表示黑色字体
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null) mImmersionBar.destroy();
        if (mButterKnife != null)
            mButterKnife.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isStatusBarEnabled() && isLazyLoad()) {
            // 重新初始化状态栏
            statusBarConfig().init();
        }
        if(isVisibleToUser){
            setStatusBarDarkFont(statusBarDarkFont());
        }
    }


    /**
     * 私有方法，只针对本框架，你要是不嫌麻烦，可以一个个fragment去设置。
     * 声明：不是继承自BaseActivity的activity ，都将不会生效
     */
    private void setStatusBarDarkFont(boolean darkFont) {
        try {
            BaseActivity activity = (BaseActivity) getActivity();
            activity.getStatusBarConfig().statusBarDarkFont(darkFont).init();
        } catch (Exception e) {
            LogUtils.d("使用了野路子activity");
        }
    }
}
