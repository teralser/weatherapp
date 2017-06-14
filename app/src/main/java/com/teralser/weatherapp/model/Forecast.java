package com.teralser.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Forecast implements Parcelable {

    @SerializedName("coordinates")
    private Coordinates coordinates;

    @SerializedName("weather")
    private List<Weather> weatherList;

    @SerializedName("main")
    private Main main;

    @SerializedName("wind")
    private Wind wind;

    @SerializedName("clouds")
    private Clouds clouds;

    @SerializedName("rain")
    private Rain rain;

    @SerializedName("snow")
    private Snow snow;

    @SerializedName("dt")
    private long dt; //Time of data calculation, unix, UTC

    @SerializedName("dt_txt")
    private String dtTxt; //Data/time of calculation, UTC

    @SerializedName("sys")
    private Sys sys;

    @SerializedName("id")
    private int id; //City ID

    @SerializedName("name")
    private String name; //City name

    public Forecast() {
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Rain getRain() {
        return rain;
    }

    public Snow getSnow() {
        return snow;
    }

    public long getDt() {
        return dt;
    }

    public String getDtTxt() {
        return dtTxt;
    }

    public Sys getSys() {
        return sys;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CharSequence getRelativeTime() {
        return DateUtils.getRelativeTimeSpanString(new Date(dt * 1000L).getTime(),
                System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS);
    }

    public String getWeatherTimeFormatted(String format) {
        return new SimpleDateFormat(format, Locale.ENGLISH).format(new Date(dt * 1000L));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.coordinates, flags);
        dest.writeTypedList(this.weatherList);
        dest.writeParcelable(this.main, flags);
        dest.writeParcelable(this.wind, flags);
        dest.writeParcelable(this.clouds, flags);
        dest.writeParcelable(this.rain, flags);
        dest.writeParcelable(this.snow, flags);
        dest.writeLong(this.dt);
        dest.writeString(this.dtTxt);
        dest.writeParcelable(this.sys, flags);
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    protected Forecast(Parcel in) {
        this.coordinates = in.readParcelable(Coordinates.class.getClassLoader());
        this.weatherList = in.createTypedArrayList(Weather.CREATOR);
        this.main = in.readParcelable(Main.class.getClassLoader());
        this.wind = in.readParcelable(Wind.class.getClassLoader());
        this.clouds = in.readParcelable(Clouds.class.getClassLoader());
        this.rain = in.readParcelable(Rain.class.getClassLoader());
        this.snow = in.readParcelable(Snow.class.getClassLoader());
        this.dt = in.readLong();
        this.dtTxt = in.readString();
        this.sys = in.readParcelable(Sys.class.getClassLoader());
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<Forecast> CREATOR = new Creator<Forecast>() {
        @Override
        public Forecast createFromParcel(Parcel source) {
            return new Forecast(source);
        }

        @Override
        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Forecast forecast = (Forecast) o;

        if (dt != forecast.dt) return false;
        if (id != forecast.id) return false;
        if (coordinates != null ? !coordinates.equals(forecast.coordinates) : forecast.coordinates != null)
            return false;
        if (weatherList != null ? !weatherList.equals(forecast.weatherList) : forecast.weatherList != null)
            return false;
        if (main != null ? !main.equals(forecast.main) : forecast.main != null) return false;
        if (wind != null ? !wind.equals(forecast.wind) : forecast.wind != null) return false;
        if (clouds != null ? !clouds.equals(forecast.clouds) : forecast.clouds != null)
            return false;
        if (rain != null ? !rain.equals(forecast.rain) : forecast.rain != null) return false;
        if (snow != null ? !snow.equals(forecast.snow) : forecast.snow != null) return false;
        if (dtTxt != null ? !dtTxt.equals(forecast.dtTxt) : forecast.dtTxt != null) return false;
        if (sys != null ? !sys.equals(forecast.sys) : forecast.sys != null) return false;
        return name != null ? name.equals(forecast.name) : forecast.name == null;

    }

    @Override
    public int hashCode() {
        int result = coordinates != null ? coordinates.hashCode() : 0;
        result = 31 * result + (weatherList != null ? weatherList.hashCode() : 0);
        result = 31 * result + (main != null ? main.hashCode() : 0);
        result = 31 * result + (wind != null ? wind.hashCode() : 0);
        result = 31 * result + (clouds != null ? clouds.hashCode() : 0);
        result = 31 * result + (rain != null ? rain.hashCode() : 0);
        result = 31 * result + (snow != null ? snow.hashCode() : 0);
        result = 31 * result + (int) (dt ^ (dt >>> 32));
        result = 31 * result + (dtTxt != null ? dtTxt.hashCode() : 0);
        result = 31 * result + (sys != null ? sys.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "coordinates=" + coordinates +
                ", weatherList=" + weatherList +
                ", main=" + main +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", rain=" + rain +
                ", snow=" + snow +
                ", dt=" + dt +
                ", dtTxt='" + dtTxt + '\'' +
                ", sys=" + sys +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
