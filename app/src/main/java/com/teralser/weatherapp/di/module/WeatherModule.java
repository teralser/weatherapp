package com.teralser.weatherapp.di.module;

import com.teralser.weatherapp.network.WeatherApi;
import com.teralser.weatherapp.network.WeatherService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ApiModule.class})
public class WeatherModule {
    @Provides
    @Singleton
    public WeatherService provideWeatherService(WeatherApi weatherApi) {
        return new WeatherService(weatherApi);
    }
}
