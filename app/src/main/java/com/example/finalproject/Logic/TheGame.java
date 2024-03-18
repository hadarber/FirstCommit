package com.example.finalproject.Logic;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.finalproject.Activity.ActivityMain;
import com.example.finalproject.Models.Empty;
import com.example.finalproject.Models.GameItem;
import com.example.finalproject.Models.Magic;
import com.example.finalproject.Models.Player;
import com.example.finalproject.Models.Enemy;
import com.example.finalproject.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Random;

import im.delight.android.location.SimpleLocation;

public class TheGame {
    public static final int COLUMNS = 5;
    public static final int ROWS = 12;
    private float SPAWN_SPEED = 5;
    private final GridLayout gameLayout;
    private final LinearLayout lifeLayout;
    private final TextView scoresTv;
    private final Player player;
    private final int[] enemy_map, active_map, magic_map, active_magic_map;
    private final Random r = new Random();
    private final Handler handler;
    private GameItem[][] matrix;

    boolean isGameOver = false;

    public SimpleLocation simpleLocation;

    private IGameListener gameListener;

    private ArrayList<Toast> msjsToast = new ArrayList<>();

    private void killAllToast(){
        for(Toast t:msjsToast){
            if(t!=null) {
                t.cancel();
            }
        }
        msjsToast.clear();
    }

    @SuppressLint("SetTextI18n")
    public TheGame(ActivityMain activity) {
        simpleLocation = new SimpleLocation(activity);
        requestLocationPermissions(activity);
        gameListener = activity;
        gameLayout = activity.findViewById(R.id.game_layout);
        lifeLayout = activity.findViewById(R.id.life_layout);
        scoresTv = activity.findViewById(R.id.scoreTv);
        scoresTv.setText("Score: " + 0);
        enemy_map = new int[COLUMNS];
        magic_map = new int[COLUMNS];
        active_map = new int[COLUMNS];
        active_magic_map = new int[COLUMNS];
        gameLayout.setColumnCount(COLUMNS);
        gameLayout.setRowCount(ROWS);
        handler = new Handler();
        matrix = new GameItem[ROWS][COLUMNS];
        for (int col = 0; col < COLUMNS; col++) {
            set(0, col, new Enemy(activity));
        }
        for (int col = 0; col < COLUMNS; col++) {
            magic_map[col] = 1;
        }
        player = new Player(activity);
        set(ROWS - 1, COLUMNS / 2, player);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (matrix[row][col] == null) {
                    set(row, col, new Empty(activity));
                }
                gameLayout.addView(matrix[row][col].getView());
            }
        }
    }


    private void resetEnemy(int lane, int row) {
        // reset enemy
        enemy_map[lane] = 0;
        swap(row, lane, 0, lane);
    }

    private void resetMagic(int lane, int row) {
        // reset enemy
        magic_map[lane] = 1;
        swap(row, lane, 1, lane);
    }

    private boolean moveEnemy(int lane) {
        int current_row = enemy_map[lane];
        int new_row = current_row + 1;
        if (new_row >= ROWS) {
            // reset enemy
            resetEnemy(lane, current_row);
            return false;
        } else {
            enemy_map[lane]++;
            if (matrix[new_row][lane] instanceof Player) {
                if (player.getLife() == 1) {
                    killAllToast();
                    Toast.makeText(gameLayout.getContext(), "You died.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast t = Toast.makeText(gameLayout.getContext(), "You got damaged!", Toast.LENGTH_SHORT);
                    t.show();
                    msjsToast.add(t);
                }
                player.damage();
                drawLives();
                if (player.isDead()) {
                    gameOver();
                }
                resetEnemy(lane, current_row);
                return false;
            } else if (matrix[new_row][lane] instanceof Magic) {
                resetEnemy(lane, current_row);
                return false;
            }
            checkEnemyHit(lane, new_row);
            swap(current_row, lane, new_row, lane);
            return true;
        }
    }

    private void requestLocationPermissions(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(context, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 101);
        } else {
            simpleLocation.beginUpdates();
        }
    }

    private void gameOver() {
        isGameOver = true;
        gameListener.gameOver();
    }

    @SuppressLint("SetTextI18n")
    private boolean moveMagic(int lane) {
        int current_row = magic_map[lane];
        int new_row = current_row + 1;

        if (new_row >= ROWS || matrix[new_row][lane] instanceof Enemy) {
            // reset enemy
            if (new_row < ROWS)
                resetEnemy(lane, new_row);
            else
                resetMagic(lane, current_row);
            return true;
        } else if (matrix[new_row][lane] instanceof Magic) {
            return true;
        } else {
            magic_map[lane]++;
            if (matrix[new_row][lane] instanceof Player) {
                // reset enemy
                Toast t = Toast.makeText(gameLayout.getContext(), "You got a score!", Toast.LENGTH_SHORT);
                t.show();
                msjsToast.add(t);
                player.incrementScore();
                scoresTv.setText("Score: " + player.getScore());
                resetMagic(lane, current_row);
                return false;
            }
            checkMagicHit(lane, new_row);
            swap(current_row, lane, new_row, lane);
            return true;
        }
    }

    public void moveLeft() {
        int prevLane = player.getLane();
        if (player.moveLeft()) {
            checkPlayerHit();
            swap(ROWS - 1, prevLane, ROWS - 1, player.getLane());
        }
    }

    public LatLng getLocation() {
        return new LatLng(simpleLocation.getLatitude(), simpleLocation.getLongitude());
    }

    private void checkPlayerHit() {
        int lane = player.getLane();
        if (matrix[ROWS - 1][lane] instanceof Enemy) {
            player.damage();
            drawLives();
            if (player.isDead()) {
                gameOver();
            } else {
                Toast t = Toast.makeText(gameLayout.getContext(), "Direct hit!", Toast.LENGTH_SHORT);
                t.show();
                msjsToast.add(t);
            }
            resetEnemy(lane, enemy_map[lane]);
        } else if (matrix[ROWS - 1][lane] instanceof Magic) {
            // reset enemy
            Toast t = Toast.makeText(gameLayout.getContext(), "You got a score!", Toast.LENGTH_SHORT);
            t.show();
            msjsToast.add(t);
            player.incrementScore();
            resetMagic(lane, magic_map[lane]);
        }
    }

    private void checkEnemyHit(int lane, int newRow) {

        if (matrix[newRow][lane] instanceof Player) {
            player.damage();
            drawLives();
            resetEnemy(lane, newRow - 1);
        }
    }

    private void checkMagicHit(int lane, int newRow) {
        if (matrix[newRow][lane] instanceof Player) {
            Toast t = Toast.makeText(gameLayout.getContext(), "You got another score!", Toast.LENGTH_SHORT);
            t.show();
            msjsToast.add(t);
            player.incrementScore();
            resetMagic(lane, newRow - 1);
        }
    }

    public void moveRight() {
        int prevLane = player.getLane();
        if (player.moveRight()) {
            checkPlayerHit();
            swap(ROWS - 1, prevLane, ROWS - 1, player.getLane());
        }
    }

    private void set(int row, int col, GameItem object) {
        matrix[row][col] = object;
        object.putPosition(row, col);
    }

    private void swap(int r1, int c1, int r2, int c2) {
        GameItem object = matrix[r1][c1];
        set(r1, c1, matrix[r2][c2]);
        set(r2, c2, object);
    }

    public void drawLives() {
        for (int i = 0; i < 3; i++) {
            lifeLayout.getChildAt(i).setVisibility(View.INVISIBLE);
        }
        for (int i = 0; i < player.getLife(); i++) {
            lifeLayout.getChildAt(i).setVisibility(View.VISIBLE);
        }
    }


    // needs the lane so encapsulate in class
    class EnemyMove implements Runnable {
        private final int lane;

        public EnemyMove(int lane) {
            this.lane = lane;
        }

        @Override
        public void run() {
            if (isGameOver) return;
            if (!moveEnemy(lane)) {
                active_map[lane] = 0;
            } else {
                handler.postDelayed(new EnemyMove(lane), (long) (5000 / SPAWN_SPEED));
            }
        }
    }


    // needs the lane and prev magic so encapsulate in class
    class MagicMove implements Runnable {
        private final int lane;
        private GameItem magic;

        public MagicMove(int lane, GameItem magic) {
            this.lane = lane;
            this.magic = magic;
        }

        @Override
        public void run() {
            if (isGameOver) return;
            if (!moveMagic(lane)) {
                active_magic_map[lane] = 0;
                magic_map[lane] = 1;
                gameLayout.removeView(magic.getView());
            } else {
                handler.postDelayed(new MagicMove(lane, magic), (long) (5000 / SPAWN_SPEED));
            }
        }
    }


    public int getScore() {
        return player.getScore();
    }

    public void setGameOver(boolean b) {
        this.isGameOver = b;
    }

    public void startGame() {
        this.isGameOver = false;
        // magics
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isGameOver) return;
                int random_lane = r.nextInt(COLUMNS);
                handler.postDelayed(this, (long) (15000 / SPAWN_SPEED));
                if (active_magic_map[random_lane] != 0) {
                    return;
                }
                if (matrix[1][random_lane] instanceof Enemy) {
                    return;
                }
                GameItem magic = new Magic(gameLayout.getContext());
                gameLayout.addView(magic.getView());
                set(1, random_lane, magic);
                active_magic_map[random_lane] = 1;
                handler.postDelayed(new MagicMove(random_lane, magic), (long) (15000 / SPAWN_SPEED));
            }
        }, (long) (15000 / SPAWN_SPEED));
        // enemies
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isGameOver) return;
                int random_lane = r.nextInt(COLUMNS);
                handler.postDelayed(this, (long) (5000 / SPAWN_SPEED));
                if (active_map[random_lane] != 0) {
                    return;
                }
                active_map[random_lane] = 1;
                handler.postDelayed(new EnemyMove(random_lane), (long) (5000 / SPAWN_SPEED));
            }
        }, (long) (5000 / SPAWN_SPEED));
    }

    public void setSpawnSpeed(float spawnSpeed) {
        this.SPAWN_SPEED = spawnSpeed;
    }
}

