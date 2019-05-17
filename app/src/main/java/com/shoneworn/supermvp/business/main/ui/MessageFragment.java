package com.shoneworn.supermvp.business.main.ui;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.shoneworn.libcore.infrastruction.base.PresenterTyper;
import com.shoneworn.supermvp.R;
import com.shoneworn.supermvp.business.main.presenter.MessagePresenter;
import com.shoneworn.supermvp.common.base.fragment.UILazyFragment;
import com.shoneworn.supermvp.common.widget.DanceNumberView;

import butterknife.BindView;


/**
 * Created by chenxiangxiang on 2018/11/13.
 */

@PresenterTyper(MessagePresenter.class)
public class MessageFragment extends UILazyFragment<MessagePresenter> {
    @BindView(R.id.ll_container)
    protected LinearLayout llContainer;
    @BindView(R.id.dnView)
    protected DanceNumberView dnView;
    @BindView(R.id.btnStart)
    protected Button btnStart;

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView() {
        dnView.createView();
        dnView.start();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setViewListener() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dnView.start();
            }
        });

    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }


}
