package com.example.koray.capstoneproject.tabfragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.koray.capstoneproject.R;
import com.example.koray.capstoneproject.Utils.Firebase;
import com.example.koray.capstoneproject.activities.RecommendedMovieActivity;

/**
 * Created by Koray on 19.12.2017.
 */

public class SelectGenreForAdvice extends Fragment implements View.OnClickListener {
    private ImageButton btnFacebook, btnAction;
    private Context mContext;
    private Activity mActivity;
    private Firebase firebase = new Firebase();

    private void init(View view) {
        btnFacebook = (ImageButton) view.findViewById(R.id.imgButtonFacebook);
        btnAction = (ImageButton) view.findViewById(R.id.imgButtonAction);
        btnAction.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_genre_for_advice, container, false);
        init(rootView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgButtonFacebook: {
                Fragment recommendFragment = new RecommendFragment();
                getFragmentManager().beginTransaction().replace(R.id.lytTab, recommendFragment).addToBackStack(null).commit();
                break;
            }
            case R.id.imgButtonAction: {
                firebase.deneme2("Action");
                Intent ıntent = new Intent(getActivity(), RecommendedMovieActivity.class);
                getActivity().startActivity(ıntent);
                Toast.makeText(mContext, "Koray", Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                break;
        }
    }
}
