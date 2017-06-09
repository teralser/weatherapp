package com.teralser.weatherapp.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.teralser.weatherapp.R;
import com.teralser.weatherapp.gps.GPSManager;
import com.teralser.weatherapp.model.Coordinates;
import com.teralser.weatherapp.mvp.presenter.impl.IBasePresenter;
import com.teralser.weatherapp.mvp.presenter.impl.IMainPresenter;
import com.teralser.weatherapp.mvp.view.MainView;
import com.teralser.weatherapp.network.WeatherService;
import com.teralser.weatherapp.utils.NetworkUtils;
import com.teralser.weatherapp.utils.RxUtils;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class MainPresenter extends BasePresenter implements IMainPresenter,
        IBasePresenter<MainView>, GPSManager.LocationListener {

    @Inject
    WeatherService weatherService;

    @Inject
    GPSManager gpsManager;

    private MainView view;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Inject
    public MainPresenter() {
    }

    @Override
    public void init(MainView view) {
        this.view = view;
        getComponent().inject(this);
        gpsManager.init((Activity) view.getContext(), this);
        getWeather(null);
    }

    /**
     * Pass null to start searching current location coordinates
     */
    public void getWeather(Coordinates coordinates) {
        if (coordinates != null) {
            getCurrentForecast(coordinates);
        } else {
            // wait for getting current location
            gpsManager.askCurrentLocation();
        }
    }

    private void getCurrentForecast(Coordinates coordinates) {
        if (NetworkUtils.isNetworkAvailable(view.getContext())) {
            Subscription subscription = weatherService.getCurrentForecast(coordinates)
                    .compose(RxUtils.applySchedulers())
                    .subscribe(forecast -> {
                        if (view != null) {
                            view.setForecast(forecast);
                        }
                    }, this::sentError);

            compositeSubscription.add(subscription);
        } else if (view != null) {
            view.showError(view.getContext().getString(R.string.no_internet_connection));
        }
    }

    private void sentError(Throwable throwable) {
        if (view != null) {
            String internalError = view.getContext().getString(R.string.internal_error);
            String error = throwable == null ? internalError : TextUtils.isEmpty(throwable.getMessage()) ?
                    internalError : throwable.getMessage();
            view.showError(error);
        }
    }

    public void onResume() {
        gpsManager.onResume();
    }

    public void onPause() {
        gpsManager.onPause();
    }

    @Override
    public void onDestroy() {
        gpsManager.onDestroy();
        compositeSubscription.clear();
        view = null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        gpsManager.onActivityResult(requestCode, resultCode, data);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        gpsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void locationObtained(@NonNull Location location) {
        getCurrentForecast(Coordinates.fromLocation(location));
    }

    @Override
    public void locationAccessGranted(boolean isGranted) {
        if (view != null) view.onLocationStatusGranted(isGranted);
    }

}
