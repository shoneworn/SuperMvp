package com.shoneworn.supermvp;

import android.content.Context;
import android.widget.TextView;

import com.shoneworn.libcore.infrastruction._view.BeamLinearLayout;
import com.shoneworn.libcore.infrastruction.base.PresenterTyper;


/**
 * Created by chenxiangxiang on 2018/11/5.
 */

@PresenterTyper(DefineLinearPresenter.class)
public class DefineLinearLayout extends BeamLinearLayout<DefineLinearPresenter> {
    TextView tv;

    public DefineLinearLayout(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        layoutInflater.inflate(R.layout.liear_define,this);
        tv = $(R.id.tv);
        getPresenter().showText();
    }

    public void setText(){
        tv.setText("我时陈祥祥");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setViewListener() {

    }
}
