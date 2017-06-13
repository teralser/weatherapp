package com.teralser.weatherapp.mvp.view;

import android.content.Context;

import com.teralser.weatherapp.model.Forecast;

import java.util.ArrayList;

public interface MainView {
    Context getContext();

    void setLocations(ArrayList<String> locations);

    void setForecast(Forecast forecast);

    void showError(String error);

    void showProgress();

    void dismissProgress();
}
