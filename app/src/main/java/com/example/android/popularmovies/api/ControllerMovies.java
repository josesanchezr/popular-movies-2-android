package com.example.android.popularmovies.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Controller for Movies.
 * Created by joseluis on 29/08/2017.
 */

public class ControllerMovies implements Callback {

    private static final String MOVIE_DB_API_URL = "https://api.themoviedb.org/3/";

    private static final String API_KEY = "3bcc03b5189ef68b0616d6a43c2326d9";

    public final MovieDetailApi movieDetailApi;

    public ControllerMovies() {
        Gson gson = new GsonBuilder().setLenient().create();

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.interceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder()
                        .addQueryParameter("api_key", API_KEY)
                        .build();
                Request newRequest = chain.request().newBuilder().url(url).build();
                return chain.proceed(newRequest);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIE_DB_API_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        movieDetailApi = retrofit.create(MovieDetailApi.class);
    }

    @Override
    public void onResponse(Call call, Response response) {

    }

    @Override
    public void onFailure(Call call, Throwable t) {

    }
}
