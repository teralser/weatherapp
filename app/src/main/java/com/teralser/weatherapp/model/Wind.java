package com.teralser.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Wind implements Parcelable {

    private static final String[] CARDINALS = {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"};

    @SerializedName("speed")
    private double speed; //Wind speed. Unit Metric: meter/sec

    @SerializedName("deg")
    private double deg; //Wind direction, degrees (meteorological)

    public Wind() {
    }

    public double getSpeed() {
        return speed;
    }

    public double getDeg() {
        return deg;
    }

    public String getCardinal() {
        return CARDINALS[(int) Math.round((deg % 360) / 45)];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.speed);
        dest.writeDouble(this.deg);
    }

    protected Wind(Parcel in) {
        this.speed = in.readDouble();
        this.deg = in.readDouble();
    }

    public static final Creator<Wind> CREATOR = new Creator<Wind>() {
        @Override
        public Wind createFromParcel(Parcel source) {
            return new Wind(source);
        }

        @Override
        public Wind[] newArray(int size) {
            return new Wind[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wind wind = (Wind) o;

        if (Double.compare(wind.speed, speed) != 0) return false;
        return Double.compare(wind.deg, deg) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(speed);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(deg);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Wind{" +
                "speed=" + speed +
                ", deg=" + deg +
                '}';
    }
}
