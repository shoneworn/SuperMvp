package com.shoneworn.libcore.infrastruction.base;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;


/**
 * Created by shoneworn on 2018/11/4.
 */

public class BasePresenter<ViewType> implements Presenter<ViewType> {
    private ViewType view;

    public void setView(ViewType view) {
        this.view = view;
    }

    public ViewType getView() {
        return view;
    }

    @Override
    public void onCreate(@NonNull ViewType view, Bundle parentBundle) {
        this.view = view;
    }

    @Override
    public void onDestory() {

    }

    @Override
    public void onCreateView(@NonNull ViewType view) {
        if (this.view == null) {
            this.view = view;
        }
    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSave(Bundle state) {

    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onUserLeaveHint() {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(int level) {

    }

    @Override
    public void onAttachFragment(Fragment fragment) {

    }

    @Override
    public void onResumeFragments() {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onPostResume() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onContentChanged() {

    }

    @Override
    public void onVisibilityChanged(View changedView, int visibility) {

    }

    @Override
    public void onFinishInflate() {

    }

    @Override
    public void onAttachedToWindow() {

    }

    @Override
    public void onDetachedFromWindow() {

    }
}
