package com.teralser.weatherapp.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.teralser.weatherapp.R;
import com.teralser.weatherapp.di.component.PresenterComponent;
import com.teralser.weatherapp.manager.GPSManager;
import com.teralser.weatherapp.model.Coordinates;
import com.teralser.weatherapp.model.LocationItem;
import com.teralser.weatherapp.mvp.presenter.impl.IBasePresenter;
import com.teralser.weatherapp.mvp.presenter.impl.IMainPresenter;
import com.teralser.weatherapp.mvp.view.MainView;
import com.teralser.weatherapp.network.WeatherService;
import com.teralser.weatherapp.utils.NetworkUtils;
import com.teralser.weatherapp.utils.RxUtils;

import java.util.ArrayList;

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
    public MainPresenter(Context context) {
        super(context);
    }

    @Override
    protected void setUpComponent(PresenterComponent presenterComponent) {
        presenterComponent.inject(this);
    }

    @Override
    public void init(MainView view) {
        this.view = view;
        gpsManager.init(view.getActivity(), this);
        updateLocationList();
    }

    private void updateLocationList() {
        ArrayList<LocationItem> locationItems = gpsManager.getLocations();
        ArrayList<String> locationNames = new ArrayList<>();
        for (LocationItem location : locationItems) {
            locationNames.add(location.getName());
        }

        view.setLocations(locationNames);
    }

    public void onLocationClicked(int position) {
        view.showProgress();

        if (position == 0) {
            getWeather(null);
        } else {
            getWeather(gpsManager.getLocations().get(position).getCoordinates());
        }
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
        if (NetworkUtils.isNetworkAvailable(appContext)) {
            Subscription subscription = weatherService.getCurrentForecast(coordinates)
                    .compose(RxUtils.applySchedulers())
                    .subscribe(forecast -> {
                        if (view != null) {
                            view.setForecast(forecast);
                            view.dismissProgress();
                        }
                    }, this::sentError);

            compositeSubscription.add(subscription);
        } else if (view != null) {
            view.dismissProgress();
            view.showError(appContext.getString(R.string.no_internet_connection));
        }
    }

    private void sentError(Throwable throwable) {
        if (view != null) {
            view.dismissProgress();
            String internalError = appContext.getString(R.string.internal_error);
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
        super.onDestroy();
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
        Coordinates myCoordinates = Coordinates.fromLocation(location);
        updateLocationList();
        getCurrentForecast(myCoordinates);
    }

    @Override
    public void locationAccessGranted(boolean isGranted) {
        if (view != null) {
            if (!isGranted) {
                view.dismissProgress();
                view.showError(appContext.getString(R.string.location_turn_on_error));
                view.setForecast(null);
            }
        }
    }

    public LocationItem getSelectedLocation(int position) {
        return gpsManager.getLocations().get(position);
    }

}