package com.teralser.weatherapp.activity;

import android.app.Activity;
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

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.sunrise)
    TextView sunrise;

    @BindView(R.id.sunset)
    TextView sunset;

    @BindView(R.id.weatherIcon)
    ImageView weatherIcon;

    @BindView(R.id.averageTemp)
    TextView averageTemp;

    @BindView(R.id.additionalInfo)
    View additionalInfo;

    @BindView(R.id.windContainer)
    View windContainer;

    @BindView(R.id.wind)
    TextView wind;

    @BindView(R.id.humidityContainer)
    View humidityContainer;

    @BindView(R.id.humidity)
    TextView humidity;

    @BindView(R.id.pressureContainer)
    View pressureContainer;

    @BindView(R.id.pressure)
    TextView pressure;

    @BindString(R.string.select_location)
    String hint;

    @BindString(R.string.temp)
    String tempPattern;

    @BindString(R.string.average_temp)
    String averageTempPattern;

    @BindString(R.string.wind_pattern)
    String windPattern;

    @BindString(R.string.humidity_pattern)
    String humidityPattern;

    @BindString(R.string.pressure_pattern)
    String pressurePattern;

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
    public Activity getActivity() {
        return this;
    }

    @OnClick(R.id.forecastView)
    public void openDetails() {
        int selected = spinner.getSelectedItemPosition() - 1;
        if (selected > 0) {
            startActivity(DetailsActivity.createIntent(this,
                    mainPresenter.getSelectedLocationCoordinates(selected)));
        }
    }

    @Override
    public void setLocations(ArrayList<String> locations) {
        Logger.logd(TAG, "setLocations: " + locations);

        runOnUiThread(() -> {
            if (adapter == null) {
                initComplete = false;
                adapter = LocationsAdapter.init(MainActivity.this, R.layout.item_spinner,
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
        });
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
        animationManager.buildAnimationBaseOut(additionalInfo).translationY(50f).start();
    }

    private void showSelectedInfo(Forecast forecast) {
        Weather weather = null;
        if (forecast.getWeatherList() != null &&
                !forecast.getWeatherList().isEmpty()) {
            weather = forecast.getWeatherList().get(0);
        }

        // main information block
        if (weather != null) {
            Glide.with(this)
                    .load(String.format(Constants.IMAGE_URL, weather.getIcon()))
                    .into(weatherIcon);
            weatherIcon.setVisibility(View.VISIBLE);
        } else {
            weatherIcon.setImageDrawable(null);
            weatherIcon.setVisibility(View.INVISIBLE);
        }

        temp.setText(String.format(tempPattern, Math.round(forecast.getMain().getTemp())));
        main.setText(weather != null ? weather.getMain() : "");
        description.setText(weather != null ? weather.getDescription() : "");
        averageTemp.setText(weather != null ? weather.getMain() != null ?
                String.format(averageTempPattern, Math.round(forecast.getMain().getTempMin()),
                        Math.round(forecast.getMain().getTempMax())) : "" : "");
        if (forecast.getSys() != null) {
            sunrise.setText(forecast.getSys().getSunriseConverted());
            sunrise.setVisibility(View.VISIBLE);
            sunset.setText(forecast.getSys().getSunsetConverted());
            sunset.setVisibility(View.VISIBLE);
        } else {
            sunrise.setVisibility(View.GONE);
            sunset.setVisibility(View.GONE);
        }
        animationManager.buildAnimationBaseIn(mainInfo).start();

        // additional information block
        if (forecast.getWind() != null) {
            wind.setText(String.format(windPattern, forecast.getWind().getCardinal(),
                    forecast.getWind().getSpeed()));
            windContainer.setVisibility(View.VISIBLE);
        } else {
            windContainer.setVisibility(View.GONE);
        }

        if (forecast.getMain() != null) {
            humidity.setText(String.format(humidityPattern, forecast.getMain().getHumidity()));
            humidityContainer.setVisibility(View.VISIBLE);
            pressure.setText(String.format(pressurePattern, forecast.getMain().getPressure()));
            pressureContainer.setVisibility(View.VISIBLE);
        } else {
            humidityContainer.setVisibility(View.GONE);
            pressureContainer.setVisibility(View.GONE);
        }
        animationManager.buildAnimationBaseIn(additionalInfo).start();
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
