package com.example.koray.capstoneproject.tabfragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.koray.capstoneproject.Adapters.ImdbMovieCustomAdapter;
import com.example.koray.capstoneproject.Listiners.FirebaseReadListiner;
import com.example.koray.capstoneproject.Models.Imdb;
import com.example.koray.capstoneproject.R;
import com.example.koray.capstoneproject.Utils.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koray on 17.12.2017.
 */

public class ImdbFragment extends Fragment {

    private Firebase firebase = new Firebase();
    private List<String> list = new ArrayList<>();
    private List<Imdb> imdbMovieList = new ArrayList<>();
    private ListView listView;
    private Context context;
    private ImdbMovieCustomAdapter ımdbMovieCustomAdapter;
    private ProgressDialog progressDialog;

    private void init(View view) {
        listView = (ListView) view.findViewById(R.id.lstImdb);
        ımdbMovieCustomAdapter = new ImdbMovieCustomAdapter(context, imdbMovieList);
        listView.setAdapter(ımdbMovieCustomAdapter);
        ımdbMovieCustomAdapter.notifyDataSetChanged();
    }

    public void deneme() {
        firebase.getImdbMovie(new FirebaseReadListiner() {
            @Override
            public void onStart() {
                //progressDialog = new ProgressDialog(context);
                //progressDialog.setMessage("Loading");
                //progressDialog.show();
            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                list.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Imdb ımdb = snapshot.getValue(Imdb.class);
                        imdbMovieList.add(ımdb);
                    }
                    ımdbMovieCustomAdapter.notifyDataSetChanged();
                }
                //progressDialog.dismiss();
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_imdb, container, false);
        init(view);
        deneme();
        return view;
    }

}
