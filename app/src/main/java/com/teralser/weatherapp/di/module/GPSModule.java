package com.teralser.weatherapp.di.module;

import com.teralser.weatherapp.gps.GPSManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GPSModule {

    @Provides
    @Singleton
    public GPSManager provideGPSManager() {
        return new GPSManager();
    }
}
