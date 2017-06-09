package com.teralser.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Main implements Parcelable {

    @SerializedName("temp")
    private double temp; //Temperature. Unit Metric: Celsius

    @SerializedName("pressure")
    private double pressure; //Atmospheric pressure (on the sea level, if there is no sea_level
    // or grnd_level data), hPa

    @SerializedName("humidity")
    private int humidity; //Humidity, %

    @SerializedName("temp_min")
    private double tempMin; //Minimum temperature at the moment. This is deviation from current temp
    // that is possible for large cities and megalopolises geographically
    // expanded (use these parameter optionally). Unit Metric: Celsius

    @SerializedName("temp_max")
    private double tempMax;  //Maximum temperature at the moment. This is deviation from current temp
    // that is possible for large cities and megalopolises geographically
    // expanded (use these parameter optionally). Unit Metric: Celsius

    @SerializedName("sea_level")
    private double seaLevel; // Atmospheric pressure on the sea level, hPa

    @SerializedName("grnd_level")
    private double grndLevel; //Atmospheric pressure on the ground level, hPa

    public Main() {
    }

    public double getTemp() {
        return temp;
    }

    public double getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public double getSeaLevel() {
        return seaLevel;
    }

    public double getGrndLevel() {
        return grndLevel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.temp);
        dest.writeDouble(this.pressure);
        dest.writeInt(this.humidity);
        dest.writeDouble(this.tempMin);
        dest.writeDouble(this.tempMax);
        dest.writeDouble(this.seaLevel);
        dest.writeDouble(this.grndLevel);
    }

    protected Main(Parcel in) {
        this.temp = in.readDouble();
        this.pressure = in.readDouble();
        this.humidity = in.readInt();
        this.tempMin = in.readDouble();
        this.tempMax = in.readDouble();
        this.seaLevel = in.readDouble();
        this.grndLevel = in.readDouble();
    }

    public static final Creator<Main> CREATOR = new Creator<Main>() {
        @Override
        public Main createFromParcel(Parcel source) {
            return new Main(source);
        }

        @Override
        public Main[] newArray(int size) {
            return new Main[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Main main = (Main) o;

        if (Double.compare(main.temp, temp) != 0) return false;
        if (Double.compare(main.pressure, pressure) != 0) return false;
        if (humidity != main.humidity) return false;
        if (Double.compare(main.tempMin, tempMin) != 0) return false;
        if (Double.compare(main.tempMax, tempMax) != 0) return false;
        if (Double.compare(main.seaLevel, seaLevel) != 0) return false;
        return Double.compare(main.grndLevel, grndLevel) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp1;
        temp1 = Double.doubleToLongBits(temp);
        result = (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(pressure);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        result = 31 * result + humidity;
        temp1 = Double.doubleToLongBits(tempMin);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(tempMax);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(seaLevel);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(grndLevel);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Main{" +
                "temp=" + temp +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", tempMin=" + tempMin +
                ", tempMax=" + tempMax +
                ", seaLevel=" + seaLevel +
                ", grndLevel=" + grndLevel +
                '}';
    }
}
