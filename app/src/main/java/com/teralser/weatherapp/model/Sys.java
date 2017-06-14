package com.teralser.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Sys implements Parcelable {

    private static final String FORMAT = "HH:mm";

    @SerializedName("country")
    private String country; //Country code (GB, JP etc.)

    @SerializedName("sunrise")
    private long sunrise; //Sunrise time, unix, UTC

    @SerializedName("sunset")
    private long sunset; //Sunset time, unix, UTC

    public Sys() {
    }

    public String getCountry() {
        return country;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public String getSunriseConverted(){
        return convertTimestamp(sunrise);
    }

    public String getSunsetConverted(){
        return convertTimestamp(sunset);
    }

    private String convertTimestamp(long time) {
        Date df = new Date(time * 1000L);
        return new SimpleDateFormat(FORMAT, Locale.ENGLISH).format(df);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.country);
        dest.writeLong(this.sunrise);
        dest.writeLong(this.sunset);
    }

    protected Sys(Parcel in) {
        this.country = in.readString();
        this.sunrise = in.readLong();
        this.sunset = in.readLong();
    }

    public static final Parcelable.Creator<Sys> CREATOR = new Parcelable.Creator<Sys>() {
        @Override
        public Sys createFromParcel(Parcel source) {
            return new Sys(source);
        }

        @Override
        public Sys[] newArray(int size) {
            return new Sys[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sys sys = (Sys) o;

        if (sunrise != sys.sunrise) return false;
        if (sunset != sys.sunset) return false;
        return country != null ? country.equals(sys.country) : sys.country == null;

    }

    @Override
    public int hashCode() {
        int result = country != null ? country.hashCode() : 0;
        result = 31 * result + (int) (sunrise ^ (sunrise >>> 32));
        result = 31 * result + (int) (sunset ^ (sunset >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Sys{" +
                "country='" + country + '\'' +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                '}';
    }
}
