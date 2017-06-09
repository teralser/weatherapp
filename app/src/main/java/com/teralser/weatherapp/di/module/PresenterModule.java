package com.teralser.weatherapp.di.module;

import com.teralser.weatherapp.mvp.presenter.DetailsPresenter;
import com.teralser.weatherapp.mvp.presenter.MainPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    @Singleton
    public MainPresenter provideMainPresenter() {
        return new MainPresenter();
    }

    @Provides
    @Singleton
    public DetailsPresenter provideDetailsPresenter() {
        return new DetailsPresenter();
    }
}
