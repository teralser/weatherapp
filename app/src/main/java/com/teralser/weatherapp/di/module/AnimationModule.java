package com.teralser.weatherapp.di.module;

import com.teralser.weatherapp.manager.AnimationManager;
import com.teralser.weatherapp.manager.GPSManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AnimationModule {

    @Provides
    @Singleton
    public AnimationManager provideAnimationManager() {
        return new AnimationManager();
    }
}
