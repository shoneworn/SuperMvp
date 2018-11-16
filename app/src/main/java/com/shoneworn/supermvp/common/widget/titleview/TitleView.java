package com.shoneworn.supermvp.common.widget.titleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shoneworn.libcore.infrastruction._view.BeamLinearLayout;
import com.shoneworn.libcore.infrastruction.base.PresenterTyper;
import com.shoneworn.supermvp.R;

/**
 * Created by chenxiangxiang on 2018/11/12.
 */

@PresenterTyper(TitlePresenter.class)
public class TitleView extends BeamLinearLayout<TitlePresenter> {
    private LinearLayout llRoot;
    private LinearLayout llLeft;
    private LinearLayout llRight;
    private ImageView ivLeft;
    private ImageView ivRight;
    private TextView tvLeft;
    private TextView tvRight;
    private TextView tvTitle;

    private int mTitleLeftResId;
    private int mTitleRightResId;
    private int mTitleTextColor;
    private int mTitleLeftTextColor;
    private int mTitleRightTextColor;
    private String mTitleText;
    private String mTitleLeftText;
    private String mTitleRightText;

    public TitleView(Context context) {
        this(context,null);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }



    public void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleView, defStyleAttr, 0);
        mTitleLeftResId =  a.getResourceId(R.styleable.TitleView_titleLeftDrawable, NO_ID);
        mTitleRightResId =  a.getResourceId(R.styleable.TitleView_titleRightDrawable, NO_ID);
        mTitleText = a.getString(R.styleable.TitleView_titleText);
        mTitleLeftText = a.getString(R.styleable.TitleView_titleLeftText);
        mTitleRightText = a.getString(R.styleable.TitleView_titleRightText);
        mTitleTextColor = a.getColor(R.styleable.TitleView_titleTextColor,Color.BLACK);
        mTitleLeftTextColor = a.getColor(R.styleable.TitleView_titleLeftTextColor,Color.BLACK);
        mTitleRightTextColor = a.getColor(R.styleable.TitleView_titleRightTextColor,Color.BLACK);
        a.recycle();
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_title;
    }

    @Override
    protected void initView() {

        llRoot = findViewById(R.id.ll_title_root);
        llLeft = findViewById(R.id.ll_title_left);
        llRight = findViewById(R.id.ll_title_right);
        ivLeft = findViewById(R.id.iv_title_left);
        ivRight = findViewById(R.id.iv_title_right);
        tvLeft = findViewById(R.id.tv_title_left);
        tvRight = findViewById(R.id.tv_title_right);
        tvTitle = findViewById(R.id.tv_title);

        if(mTitleLeftResId!=NO_ID){
            ivLeft.setImageResource(mTitleLeftResId);
        }
        if(mTitleRightResId!=NO_ID){
            ivRight.setImageResource(mTitleLeftResId);
        }
        if(!TextUtils.isEmpty(mTitleText)){
            tvTitle.setText(mTitleText);
        }
        if(!TextUtils.isEmpty(mTitleLeftText)){
            tvLeft.setText(mTitleLeftText);
        }
        if(!TextUtils.isEmpty(mTitleRightText)){
            tvRight.setText(mTitleRightText);
        }
        tvTitle.setTextColor(mTitleTextColor);
        tvLeft.setTextColor(mTitleLeftTextColor);
        tvRight.setTextColor(mTitleRightTextColor);
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

    public void setTitleText(@NonNull String title) {
        if (title != null)
            tvTitle.setText(title);
    }

    public void setTitleTextColor(@NonNull int color){
        if(tvTitle!=null){
            tvTitle.setTextColor(color);
        }
    }

    public void setLeftIcon(@DrawableRes int resId) {
        if(ivLeft==null) return;
        ivLeft.setVisibility(VISIBLE);
        ivLeft.setImageResource(resId);
    }

    public void setLeftIconVisiable(boolean visiable){
        if(visiable){
            ivLeft.setVisibility(VISIBLE);
        }else{
            ivLeft.setVisibility(GONE);
        }
    }

    public void setRightIcon(@DrawableRes int resId) {
        if(ivRight==null) return;
        ivRight.setVisibility(VISIBLE);
        ivRight.setImageResource(resId);
    }

    public void setRightIconVisiable(boolean visiable){
        if(visiable){
            ivRight.setVisibility(VISIBLE);
        }else{
            ivRight.setVisibility(GONE);
        }
    }


    public void setTvRightText(String text) {
        if (text != null) {
            tvRight.setText(text);
        }
    }

    public void setTvRightTextColor(int color) {
        if (tvRight != null) {
            tvRight.setTextColor(color);
        }
    }

    public void setTvLeftText(String text) {
        if (text != null) {
            tvLeft.setText(text);
        }
    }

    public void setTvLeftTextColor(int color) {
        if (tvLeft != null) {
            tvLeft.setTextColor(color);
        }
    }

    public void setBgAlpha(float alpha) {
        llRoot.setAlpha(alpha);
    }
}
