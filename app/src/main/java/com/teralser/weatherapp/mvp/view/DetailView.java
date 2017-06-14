package com.teralser.weatherapp.mvp.view;

import com.teralser.weatherapp.model.Forecast;

import java.util.List;

public interface DetailView {

    void setForecast(List<Forecast> forecastList);

    void showError(String error);

}
