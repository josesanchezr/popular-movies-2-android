package com.example.android.popularmovies.api;

import com.example.android.popularmovies.data.MovieDetail;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Interface to get list of movies.
 * Created by joseluis on 29/08/2017.
 */

public interface MovieDetailApi {

    @GET("movie/popular")
    Call<MovieDetail.Response> discoverMoviesPopular();

    @GET("movie/top_rated")
    Call<MovieDetail.Response> discoverMoviesTopRated();
}
