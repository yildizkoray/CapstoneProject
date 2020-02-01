package com.example.koray.capstoneproject.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.koray.capstoneproject.Listiners.CheckFirebaseData;
import com.example.koray.capstoneproject.Listiners.FirebaseReadListiner;
import com.example.koray.capstoneproject.Models.Imdb;
import com.example.koray.capstoneproject.Models.RecommendedMovie;
import com.example.koray.capstoneproject.Models.UserImdbMovie;
import com.example.koray.capstoneproject.R;
import com.example.koray.capstoneproject.Utils.Firebase;
import com.example.koray.capstoneproject.Utils.MakeBlur;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MovieSelectionActivity extends YouTubeBaseActivity {

    RelativeLayout lyt, subLyt;
    Random random;
    MakeBlur makeBlur;
    int h, w;
    private Button btnSelectNext, btnSelectSkip, btnExit;
    private TextView txtSelectMovieName, rateMovieRatio;
    private RatingBar ratingBar;
    private ImageView ımageView;
    private List<String> movieId = new ArrayList<>();
    private Firebase firebase = new Firebase();
    private List<Integer> beforeRandomNumber = new ArrayList<>();
    private Imdb ratedImdbMovie;
    private String genre;
    private ProgressDialog progressDialog;
    private RecommendedMovie recommendedMovie;
    private ImageButton imgbtnPlay;


    private void init() {
        imgbtnPlay = (ImageButton) findViewById(R.id.imgButtonTrailer);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Yükleniyor");
        progressDialog.setCancelable(false);
        btnExit = (Button) findViewById(R.id.btnExit);
        lyt = (RelativeLayout) findViewById(R.id.lytMovieSelection);
        subLyt = (RelativeLayout) findViewById(R.id.lytSubMovieSelection);
        random = new Random();
        btnSelectNext = (Button) findViewById(R.id.btnSelectNext);
        btnSelectSkip = (Button) findViewById(R.id.btnSelectSkip);
        txtSelectMovieName = (TextView) findViewById(R.id.txtSelectMovieName);
        ımageView = (ImageView) findViewById(R.id.imgSelectMoviePoster);
        ratingBar = (RatingBar) findViewById(R.id.rtBar);
        rateMovieRatio = (TextView) findViewById(R.id.rateStars);
        ratingBar.setRating(0);
        makeBlur = new MakeBlur(this);
        ımageView.post(new Runnable() {
            @Override
            public void run() {
                h = ımageView.getMeasuredHeight();
                w = ımageView.getMeasuredWidth();
            }
        });
    }

    public void doButton(int id) {
        switch (id) {
            case 0: {
                updateRecommendedMovieInfo(recommendedMovie);
                break;
            }
            case R.id.btnActionGenre: {
                genre = "Action";
                getMovieId(genre);
                break;
            }
            case R.id.btnAdventureGenre: {
                genre = "Adventure";
                getMovieId(genre);
                break;
            }
        }
    }

    public void randomMovie(int a) {
        int rdn = random.nextInt(a);
        if (beforeRandomNumber.contains(rdn)) {
            randomMovie(a);
        } else {
            beforeRandomNumber.add(rdn);
            getMovieInfo(rdn);
        }
    }

    public void getMovieInfo(final int position) {
        firebase.getImdbMovieByMovieId(movieId.get(position), new FirebaseReadListiner() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final Imdb ımdb = dataSnapshot.getValue(Imdb.class);
                    firebase.checkRatingMovie(movieId.get(position), genre, new CheckFirebaseData() {
                        @Override
                        public void onSuccess(DataSnapshot snapshot, boolean status) {
                            if (status) {
                                updateMovieInfo(ımdb);
                            } else {
                                randomMovie(movieId.size());
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }

    public void updateRecommendedMovieInfo(final RecommendedMovie recommendedMovie) {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        final String url = "https://image.tmdb.org/t/p/w500" + recommendedMovie.getMoviePoster();
        Picasso
                .with(this)
                .load(url)
                .fetch(new Callback() {
                    @Override
                    public void onSuccess() {
                        txtSelectMovieName.setText(recommendedMovie.getMovieName());
                        Picasso
                                .with(MovieSelectionActivity.this)
                                .load(url)
                                .centerCrop()
                                .resize(200, 300)
                                .into(ımageView);
                        Bitmap bitmap = makeBlur.blur(makeBlur.getBitmapFromURL(url));
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        lyt.setBackground(drawable);
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public void updateMovieInfo(final Imdb ımdb) {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        ratedImdbMovie = ımdb;

        final String url = "http://img.omdbapi.com/?i=" + ımdb.getTconst() + "&h=600&apikey=4074da0e";
        Picasso
                .with(this)
                .load(url)
                .fetch(new Callback() {
                    @Override
                    public void onSuccess() {
                        txtSelectMovieName.setText(ımdb.getPrimaryTitle());
                        Picasso
                                .with(MovieSelectionActivity.this)
                                .load(url)
                                .centerCrop()
                                .resize(200, 300)
                                .into(ımageView);
                        Bitmap bitmap = makeBlur.blur(makeBlur.getBitmapFromURL(url));
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        lyt.setBackground(drawable);
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public void getMovieId(final String genre) {
        firebase.getImdbMovieByGenres(genre, new FirebaseReadListiner() {
            @Override
            public void onStart() {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                movieId.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String id = snapshot.getKey();
                        movieId.add(id);
                    }
                }
                randomMovie(movieId.size());
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }

    public void btnSkipClicked() {
        btnSelectSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserImdbMovie userImdbMovie = new UserImdbMovie(ratedImdbMovie.getTconst(), genre, (double) ratingBar.getRating());
                firebase.saveUserImdbMovie(userImdbMovie, 0);
                randomMovie(movieId.size());
                ratingBar.setRating(0);
            }
        });

    }

    public void btnImgPlayClicked() {
        imgbtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //playerView.initialize("AIzaSyB2UFRik4G4RVCRwlTk9Qt5rF27vxp16Xs",ınitializedListener);
            }
        });
    }

    public void btnNextClicked() {
        btnSelectNext.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ratingBar.getRating() != 0) {
                            UserImdbMovie userImdbMovie = new UserImdbMovie(ratedImdbMovie.getTconst(), genre, (double) ratingBar.getRating());
                            firebase.saveUserImdbMovie(userImdbMovie, 1);
                            ratingBar.setRating(0);
                            randomMovie(movieId.size());
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MovieSelectionActivity.this);
                            builder.setTitle("OOPPSSS NEREYE");
                            builder.setMessage("İlerlemek için puan ver!");
                            builder.setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder.show();
                        }
                    }
                }
        );
    }

    public void registerHandlers() {
        btnNextClicked();
        btnSkipClicked();
        ratingBarCliked();
        btnExitClicked();
        btnImgPlayClicked();
    }

    public void btnExitClicked() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void ratingBarCliked() {

    }


    @Override
    protected void onStart() {
        super.onStart();
        ratingBar.setRating(0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_selection);
        init();
        Intent ıntent = getIntent();
        int id = ıntent.getIntExtra("buttonId", 0);
        RecommendedMovie movie = (RecommendedMovie) ıntent.getSerializableExtra("deneme");
        recommendedMovie = movie;
        doButton(id);
        registerHandlers();
    }
}
