package com.example.koray.capstoneproject.Models;

/**
 * Created by Koray on 27.12.2017.
 */

public class UserMovieLensRate {
    String userId,movieId;
    int rate;

    public UserMovieLensRate() {
    }

    public UserMovieLensRate(String userId, String movieId, int rate) {
        this.userId = userId;
        this.movieId = movieId;
        this.rate = rate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
