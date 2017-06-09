package com.teralser.weatherapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.teralser.weatherapp.R;
import com.teralser.weatherapp.di.component.ActivityComponent;
import com.teralser.weatherapp.model.Coordinates;
import com.teralser.weatherapp.model.Forecast;
import com.teralser.weatherapp.mvp.presenter.DetailsPresenter;
import com.teralser.weatherapp.mvp.view.DetailView;
import com.teralser.weatherapp.utils.Logger;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class DetailsActivity extends BaseActivity implements DetailView {

    private static final String TAG = DetailsActivity.class.getSimpleName();
    private static final String COORDINATES = "COORDINATES";

    @Inject
    DetailsPresenter detailsPresenter;

    public static Intent createIntent(Context context, Coordinates coordinates) {
        return new Intent(context, DetailsActivity.class).putExtra(COORDINATES, coordinates);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        init();
    }

    @Override
    protected void setUpComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }


    private void init() {
        ButterKnife.bind(this);
        detailsPresenter.init(this);
        detailsPresenter.get5daysForecast(getIntent().getParcelableExtra(COORDINATES));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (detailsPresenter != null)
            detailsPresenter.onDestroy();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setForecast(List<Forecast> forecastList) {
        Logger.logd(TAG, "setForecast: " + forecastList);
    }

    @Override
    public void showError(String error) {
        Logger.loge(TAG, "showError: " + error);
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}
