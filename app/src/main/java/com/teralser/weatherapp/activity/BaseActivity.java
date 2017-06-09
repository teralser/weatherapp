package com.teralser.weatherapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.teralser.weatherapp.WeatherApp;
import com.teralser.weatherapp.di.component.ActivityComponent;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpComponent(((WeatherApp) getApplication()).getComponent());
    }

    protected abstract void setUpComponent(ActivityComponent activityComponent);
}
