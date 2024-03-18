package com.example.finalproject.Models;

import com.example.finalproject.Models.Score;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ScoreList {

    private ArrayList<Score> scores;

    public ScoreList() { }

    public ArrayList<Score> getScores() {
        if(scores == null){
            scores = new ArrayList<>();
        }
        return scores;
    }

    public static ScoreList fromJson(String json) {
        return new Gson().fromJson(json, ScoreList.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public void addScore(Score score) {
        getScores().add(score);
    }

    public void sortByScores() {
        scores.sort(new ByScore());
    }
}
