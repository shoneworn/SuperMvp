package com.shoneworn.supermvp;


import android.widget.Toast;

import com.shoneworn.libcore.infrastruction._view.PresenterWrapper;

/**
 * Created by chenxiangxiang on 2018/11/5.
 */

public class DefineLinearPresenter extends PresenterWrapper<DefineLinearLayout> {

    public void showText(){
        getView().setText();
        Toast.makeText(ctx, "ctx", Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "getActivity", Toast.LENGTH_SHORT).show();
    }

}
