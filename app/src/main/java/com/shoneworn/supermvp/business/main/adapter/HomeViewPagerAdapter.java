package com.shoneworn.supermvp.business.main.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


import com.shoneworn.supermvp.business.main.ui.DiscoveryFragment;
import com.shoneworn.supermvp.business.home.ui.HomeFragment;
import com.shoneworn.supermvp.business.main.ui.MessageFragment;
import com.shoneworn.supermvp.business.main.ui.MineFragment;
import com.shoneworn.supermvp.common.base.adapter.BaseFragmentPagerAdapter;
import com.shoneworn.supermvp.common.base.fragment.UILazyFragment;

import java.util.List;

public final class HomeViewPagerAdapter extends BaseFragmentPagerAdapter<UILazyFragment> {

    public HomeViewPagerAdapter(FragmentActivity activity) {
        super(activity);
    }

    @Override
    protected void init(FragmentManager fm, List<UILazyFragment> list) {
        list.add(HomeFragment.newInstance());
        list.add(DiscoveryFragment.newInstance());
        list.add(MessageFragment.newInstance());
        list.add(MineFragment.newInstance());
    }
}