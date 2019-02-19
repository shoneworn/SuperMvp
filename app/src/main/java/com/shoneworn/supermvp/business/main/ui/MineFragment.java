package com.shoneworn.supermvp.business.main.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.shoneworn.libcore.infrastruction.base.PresenterTyper;
import com.shoneworn.supermvp.R;
import com.shoneworn.supermvp.business.main.presenter.MinePresenter;
import com.shoneworn.supermvp.common.base.fragment.UILazyFragment;
import com.shoneworn.supermvp.common.widget.Image.ImageLoader;

import butterknife.BindView;


/**
 * Created by chenxiangxiang on 2018/11/13.
 */

@PresenterTyper(MinePresenter.class)
public class MineFragment extends UILazyFragment<MinePresenter> {
    @BindView(R.id.iv_head)
    protected ImageView ivHead;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        ImageLoader.with(getActivity(), ivHead, "https://avatar.csdn.net/B/D/6/1_shoneworn.jpg?1542348228");
    }

    @Override
    protected void setViewListener() {
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("app://test");
                Intent intent=new Intent();
                intent.setAction("com.cxx.shone");
                intent.setData(uri);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title;
    }

    @Override
    protected boolean statusBarDarkFont() {
        return false;
    }


}
