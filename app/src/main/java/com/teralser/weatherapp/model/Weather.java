package com.teralser.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Weather implements Parcelable {

    @SerializedName("id")
    private int id; //Weather condition id

    @SerializedName("main")
    private String main; //Group of weather parameters (Rain, Snow, Extreme etc.)

    @SerializedName("description")
    private String description; //Weather condition within the group

    @SerializedName("icon")
    private String icon; //Weather icon id

    public Weather() {
    }

    public int getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.main);
        dest.writeString(this.description);
        dest.writeString(this.icon);
    }

    protected Weather(Parcel in) {
        this.id = in.readInt();
        this.main = in.readString();
        this.description = in.readString();
        this.icon = in.readString();
    }

    public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel source) {
            return new Weather(source);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weather weather = (Weather) o;

        if (id != weather.id) return false;
        if (main != null ? !main.equals(weather.main) : weather.main != null) return false;
        if (description != null ? !description.equals(weather.description) : weather.description != null)
            return false;
        return icon != null ? icon.equals(weather.icon) : weather.icon == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (main != null ? main.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", main='" + main + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
