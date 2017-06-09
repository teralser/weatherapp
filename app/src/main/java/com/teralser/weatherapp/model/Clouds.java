package com.teralser.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Clouds implements Parcelable {

    @SerializedName("all")
    private int all; // Cloudiness, %

    public Clouds() {
    }

    public int getAll() {
        return all;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.all);
    }

    protected Clouds(Parcel in) {
        this.all = in.readInt();
    }

    public static final Parcelable.Creator<Clouds> CREATOR = new Parcelable.Creator<Clouds>() {
        @Override
        public Clouds createFromParcel(Parcel source) {
            return new Clouds(source);
        }

        @Override
        public Clouds[] newArray(int size) {
            return new Clouds[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Clouds clouds = (Clouds) o;

        return all == clouds.all;

    }

    @Override
    public int hashCode() {
        return all;
    }

    @Override
    public String toString() {
        return "Clouds{" +
                "all=" + all +
                '}';
    }
}
