package com.teralser.weatherapp.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teralser.weatherapp.R;
import com.teralser.weatherapp.model.Forecast;
import com.teralser.weatherapp.model.Weather;
import com.teralser.weatherapp.other.Constants;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ForecastViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.weatherIcon)
    ImageView weatherIcon;

    @BindView(R.id.temp)
    TextView temp;

    @BindView(R.id.wind)
    TextView wind;

    @BindString(R.string.wind_pattern)
    String windPattern;

    @BindString(R.string.temp)
    String tempPattern;

    public ForecastViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(Forecast forecast) {
        Weather weather = forecast.getWeatherList().get(0);

        Glide.with(weatherIcon.getContext())
                .load(String.format(Constants.IMAGE_URL, weather.getIcon()))
                .into(weatherIcon);

        time.setText(forecast.getWeatherTimeFormatted(Constants.SHORT_TIME_FORMAT));
        temp.setText(String.format(tempPattern,
                String.valueOf(Math.round(forecast.getMain().getTemp()))));

        if (forecast.getWind() != null) {
            wind.setText(String.format(windPattern,
                    String.valueOf(forecast.getWind().getCardinal()),
                    String.valueOf(forecast.getWind().getSpeed())));
            wind.setVisibility(View.VISIBLE);
        } else {
            wind.setVisibility(View.GONE);
        }
    }
}
