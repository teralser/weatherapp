package com.teralser.weatherapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LocationsAdapter extends ArrayAdapter<String> {

    private String hint;

    public static LocationsAdapter init(@NonNull Context context,
                                        @LayoutRes int spinnerItemRes,
                                        @LayoutRes int dropDownRes,
                                        @NonNull List<String> objects,
                                        @NonNull String hint) {
        objects.add(0, hint);
        LocationsAdapter adapter = new LocationsAdapter(context, spinnerItemRes, objects);
        adapter.setHint(hint);
        adapter.setDropDownViewResource(dropDownRes);
        return adapter;
    }

    private LocationsAdapter(@NonNull Context context,
                             @LayoutRes int resource,
                             @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0) {
            // Disable the first item from Spinner
            // First item will be use for hint
            return false;
        } else {
            return true;
        }
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView tv = (TextView) view;
        if (position == 0) {
            // Set the hint text color gray
            tv.setTextColor(Color.GRAY);
        } else {
            tv.setTextColor(Color.BLACK);
        }
        return view;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void updateList(ArrayList<String> locations) {
        clear();
        locations.add(0, hint);
        addAll(locations);
        notifyDataSetChanged();
    }
}
