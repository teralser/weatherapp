package com.teralser.weatherapp;

import android.app.Application;

import com.teralser.weatherapp.di.component.ActivityComponent;
import com.teralser.weatherapp.di.component.DaggerActivityComponent;

public class WeatherApp extends Application {

    private ActivityComponent activityComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        activityComponent = DaggerActivityComponent.builder().build();
    }

    public ActivityComponent getComponent() {
        return activityComponent;
    }
}
