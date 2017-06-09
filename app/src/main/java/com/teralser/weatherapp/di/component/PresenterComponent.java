package com.teralser.weatherapp.di.component;

import com.teralser.weatherapp.di.module.GPSModule;
import com.teralser.weatherapp.di.module.WeatherModule;
import com.teralser.weatherapp.mvp.presenter.DetailsPresenter;
import com.teralser.weatherapp.mvp.presenter.MainPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {GPSModule.class, WeatherModule.class})
public interface PresenterComponent {

    void inject(MainPresenter presenter);

    void inject(DetailsPresenter presenter);
}
