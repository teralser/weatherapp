package com.teralser.weatherapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.teralser.weatherapp.R;
import com.teralser.weatherapp.adapter.LocationsAdapter;
import com.teralser.weatherapp.di.component.ActivityComponent;
import com.teralser.weatherapp.manager.AnimationManager;
import com.teralser.weatherapp.model.Forecast;
import com.teralser.weatherapp.model.Weather;
import com.teralser.weatherapp.mvp.presenter.MainPresenter;
import com.teralser.weatherapp.mvp.view.MainView;
import com.teralser.weatherapp.other.Constants;
import com.teralser.weatherapp.utils.Logger;
import com.teralser.weatherapp.view.NDSpinner;
import com.vlad1m1r.lemniscate.BernoullisProgressView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainView {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    MainPresenter mainPresenter;

    @Inject
    AnimationManager animationManager;

    @BindView(R.id.spinner)
    NDSpinner spinner;

    @BindView(R.id.progressBar)
    BernoullisProgressView progressBar;

    @BindView(R.id.mainInfo)
    View mainInfo;

    @BindView(R.id.temp)
    TextView temp;

    @BindView(R.id.main)
    TextView main;

    @BindView(R.id.descr)
    TextView descr;

    @BindView(R.id.tempInfo)
    View tempInfo;

    @BindView(R.id.weatherIcon)
    ImageView weatherIcon;

    @BindView(R.id.minTemp)
    TextView minTemp;

    @BindView(R.id.maxTemp)
    TextView maxTemp;

    @BindView(R.id.arrowDetails)
    View arrowDetails;

    @BindString(R.string.select_location)
    String hint;

    @BindString(R.string.temp)
    String tempPattern;

    @BindString(R.string.min_temp)
    String minTempPattern;

    @BindString(R.string.max_temp)
    String maxTempPattern;

    private LocationsAdapter adapter;
    private boolean initComplete;
    private int previousSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainPresenter.init(this);
    }

    @Override
    protected void setUpComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainPresenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mainPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mainPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @OnClick(R.id.weatherDataContainer)
    public void openDetails() {
        startActivity(DetailsActivity.createIntent(this,
                mainPresenter.getSelectedLocationCoordinates(spinner.getSelectedItemPosition() - 1)));
    }

    @Override
    public void setLocations(ArrayList<String> locations) {
        Logger.logd(TAG, "setLocations: " + locations);

        if (adapter == null) {
            initComplete = false;
            adapter = LocationsAdapter.init(this, R.layout.item_spinner,
                    R.layout.item_spinner_dropdown, locations, hint);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Logger.logd(TAG, "setOnItemSelected: " + i);
                    if (initComplete) {
                        previousSelected = i;
                        mainPresenter.onLocationClicked(i - 1);
                    } else {
                        if (previousSelected != 0) {
                            spinner.setSelection(previousSelected);
                            previousSelected = 0;
                        }
                        initComplete = true;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Logger.logd(TAG, "onNothingSelected");
                }
            });
        } else {
            adapter.updateList(locations);
        }
    }

    @Override
    public void setForecast(Forecast forecast) {
        Logger.logd(TAG, "setForecast: " + forecast);

        if (spinner.getSelectedItemPosition() != 0) {
            removePreviousInfo();
            if (forecast != null) {
                new Handler().postDelayed(() -> runOnUiThread(() -> showSelectedInfo(forecast)), 450);
            }
        } else if (forecast != null) {
            showSelectedInfo(forecast);
        }
    }

    private void removePreviousInfo() {
        animationManager.buildAnimationBaseOut(mainInfo).translationY(-50f).start();
        animationManager.buildAnimationBaseOut(tempInfo).translationY(50f).start();
        animationManager.buildAnimationBaseOut(arrowDetails).translationX(50f).start();
    }

    private void showSelectedInfo(Forecast forecast) {
        Weather weather = null;
        if (forecast.getWeatherList() != null &&
                !forecast.getWeatherList().isEmpty()) {
            weather = forecast.getWeatherList().get(0);
        }

        // main information block
        temp.setText(String.format(tempPattern, Math.round(forecast.getMain().getTemp())));
        main.setText(weather != null ? weather.getMain() : "");
        descr.setText(weather != null ? weather.getDescription() : "");
        animationManager.buildAnimationBaseIn(mainInfo).start();

        // temperature information block
        if (weather != null) {
            Glide.with(this)
                    .load(String.format(Constants.IMAGE_URL, weather.getIcon()))
                    .into(weatherIcon);
        } else {
            weatherIcon.setImageDrawable(null);
        }
        minTemp.setText(String.format(minTempPattern, forecast.getMain().getTempMin()));
        maxTemp.setText(String.format(maxTempPattern, forecast.getMain().getTempMax()));
        animationManager.buildAnimationBaseIn(tempInfo).start();

        // arrow
        animationManager.buildAnimationBaseIn(arrowDetails).start();
    }

    @Override
    public void showError(String error) {
        Logger.loge(TAG, "showError: " + error);
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
