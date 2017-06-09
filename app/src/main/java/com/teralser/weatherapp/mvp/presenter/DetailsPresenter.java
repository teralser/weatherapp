package com.teralser.weatherapp.mvp.presenter;

import android.text.TextUtils;

import com.teralser.weatherapp.R;
import com.teralser.weatherapp.model.Coordinates;
import com.teralser.weatherapp.model.ForecastListResponse;
import com.teralser.weatherapp.mvp.presenter.impl.IBasePresenter;
import com.teralser.weatherapp.mvp.presenter.impl.IDetailsPresenter;
import com.teralser.weatherapp.mvp.view.DetailView;
import com.teralser.weatherapp.network.WeatherService;
import com.teralser.weatherapp.utils.NetworkUtils;
import com.teralser.weatherapp.utils.RxUtils;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class DetailsPresenter extends BasePresenter implements IDetailsPresenter,
        IBasePresenter<DetailView> {

    @Inject
    WeatherService weatherService;

    private DetailView view;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Inject
    public DetailsPresenter() {
    }


    @Override
    public void init(DetailView view) {
        this.view = view;
        getComponent().inject(this);
    }

    public void get5daysForecast(Coordinates coordinates) {
        if (NetworkUtils.isNetworkAvailable(view.getContext())) {
            Subscription subscription = weatherService.get5daysForecast(coordinates)
                    .compose(RxUtils.applySchedulers())
                    .map(ForecastListResponse::getForecastList)
                    .subscribe(forecasts -> {
                        if (view != null) {
                            view.setForecast(forecasts);
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

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
        view = null;
    }
}
