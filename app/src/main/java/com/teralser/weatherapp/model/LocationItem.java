package com.teralser.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationItem implements Parcelable {

    private String name;
    private Coordinates coordinates;

    public LocationItem(String name, Coordinates coordinates) {
        this.name = name;
        this.coordinates = coordinates;
    }

    public LocationItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeParcelable(this.coordinates, flags);
    }

    protected LocationItem(Parcel in) {
        this.name = in.readString();
        this.coordinates = in.readParcelable(Coordinates.class.getClassLoader());
    }

    public static final Parcelable.Creator<LocationItem> CREATOR = new Parcelable.Creator<LocationItem>() {
        @Override
        public LocationItem createFromParcel(Parcel source) {
            return new LocationItem(source);
        }

        @Override
        public LocationItem[] newArray(int size) {
            return new LocationItem[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationItem locationItem = (LocationItem) o;

        if (name != null ? !name.equals(locationItem.name) : locationItem.name != null) return false;
        return coordinates != null ? coordinates.equals(locationItem.coordinates) : locationItem.coordinates == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LocationItem{" +
                "name='" + name + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}
