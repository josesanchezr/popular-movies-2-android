package com.example.android.popularmovies.ui;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.MovieReviewsAdapter;
import com.example.android.popularmovies.adapter.MovieTrailersAdapter;
import com.example.android.popularmovies.api.ControllerMovies;
import com.example.android.popularmovies.data.MovieDetail;
import com.example.android.popularmovies.data.MovieReview;
import com.example.android.popularmovies.data.MovieTrailer;
import com.example.android.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsMovie extends AppCompatActivity {

    @BindView(R.id.original_title)
    TextView originalTitle;

    @BindView(R.id.release_date)
    TextView releaseDate;

    @BindView(R.id.overview)
    TextView synopsis;

    @BindView(R.id.image_poster)
    ImageView imagePoster;

    @BindView(R.id.rating_bar)
    RatingBar voteAverage;

    private ControllerMovies controllerMovies;

    @BindView(R.id.trailers_movie)
    RecyclerView trailersRecyclerView;

    @BindView(R.id.reviews_movie)
    RecyclerView reviewsRecyclerView;

    @BindView(R.id.favoriteButton)
    Button favoriteButton;

    private MovieDetail movieDetail;

    private boolean isFavoriteMovie;

    private List<MovieTrailer> trailers;

    private List<MovieReview> reviews;

    private static final String MOVIE_DETAIL = "MOVIE_DETAIL";
    private static final String MOVIE_TRAILERS = "MOVIE_TRAILERS";
    private static final String MOVIE_REVIEWS = "MOVIE_REVIEWS";
    private static final String MOVIE_FAVORITE = "MOVIE_FAVORITE";
    private static final String MOVIE_FAVORITE_TEXT = "MOVIE_FAVORITE_TEXT";

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(MOVIE_DETAIL, movieDetail);

        ArrayList<Parcelable> parcelablesTrailers = new ArrayList<>();

        for (MovieTrailer movieTrailer : trailers) {
            Parcelable parcelable = movieTrailer;
            parcelablesTrailers.add(parcelable);
        }
        outState.putParcelableArrayList(MOVIE_TRAILERS, parcelablesTrailers);

        ArrayList<Parcelable> parcelablesReviews = new ArrayList<>();

        for (MovieReview movieReview : reviews) {
            Parcelable parcelable = movieReview;
            parcelablesReviews.add(parcelable);
        }
        outState.putParcelableArrayList(MOVIE_REVIEWS, parcelablesReviews);

        outState.putBoolean(MOVIE_FAVORITE, isFavoriteMovie);
        outState.putString(MOVIE_FAVORITE_TEXT, favoriteButton.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        movieDetail = (MovieDetail) savedInstanceState.get(MOVIE_DETAIL);
        trailers = savedInstanceState.getParcelableArrayList(MOVIE_TRAILERS);
        reviews = savedInstanceState.getParcelableArrayList(MOVIE_REVIEWS);
        isFavoriteMovie = savedInstanceState.getBoolean(MOVIE_FAVORITE);

        MovieTrailersAdapter adapterTrailers = new MovieTrailersAdapter(trailers);
        trailersRecyclerView.setAdapter(adapterTrailers);
        adapterTrailers.notifyDataSetChanged();

        MovieReviewsAdapter adapterReviews = new MovieReviewsAdapter(reviews);
        reviewsRecyclerView.setAdapter(adapterReviews);
        adapterReviews.notifyDataSetChanged();

        String favoriteTextButton = savedInstanceState.getString(MOVIE_FAVORITE_TEXT);
        favoriteButton.setText(favoriteTextButton);

        createOrRestartUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            controllerMovies = new ControllerMovies();

            movieDetail = getIntent().getParcelableExtra("DETAILS_MOVIE");

            Uri uri = ContentUris.withAppendedId(MoviesContract.MoviesEntry.CONTENT_URI, Integer.valueOf(movieDetail.id));
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor.getCount() == 0) {
                isFavoriteMovie = false;
                favoriteButton.setText(getResources().getString(R.string.add_favorites));
            } else {
                favoriteButton.setText(getResources().getString(R.string.del_favorites));
                isFavoriteMovie = true;
            }

            controllerMovies.movieDetailApi.fetchMovieTrailers(movieDetail.id).enqueue(trailersCallback);
            controllerMovies.movieDetailApi.fetchMovieReviews(movieDetail.id).enqueue(reviewsCallback);

            createOrRestartUI();
        }
    }

    private void createOrRestartUI() {
        originalTitle.setText(movieDetail.originalTitle);
        releaseDate.setText(movieDetail.releaseDate);
        synopsis.setText(movieDetail.synopsis);
        voteAverage.setRating((movieDetail.userRating).floatValue());

        String imagen = "http://image.tmdb.org/t/p/w185/" + movieDetail.imageThumbnail;

        Picasso.with(this)
                .load(imagen)
                .resize(200, 200)
                .centerCrop()
                .into(imagePoster);

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavoriteMovie) {
                    deleteMovie();
                } else {
                    inserMovie();
                }
            }
        });

        RecyclerView.LayoutManager layoutManagerTrailers = new LinearLayoutManager(this);
        trailersRecyclerView.setLayoutManager(layoutManagerTrailers);
        trailersRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManagerReviews = new LinearLayoutManager(this);
        reviewsRecyclerView.setLayoutManager(layoutManagerReviews);
        reviewsRecyclerView.setHasFixedSize(true);
    }

    private final Callback<MovieTrailer.Response> trailersCallback = new Callback<MovieTrailer.Response>() {
        @Override
        public void onResponse(Call<MovieTrailer.Response> call, Response<MovieTrailer.Response> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    trailers = response.body().trailers;
                    Log.d("MOVIE-TRAILER", "Cantidad de trailers " + String.valueOf(trailers.size()));
                    for (MovieTrailer trailer : trailers) {
                        Log.d("MOVIE-TRAILER", "Name: " + trailer.name);
                        Log.d("MOVIE-TRAILER", "Key: " + trailer.key);
                    }
                    MovieTrailersAdapter adapter = new MovieTrailersAdapter(trailers);
                    trailersRecyclerView.setAdapter(adapter);
                } else {
                    Log.w("MOVIE-TRAILER", "No hay trailers");
                }
            } else {
                Log.d("MOVIE-TRAILER", "Ha ocurrido un error: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<MovieTrailer.Response> call, Throwable t) {
            t.printStackTrace();
        }
    };

    private final Callback<MovieReview.Response> reviewsCallback = new Callback<MovieReview.Response>() {
        @Override
        public void onResponse(Call<MovieReview.Response> call, Response<MovieReview.Response> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    reviews = response.body().reviews;
                    Log.d("MOVIE-REVIEW", "Cantidad de reviews " + String.valueOf(reviews.size()));
                    for (MovieReview review : reviews) {
                        Log.d("MOVIE-REVIEW", "Name: " + review.author);
                    }
                    MovieReviewsAdapter adapter = new MovieReviewsAdapter(reviews);
                    reviewsRecyclerView.setAdapter(adapter);
                } else {
                    Log.w("MOVIE-REVIEW", "No hay reviews");
                }
            } else {
                Log.d("MOVIE-REVIEW", "Ha ocurrido un error: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<MovieReview.Response> call, Throwable t) {
            t.printStackTrace();
        }
    };

    private void inserMovie() {
        ContentValues movieContentValues = new ContentValues();
        movieContentValues.put(MoviesContract.MoviesEntry._ID, movieDetail.id);
        movieContentValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE, movieDetail.originalTitle);
        movieContentValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, movieDetail.imageThumbnail);
        movieContentValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, movieDetail.synopsis);
        movieContentValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, movieDetail.userRating);
        movieContentValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, movieDetail.releaseDate);

        Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, movieContentValues);

        Log.d("MOVIE-INSERT", "Movie adicionada: " + uri.getPath());
        String id = "/movie/" + movieDetail.id;
        if (id.equals(uri.getPath())) {
            isFavoriteMovie = true;
            favoriteButton.setText(getResources().getString(R.string.del_favorites));
        }
    }

    private void deleteMovie() {
        Uri uri = MoviesContract.MoviesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movieDetail.id).build();

        int countDelete = getContentResolver().delete(uri, null, null);

        Log.d("MOVIE-DELETE", "Cantidad de movies borradas: " + countDelete);

        if (countDelete > 0) {
            isFavoriteMovie = false;
            favoriteButton.setText(getResources().getString(R.string.add_favorites));
        }
    }

}
