package com.example.koray.capstoneproject.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.koray.capstoneproject.Listiners.FirebaseReadListiner;
import com.example.koray.capstoneproject.Models.Movie;
import com.example.koray.capstoneproject.R;
import com.example.koray.capstoneproject.Utils.Firebase;
import com.example.koray.capstoneproject.Utils.MakeBlur;
import com.example.koray.capstoneproject.Utils.ProgDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class FacebookMovieInfoActivity extends AppCompatActivity {

    private ImageView imgMoviePoster;
    private TextView txtMovieName, txtMovieYear, txtMovieGenre, txtMovieWriter, txtMovieStar;
    private Firebase firebase = new Firebase();
    private MakeBlur makeBlur;
    private RelativeLayout layout;
    private ProgDialog dialog;

    private void init() {
        layout = (RelativeLayout) findViewById(R.id.lyLytFBMI);
        imgMoviePoster = (ImageView) findViewById(R.id.imgMoviePosterFBMI);
        txtMovieName = (TextView) findViewById(R.id.txtMovieNameFBMI);
        txtMovieGenre = (TextView) findViewById(R.id.txtMovieGenreFBMI);
        txtMovieYear = (TextView) findViewById(R.id.txtMovieYearFBMI);
        txtMovieStar = (TextView) findViewById(R.id.txtMovieStarFBMI);
        makeBlur = new MakeBlur(this);
        dialog = new ProgDialog(this);
        dialog.show();
    }

    public void viewHolder(final Movie movie) {
        Picasso
                .with(FacebookMovieInfoActivity.this)
                .load(movie.getPosterUrl())
                .fetch(new Callback() {
                    @Override
                    public void onSuccess() {
                        txtMovieName.setText(movie.getName());
                        txtMovieGenre.setText(movie.getGenre());
                        txtMovieStar.setText(movie.getBirthday());
                        String a = movie.getStarring().replaceAll(",", "\n");
                        txtMovieStar.setText(a);
                        Picasso
                                .with(FacebookMovieInfoActivity.this)
                                .load(movie.getPosterUrl())
                                .resize(300, 400)
                                .centerCrop()
                                .into(imgMoviePoster);
                        Bitmap bitmap = makeBlur.getBitmapFromURL(movie.getPosterUrl());
                        Bitmap resized = Bitmap.createScaledBitmap(bitmap, layout.getMeasuredWidth(), layout.getMeasuredHeight(), true);
                        Bitmap blured = makeBlur.blur(resized);
                        Drawable drawable = new BitmapDrawable(getResources(), blured);
                        layout.setBackground(drawable);
                        dialog.stop();
                    }

                    @Override
                    public void onError() {

                    }
                });

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_movie_info);
        init();
        Intent ıntent = getIntent();
        String a = ıntent.getStringExtra("movieName");
        firebase.getFacebookMovieByName(a, new FirebaseReadListiner() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Movie movie = snapshot.getValue(Movie.class);
                        viewHolder(movie);
                    }

                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }
}
