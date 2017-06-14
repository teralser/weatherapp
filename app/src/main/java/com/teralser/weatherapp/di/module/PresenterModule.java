package com.teralser.weatherapp.di.module;

import android.content.Context;

import com.teralser.weatherapp.mvp.presenter.DetailsPresenter;
import com.teralser.weatherapp.mvp.presenter.MainPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    @Singleton
    public MainPresenter provideMainPresenter(Context context) {
        return new MainPresenter(context);
    }

    @Provides
    @Singleton
    public DetailsPresenter provideDetailsPresenter(Context context) {
        return new DetailsPresenter(context);
    }
}
