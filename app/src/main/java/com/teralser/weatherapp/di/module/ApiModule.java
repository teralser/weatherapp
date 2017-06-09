package com.teralser.weatherapp.di.module;

import com.teralser.weatherapp.network.WeatherApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module(includes = {RetrofitModule.class})
public class ApiModule {

    @Provides
    @Singleton
    public WeatherApi provideWeatherService(Retrofit retrofit) {
        return retrofit.create(WeatherApi.class);
    }
}
