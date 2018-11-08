package com.shoneworn.libcore.infrastruction.base;

/**
 * Created by shoneworn on 2018/11/4.
 */

public interface BaseView<PresenterType extends Presenter> {

    void setPresenter(PresenterType presenter);

    PresenterType getPresenter();
}
