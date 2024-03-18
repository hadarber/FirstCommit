package com.example.finalproject.Models;


import java.util.Comparator;

public class ByScore implements Comparator<Score> {

    @Override
    public int compare(Score rec1, Score rec2) {

        return rec2.getScore() - rec1.getScore();
    }
}
