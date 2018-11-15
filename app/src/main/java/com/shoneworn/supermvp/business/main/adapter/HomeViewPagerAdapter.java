package com.shoneworn.supermvp.business.main.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


import com.shoneworn.supermvp.business.main.ui.DiscoveryFragment;
import com.shoneworn.supermvp.business.main.ui.HomeFragment;
import com.shoneworn.supermvp.common.base.adapter.BaseFragmentPagerAdapter;
import com.shoneworn.supermvp.common.base.fragment.UILazyFragment;

import java.util.List;

/**
 *    author : HJQ
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 主页界面 ViewPager + Fragment 适配器
 */
public final class HomeViewPagerAdapter extends BaseFragmentPagerAdapter<UILazyFragment> {

    public HomeViewPagerAdapter(FragmentActivity activity) {
        super(activity);
    }

    @Override
    protected void init(FragmentManager fm, List<UILazyFragment> list) {
        list.add(HomeFragment.newInstance());
        list.add(DiscoveryFragment.newInstance());
        list.add(HomeFragment.newInstance());
        list.add(DiscoveryFragment.newInstance());
    }
}