package com.example.finalproject.Activity;

import static com.example.finalproject.Constants.*;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Logic.TheGame;
import com.example.finalproject.Logic.IGameListener;
import com.example.finalproject.Models.Score;
import com.example.finalproject.Database.Prefs;
import com.example.finalproject.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivityMain extends AppCompatActivity implements SensorEventListener, IGameListener {
    private TheGame theGame;


    private Sensor sensor;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            theGame.simpleLocation.beginUpdates();
        }
    }

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        theGame = new TheGame(this);

        FloatingActionButton moveLeftBtn = findViewById(R.id.moveLeftBtn);
        FloatingActionButton moveRightBtn = findViewById(R.id.moveRightBtn);

        moveLeftBtn.setOnClickListener((v) -> {
            theGame.moveLeft();
        });

        moveRightBtn.setOnClickListener((v) -> {
            theGame.moveRight();
        });

        String mode = getIntent().getStringExtra(MODE_ARG);
        setupSensor(mode);


        name = getIntent().getStringExtra(NAME_ARG);
        if (MODE_SENSOR_HARD.equals(mode)
                || MODE_HARD.equals(mode)) {
            theGame.setSpawnSpeed(10.0f);
        }
    }

    void setupSensor(String mode ) {
        SensorManager manager = getSystemService(SensorManager.class);
        if (MODE_SENSOR_HARD.equals(mode)
                || MODE_SENSOR_NORMAL.equals(mode)) {
            sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }


    long prevUpdateTime = 0;
    float xLast = 0;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float xNew = sensorEvent.values[0];
        long now = System.currentTimeMillis();
        if (Math.abs(xNew - xLast) > 0.5f) {
            if (now - prevUpdateTime > 400) {
                prevUpdateTime = now;
                if (xNew - xLast > 0) {
                    theGame.moveRight();
                } else {
                    theGame.moveLeft();
                }
                xLast = xNew;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        theGame.setGameOver(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        theGame.setGameOver(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        theGame.startGame();
    }

    @Override
    public void gameOver() {
        LatLng location = theGame.getLocation();
        int score = theGame.getScore();
        Score scoreObject = new Score();
        scoreObject.setName(name);
        scoreObject.setLat(location.latitude);
        scoreObject.setLon(location.longitude);
        scoreObject.setDate(System.currentTimeMillis());
        scoreObject.setScore(score);
        if (Prefs.getInstance(this).addScore(scoreObject)) {
            Toast.makeText(this, "New personal best : " + score, Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}