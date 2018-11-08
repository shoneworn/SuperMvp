package com.shoneworn.myframework;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.shoneworn.libcore.infrastruction._activity_fragment.PresenterWrapper;


/**
 * Created by chenxiangxiang on 2018/11/5.
 */

public class HomePresenter extends PresenterWrapper<HomeFragment> {

    @Override
    public void onCreate(@NonNull HomeFragment view, Bundle parentBundle) {
        super.onCreate(view, parentBundle);
    }

    public void showText(){
        getView().setText();
    }
}
