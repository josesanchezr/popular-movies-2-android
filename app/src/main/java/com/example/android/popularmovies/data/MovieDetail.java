package com.example.android.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Class use as model.
 * Created by joseluis on 27/08/2017.
 */

public class MovieDetail implements Parcelable {

    @SerializedName("id")
    public final String id;

    @SerializedName("original_title")
    public final String originalTitle;

    @SerializedName("poster_path")
    public final String imageThumbnail;

    @SerializedName("overview")
    public final String synopsis;

    @SerializedName("vote_average")
    public final Double userRating;

    @SerializedName("release_date")
    public final String releaseDate;

    public MovieDetail(String id, String originalTitle, String imageThumbnail, String synopsis, Double userRating, String releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.imageThumbnail = imageThumbnail;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    private MovieDetail(Parcel in) {
        id = in.readString();
        originalTitle = in.readString();
        imageThumbnail = in.readString();
        synopsis = in.readString();
        userRating = in.readDouble();
        releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return originalTitle + "--" + imageThumbnail + "--" + synopsis + "--" + userRating + "--" + releaseDate;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(originalTitle);
        parcel.writeString(imageThumbnail);
        parcel.writeString(synopsis);
        parcel.writeDouble(userRating);
        parcel.writeString(releaseDate);
    }

    public static final Parcelable.Creator<MovieDetail> CREATOR = new Parcelable.Creator<MovieDetail>() {

        @Override
        public MovieDetail createFromParcel(Parcel parcel) {
            return new MovieDetail(parcel);
        }

        @Override
        public MovieDetail[] newArray(int i) {
            return new MovieDetail[i];
        }
    };

    public static final class Response {

        @SerializedName("results")
        public List<MovieDetail> movies = new ArrayList<>();
    }
}
