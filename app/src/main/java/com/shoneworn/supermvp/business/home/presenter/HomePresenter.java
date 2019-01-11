package com.shoneworn.supermvp.business.home.presenter;

import android.support.annotation.Nullable;

import com.shoneworn.libcore.common.net.listener.RequestDataListener;
import com.shoneworn.libcore.infrastruction._activity_fragment.PresenterWrapper;
import com.shoneworn.supermvp.business.home.model.HomeModel;
import com.shoneworn.supermvp.business.home.ui.HomeFragment;
import com.shoneworn.supermvp.common.bean.PrettyGirl;
import com.shoneworn.supermvp.common.serverbean.ServerPrettyGirl;

import java.util.List;

import okhttp3.Response;

/**
 * Created by chenxiangxiang on 2018/11/13.
 */

public class HomePresenter extends PresenterWrapper<HomeFragment> {


    public void getPrettyGirlList(){
        HomeModel.getInstance().httpTest(getActivity().hashCode(), 1, new RequestDataListener<ServerPrettyGirl, List<PrettyGirl>>() {
            @Override
            public void onSuccess(List<PrettyGirl> list, ServerPrettyGirl serverPrettyGirl, @Nullable Response var3) {

            }
        });
    }

}
