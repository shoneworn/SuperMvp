package com.shoneworn.supermvp.business.main.ui;

import com.shoneworn.libcore.infrastruction.base.PresenterTyper;
import com.shoneworn.supermvp.R;
import com.shoneworn.supermvp.business.main.presenter.MessagePresenter;
import com.shoneworn.supermvp.common.base.fragment.UILazyFragment;


/**
 * Created by chenxiangxiang on 2018/11/13.
 */

@PresenterTyper(MessagePresenter.class)
public class MessageFragment extends UILazyFragment<MessagePresenter
        > {

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }


}
