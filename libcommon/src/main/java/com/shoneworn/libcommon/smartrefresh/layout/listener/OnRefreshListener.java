package com.shoneworn.libcommon.smartrefresh.layout.listener;

import android.support.annotation.NonNull;

import com.shoneworn.libcommon.smartrefresh.layout.api.RefreshLayout;


/**
 * 刷新监听器
 * Created by SCWANG on 2017/5/26.
 */

public interface OnRefreshListener {
    void onRefresh(@NonNull RefreshLayout refreshLayout);
}
