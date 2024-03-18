package com.example.finalproject.Activity;


import static com.example.finalproject.Constants.*;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Dialogs.StartGameDialog;
import com.example.finalproject.Logic.GameUtils;
import com.example.finalproject.R;
import com.google.android.material.button.MaterialButton;

public class ActivityMenu extends AppCompatActivity {
    private MaterialButton button_button;
    private MaterialButton button_sensor;

    private MaterialButton button_button_hard;
    private MaterialButton button_sensor_hard;
    private MaterialButton button_highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_game);
        GameUtils.hideSystemUI(this);
        findView();

        clicked();
    }

    private void clicked() {
        button_button.setOnClickListener(view -> startGame(MODE_NORMAL));
        button_sensor.setOnClickListener(view -> startGame(MODE_SENSOR_NORMAL));
        button_button_hard.setOnClickListener(view -> startGame(MODE_HARD));
        button_sensor_hard.setOnClickListener(view -> startGame(MODE_SENSOR_HARD));
        button_highScores.setOnClickListener(view -> moveToScores());
    }

    private void findView() {
        button_button = findViewById(R.id.menu_BTN_button);
        button_sensor = findViewById(R.id.menu_BTN_sensor);
        button_button_hard = findViewById(R.id.menu_BTN_button_hard);
        button_sensor_hard = findViewById(R.id.menu_BTN_sensor_hard);
        button_highScores = findViewById(R.id.menu_BTN_highScores);
    }

    public void startGame(String mode) {
        StartGameDialog.show(mode, getLayoutInflater(), this);
    }

    public void moveToScores() {
        Intent ScoreIntent = new Intent(this, ActivityScores.class);
        startActivity(ScoreIntent);
    }
}
