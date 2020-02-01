package com.example.koray.capstoneproject.Models;

/**
 * Created by Koray on 12.12.2017.
 */

public class UserMovie {
    private String movieId;
    private int status;

    public UserMovie() {
    }

    public UserMovie(String movieId, int status) {
        this.movieId = movieId;
        this.status = status;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
