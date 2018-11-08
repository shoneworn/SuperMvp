package com.shoneworn.libcore.infrastruction._activity_fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.shoneworn.libcore.infrastruction.base.BasePresenter;
import com.shoneworn.libcore.infrastruction.base.BaseView;
import com.shoneworn.libcore.infrastruction.base.Presenter;


/**
 * Created by shoneworn on 2018/11/4.
 */

public class PresenterWrapper<ViewType extends BaseView> extends BasePresenter<ViewType> {

    public FragmentActivity getActivity() {
        Activity activity = null;
        if (getView() == null) {
            return (FragmentActivity) activity;
        } else {
            if (getView() instanceof Activity) {
                activity = (Activity) this.getView();
            } else if (getView() instanceof Fragment) {
                activity = ((Fragment) this.getView()).getActivity();
            }
            return (FragmentActivity) activity;
        }
    }


}
