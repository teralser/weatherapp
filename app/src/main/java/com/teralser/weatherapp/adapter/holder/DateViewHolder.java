package com.teralser.weatherapp.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.teralser.weatherapp.R;
import com.teralser.weatherapp.model.Forecast;
import com.teralser.weatherapp.other.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DateViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    TextView titleView;

    @BindView(R.id.title2)
    TextView titleView2;

    public DateViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(Forecast forecast) {
        titleView.setText(forecast.getRelativeTime());
        titleView2.setText(forecast.getWeatherTimeFormatted(Constants.NORMAL_TIME_FORMAT));
    }
}
