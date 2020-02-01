package com.example.koray.capstoneproject.tabfragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.koray.capstoneproject.Adapters.MovieCustomAdapter;
import com.example.koray.capstoneproject.Listiners.FirebaseReadListiner;
import com.example.koray.capstoneproject.Models.Movie;
import com.example.koray.capstoneproject.R;
import com.example.koray.capstoneproject.Utils.Firebase;
import com.example.koray.capstoneproject.activities.FacebookMovieInfoActivity;
import com.example.koray.capstoneproject.activities.TabActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;


public class RecommendFragment extends Fragment {

    Firebase firebase = new Firebase();
    List<String> id = new ArrayList<>();
    ListView listView;
    Activity activity;
    Context mContext;
    List<Movie> lstMovies = new ArrayList<>();
    MovieCustomAdapter adapter;

    private void init(View view) {
        listView = (ListView) view.findViewById(R.id.lstMovieRecom);
        adapter = new MovieCustomAdapter(activity, lstMovies);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 64, 0, 0);
        listView.setLayoutParams(params);
    }

    public void asd() {
        firebase.getSimiMovie(new FirebaseReadListiner() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                lstMovies.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                if (snap.getKey().equals("simi")) {

                                } else {
                                    firebase.getMovieById(snap.getKey().toString(), new FirebaseReadListiner() {
                                        @Override
                                        public void onStart() {

                                        }

                                        @Override
                                        public void onSuccess(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                Movie movie = dataSnapshot.getValue(Movie.class);
                                                lstMovies.add(movie);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onFailed(DatabaseError databaseError) {

                                        }
                                    });
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

    public void registerHandler() {
        listViewClicked();
    }

    public void listViewClicked() {
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent ıntent = new Intent(getActivity(), FacebookMovieInfoActivity.class);
                        String movieName = ((TextView) view.findViewById(R.id.txtMovieName)).getText().toString();
                        ıntent.putExtra("movieName", movieName);
                        getActivity().startActivity(ıntent);
                    }
                }
        );
    }


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        mContext = context;
        if (context instanceof Activity) {
            activity = (Activity) getActivity();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((TabActivity) getActivity()).tabLayout.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((TabActivity) getActivity()).tabLayout.setVisibility(View.VISIBLE);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deneme_fragment, container, false);
        init(view);
        asd();
        registerHandler();
        return view;
    }
}
