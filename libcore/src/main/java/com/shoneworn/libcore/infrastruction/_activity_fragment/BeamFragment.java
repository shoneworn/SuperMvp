package com.shoneworn.libcore.infrastruction._activity_fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shoneworn.libcore.infrastruction.base.AnnotationUtil;
import com.shoneworn.libcore.infrastruction.base.BeamPresenter;
import com.shoneworn.libcore.infrastruction.base.BaseView;


/**
 * Created by chenxiangxiang on 2018/11/5.
 */

public abstract class BeamFragment<PresenterType extends BeamPresenter> extends Fragment implements BaseView<PresenterType> {

    PresenterType presenter;

    protected Activity ctx;
    protected LayoutInflater baseLayoutInflater;
    protected View contentView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (presenter == null) {
            presenter = (PresenterType) AnnotationUtil.getAnnotationValue(this.getClass());
        }
        if (presenter != null) {
            presenter.onCreate(this, savedInstanceState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ctx = getActivity();
        Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), getThemeResId());
        baseLayoutInflater = inflater.cloneInContext(contextThemeWrapper);
        if (presenter != null) {
            presenter.onCreateView(this);
        }
        if (contentView == null && getLayoutId() > 0) {
            contentView = inflater.inflate(getLayoutId(), null);
        }

        ViewGroup parent = (ViewGroup) contentView.getParent();
        if (parent != null) {
            parent.removeView(contentView);
        }

        return contentView;

    }


    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.onResume();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestory();
        }
        ctx = null;
        contentView = null;
        baseLayoutInflater = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.onDestroyView();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (presenter != null) {
            presenter.onAttachedToWindow();
        }
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (presenter != null) {
            presenter.onAttachFragment(childFragment);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (presenter != null) {
            presenter.onDetachedFromWindow();
        }
    }


    protected abstract int getLayoutId();


    private void _setTheme() {
        int themeResId = getThemeResId();
        if (themeResId == View.NO_ID || themeResId == 0) {
            return;
        }
        ctx.setTheme(themeResId);
    }

    public int getThemeResId() {
        return View.NO_ID;
    }


    @Override
    public void setPresenter(PresenterType presenter) {
        this.presenter = presenter;
    }

    @Override
    public PresenterType getPresenter() {
        return presenter;
    }

}
