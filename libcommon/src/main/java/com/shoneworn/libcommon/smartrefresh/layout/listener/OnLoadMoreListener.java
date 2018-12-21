package com.shoneworn.libcommon.smartrefresh.layout.listener;

import android.support.annotation.NonNull;

import com.shoneworn.libcommon.smartrefresh.layout.api.RefreshLayout;


/**
 * 加载更多监听器
 * Created by SCWANG on 2017/5/26.
 */

public interface OnLoadMoreListener {
    void onLoadMore(@NonNull RefreshLayout refreshLayout);
}
