package com.teralser.weatherapp.network;

import com.teralser.weatherapp.model.Coordinates;
import com.teralser.weatherapp.model.Forecast;
import com.teralser.weatherapp.model.ForecastListResponse;

import rx.Observable;

public class WeatherService {

    private WeatherApi weatherApi;

    public WeatherService(WeatherApi weatherApi) {
        this.weatherApi = weatherApi;
    }

    public Observable<Forecast> getCurrentForecast(Coordinates coordinates) {
        return weatherApi.getCurrentForecast(coordinates.getLat(), coordinates.getLon());
    }

    public Observable<ForecastListResponse> get5daysForecast(Coordinates coordinates){
        return weatherApi.get5daysForecast(coordinates.getLat(), coordinates.getLon());
    }

}
