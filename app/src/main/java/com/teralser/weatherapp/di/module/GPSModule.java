package com.teralser.weatherapp.di.module;

import android.content.Context;

import com.teralser.weatherapp.manager.GPSManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GPSModule {

    @Provides
    @Singleton
    public GPSManager provideGPSManager(Context context) {
        return new GPSManager(context);
    }
}
