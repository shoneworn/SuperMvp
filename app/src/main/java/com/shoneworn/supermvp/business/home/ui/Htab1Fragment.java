package com.shoneworn.supermvp.business.home.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shoneworn.libcommon.base_recyclerview_adapter_helper.decoration.SpacesItemDecoration;
import com.shoneworn.libcore.infrastruction.base.PresenterTyper;
import com.shoneworn.supermvp.R;
import com.shoneworn.supermvp.business.home.presenter.Htab1Presenter;
import com.shoneworn.supermvp.business.home.adapter.HtabAdapter;
import com.shoneworn.supermvp.common.bean.MySection;
import com.shoneworn.supermvp.common.base.fragment.UILazyFragment;
import com.shoneworn.supermvp.uitls.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by chenxiangxiang on 2018/12/20.
 */
@PresenterTyper(Htab1Presenter.class)
public class Htab1Fragment extends UILazyFragment<Htab1Presenter> {
    @BindView(R.id.rv_htab)
    protected RecyclerView recyclerView;
    private List<MySection> list = new ArrayList<>();

    public static Htab1Fragment newInstance() {
        return new Htab1Fragment();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_htab;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        list = getPresenter().createData();
        HtabAdapter sectionAdapter = new HtabAdapter(R.layout.item_section_content, R.layout.item_section_head, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        SpacesItemDecoration spaceItemDecoration = new SpacesItemDecoration(DensityUtil.dp2px(6), DensityUtil.dp2px(6));
        recyclerView.addItemDecoration(spaceItemDecoration);
        recyclerView.setAdapter(sectionAdapter);
    }
}
