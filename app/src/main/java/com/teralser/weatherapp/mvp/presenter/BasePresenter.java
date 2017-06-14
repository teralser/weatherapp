package com.teralser.weatherapp.mvp.presenter;

import android.content.Context;

import com.teralser.weatherapp.WeatherApp;
import com.teralser.weatherapp.di.component.DaggerPresenterComponent;
import com.teralser.weatherapp.di.component.PresenterComponent;
import com.teralser.weatherapp.di.module.ApplicationModule;

public abstract class BasePresenter {

    protected Context appContext;

    public BasePresenter(Context context) {
        appContext = context;
        setUpComponent(DaggerPresenterComponent.builder()
                .applicationModule(new ApplicationModule((WeatherApp) context.getApplicationContext()))
                .build());
    }

    protected abstract void setUpComponent(PresenterComponent presenterComponent);

    public void onDestroy() {
        appContext = null;
    }
}
