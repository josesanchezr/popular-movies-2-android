package com.example.android.popularmovies.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MovieDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter to show each item.
 * Created by joseluis on 27/08/2017.
 */

public class MovieDetailAdapter extends ArrayAdapter<MovieDetail> {

    public MovieDetailAdapter(Activity context, List<MovieDetail> movieDetails) {
        super(context, 0, movieDetails);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieDetail movieDetail = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie, parent, false);
        }

        if (movieDetail != null) {
            ImageView iconView = convertView.findViewById(R.id.item_image);
            String imagen = "http://image.tmdb.org/t/p/w185/" + movieDetail.imageThumbnail;
            Picasso.with(getContext())
                    .load(imagen)
                    .resize(200, 200)
                    .centerCrop()
                    .into(iconView);

            TextView movieTitle = convertView.findViewById(R.id.movie_title);
            movieTitle.setText(movieDetail.originalTitle);
        }
        return convertView;
    }
}
