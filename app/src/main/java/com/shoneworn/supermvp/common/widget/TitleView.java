package com.shoneworn.supermvp.common.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shoneworn.libcore.infrastruction._view.BeamLinearLayout;
import com.shoneworn.supermvp.R;

/**
 * Created by chenxiangxiang on 2018/11/12.
 */

public class TitleView extends BeamLinearLayout {
    private LinearLayout llRoot;
    private LinearLayout llLeft;
    private LinearLayout llRight;
    private ImageView ivLeft;
    private ImageView ivRight;
    private TextView tvLeft;
    private TextView tvRight;
    private TextView tvTitle;

    public TitleView(Context context) {
        super(context);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(21)
    public TitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initView() {
        layoutInflater.inflate(R.layout.view_title, this);
        llRoot = findViewById(R.id.ll_title_root);
        llLeft = findViewById(R.id.ll_title_left);
        llRight = findViewById(R.id.ll_title_right);
        ivLeft = findViewById(R.id.iv_title_left);
        ivRight = findViewById(R.id.iv_title_right);
        tvLeft = findViewById(R.id.tv_title_left);
        tvRight = findViewById(R.id.tv_title_right);
        tvTitle = findViewById(R.id.tv_title);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setViewListener() {

    }


    public void setLeftOnclickListener(OnClickListener clickListener) {
        if (clickListener != null&&llLeft!=null)
            llLeft.setOnClickListener(clickListener);
    }

    public void setTvRightOnclickListener(OnClickListener clickListener) {
        if (clickListener != null&&llRight!=null)
            llRight.setOnClickListener(clickListener);
    }

    public void setTitle(String title) {
        if (title != null)
            tvTitle.setText(title);
    }

    public void setLeftIcon(@DrawableRes int resId) {
        ivLeft.setImageResource(resId);
    }

    public void setRightIcon(@DrawableRes int resId) {
        ivRight.setImageResource(resId);
    }


    public void setTvRightText(String text) {
        if (text != null) {
            tvRight.setText(text);
        }
    }

    public void setTvLeftText(String text) {
        if (text != null) {
            tvLeft.setText(text);
        }
    }

    public void setBgAlpha(float alpha) {
        llRoot.setAlpha(alpha);
    }
}
