package com.example.koray.capstoneproject.tabfragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.koray.capstoneproject.Adapters.MovieCustomAdapter;
import com.example.koray.capstoneproject.Listiners.FirebaseReadListiner;
import com.example.koray.capstoneproject.Models.Movie;
import com.example.koray.capstoneproject.Models.User;
import com.example.koray.capstoneproject.Models.UserMovie;
import com.example.koray.capstoneproject.R;
import com.example.koray.capstoneproject.Utils.Firebase;
import com.example.koray.capstoneproject.activities.FacebookMovieInfoActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private Firebase firebase;
    private List<UserMovie> userMovieList = new ArrayList<>();
    private List<Movie> likedList = new ArrayList<>();
    private List<Movie> whatchedList = new ArrayList<>();
    private List<Movie> wantedList = new ArrayList<>();
    private CircleImageView imgProfilePhoto;
    private Context context;
    private Activity act;
    private ListView movieListView;
    private TextView txtUserName;
    private SegmentedButtonGroup segmentedButtonGroup;
    private MovieCustomAdapter likedAdapter, whatchedAdapter, wantedAdapter;
    private int width, height;

    private void init(Activity activity, View view) {
        firebase = new Firebase(activity);
        txtUserName = (TextView) view.findViewById(R.id.txtUserName);
        imgProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_image);
        movieListView = (ListView) view.findViewById(R.id.lstMovie);
        segmentedButtonGroup = (SegmentedButtonGroup) view.findViewById(R.id.segmentedButtonGroup);
        imgProfilePhoto.post(new Runnable() {
            @Override
            public void run() {
                width = imgProfilePhoto.getMeasuredWidth();
                height = imgProfilePhoto.getMeasuredHeight();
            }
        });
        likedAdapter = new MovieCustomAdapter(act, likedList);
        wantedAdapter = new MovieCustomAdapter(act, wantedList);
        whatchedAdapter = new MovieCustomAdapter(act, whatchedList);
        updateposition(segmentedButtonGroup.getPosition());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogout: {
                firebase.logOut();
                LoginManager.getInstance().logOut();
                act.finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void btnClick() {
        segmentedButtonGroup.setOnClickedButtonListener(new SegmentedButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(int position) {
                updateposition(position);
            }
        });
    }

    public void updateposition(int po) {
        switch (po) {
            case 0: {
                movieListView.setAdapter(likedAdapter);
                break;
            }
            case 1: {

                movieListView.setAdapter(whatchedAdapter);
                break;
            }
            case 2: {
                movieListView.setAdapter(wantedAdapter);
                break;
            }
        }
    }


    public void getUserInfo() {
        firebase.getCurrentUserInfo(new FirebaseReadListiner() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                txtUserName.setText(user.getFullName());
                if (width == 0) {
                    Picasso.with(context).load(user.getPpUrl()).resize(200, 200).into(imgProfilePhoto);
                } else {
                    Picasso.with(context).load(user.getPpUrl()).resize(width, height).into(imgProfilePhoto);
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }

    public void getMovieInfos(final List<UserMovie> lst) {
        firebase.getMovies(new FirebaseReadListiner() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                likedList.clear();
                wantedList.clear();
                whatchedList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Movie movie = snapshot.getValue(Movie.class);
                    for (int i = 0; i < lst.size(); i++) {
                        if (movie.getMovieID().equals(lst.get(i).getMovieId())) {
                            switch (lst.get(i).getStatus()) {
                                case 0: {
                                    likedList.add(movie);
                                    break;
                                }
                                case 1: {
                                    whatchedList.add(movie);
                                    break;
                                }
                                case 2: {
                                    wantedList.add(movie);
                                    break;
                                }
                            }
                        }
                    }
                    likedAdapter.notifyDataSetChanged();
                    whatchedAdapter.notifyDataSetChanged();
                    wantedAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }


    public void getCurrentUserMovies() {
        firebase.getUserMovies(new FirebaseReadListiner() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                userMovieList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserMovie userMovie = snapshot.getValue(UserMovie.class);
                    userMovieList.add(userMovie);
                }
                getMovieInfos(userMovieList);
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }

    public void deneme() {
        firebase.getUserMovies(new FirebaseReadListiner() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                userMovieList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserMovie userMovie = snapshot.getValue(UserMovie.class);
                    deneme2(userMovie);
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }

    public void deneme2(final UserMovie userMovie) {
        firebase.getMovieById(userMovie.getMovieId(), new FirebaseReadListiner() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    switch (userMovie.getStatus()) {
                        case 0:
                            likedList.add(movie);
                            break;
                        case 1:
                            whatchedList.add(movie);
                            break;
                        case 2:
                            wantedList.add(movie);
                            break;
                        default:
                            break;
                    }
                    likedAdapter.notifyDataSetChanged();
                    whatchedAdapter.notifyDataSetChanged();
                    wantedAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }

    public void registerHandlers() {
        listViewClicked();
        btnClick();
    }

    public void listViewClicked() {
        movieListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String movieName = ((TextView) view.findViewById(R.id.txtMovieName)).getText().toString();
                        Intent ıntent = new Intent(context, FacebookMovieInfoActivity.class);
                        ıntent.putExtra("movieName", movieName);
                        getActivity().startActivity(ıntent);
                    }
                }
        );
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        act = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.i("CSD","Create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("CSD","Destroy");
    }

    @Override
    public void onStop() {
        super.onStop();
        likedList.clear();
        whatchedList.clear();
        wantedList.clear();
        Log.i("CSD","Stop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("CSD","Resume");
    }

    @Override
    public void onStart() {
        super.onStart();
        getUserInfo();
        deneme();
        updateposition(segmentedButtonGroup.getPosition());
        Log.i("CSD","Start");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        init(act, view);

        //getCurrentUserMovies();

        registerHandlers();
        return view;
    }
}
