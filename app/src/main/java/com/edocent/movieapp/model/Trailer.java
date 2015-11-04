package com.edocent.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ankursrivastava on 10/25/15.
 */
public class Trailer implements Parcelable {

    String trailerKey;
    String trailerName;

    public Trailer() {

    }

    public Trailer(Parcel in) {
        setTrailerKey(in.readString());
        setTrailerName(in.readString());
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getTrailerKey());
        dest.writeString(getTrailerName());
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }
}
