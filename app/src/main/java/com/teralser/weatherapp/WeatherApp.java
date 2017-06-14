package com.teralser.weatherapp;

import android.app.Application;

import com.teralser.weatherapp.di.component.ActivityComponent;
import com.teralser.weatherapp.di.component.DaggerActivityComponent;
import com.teralser.weatherapp.di.module.ApplicationModule;

public class WeatherApp extends Application {

    private ActivityComponent activityComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        activityComponent = DaggerActivityComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}
