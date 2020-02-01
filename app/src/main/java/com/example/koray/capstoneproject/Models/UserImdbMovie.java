package com.example.koray.capstoneproject.Models;

/**
 * Created by Koray on 20.12.2017.
 */

public class UserImdbMovie {
    private String movieId,movieGenre;
    private Double userRate;

    public UserImdbMovie() {

    }

    public UserImdbMovie(String movieId, String movieGenre, Double userRate) {
        this.movieId = movieId;
        this.movieGenre = movieGenre;
        this.userRate = userRate;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }

    public Double getUserRate() {
        return userRate;
    }

    public void setUserRate(Double userRate) {
        this.userRate = userRate;
    }
}
