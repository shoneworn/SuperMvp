package com.shoneworn.libcore.infrastruction._activity_fragment;


import android.app.Activity;
import android.support.v4.app.Fragment;
import com.shoneworn.libcore.infrastruction.base.BeamPresenter;
import com.shoneworn.libcore.infrastruction.base.BaseView;


/**
 * Created by shoneworn on 2018/11/4.
 */

public class PresenterWrapper<ViewType extends BaseView> extends BeamPresenter<ViewType> {

    public Activity getActivity() {
        Activity activity = null;
        if (getView() == null) {
            return  activity;
        } else {
            if (getView() instanceof Activity) {
                activity = (Activity) this.getView();
            } else if (getView() instanceof Fragment) {
                activity = ((Fragment) this.getView()).getActivity();
            }
            return  activity;
        }
    }


}
