package com.example.koray.capstoneproject.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.koray.capstoneproject.Listiners.FacebookLoginListiner;
import com.example.koray.capstoneproject.Models.Movie;
import com.example.koray.capstoneproject.Models.User;
import com.example.koray.capstoneproject.Models.UserMovie;
import com.example.koray.capstoneproject.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Koray on 12.12.2017.
 */

public class Facebook {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Activity activity;
    private Firebase firebase = new Firebase();
    private String movieFBID, movieFBName, movieFBGenre, movieFBDirector, movieFBWriter, movieFBBth, movieURL, movieFBStarring, isOfficial, movieFBStory;


    public Facebook() {

    }

    public Facebook(Activity act) {
        activity = act;
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) act.findViewById(R.id.btnLogin);
    }

    public void login(final FacebookLoginListiner listiner) {
        loginButton.setReadPermissions("email", "user_birthday", "user_likes", "user_actions.video");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                listiner.onSuccess(loginResult);
            }

            @Override
            public void onCancel() {
                listiner.onCancel();
            }

            @Override
            public void onError(FacebookException error) {
                listiner.onFailed(error);
            }
        });
    }

    public void getMovieInfo(final String movieID, final int check) {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + movieID,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            JSONObject jsonObject = response.getJSONObject();
                            try {
                                if (jsonObject.has("verification_status")) {
                                    isOfficial = jsonObject.getString("verification_status");

                                    if (isOfficial.equals("blue_verified")) {
                                        movieFBID = jsonObject.getString("id");
                                        movieFBName = jsonObject.getString("name");
                                        if (jsonObject.has("genre")) {
                                            movieFBGenre = jsonObject.getString("genre");
                                        } else {
                                            movieFBGenre = "";
                                        }

                                        if (jsonObject.has("description")) {
                                            movieFBStory = jsonObject.getString("description");
                                        } else {
                                            movieFBStory = "";
                                        }
                                        if (jsonObject.has("starring")) {
                                            movieFBStarring = jsonObject.getString("starring");
                                        } else {
                                            movieFBStarring = "";
                                        }
                                        if (jsonObject.has("directed_by")) {
                                            movieFBDirector = jsonObject.getString("directed_by");
                                        } else {
                                            movieFBDirector = "";
                                        }
                                        if (jsonObject.has("written_by")) {
                                            movieFBWriter = jsonObject.getString("written_by");
                                        } else {
                                            movieFBWriter = "";
                                        }
                                        if (jsonObject.has("birthday")) {
                                            movieFBBth = jsonObject.getString("birthday");
                                        } else {
                                            movieFBBth = "";
                                        }
                                        movieURL = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
                                        Movie movie = new Movie(movieFBID, movieFBName, movieFBGenre, movieFBDirector, movieFBWriter, movieFBBth, isOfficial, movieFBStarring, movieFBStory, movieURL);
                                        UserMovie userMovie = new UserMovie(movieID, check);
                                        firebase.saveMovieInfo(movie);
                                        firebase.saveUserMovies(userMovie);
                                    } else {

                                    }
                                } else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,genre,directed_by,written_by,birthday,starring,verification_status,description,picture.width(1080).height(1080)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void getUserInfo() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response != null) {
                            object = response.getJSONObject();
                            try {
                                String fName = object.getString("first_name");
                                String lName = object.getString("last_name");
                                String fullName = object.getString("name");
                                String email = object.getString("email");
                                String gender = object.getString("gender");
                                String ID = object.getString("id");
                                String bDay = object.getString("birthday");
                                String FireID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                String ppUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                User user = new User(FireID, ID, fName, lName, fullName, email, bDay, gender, ppUrl);
                                firebase.saveUserInfo(user);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,email,birthday,gender,picture.width(1024).height(1024)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void getMovies() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response != null) {
                            object = response.getJSONObject();
                            try {
                                if (object.has("movies")) {
                                    JSONObject likedMovies = object.getJSONObject("movies");
                                    JSONArray arrayLikedMovies = likedMovies.getJSONArray("data");
                                    for (int i = 0; i < arrayLikedMovies.length(); i++) {
                                        String id = arrayLikedMovies.getJSONObject(i).getString("id");
                                        getMovieInfo(id, 0);
                                    }
                                }
                                if (object.has("video.watches")) {
                                    JSONObject watchedMovies = object.getJSONObject("video.watches");
                                    JSONArray arrayWatchedMovies = watchedMovies.getJSONArray("data");
                                    for (int i = 0; i < arrayWatchedMovies.length(); i++) {
                                        if (arrayWatchedMovies.getJSONObject(i).getJSONObject("data").has("movie")) {
                                            String id = arrayWatchedMovies.getJSONObject(i).getJSONObject("data").getJSONObject("movie").getString("id");
                                            getMovieInfo(id, 1);
                                        }
                                    }
                                }
                                if (object.has("video.wants_to_watch")) {
                                    JSONObject watchedMovies = object.getJSONObject("video.wants_to_watch");
                                    JSONArray arrayWatchedMovies = watchedMovies.getJSONArray("data");
                                    for (int i = 0; i < arrayWatchedMovies.length(); i++) {
                                        if (arrayWatchedMovies.getJSONObject(i).getJSONObject("data").has("movie")) {
                                            String id = arrayWatchedMovies.getJSONObject(i).getJSONObject("data").getJSONObject("movie").getString("id");
                                            getMovieInfo(id, 2);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "movies.limit(100){id},video.watches,video.wants_to_watch");
        request.setParameters(parameters);
        request.executeAsync();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void logOut() {
        LoginManager.getInstance().logOut();
    }
}
