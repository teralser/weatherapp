package com.teralser.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastListResponse implements Parcelable {

    @SerializedName("list")
    private List<Forecast> forecastList;

    public List<Forecast> getForecastList() {
        return forecastList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.forecastList);
    }

    public ForecastListResponse() {
    }

    protected ForecastListResponse(Parcel in) {
        this.forecastList = in.createTypedArrayList(Forecast.CREATOR);
    }

    public static final Parcelable.Creator<ForecastListResponse> CREATOR = new Parcelable.Creator<ForecastListResponse>() {
        @Override
        public ForecastListResponse createFromParcel(Parcel source) {
            return new ForecastListResponse(source);
        }

        @Override
        public ForecastListResponse[] newArray(int size) {
            return new ForecastListResponse[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForecastListResponse that = (ForecastListResponse) o;

        return forecastList != null ? forecastList.equals(that.forecastList) : that.forecastList == null;

    }

    @Override
    public int hashCode() {
        return forecastList != null ? forecastList.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ForecastListResponse{" +
                "forecastList=" + forecastList +
                '}';
    }
}
