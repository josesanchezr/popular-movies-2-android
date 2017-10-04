package com.example.android.popularmovies.api;

import com.example.android.popularmovies.data.MovieDetail;
import com.example.android.popularmovies.data.MovieReview;
import com.example.android.popularmovies.data.MovieTrailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Interface to get list of movies.
 * Created by joseluis on 29/08/2017.
 */

public interface MovieDetailApi {

    @GET("movie/popular")
    Call<MovieDetail.Response> discoverMoviesPopular();

    @GET("movie/top_rated")
    Call<MovieDetail.Response> discoverMoviesTopRated();

    @GET("movie/{id}/videos")
    Call<MovieTrailer.Response> fetchMovieTrailers(@Path("id") String id);

    @GET("movie/{id}/reviews")
    Call<MovieReview.Response> fetchMovieReviews(@Path("id") String id);
}
