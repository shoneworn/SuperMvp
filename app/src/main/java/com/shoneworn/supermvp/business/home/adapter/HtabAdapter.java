package com.shoneworn.supermvp.business.home.adapter;

import com.shoneworn.libcommon.base_recyclerview_adapter_helper.BaseSectionQuickAdapter;
import com.shoneworn.libcommon.base_recyclerview_adapter_helper.BaseViewHolder;
import com.shoneworn.supermvp.R;
import com.shoneworn.supermvp.common.Bean.MySection;


import java.util.List;

/**
 * Created by chenxiangxiang on 2018/12/17.
 */

public class HtabAdapter extends BaseSectionQuickAdapter<MySection,BaseViewHolder> {


    public HtabAdapter(int layoutResId, int sectionHeadResId, List<MySection> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, MySection item) {
        helper.setText(R.id.header, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, MySection item) {
        helper.setText(R.id.tv_desc,item.t.getCreateTime());
    }
}
