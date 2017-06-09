package com.teralser.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Rain implements Parcelable {

    @SerializedName("3h")
    private double _3h; //Rain volume for the last 3 hours

    public Rain() {
    }

    public double get3h() {
        return _3h;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this._3h);
    }

    protected Rain(Parcel in) {
        this._3h = in.readDouble();
    }

    public static final Creator<Rain> CREATOR = new Creator<Rain>() {
        @Override
        public Rain createFromParcel(Parcel source) {
            return new Rain(source);
        }

        @Override
        public Rain[] newArray(int size) {
            return new Rain[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rain rain = (Rain) o;

        return Double.compare(rain._3h, _3h) == 0;

    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(_3h);
        return (int) (temp ^ (temp >>> 32));
    }

    @Override
    public String toString() {
        return "Rain{" +
                "_3h=" + _3h +
                '}';
    }
}
