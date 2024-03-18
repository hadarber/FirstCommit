package com.example.finalproject.Database;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.finalproject.Models.Score;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Prefs {


    private Gson g = new Gson();

    private static Prefs instance;
    private final SharedPreferences sharedPreferences;


    private Prefs(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
    }

    public List<Score> getScores() {
        Set<String> score_strings = sharedPreferences.getStringSet("scores", new HashSet<>());

        return score_strings
                .stream()
                .map(s -> g.fromJson(s, Score.class))
                .sorted()
                .collect(Collectors.toList());
    }
    public void saveAll(List<Score> scores) {
        HashSet<String> set = new HashSet<>();
        for (Score score : scores) {
            set.add(g.toJson(score));
        }
        sharedPreferences.edit()
                .putStringSet("scores", set)
                .apply();
    }
    public boolean addScore(Score score) {
        List<Score> scores = getScores();
        for (Score s : scores) {
            if (s.getName().equals(score.getName())) {
                if (s.getScore() < score.getScore()) {
                    s.setScore(score.getScore());
                    s.setLat(score.getLat());
                    s.setLon(score.getLon());
                    saveAll(scores);
                    return true;
                } else {
                    return false;
                }
            }
        }
        scores.add(score);
        saveAll(scores);
        return true;
    }

    public static Prefs getInstance(Context context) {
        if (instance == null) {
            instance = new Prefs(context);
        }
        return instance;
    }
    public String getString(String KEY, String defValue) {
        return sharedPreferences.getString(KEY, defValue);
    }

    public void putString(String KEY, String value) {
        sharedPreferences.edit().putString(KEY, value).apply();
    }
}
