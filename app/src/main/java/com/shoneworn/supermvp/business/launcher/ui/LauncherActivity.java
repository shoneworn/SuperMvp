package com.shoneworn.supermvp.business.launcher.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import com.gyf.barlibrary.BarHide;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.shoneworn.libcore.infrastruction.base.PresenterTyper;
import com.shoneworn.supermvp.business.main.ui.MainActivity;
import com.shoneworn.supermvp.R;
import com.shoneworn.supermvp.business.launcher.presenter.LauncherPresenter;
import com.shoneworn.supermvp.common.base._activity.CommonActivity;

import java.util.List;

import butterknife.BindView;

/**
 * Created by chenxiangxiang on 2018/11/12.
 */
@PresenterTyper(LauncherPresenter.class)
public class LauncherActivity extends CommonActivity<LauncherPresenter>
        implements OnPermission, Animation.AnimationListener {

    @BindView(R.id.iv_launcher_bg)
    ImageView mImageView;
    @BindView(R.id.iv_launcher_icon)
    ImageView mIconView;
    @BindView(R.id.iv_launcher_name)
    TextView mTextView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void setViewListener() {

    }

    protected void initView() {
        //初始化动画
        initStartAnim();
        //设置状态栏和导航栏参数
        getStatusBarConfig()
                .fullScreen(true)//有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                .hideBar(BarHide.FLAG_HIDE_STATUS_BAR)//隐藏状态栏
                .transparentNavigationBar()//透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                .init();
    }


    private static final int ANIM_TIME = 1000;

    /**
     * 启动动画
     */
    private void initStartAnim() {
        // 渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.4f, 1.0f);
        aa.setDuration(ANIM_TIME * 2);
        aa.setAnimationListener(this);
        mImageView.startAnimation(aa);

        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(ANIM_TIME);
        mIconView.startAnimation(sa);

        RotateAnimation ra = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(ANIM_TIME);
        mTextView.startAnimation(ra);
    }

    private void requestFilePermission() {
        XXPermissions.with(this)
                .permission(Permission.Group.STORAGE)
                .request(this);
    }

    /**
     * {@link OnPermission}
     */

    @Override
    public void hasPermission(List<String> granted, boolean isAll) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        //禁用返回键
        //super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (XXPermissions.isHasPermission(LauncherActivity.this, Permission.Group.STORAGE)) {
            hasPermission(null, true);
        } else {
            requestFilePermission();
        }
    }

    @Override
    public boolean isSupportSwipeBack() {
        //不使用侧滑功能
        return !super.isSupportSwipeBack();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void noPermission(List<String> denied, boolean quick) {
        if (quick) {
            XXPermissions.gotoPermissionSettings(LauncherActivity.this, true);
        } else {
            getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestFilePermission();
                }
            }, 2000);
        }
    }

    /**
     * {@link Animation.AnimationListener}
     */

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        requestFilePermission();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }


}
