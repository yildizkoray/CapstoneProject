package com.example.koray.capstoneproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.koray.capstoneproject.Models.RecommendedMovie;
import com.example.koray.capstoneproject.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Koray on 22.12.2017.
 */

public class CustomRecommendedMovieAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context mContext;
    private List<RecommendedMovie> recommendedMovieList;

    public CustomRecommendedMovieAdapter(Context context, List<RecommendedMovie> list) {
        mContext = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        recommendedMovieList = list;
    }

    @Override
    public int getCount() {
        return recommendedMovieList.size();
    }

    @Override
    public Object getItem(int i) {
        return recommendedMovieList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView  = layoutInflater.inflate(R.layout.cell_recommended_movie,null);
        RecommendedMovie movie = recommendedMovieList.get(i);
        TextView textView = (TextView) rootView.findViewById(R.id.txtMovieNameRecommendedCelll);
        TextView textView1 = (TextView) rootView.findViewById(R.id.txtMovieYearRecommendedCell);
        final ImageView ımageView = (ImageView) rootView.findViewById(R.id.imgViewRecommendedMovie);

        final RecommendedMovie recommendedMovie = recommendedMovieList.get(i);
        textView.setText("#" + recommendedMovie.getMovieName());
        textView1.setText(recommendedMovie.getMovieYear().substring(0,4));
        Picasso
                .with(mContext)
                .load("http://image.tmdb.org/t/p/w500/o4I5sHdjzs29hBWzHtS2MKD3JsM.jpg")
                .fetch(new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso
                                .with(mContext)
                                .load("http://image.tmdb.org/t/p/w500/"+ recommendedMovie.getMoviePosterBack())
                                .resize(500,200)
                                .into(ımageView);
                    }

                    @Override
                    public void onError() {

                    }
                });

        return rootView;
    }
}
