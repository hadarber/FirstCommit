package com.example.finalproject.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Adapter.ScoreAdapter;
import com.example.finalproject.Fragments.FragmentMap;
import com.example.finalproject.Logic.ScoreClickListener;
import com.example.finalproject.Models.Score;
import com.example.finalproject.R;
import com.example.finalproject.Fragments.FragmentScoreList;

public class ActivityScores extends AppCompatActivity implements ScoreClickListener {

    private FragmentMap fragmentMap;
    private FragmentScoreList fragmentScoreList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        fragmentMap = new FragmentMap();
        fragmentScoreList = new FragmentScoreList();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mapContainer, fragmentMap)
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.scoresFragment, fragmentScoreList)
                .commit();

        findViewById(R.id.backToMenu).setOnClickListener(view -> finish());
    }


    @Override
    public void onScoreClicked(Score score) {
        fragmentMap.zoom(score);
    }
}
