package com.teralser.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Snow implements Parcelable {

    @SerializedName("3h")
    private double _3h; //Snow volume for the last 3 hours

    public Snow() {
    }

    public double get_3h() {
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

    protected Snow(Parcel in) {
        this._3h = in.readDouble();
    }

    public static final Creator<Snow> CREATOR = new Creator<Snow>() {
        @Override
        public Snow createFromParcel(Parcel source) {
            return new Snow(source);
        }

        @Override
        public Snow[] newArray(int size) {
            return new Snow[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Snow snow = (Snow) o;

        return Double.compare(snow._3h, _3h) == 0;

    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(_3h);
        return (int) (temp ^ (temp >>> 32));
    }

    @Override
    public String toString() {
        return "Snow{" +
                "_3h=" + _3h +
                '}';
    }
}
