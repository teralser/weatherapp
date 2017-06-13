package com.teralser.weatherapp.di.component;

import com.teralser.weatherapp.activity.DetailsActivity;
import com.teralser.weatherapp.activity.MainActivity;
import com.teralser.weatherapp.di.module.AnimationModule;
import com.teralser.weatherapp.di.module.PresenterModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {PresenterModule.class, AnimationModule.class})
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(DetailsActivity activity);

}