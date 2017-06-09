package com.teralser.weatherapp.mvp.view;

import android.content.Context;

import com.teralser.weatherapp.model.Coordinates;
import com.teralser.weatherapp.model.Forecast;

public interface MainView {
    Context getContext();

    void openDetails(Coordinates coordinates);

    void setForecast(Forecast forecast);

    void showError(String error);

    void onLocationStatusGranted(boolean isGranted);
}
