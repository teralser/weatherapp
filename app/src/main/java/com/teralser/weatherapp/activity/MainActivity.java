package com.teralser.weatherapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.teralser.weatherapp.R;
import com.teralser.weatherapp.di.component.ActivityComponent;
import com.teralser.weatherapp.model.Coordinates;
import com.teralser.weatherapp.model.Forecast;
import com.teralser.weatherapp.mvp.presenter.MainPresenter;
import com.teralser.weatherapp.mvp.view.MainView;
import com.teralser.weatherapp.utils.Logger;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainView {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    MainPresenter mainPresenter;

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
        if (mainPresenter != null)
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

    @Override
    public void openDetails(Coordinates coordinates) {
        startActivity(DetailsActivity.createIntent(this, coordinates));
    }

    @Override
    public void setForecast(Forecast forecast) {
        Logger.logd(TAG, "setForecast: " + forecast);
    }

    @Override
    public void showError(String error) {
        Logger.loge(TAG, "showError: " + error);
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationStatusGranted(boolean isGranted) {

    }
}
