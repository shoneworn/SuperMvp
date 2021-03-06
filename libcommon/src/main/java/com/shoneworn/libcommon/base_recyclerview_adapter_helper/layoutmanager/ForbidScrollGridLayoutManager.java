package com.shoneworn.libcommon.base_recyclerview_adapter_helper.layoutmanager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

public class ForbidScrollGridLayoutManager extends GridLayoutManager {
    private boolean isScrollEnabled = true;

    public ForbidScrollGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ForbidScrollGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public ForbidScrollGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}
