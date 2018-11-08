package com.shoneworn.libcore.infrastruction._activity_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.shoneworn.libcore.infrastruction.base.AnnotationUtil;
import com.shoneworn.libcore.infrastruction.base.BasePresenter;
import com.shoneworn.libcore.infrastruction.base.BaseView;

/**
 * Created by shoneworn on 2018/11/4.
 */

public class BaseActivity<PresenterType extends BasePresenter> extends FragmentActivity implements BaseView<PresenterType> {

    PresenterType presetner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (presetner == null) {
            presetner = (PresenterType) AnnotationUtil.getAnnotationValue(this.getClass());
            ;
        }
        if (presetner != null) {
            presetner.onCreate(this, savedInstanceState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presetner != null) {
            presetner.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presetner != null) {
            presetner.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presetner != null) {
            presetner.onDestory();
        }
    }


    @Override
    public void setPresenter(PresenterType presenter) {
        this.presetner = presenter;
    }

    @Override
    public PresenterType getPresenter() {
        return presetner;
    }

}

