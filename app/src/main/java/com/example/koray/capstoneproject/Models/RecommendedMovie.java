package com.example.koray.capstoneproject.Models;

import java.io.Serializable;

/**
 * Created by Koray on 22.12.2017.
 */

public class RecommendedMovie implements Serializable{
    String movieName,movieYear,movieId,moviePoster,moviePosterBack;

    public RecommendedMovie() {
    }

    public RecommendedMovie(String movieName, String movieYear, String movieId, String moviePoster, String moviePosterBack) {
        this.movieName = movieName;
        this.movieYear = movieYear;
        this.movieId = movieId;
        this.moviePoster = moviePoster;
        this.moviePosterBack = moviePosterBack;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMoviePosterBack() {
        return moviePosterBack;
    }

    public void setMoviePosterBack(String moviePosterBack) {
        this.moviePosterBack = moviePosterBack;
    }
}
