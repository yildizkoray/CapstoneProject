package com.example.koray.capstoneproject.Models;

/**
 * Created by Koray on 12.12.2017.
 */

public class Movie {
    String movieID,name,genre,director,writer,birthday,isOfficial,starring,story, posterUrl;

    public Movie() {

    }

    public Movie(String movieID, String name, String genre, String director, String writer, String birthday, String isOfficial, String starring, String story, String posterUrl) {
        this.movieID = movieID;
        this.name = name;
        this.genre = genre;
        this.director = director;
        this.writer = writer;
        this.birthday = birthday;
        this.isOfficial = isOfficial;
        this.starring = starring;
        this.story = story;
        this.posterUrl = posterUrl;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(String isOfficial) {
        this.isOfficial = isOfficial;
    }

    public String getStarring() {
        return starring;
    }

    public void setStarring(String starring) {
        this.starring = starring;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
