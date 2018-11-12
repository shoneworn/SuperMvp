package com.shoneworn.supermvp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shoneworn.libcore.infrastruction.base.PresenterTyper;
import com.shoneworn.libcore.infrastruction._activity_fragment.BeamFragment;

/**
 * Created by chenxiangxiang on 2018/11/5.
 */
@PresenterTyper(HomePresenter.class)
public class HomeFragment extends BeamFragment<HomePresenter> {
    TextView tv;

    @Override
    protected View onCreateView(ViewGroup container, Bundle savedInstanceState) {
        return baseLayoutInflater.inflate(R.layout.fragment_home,null);
    }

    @Override
    protected void initViews(View view) {
        tv = view.findViewById(R.id.tv);
        getPresenter().showText();
    }

    public void setText(){
        tv.setText("我在fragment");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void setViewListener() {

    }
}
