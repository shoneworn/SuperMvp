package com.shoneworn.supermvp;

import android.widget.Toast;

import com.shoneworn.libcore.infrastruction._activity_fragment.PresenterWrapper;
import com.shoneworn.supermvp.business.home.MainActivity;

/**
 * Created by chenxiangxiang on 2018/11/5.
 */

public class MainPresenter extends PresenterWrapper<MainActivity> {

    @Override
    public void onResume() {
        super.onResume();
        getView().showToast();

    }

    public void showToast(){
        Toast.makeText(getActivity(), "在presenter里", Toast.LENGTH_SHORT).show();
    }
}
