package com.teralser.weatherapp.network;

import com.teralser.weatherapp.model.Forecast;
import com.teralser.weatherapp.model.ForecastListResponse;
import com.teralser.weatherapp.other.Constants;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface WeatherApi {

    @GET("/data/2.5/weather?appid=" + Constants.APP_ID + "&units=metric")
    Observable<Forecast> getCurrentForecast(@Query("lat") double lat,
                                            @Query("lon") double lon);

    @GET("/data/2.5/forecast?appid=" + Constants.APP_ID + "&units=metric")
    Observable<ForecastListResponse> get5daysForecast(@Query("lat") double lat,
                                                      @Query("lon") double lon);
}