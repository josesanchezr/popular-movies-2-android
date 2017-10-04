package com.example.android.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Class use as model to the trailer.
 * Created by joseluis on 28/09/2017.
 */

public class MovieTrailer implements Parcelable {

    @SerializedName("id")
    public final String id;

    @SerializedName("key")
    public final String key;

    @SerializedName("name")
    public final String name;

    @SerializedName("site")
    public final String site;

    private MovieTrailer(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
    }

    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeString(site);
    }

    public static final class Response {

        @SerializedName("results")
        public List<MovieTrailer> trailers = new ArrayList<>();
    }
}
