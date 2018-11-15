package com.shoneworn.supermvp.business.main.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.shoneworn.libcore.infrastruction.base.PresenterTyper;
import com.shoneworn.supermvp.R;
import com.shoneworn.supermvp.business.main.presenter.DiscoveryPresenter;
import com.shoneworn.supermvp.common.base.fragment.UILazyFragment;
import com.shoneworn.supermvp.common.widget.toastcompat.titleview.TitleView;
import com.shoneworn.supermvp.common.widget.toastcompat.utils.PromptUtils;

import butterknife.BindView;

/**
 * Created by chenxiangxiang on 2018/11/13.
 */

@PresenterTyper(DiscoveryPresenter.class)
public class DiscoveryFragment extends UILazyFragment<DiscoveryPresenter> {
    @BindView(R.id.title)
    TitleView titleView;

    public static DiscoveryFragment newInstance() {
        return new DiscoveryFragment();
    }

    @Override
    protected View onCreateView(ViewGroup container, Bundle savedInstanceState) {
        return baseLayoutInflater.inflate(R.layout.fragment_discovery, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initData() {

        PromptUtils.showToast(getPresenter().getActivity(), "frgment2");

    }

    @Override
    public boolean isStatusBarEnabled() {
        return false;
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected int getTitleBarId() {
        return R.id.title;
    }


}
