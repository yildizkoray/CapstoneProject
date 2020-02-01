package com.example.koray.capstoneproject.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.koray.capstoneproject.Listiners.CheckFirebaseData;
import com.example.koray.capstoneproject.Listiners.FacebookLoginListiner;
import com.example.koray.capstoneproject.Listiners.FirebaseReadListiner;
import com.example.koray.capstoneproject.Models.MovieLens;
import com.example.koray.capstoneproject.Models.UserMovieLensRate;
import com.example.koray.capstoneproject.R;
import com.example.koray.capstoneproject.Utils.CalculateSimilarity;
import com.example.koray.capstoneproject.Utils.Facebook;
import com.example.koray.capstoneproject.Utils.Firebase;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {

    private Facebook facebook;
    private Firebase firebase;
    private TextView txt;

    private void init() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        facebook = new Facebook(LoginActivity.this);
        firebase = new Firebase(LoginActivity.this);
        txt = (TextView) findViewById(R.id.txtTitle);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/billabong.ttf");
        txt.setTypeface(font);
    }

    public void facebookLogin() {
        facebook.login(new FacebookLoginListiner() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebase.FirebaseLogin(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFailed(FacebookException e) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        facebookLogin();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebase.isUserLogged(firebaseUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.onActivityResult(requestCode, resultCode, data);
    }


}
