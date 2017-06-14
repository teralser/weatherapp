package com.teralser.weatherapp.di.module;

import android.content.Context;

import com.teralser.weatherapp.WeatherApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final WeatherApp app;

    public ApplicationModule(WeatherApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Context providesContext() {
        return app;
    }

}
