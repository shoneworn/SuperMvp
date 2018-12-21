package com.shoneworn.supermvp.common.Bean;

import com.shoneworn.libcommon.base_recyclerview_adapter_helper.entity.SectionEntity;

/**
 * Created by chenxiangxiang on 2018/12/17.
 */

public class MySection extends SectionEntity<Type> {


    public MySection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public MySection(Type type) {
        super(type);
    }
}
