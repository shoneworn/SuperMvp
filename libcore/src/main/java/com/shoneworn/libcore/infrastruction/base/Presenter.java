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

public interface Presenter<ViewType> {

    void onCreate(@NonNull ViewType view, Bundle parentBundle);

    void onDestory();

    void onCreateView(@NonNull ViewType view);

    void onDestroyView();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSave(Bundle state);

    void onResult(int requestCode, int resultCode, Intent data);

    void onRestart();

    void onRestoreInstanceState(Bundle savedInstanceState);

    void onUserLeaveHint();

    void onNewIntent(Intent intent);

    void onLowMemory();

    void onTrimMemory(int level);

    void onAttachFragment(Fragment fragment);

    void onResumeFragments();

    void onBackPressed();

    void onPostResume();

    void onConfigurationChanged(Configuration newConfig);

    void onContentChanged();

    void onVisibilityChanged(View changedView, int visibility);

    void onFinishInflate();

    void onAttachedToWindow();

    void onDetachedFromWindow();


}
