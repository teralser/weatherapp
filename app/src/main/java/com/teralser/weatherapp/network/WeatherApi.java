package com.teralser.weatherapp.network;

import com.teralser.weatherapp.model.Forecast;
import com.teralser.weatherapp.model.ForecastListResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface WeatherApi {

    String APP_ID = "ca92432c0345f8f494509fb5c97298fe";

    @GET("/data/2.5/weather?appid=" + APP_ID + "&units=metric")
    Observable<Forecast> getCurrentForecast(@Query("lat") double lat,
                                            @Query("lon") double lon);

    @GET("/data/2.5/forecast?appid=" + APP_ID + "&units=metric")
    Observable<ForecastListResponse> get5daysForecast(@Query("lat") double lat,
                                                      @Query("lon") double lon);
}