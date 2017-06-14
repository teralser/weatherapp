package com.teralser.weatherapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.teralser.weatherapp.R;
import com.teralser.weatherapp.adapter.holder.DateViewHolder;
import com.teralser.weatherapp.adapter.holder.ForecastViewHolder;
import com.teralser.weatherapp.model.Forecast;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.Collections;
import java.util.List;

public class ForecastRecyclerAdapter extends RecyclerView.Adapter<ForecastViewHolder>
        implements StickyRecyclerHeadersAdapter<DateViewHolder> {

    private List<Forecast> data;

    public ForecastRecyclerAdapter() {
        this.data = Collections.emptyList();
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ForecastViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_forecast, parent, false));
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getRelativeTime().hashCode();
    }

    @Override
    public DateViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new DateViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.holder_date, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(DateViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public void setData(List<Forecast> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public Forecast getItem(int position) {
        return data.get(position);
    }
}
