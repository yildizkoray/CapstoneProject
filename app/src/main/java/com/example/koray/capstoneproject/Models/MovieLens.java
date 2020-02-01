package com.example.koray.capstoneproject.Models;

/**
 * Created by Koray on 27.12.2017.
 */

public class MovieLens {
    String movieId,movieImdbId;

    public MovieLens() {
    }

    public MovieLens(String movieId, String movieImdbId) {
        this.movieId = movieId;
        this.movieImdbId = movieImdbId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieImdbId() {
        return movieImdbId;
    }

    public void setMovieImdbId(String movieImdbId) {
        this.movieImdbId = movieImdbId;
    }
}
