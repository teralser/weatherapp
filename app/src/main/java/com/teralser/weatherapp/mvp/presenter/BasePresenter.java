package com.teralser.weatherapp.mvp.presenter;

import com.teralser.weatherapp.di.component.DaggerPresenterComponent;
import com.teralser.weatherapp.di.component.PresenterComponent;

public abstract class BasePresenter {

    private PresenterComponent presenterComponent;

    public BasePresenter() {
        presenterComponent = DaggerPresenterComponent.builder().build();
    }

    protected PresenterComponent getComponent() {
        return presenterComponent;
    }

    public abstract void onDestroy();
}
