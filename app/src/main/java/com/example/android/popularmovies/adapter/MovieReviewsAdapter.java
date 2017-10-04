package com.example.android.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MovieReview;

import java.util.List;

import butterknife.BindView;

/**
 * Created by joseluis on 2/10/2017.
 */

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewViewHolder> {

    private List<MovieReview> movieReviewList;

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder {

        public TextView reviewAuthor;

        public TextView reviewContent;

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            reviewAuthor = (TextView) itemView.findViewById(R.id.review_author);
            reviewContent = (TextView) itemView.findViewById(R.id.review_content);
        }
    }

    public MovieReviewsAdapter(List<MovieReview> movieReviews) {
        this.movieReviewList = movieReviews;
    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_review, parent, false);
        return new MovieReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        final MovieReview movieReview = movieReviewList.get(position);
        holder.reviewAuthor.setText(movieReview.author);
        holder.reviewContent.setText(movieReview.content);
    }

    @Override
    public int getItemCount() {
        return movieReviewList.size();
    }
}
