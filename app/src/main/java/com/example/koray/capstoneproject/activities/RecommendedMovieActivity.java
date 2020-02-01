package com.example.koray.capstoneproject.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.koray.capstoneproject.Adapters.CustomRecommendedMovieAdapter;
import com.example.koray.capstoneproject.Listiners.FirebaseReadListiner;
import com.example.koray.capstoneproject.Models.RecommendedMovie;
import com.example.koray.capstoneproject.R;
import com.example.koray.capstoneproject.Utils.Firebase;
import com.example.koray.capstoneproject.Utils.HttpHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class RecommendedMovieActivity extends AppCompatActivity {

    private List<RecommendedMovie> list = new ArrayList<>();
    private List<RecommendedMovie> lstMovieByCosineSimi = new ArrayList<>();
    private ListView listView;
    private int pageCount = 1;
    private List<String> listMovieCosine = new ArrayList<>();
    private ProgressDialog dialog;
    private Button loadMore;
    private ImageView ımageButton;
    private SegmentedButtonGroup btnButtonGroup;
    private Firebase firebase = new Firebase();
    private boolean check;


    private void init() {
        btnButtonGroup = (SegmentedButtonGroup) findViewById(R.id.btnGroupRecommendedMovie);
        ımageButton = (ImageView) findViewById(R.id.imgViewRecommendedMovie);
        listView = (ListView) findViewById(R.id.lstDiscoverMovie);
        dialog = new ProgressDialog(this);
        loadMore = new Button(this);
        loadMore.setText("Load More");
        listView.addFooterView(loadMore);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_movie);
        init();
        registerHandlers();
        updateposition(btnButtonGroup.getPosition());
    }

    private void registerHandlers() {
        btnLoadMoreClicked();
        listViewClick();
        segmentButtonClicked();
    }

    private void btnLoadMoreClicked() {
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageCount++;
                new LoadMoreMovie().execute();
            }
        });
    }

    private void listViewClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RecommendedMovie vale = (RecommendedMovie) listView.getAdapter().getItem(i);
                Intent ıntent = new Intent(RecommendedMovieActivity.this, MovieSelectionActivity.class);
                ıntent.putExtra("deneme", (Serializable) vale);

                startActivity(ıntent);
            }
        });
    }

    private void segmentButtonClicked() {
        btnButtonGroup.setOnClickedButtonListener(new SegmentedButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(int position) {
                updateposition(position);
            }
        });
    }

    public void updateposition(int p) {
        switch (p) {
            case 0:
                getRecommendedMovieCosineSimi();
                break;
            case 1:
                new LoadMoreMovie().execute();
                break;
            default:
                break;
        }
    }

    public void getRecommendedMovieCosineSimi() {
        firebase.getCosineSimi("Action", new FirebaseReadListiner() {
            @Override
            public void onStart() {
                dialog.setMessage("Yükleniyor");
                dialog.show();
            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                listMovieCosine.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                if (snapshot1.exists()) {
                                    for (final DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                        if (snapshot2.exists()) {
                                            if (!snapshot2.getKey().equals("cosineSimi")) {
                                                if (Double.valueOf(snapshot2.getValue().toString()) > 9.0) {
                                                    new getCosineMovieInfoById(snapshot2.getKey().toString()).execute();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }

    private class getCosineMovieInfoById extends AsyncTask<Void, Void, Void> {

        String id = "";

        public getCosineMovieInfoById(String movieId) {
            id = movieId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lstMovieByCosineSimi.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            String URL = "https://api.themoviedb.org/3/find/" + id + "?api_key=a9ef7ae2bf7d0170b26fa9c26f151a7c&external_source=imdb_id";
            String jsonStr = handler.makeServiceCall(URL);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray results = jsonObject.getJSONArray("movie_results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movieObject = results.getJSONObject(i);
                        String name = movieObject.getString("title");
                        String id = movieObject.getString("id");
                        String year = movieObject.getString("release_date");
                        String poster = movieObject.getString("poster_path");
                        String backPoster = movieObject.getString("backdrop_path");
                        RecommendedMovie movie = new RecommendedMovie(name, year, id, poster, backPoster);
                        lstMovieByCosineSimi.add(movie);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CustomRecommendedMovieAdapter customRecommendedMovieAdapter = new CustomRecommendedMovieAdapter(RecommendedMovieActivity.this, lstMovieByCosineSimi);
            listView.setAdapter(customRecommendedMovieAdapter);
            dialog.dismiss();
        }
    }


    private class getDetailMovie extends AsyncTask<Void, Void, Boolean> {
        RecommendedMovie movieR;

        public getDetailMovie(RecommendedMovie recommendedMovie){
            movieR = recommendedMovie;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            String URL = "https://api.themoviedb.org/3/movie/"+ movieR.getMovieId() +"?api_key=a9ef7ae2bf7d0170b26fa9c26f151a7c";
            String jsonStr = handler.makeServiceCall(URL);
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                String imdb_id = jsonObject.getString("imdb_id");
                firebase.getImdbMovieByMovieId(imdb_id, new FirebaseReadListiner() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            check = true;
                        }else {
                            check = false;
                        }
                    }
                    @Override
                    public void onFailed(DatabaseError databaseError) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return check;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean){
                list.add(movieR);
            }
            CustomRecommendedMovieAdapter customRecommendedMovieAdapter = new CustomRecommendedMovieAdapter(RecommendedMovieActivity.this, list);
            listView.setAdapter(customRecommendedMovieAdapter);
        }
    }


    private class LoadMoreMovie extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list.clear();
            dialog.setMessage("Yükleniyor");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            String URL = "https://api.themoviedb.org/3/discover/movie?api_key=a9ef7ae2bf7d0170b26fa9c26f151a7c&page=" + pageCount + "&with_genres=28";
            String jsonString = handler.makeServiceCall(URL);
            if (jsonString != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray results = jsonObject.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movieObject = results.getJSONObject(i);
                        String name = movieObject.getString("title");
                        String id = movieObject.getString("id");
                        String year = movieObject.getString("release_date");
                        String poster = movieObject.getString("poster_path");
                        String backPoster = movieObject.getString("backdrop_path");
                        RecommendedMovie movie = new RecommendedMovie(name, year, id, poster, backPoster);
                        new getDetailMovie(movie).execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }
    }
}
