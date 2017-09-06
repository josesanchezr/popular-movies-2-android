package com.example.android.popularmovies.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MovieDetail;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);
        ButterKnife.bind(this);

        MovieDetail movieDetail = getIntent().getParcelableExtra("DETAILS_MOVIE");

        originalTitle.setText(movieDetail.originalTitle);
        releaseDate.setText(movieDetail.releaseDate);
        synopsis.setText(movieDetail.synopsis);
        voteAverage.setRating((movieDetail.userRating).floatValue());

        String imagen = "http://image.tmdb.org/t/p/w185/" + movieDetail.imageThumbnail;
        Log.d("Details Movie", imagen);
        Picasso.with(this)
                .load(imagen)
                .resize(200, 200)
                .centerCrop()
                .into(imagePoster);
    }
}
