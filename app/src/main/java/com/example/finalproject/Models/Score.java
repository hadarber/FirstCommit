package com.example.finalproject.Models;

import java.util.Objects;

public class Score implements Comparable<Score> {

    private String name;
    private int score;
    private double lat;
    private double lon;

    private long date;
    public Score() { }

    public Score(String name, int score, double lat, double lon) {
        this.name = name;
        this.score = score;
        this.lat = lat;
        this.lon = lon;
        date = System.currentTimeMillis();
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name with fluent interface
    public Score setName(String name) {
        this.name = name;
        return this;
    }

    // Getter for score
    public int getScore() {
        return score;
    }

    // Setter for score with fluent interface
    public Score setScore(int score) {
        this.score = score;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Score setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public Score setLon(double lon) {
        this.lon = lon;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score1 = (Score) o;
        return Objects.equals(name, score1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, score, lat, lon);
    }

    @Override
    public int compareTo(Score score) {
        return Integer.compare(score.getScore(), this.score);
    }
}
