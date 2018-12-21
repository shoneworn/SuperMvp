package com.shoneworn.libcommon.base_recyclerview_adapter_helper.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private SpacesItemDecorationDelegate mDelegate;
    private int mColor;
    private int leftRight;
    private int topBottom;

    private int topBottomEx;

    public SpacesItemDecoration(int leftRight, int topBottom) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
    }

    public SpacesItemDecoration(int leftRight, int topBottom, int mColor) {
        this(leftRight, topBottom);
        this.mColor = mColor;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDelegate == null) {
            mDelegate = getDelegate(parent.getLayoutManager());
        }
        mDelegate.onDraw(c, parent, state);
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDelegate == null) {
            mDelegate = getDelegate(parent.getLayoutManager());
        }
        mDelegate.getItemOffsets(outRect, view, parent, state);
    }

    public void reset() {
        mDelegate = null;
    }

    private SpacesItemDecorationDelegate getDelegate(RecyclerView.LayoutManager manager) {
        SpacesItemDecorationDelegate delegate = null;
        //要注意这边的GridLayoutManager是继承LinearLayoutManager，所以要先判断GridLayoutManager
        if (manager instanceof GridLayoutManager) {
            delegate = new GridDelegate(leftRight, topBottom, mColor);
        } else if (manager instanceof StaggeredGridLayoutManager) {
            delegate = new StaggeredGridDelegate(leftRight, topBottom, mColor);
        } else {//其他的都当做Linear来进行计算
            delegate = new LinearDelegate(leftRight, topBottom, mColor);
        }
        delegate.setTopBottomEx(topBottomEx);
        return delegate;
    }


    public void setTopBottomEx(int topBottomEx) {
        this.topBottomEx = topBottomEx;
    }
}