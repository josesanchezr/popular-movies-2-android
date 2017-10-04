package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MovieTrailer;

import java.util.List;

/**
 * Created by joseluis on 29/09/2017.
 */

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.MovieTrailerViewHolder> {

    private List<MovieTrailer> movieTrailerList;
    private static final String SITE = "YouTube";

    public class MovieTrailerViewHolder extends RecyclerView.ViewHolder {

        public TextView trailerName;
        public final View mView;

        public MovieTrailerViewHolder(View view) {
            super(view);
            trailerName = (TextView) view.findViewById(R.id.trailer_name);
            mView = view;
        }
    }

    public MovieTrailersAdapter(List<MovieTrailer> movieTrailers) {
        this.movieTrailerList = movieTrailers;
    }

    @Override
    public MovieTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_trailer, parent, false);

        return new MovieTrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieTrailerViewHolder holder, int position) {
        final MovieTrailer movieTrailer = movieTrailerList.get(position);
        holder.trailerName.setText(movieTrailer.name);

        if (SITE.equals(movieTrailer.site)) {
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Uri uriTrailer = Uri.parse("https://www.youtube.com/watch?v=" + movieTrailer.key);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uriTrailer);
                    context.startActivity(intent);
                }
            });
        } else {
            Log.w("MOVIE-TRAILER-ADAPTER", "This trailer isn't youtube");
        }
    }

    @Override
    public int getItemCount() {
        return movieTrailerList.size();
    }
}
