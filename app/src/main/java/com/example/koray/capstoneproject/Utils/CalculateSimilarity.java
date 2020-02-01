package com.example.koray.capstoneproject.Utils;

import com.example.koray.capstoneproject.Models.User;
import com.example.koray.capstoneproject.Models.UserImdbMovie;
import com.example.koray.capstoneproject.Models.UserMovie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koray on 16.12.2017.
 */

public class CalculateSimilarity {
    private User user;
    private List<UserImdbMovie> dMovie = new ArrayList<>();
    private List<UserImdbMovie> sMovie = new ArrayList<>();
    private List<String> sameMovie = new ArrayList<>();
    private List<String> difMovie = new ArrayList<>();
    private Firebase firebase = new Firebase();
    private List<Double> cUserRateList = new ArrayList<>();
    private List<Double> userRateList = new ArrayList<>();
    private double meanCenterToplamCUser = 0, meanCenterToplamUUser = 0, meanCenterC, meanCenterU;
    private double dotProduct = 0;
    private double normCUser = 0, normUUser = 0;


    public CalculateSimilarity() {

    }

    public void calculatePrediction(List<UserImdbMovie> dMovie, double simi) {
        for (int i = 0; i < dMovie.size(); i++) {
            double predictRate = meanCenterC + (simi * (dMovie.get(i).getUserRate() - meanCenterU)) / simi;
            firebase.saveCosineSimi(user, simi, dMovie.get(i).getMovieId(), predictRate);
        }

    }

    public CalculateSimilarity(User user) {
        this.user = user;
    }

    public void findCosineSimi(List<UserImdbMovie> cUserImdbMovieList, List<UserImdbMovie> userImdbMovieList) {
        for (int i = 0; i < cUserImdbMovieList.size(); i++) {
            meanCenterToplamCUser = meanCenterToplamCUser + cUserImdbMovieList.get(i).getUserRate();
        }
        for (int i = 0; i < userImdbMovieList.size(); i++) {
            meanCenterToplamUUser = meanCenterToplamUUser + userImdbMovieList.get(i).getUserRate();
        }
        meanCenterC = meanCenterToplamCUser / cUserImdbMovieList.size();
        meanCenterU = meanCenterToplamUUser / userImdbMovieList.size();


        for (int i = 0; i < userImdbMovieList.size(); i++) {
            int check = 0;
            for (int j = 0; j < cUserImdbMovieList.size(); j++) {
                if (userImdbMovieList.get(i).getMovieId().equals(cUserImdbMovieList.get(j).getMovieId())) {
                    sMovie.add(cUserImdbMovieList.get(j));
                    cUserRateList.add(cUserImdbMovieList.get(j).getUserRate());
                    userRateList.add(userImdbMovieList.get(i).getUserRate());
                    check = 1;
                }
            }
            if (check == 0) {
                dMovie.add(userImdbMovieList.get(i));
            }
        }
        calculateCosineSimi(cUserRateList, userRateList);
    }

    public void calculateCosineSimi(List<Double> cUserRate, List<Double> userRate) {
        for (int i = 0; i < cUserRate.size(); i++) {
            dotProduct = dotProduct + (cUserRate.get(i) * userRate.get(i));
            normCUser = (float) (normCUser + (Math.pow(cUserRate.get(i), 2)));
            normUUser = (float) (normUUser + (Math.pow(userRate.get(i), 2)));
        }
        double cosineSimilarity = (dotProduct / (Math.sqrt(normCUser * normUUser)));
        calculatePrediction(dMovie, cosineSimilarity);
    }

    public void findSameMovie(List<UserMovie> currentUserList, List<UserMovie> userMovieList) {
        for (int i = 0; i < userMovieList.size(); i++) {
            int check = 0;
            for (int j = 0; j < currentUserList.size(); j++) {
                if (userMovieList.get(i).getMovieId().equals(currentUserList.get(j).getMovieId())) {
                    sameMovie.add(currentUserList.get(j).getMovieId().toString());
                    check = 1;
                }
            }
            if (check == 0) {
                difMovie.add(userMovieList.get(i).getMovieId());
            }
        }
        calSame(currentUserList.size(), userMovieList.size(), sameMovie.size(), difMovie);
    }


    public void calSame(double cUserListSize, double oUserListSize, double sListSize, List<String> difMovies) {
        double similarity = (sListSize / (cUserListSize + oUserListSize)) * 100;
        for (int i = 0; i < difMovies.size(); i++) {
            firebase.saveSimilarity(user, similarity, difMovies.get(i).toString());
        }
    }


}
