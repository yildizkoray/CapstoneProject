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
import android.widget.Button;

import com.example.koray.capstoneproject.R;
import com.example.koray.capstoneproject.activities.MovieSelectionActivity;

/**
 * Created by Koray on 17.12.2017.
 */

public class ImdbGenreSelection extends Fragment {

    private Button btnActionGenre, btnAdventureGenre;
    private Context mContext;

    private void init(View view) {
        btnActionGenre = (Button) view.findViewById(R.id.btnActionGenre);
        btnAdventureGenre = (Button) view.findViewById(R.id.btnAdventureGenre);
    }

    public void btnActionGenreClick() {
        btnActionGenre.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ıntent = new Intent(mContext, MovieSelectionActivity.class);
                        ıntent.putExtra("buttonId", view.getId());
                        mContext.startActivity(ıntent);
                    }
                }
        );
    }

    public void btnAdventureGenreClick() {
        btnAdventureGenre.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ıntent = new Intent(mContext, MovieSelectionActivity.class);
                        ıntent.putExtra("buttonId", view.getId());
                        mContext.startActivity(ıntent);
                    }
                }
        );
    }

    public void registerHandlers() {
        btnActionGenreClick();
        btnAdventureGenreClick();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre_select, container, false);
        init(view);
        registerHandlers();
        return view;
    }
}
