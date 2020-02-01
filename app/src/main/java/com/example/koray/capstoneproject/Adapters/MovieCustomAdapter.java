package com.example.koray.capstoneproject.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.koray.capstoneproject.Models.Movie;
import com.example.koray.capstoneproject.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Koray on 12.12.2017.
 */

public class MovieCustomAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Activity activity;
    private List<Movie> movieList;
    private int w, h;


    public MovieCustomAdapter(Activity activity, List<Movie> movies) {
        this.activity = activity;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        movieList = movies;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int i) {
        return movieList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.cell_movie, null);
            viewHolder = new ViewHolder();
            viewHolder.posterImageView = (ImageView) view.findViewById(R.id.imgMoviePoster);
            viewHolder.txtUserName = (TextView) view.findViewById(R.id.txtMovieName);
            viewHolder.txtGenre = (TextView) view.findViewById(R.id.txtGenre);
            viewHolder.txtYear = (TextView) view.findViewById(R.id.txtYear);
            viewHolder.posterImageView.post(new Runnable() {
                @Override
                public void run() {
                    w = viewHolder.posterImageView.getMeasuredWidth();
                    h = viewHolder.posterImageView.getMeasuredHeight();
                }
            });
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        final Movie movie = movieList.get(i);

        viewHolder.txtUserName.setText(movie.getName());
        viewHolder.txtGenre.setText(movie.getGenre());
        viewHolder.txtYear.setText(movie.getBirthday());
        Picasso
                .with(activity)
                .load(movie.getPosterUrl())
                .fetch(new Callback() {
                    @Override
                    public void onSuccess() {
                        if (w == 0) {
                            Picasso
                                    .with(activity)
                                    .load(movie.getPosterUrl())
                                    .resize(100, 100)
                                    .placeholder(R.drawable.imageplaceholder)
                                    .into(viewHolder.posterImageView);
                        } else {
                            Picasso
                                    .with(activity)
                                    .load(movie.getPosterUrl())
                                    .resize(w, h)
                                    .placeholder(R.drawable.imageplaceholder)
                                    .into(viewHolder.posterImageView);
                        }

                    }

                    @Override
                    public void onError() {

                    }
                });

        return view;
    }

    private static class ViewHolder {
        private ImageView posterImageView;
        private TextView txtUserName, txtGenre, txtYear;
    }
}
