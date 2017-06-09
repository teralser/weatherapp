package com.teralser.weatherapp.mvp.view;

import android.content.Context;

import com.teralser.weatherapp.model.Forecast;

import java.util.List;

public interface DetailView {
    Context getContext();

    void setForecast(List<Forecast> forecastList);

    void showError(String error);

}
