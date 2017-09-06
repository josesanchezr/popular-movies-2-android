package com.example.android.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmovies.ui.DetailsMovie;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.MovieDetailAdapter;
import com.example.android.popularmovies.api.ControllerMovies;
import com.example.android.popularmovies.data.MovieDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityFragment extends Fragment {

    private GridView gridView;

    private ControllerMovies controllerMovies;

    public MainActivityFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = rootView.findViewById(R.id.movies_grid);

        controllerMovies = new ControllerMovies();
        controllerMovies.movieDetailApi.discoverMoviesPopular().enqueue(moviesCallback);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MovieDetail movieDetail = (MovieDetail) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(view.getContext(), DetailsMovie.class);
                intent.putExtra("DETAILS_MOVIE", movieDetail);

                startActivity(intent);
            }
        });

        return rootView;
    }

    private final Callback<MovieDetail.Response> moviesCallback = new Callback<MovieDetail.Response>() {
        @Override
        public void onResponse(Call<MovieDetail.Response> call, Response<MovieDetail.Response> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    List<MovieDetail> movies = response.body().movies;
                    Log.d("MOVIE", "Cantidad de peliculas " + String.valueOf(movies.size()));
                    for (MovieDetail movie : movies) {
                        Log.d("MOVIE", "Titulo: " + movie.originalTitle);
                        Log.d("MOVIE", "Imagen: " + movie.imageThumbnail);
                    }
                    MovieDetailAdapter moviesAdapter = new MovieDetailAdapter(getActivity(), movies);
                    gridView.setAdapter(moviesAdapter);
                } else {
                    Log.w("MOVIE", "No hay movies");
                }
            } else {
                Log.d("MOVIE", "Ha ocurrido un error: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<MovieDetail.Response> call, Throwable t) {
            t.printStackTrace();
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.movie_popular:
                controllerMovies.movieDetailApi.discoverMoviesPopular().enqueue(moviesCallback);
                return false;
            case R.id.movie_top_rated:
                controllerMovies.movieDetailApi.discoverMoviesTopRated().enqueue(moviesCallback);
                return false;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
