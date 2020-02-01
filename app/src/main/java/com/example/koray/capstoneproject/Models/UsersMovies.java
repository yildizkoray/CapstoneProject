package com.example.koray.capstoneproject.Models;

import java.util.List;

/**
 * Created by Koray on 16.12.2017.
 */

public class UsersMovies {
    private User user;
    private List<UserMovie> userMovieList;

    public UsersMovies() {
    }

    public UsersMovies(User user, List<UserMovie> userMovieList) {
        this.user = user;
        this.userMovieList = userMovieList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserMovie> getUserMovieList() {
        return userMovieList;
    }

    public void setUserMovieList(List<UserMovie> userMovieList) {
        this.userMovieList = userMovieList;
    }
}
