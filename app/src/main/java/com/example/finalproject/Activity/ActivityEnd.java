package com.example.finalproject.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.finalproject.Logic.GameUtils;
import com.example.finalproject.Fragments.GPS;
import com.example.finalproject.Models.Score;
import com.example.finalproject.Models.ScoreList;
import com.example.finalproject.Database.Prefs;
import com.example.finalproject.R;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class ActivityEnd extends AppCompatActivity {


    private final String background = "https://opengameart.org/sites/default/files/background-1_0.png";
    private AppCompatImageView street_IMG_background;
    private TextView gameOver_LBL_result;
    private EditText gameOver_LBL_name;
    private MaterialButton gameOver_BTN_saveRecord;
    private MaterialButton gameOver_BTN_back;
    private GPS gpsService;
    private ScoreList scoreList;
    private String playerName;
    private int score;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        GameUtils.hideSystemUI(this);

        score = Objects.requireNonNull(getIntent().getExtras()).getInt("score");

        findView();
        GameUtils.setBackground(this, street_IMG_background, background);

        gameOver_LBL_result.setText("Score: " + score);


        gameOver_BTN_back.setOnClickListener(view -> finish());
        gameOver_BTN_saveRecord.setOnClickListener(view -> {
            double latitude = 0.0;
            double longitude = 0.0;

            playerName = gameOver_LBL_name.getText().toString();

            gpsService = new GPS(ActivityEnd.this);
            if (gpsService.canGetLocation()) {
                latitude = gpsService.getLatitude();
                longitude = gpsService.getLongitude();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            } else {
                gpsService.showSettingsAlert();
            }
            gameOver_LBL_name.setVisibility(View.INVISIBLE);
            gameOver_BTN_saveRecord.setVisibility(View.INVISIBLE);
            saveNewScore(playerName, score, longitude, latitude);
        });

    }

    private void findView() {

        gameOver_LBL_result = findViewById(R.id.gameOver_LBL_result);
        gameOver_LBL_name = findViewById(R.id.gameOver_LBL_name);
        street_IMG_background = findViewById(R.id.game_IMG_background);

        gameOver_BTN_saveRecord = findViewById(R.id.gameOver_BTN_saveRecord);
        gameOver_BTN_back = findViewById(R.id.gameOver_BTN_back);


    }

    private void saveNewScore(String player_name, int score, double longitude, double latitude) {
        scoreList = ScoreList.fromJson(
                Prefs.getInstance(this)
                        .getString("DB", ""));
        scoreList.addScore(new Score()
                .setName(player_name)
                .setScore(score)
                .setLat(latitude)
                .setLon(longitude)
        );
        scoreList.sortByScores();
        Prefs.getInstance(this)
                .putString("DB", scoreList.toJson());
    }
}
