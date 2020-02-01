package com.example.koray.capstoneproject.Utils;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.koray.capstoneproject.Listiners.CheckFirebaseData;
import com.example.koray.capstoneproject.Listiners.FirebaseReadListiner;
import com.example.koray.capstoneproject.Models.Movie;
import com.example.koray.capstoneproject.Models.User;
import com.example.koray.capstoneproject.Models.UserImdbMovie;
import com.example.koray.capstoneproject.Models.UserMovie;
import com.example.koray.capstoneproject.activities.TabActivity;
import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Koray on 12.12.2017.
 */

public class Firebase {

    int count = 0;
    int count2 = 0;
    private List<UserImdbMovie> cUserImdbMovies = new ArrayList<>();
    private List<UserImdbMovie> userImdbMovies = new ArrayList<>();
    private CalculateSimilarity similarity;
    private List<UserMovie> cUserList = new ArrayList<>();
    private List<UserMovie> oUserMovieList = new ArrayList<>();
    private Facebook facebook;
    private Activity activity;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userMovieReference;

    public Firebase() {

    }

    public Firebase(Activity act) {
        activity = act;
    }

    public void FirebaseLogin(AccessToken accessToken) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(authCredential).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    isUserLogged(firebaseUser);
                }
            }
        });
    }

    public void logOut() {
        auth.signOut();
    }

    public void isUserLogged(FirebaseUser user) {
        if (user != null) {
            facebook = new Facebook();
            facebook.getUserInfo();
            facebook.getMovies();
            deneme();
            deneme2("Action");
            Intent ıntent = new Intent(activity, TabActivity.class);
            activity.startActivity(ıntent);
        }
    }


    public void saveSimilarity(User user, double simi, String movieId) {
        database.getReference("usersimi").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(user.getFirebaseID()).child("simi").setValue(simi);
        database.getReference("usersimi").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(user.getFirebaseID()).child(movieId).setValue(true);
    }


    public void saveUserMovies(final UserMovie userMovie) {
        count++;
        userMovieReference = database.getReference("usermovie").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userMovieReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int check = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserMovie uMovie = snapshot.getValue(UserMovie.class);
                        if (uMovie.getMovieId().equals(userMovie.getMovieId()) && uMovie.getStatus() == userMovie.getStatus()) {
                            check = 1;
                            count2++;
                        }
                    }
                    if (check == 0) {
                        count2++;
                        database.getReference("usermovie").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(userMovie);
                    }
                } else {
                    count2++;
                    database.getReference("usermovie").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(userMovie);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.i("Coun1:", String.valueOf(count));
        Log.i("Coun2:", String.valueOf(count2));
    }

    public void saveUserInfo(final User user) {
        database.getReference("users").child(user.getFirebaseID()).setValue(user);
    }

    public void saveMovieInfo(final Movie movie) {
        database.getReference("movies").child(movie.getMovieID()).setValue(movie);
    }

    public void getCurrentUserInfo(final FirebaseReadListiner listiner) {
        listiner.onStart();
        Query query = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listiner.onSuccess(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listiner.onFailed(databaseError);
            }
        });
    }

    public void getMovies(final FirebaseReadListiner listiner) {
        listiner.onStart();
        Query query = database.getReference("movies").orderByChild("name");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listiner.onSuccess(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listiner.onFailed(databaseError);
            }
        });
    }

    public void getUserMovies(final FirebaseReadListiner listiner) {
        listiner.onStart();
        Query query = database.getReference("usermovie").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listiner.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listiner.onFailed(databaseError);
            }
        });
    }

    public void getFacebookMovieByName(String name, final FirebaseReadListiner listiner) {
        listiner.onStart();
        database.getReference("movies").orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listiner.onSuccess(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getAllUsers(final FirebaseReadListiner listiner) {
        listiner.onStart();
        database.getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listiner.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listiner.onFailed(databaseError);
            }
        });
    }

    public void getUserMovieById(String userId, final FirebaseReadListiner listiner) {
        listiner.onStart();
        database.getReference("usermovie").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listiner.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listiner.onFailed(databaseError);
            }
        });
    }

    public void getMovieById(String id, final FirebaseReadListiner listiner) {
        listiner.onStart();
        database.getReference("movies").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listiner.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deneme() {
        getUserMovies(new FirebaseReadListiner() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserMovie userMovie = snapshot.getValue(UserMovie.class);
                        cUserList.add(userMovie);
                    }
                    getAllUsers(new FirebaseReadListiner() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    final User userUser = snap.getValue(User.class);
                                    Log.i("USERUSER", userUser.getFirstName());
                                    if (userUser.getFirebaseID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                    } else {
                                        getUserMovieById(userUser.getFirebaseID(), new FirebaseReadListiner() {
                                            @Override
                                            public void onStart() {

                                            }

                                            @Override
                                            public void onSuccess(DataSnapshot dataSnapshot) {
                                                oUserMovieList.clear();
                                                if (dataSnapshot.exists()) {
                                                    for (DataSnapshot sn : dataSnapshot.getChildren()) {
                                                        UserMovie userMovie = sn.getValue(UserMovie.class);
                                                        oUserMovieList.add(userMovie);
                                                    }
                                                }
                                                User user = new User();
                                                user = userUser;
                                                similarity = new CalculateSimilarity(user);
                                                Log.i("USER", user.getFirstName());
                                                similarity.findSameMovie(cUserList, oUserMovieList);
                                            }

                                            @Override
                                            public void onFailed(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailed(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }

    public void getSimiMovie(final FirebaseReadListiner listiner) {
        listiner.onStart();
        database.getReference("usersimi").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("simi").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listiner.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getImdbMovie(final FirebaseReadListiner listiner) {
        listiner.onStart();
        database.getReference("imdb").orderByChild("averageRating").limitToLast(25).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listiner.onSuccess(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getImdbMovieByGenres(final String genre, final FirebaseReadListiner listiner) {
        listiner.onStart();
        database.getReference("imdbgenre").child(genre).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listiner.onSuccess(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listiner.onFailed(databaseError);
            }
        });
    }

    public void getImdbMovieByMovieId(String movieId, final FirebaseReadListiner listiner) {
        listiner.onStart();
        database.getReference("imdb").child(movieId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listiner.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listiner.onFailed(databaseError);
            }
        });
    }

    public void saveUserImdbMovie(UserImdbMovie userImdbMovie, int status) {
        database.getReference("userimdbmovie")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(userImdbMovie.getMovieGenre()).child(userImdbMovie.getMovieId()).setValue(userImdbMovie, status);
    }

    public void checkRatingMovie(String movieId, String genre, final CheckFirebaseData listiner) {
        database
                .getReference("userimdbmovie")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(genre).child(movieId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listiner.onSuccess(dataSnapshot, false);
                } else {
                    listiner.onSuccess(dataSnapshot, true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUserImdbMovieByUserId(String userId,String genre,final FirebaseReadListiner listiner){
        listiner.onStart();
        database.getReference("userimdbmovie").child(userId).child(genre).orderByChild("movieId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listiner.onSuccess(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deneme2(String genre) {
        database
                .getReference("userimdbmovie")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(genre).orderByChild("movieId")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot.exists()) {
                                    UserImdbMovie userImdbMovie = snapshot.getValue(UserImdbMovie.class);
                                    if (userImdbMovie.getUserRate()!= 0.0){
                                        cUserImdbMovies.add(userImdbMovie);
                                    }
                                }
                            }
                        }
                        getAllUsers(new FirebaseReadListiner() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        final User user1 = snapshot.getValue(User.class);
                                        if (user1.getFirebaseID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                                        }else {
                                            getUserImdbMovieByUserId(user1.getFirebaseID(), "Action", new FirebaseReadListiner() {
                                                @Override
                                                public void onStart() {

                                                }

                                                @Override
                                                public void onSuccess(DataSnapshot dataSnapshot) {
                                                    userImdbMovies.clear();
                                                    if (dataSnapshot.exists()){
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                            if (snapshot.exists()){
                                                                UserImdbMovie userImdbMovie = snapshot.getValue(UserImdbMovie.class);
                                                                if (userImdbMovie.getUserRate() != 0.0){
                                                                    userImdbMovies.add(userImdbMovie);
                                                                }
                                                            }
                                                        }
                                                        User user = new User();
                                                        user = user1;
                                                        similarity = new CalculateSimilarity(user);
                                                        similarity.findCosineSimi(cUserImdbMovies,userImdbMovies);
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

                            @Override
                            public void onFailed(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseer) {

                    }
                });
    }

    public void saveCosineSimi(User user,double cosineSimilarity,String movieId,double prediction){
        database.getReference("cosineSimi").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(user.getFirebaseID()).child("Action").child(movieId).setValue(prediction);
        database.getReference("cosineSimi").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(user.getFirebaseID()).child("Action").child("cosineSimi").setValue(cosineSimilarity);
    }

    public void getCosineSimi(String genre,final FirebaseReadListiner listiner){
        listiner.onStart();
        database.getReference("cosineSimi").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("cosineSimi").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listiner.onSuccess(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
