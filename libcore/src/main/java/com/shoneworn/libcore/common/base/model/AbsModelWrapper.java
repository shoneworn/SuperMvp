package com.shoneworn.libcore.common.base.model;

import com.shoneworn.libcore.infrastruction.model.AbsModel;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


public abstract class AbsModelWrapper extends AbsModel {
    public AbsModelWrapper() {
    }

    public static AbsModelWrapper getInstance() {
        return (AbsModelWrapper)getInstance(AbsModelWrapper.class);
    }
}