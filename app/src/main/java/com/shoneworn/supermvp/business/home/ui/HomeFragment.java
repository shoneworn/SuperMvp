package com.shoneworn.supermvp.business.home.ui;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.shoneworn.libcommon.widget.SmartTabLayout;
import com.shoneworn.libcore.infrastruction.base.PresenterTyper;
import com.shoneworn.supermvp.R;
import com.shoneworn.supermvp.business.home.adapter.TabFragmentPagerAdapter;
import com.shoneworn.supermvp.business.home.presenter.HomePresenter;
import com.shoneworn.supermvp.business.home.presenter.Htab1Presenter;
import com.shoneworn.supermvp.common.base.fragment.UILazyFragment;
import com.shoneworn.supermvp.common.widget.titleview.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by chenxiangxiang on 2018/11/13.
 */

@PresenterTyper(HomePresenter.class)
public class HomeFragment extends UILazyFragment<HomePresenter> {
    @BindView(R.id.title)
    protected TitleView titleView;
    @BindView(R.id.stl_home)
    protected SmartTabLayout smartTabLayout;
    @BindView(R.id.vp_home)
    protected ViewPager viewPager;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        getPresenter().getPrettyGirlList();
    }

    @Override
    protected void initData() {
        List<Fragment> list = new ArrayList<>();
        list.add(Htab1Fragment.newInstance());
        list.add(Htab1Fragment.newInstance());
        list.add(Htab1Fragment.newInstance());
        List<String> listTitle = new ArrayList<>();
        listTitle.add("音乐");
        listTitle.add("视频");
        listTitle.add("电台");
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getFragmentManager(), list, listTitle);
        viewPager.setAdapter(adapter);
        smartTabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void setViewListener() {
    }

    @Override
    public boolean isStatusBarEnabled() {
        return false;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title;
    }


}
