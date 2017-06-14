package com.teralser.weatherapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.teralser.weatherapp.R;
import com.teralser.weatherapp.adapter.ForecastRecyclerAdapter;
import com.teralser.weatherapp.di.component.ActivityComponent;
import com.teralser.weatherapp.model.Forecast;
import com.teralser.weatherapp.model.LocationItem;
import com.teralser.weatherapp.mvp.presenter.DetailsPresenter;
import com.teralser.weatherapp.mvp.view.DetailView;
import com.teralser.weatherapp.utils.Logger;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

public class DetailsActivity extends BaseActivity implements DetailView {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    @Inject
    DetailsPresenter detailsPresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.location)
    TextView location;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ForecastRecyclerAdapter adapter;

    public static Intent createIntent(Context context, LocationItem item) {
        return new Intent(context, DetailsActivity.class)
                .putExtra(LocationItem.class.getName(), item);
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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        LocationItem item = getIntent().getParcelableExtra(LocationItem.class.getName());
        location.setText(item.getName());
        detailsPresenter.get5daysForecast(item.getCoordinates());

        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        adapter = new ForecastRecyclerAdapter();
        final StickyRecyclerHeadersDecoration hd = new StickyRecyclerHeadersDecoration(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                hd.invalidateHeaders();
            }
        });

        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setInterpolator(new DecelerateInterpolator());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(alphaAdapter);
        recyclerView.addItemDecoration(hd);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (detailsPresenter != null) detailsPresenter.onDestroy();
    }

    @Override
    public void setForecast(List<Forecast> forecastList) {
        Logger.logd(TAG, "setForecast: " + forecastList);
        adapter.setData(forecastList);
    }

    @Override
    public void showError(String error) {
        Logger.loge(TAG, "showError: " + error);
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
