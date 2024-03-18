package com.example.finalproject.Models;

import android.content.Context;
import android.widget.ImageView;

import com.example.finalproject.Logic.TheGame;
import com.example.finalproject.R;

public class Player extends GameItem {
    public static final int MAX_LIFE = 3;
    private int life;
    private int lane;
    private int score;

    public Player(Context context) {
        super(new ImageView(context));
        this.life = MAX_LIFE;
        lane = TheGame.COLUMNS / 2;
        getView().setImageResource(R.drawable.harry_potter);
    }

    public void damage() {
        life--;
    }

    public int getLife() {
        return life;
    }

    public boolean moveLeft() {
        if (lane - 1 < 0) return false;
        lane--;
        return true;
    }

    public boolean moveRight() {
        if (lane + 1 >= TheGame.COLUMNS) return false;
        lane++;
        return true;
    }

    public int getLane() {
        return lane;
    }

    public void incrementScore() {
        score++;
    }

    public int getScore() {
        return score;
    }

    public boolean isDead() {
        return life <= 0;
    }
}
