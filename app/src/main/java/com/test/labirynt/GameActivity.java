package com.test.labirynt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements SensorEventListener {


    View view;
    SensorManager mySM;
    Sensor sensor;

    private final double sensitivity = 3d;
    private final int boost = 1;
    private final int boostTimeMilisVal = 100;
    private final int maxboostTimeMilis = 500;
    private final int delayMilis = 50;

    private int boostTimeMilis = 0;



    private ArrayList<Wall> walls;

    private Player player;
    private Star star;

    private ProgressBar progressBar;

    private int milisec;
    private int seconds;
    private int minutes;


    ConstraintLayout gameLayout;

    //TO TIME
    TextView timerTextView;
    long startTime = 0;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
                long milis = System.currentTimeMillis() - startTime;
                milisec = (int) milis % 1000;
                seconds = (int) (milis / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;

                checkAndReduceBoost();
                timerTextView.setText(String.format("%d:%02d:%03d", minutes, seconds, milisec));

                timerHandler.postDelayed(this, delayMilis);
        }
    };

    //-TO TIME


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        mySM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = mySM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mySM.registerListener(GameActivity.this, sensor, SensorManager.SENSOR_DELAY_FASTEST);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(maxboostTimeMilis);

        wallsAdd();
        player = new Player(findViewById(R.id.player));
        star = new Star(findViewById(R.id.star));

        gameLayout = findViewById(R.id.game_layout);
        gameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boostTimeMilis += boostTimeMilisVal;
                Thing.setVelocity(Thing.getMainVelocity() + boost *(1+ boostTimeMilisVal/100));

            }
        });

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);


    }


    public void checkAndReduceBoost() {
        if (boostTimeMilis > 0) {
            boostTimeMilis -= delayMilis;

        } else {
            boostTimeMilis = 0;
            Thing.setVelocity(Thing.getMainVelocity());
        }

        progressBar.setProgress(boostTimeMilis);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double X = event.values[0];
        double Y = event.values[1];

        if (player.getLeft() == 0) {
            updateWallsPos();
            updateStarPos();
        }

        playerMove(X, Y);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface onZdarzenieListener {
        public void onZdarzenie();
    }


    private boolean isLeftCollect() {
        updatePlayerPos();


        return (player.getLeft() <= star.getRight() &&
                player.getRight() > star.getRight() &&
                (player.getTop() <= star.getBottom() && player.getTop() >= star.getTop() ||
                        player.getBottom() <= star.getBottom() && player.getBottom() >= star.getTop()));
    }

    private boolean isRightCollect() {
        return (player.getRight() >= star.getLeft() &&
                player.getLeft() < star.getLeft() &&
                (player.getTop() <= star.getBottom() && player.getTop() >= star.getTop() ||
                        player.getBottom() <= star.getBottom() && player.getBottom() >= star.getTop()));
    }

    private boolean isTopCollect() {
        return (player.getTop() <= star.getBottom() &&
                player.getBottom() > star.getBottom() &&
                (star.getLeft() <= player.getRight() && star.getLeft() >= player.getLeft() ||
                        star.getRight() <= player.getRight() && star.getRight() >= player.getLeft()));
    }

    private boolean isBottomCollect() {
        return (player.getBottom() >= star.getTop() &&
                player.getTop() < star.getTop() &&
                (star.getLeft() <= player.getRight() && star.getLeft() >= player.getLeft() ||
                        star.getRight() <= player.getRight() && star.getRight() >= player.getLeft()));
    }

    private boolean willBeLeftCollision() {
        updatePlayerPos();
//        updateWallsPos();
        for (Wall wall : walls) {
            if (!wall.isHorizontal()) {
                if (player.getLeft() - Thing.getVelocity() <= wall.getRight() &&
                        player.getRight() > wall.getRight() &&
                        (player.getTop() <= wall.getBottom() && player.getTop() >= wall.getTop() ||
                                player.getBottom() <= wall.getBottom() && player.getBottom() >= wall.getTop()))
                    return true;
            } else {
                if (player.getLeft() - Thing.getVelocity() <= wall.getRight() &&
                        player.getRight() > wall.getRight() &&
                        (wall.getTop() <= player.getBottom() && wall.getTop() >= player.getTop() ||
                                wall.getBottom() <= player.getBottom() && wall.getBottom() >= player.getTop()))
                    return true;
            }
        }
        return false;
    }

    private boolean willBeRightCollision() {
        updatePlayerPos();
//        updateWallsPos();
        for (Wall wall : walls) {
            if (!wall.isHorizontal()) {
                if (player.getRight() + Thing.getVelocity() >= wall.getLeft() &&
                        player.getLeft() < wall.getLeft() &&
                        (player.getTop() <= wall.getBottom() && player.getTop() >= wall.getTop() ||
                                player.getBottom() <= wall.getBottom() && player.getBottom() >= wall.getTop()))
                    return true;
            } else {
                if (player.getRight() + Thing.getVelocity() >= wall.getLeft() &&
                        player.getLeft() < wall.getLeft() &&
                        (wall.getTop() <= player.getBottom() && wall.getTop() >= player.getTop() ||
                                wall.getBottom() <= player.getBottom() && wall.getBottom() >= player.getTop()))
                    return true;
            }
        }
        return false;
    }

    private boolean willBeBottomCollision() {
        updatePlayerPos();
//        updateWallsPos();
        for (Wall wall : walls) {
            if (!wall.isHorizontal()) {
                if (player.getBottom() + Thing.getVelocity() >= wall.getTop() &&
                        player.getTop() < wall.getTop() &&
                        (wall.getLeft() <= player.getRight() && wall.getLeft() >= player.getLeft() ||
                                wall.getRight() <= player.getRight() && wall.getRight() >= player.getLeft()))
                    return true;
            } else {
                if (player.getBottom() + Thing.getVelocity() >= wall.getTop() &&
                        player.getTop() < wall.getTop() &&
                        (player.getLeft() <= wall.getRight() && player.getLeft() >= wall.getLeft() ||
                                player.getRight() <= wall.getRight() && player.getRight() >= wall.getLeft()))
                    return true;
            }
        }
        return false;
    }


    private boolean willBeTopCollision() {
        updatePlayerPos();
//        updateWallsPos();
        for (Wall wall : walls) {
            if (!wall.isHorizontal()) {
                if (player.getTop() - Thing.getVelocity() <= wall.getBottom() &&
                        player.getBottom() > wall.getBottom() &&
                        (wall.getLeft() <= player.getRight() && wall.getLeft() >= player.getLeft() ||
                                wall.getRight() <= player.getRight() && wall.getRight() >= player.getLeft()))
                    return true;
            } else {
                if (player.getTop() - Thing.getVelocity() <= wall.getBottom() &&
                        player.getBottom() > wall.getBottom() &&
                        (player.getLeft() <= wall.getRight() && player.getLeft() >= wall.getLeft() ||
                                player.getRight() <= wall.getRight() && player.getRight() >= wall.getLeft()))
                    return true;
            }
        }
        return false;
    }


    private boolean willCrossLeftMargin() {
        if (player.getView().getX() - Thing.getVelocity() < 0)
            return true;
        return false;
    }

    private boolean willCrossUpMargin() {
        if (player.getView().getY() - Thing.getVelocity() < 0)
            return true;
        return false;
    }

    private boolean willCrossRightMargin() {
//        float mapWidth = mainActivity.findViewById(R.id.viewpager_id).getWidth();
        float mapWidth = findViewById(R.id.game_layout).getWidth();

        if (player.getView().getX() + player.getView().getWidth() + Thing.getVelocity() > mapWidth)
            return true;
        return false;
    }

    private boolean willCrossDownMargin() {
//        float mapHeight = mainActivity.findViewById(R.id.viewpager_id).getHeight();
        float mapHeight = findViewById(R.id.game_layout).getHeight();


        if (player.getView().getY() + player.getView().getHeight() + Thing.getVelocity() > mapHeight)
            return true;
        return false;

    }


    private void updatePlayerPos() {
        player.setPos(player.getView().getX(),
                player.getView().getX() + player.getView().getWidth(),
                player.getView().getY(),
                player.getView().getY() + player.getView().getHeight());
    }


    private void wallsAdd() {
        walls = new ArrayList<>();

        //HERE ADD WALLS
        walls.add(new Wall(findViewById(R.id.wall)));
        walls.add(new Wall(findViewById(R.id.wall2)));
        walls.add(new Wall(findViewById(R.id.wall3)));
        walls.add(new Wall(findViewById(R.id.wall4)));
        walls.add(new Wall(findViewById(R.id.wall5)));
        walls.add(new Wall(findViewById(R.id.wall6)));
        walls.add(new Wall(findViewById(R.id.wall7), true));
        walls.add(new Wall(findViewById(R.id.wall8), true));
        walls.add(new Wall(findViewById(R.id.wall9), true));
        walls.add(new Wall(findViewById(R.id.wall10), true));
        walls.add(new Wall(findViewById(R.id.wall11), true));
        walls.add(new Wall(findViewById(R.id.wall12), true));
        walls.add(new Wall(findViewById(R.id.wall13), true));
        walls.add(new Wall(findViewById(R.id.wall14), true));
        walls.add(new Wall(findViewById(R.id.wall15), true));
        walls.add(new Wall(findViewById(R.id.wall16), true));
        walls.add(new Wall(findViewById(R.id.wall17), true));
        walls.add(new Wall(findViewById(R.id.wall18), true));


        for (Wall wall : walls) {
            wall.setPos(wall.getView().getX(),
                    wall.getView().getX() + wall.getView().getWidth(),
                    wall.getView().getY(),
                    wall.getView().getY() + wall.getView().getHeight());
        }

    }

    public void updateWallsPos() {
        for (Wall wall : walls) {
            wall.setPos(wall.getView().getX(),
                    wall.getView().getX() + wall.getView().getWidth(),
                    wall.getView().getY(),
                    wall.getView().getY() + wall.getView().getHeight());
        }
    }

    public void updateStarPos() {
        star.setPos(star.getView().getX(),
                star.getView().getX() + star.getView().getWidth(),
                star.getView().getY(),
                star.getView().getY() + star.getView().getHeight());
    }

    private void playerMove(double X, double Y) {
        if (X < -sensitivity && !willCrossRightMargin() && !willBeRightCollision()) {

            player.moveRight();
        }
        if (X > sensitivity && !willCrossLeftMargin() && !willBeLeftCollision()) {
            player.moveLeft();
        }
        if (Y < -sensitivity && !willCrossUpMargin() && !willBeTopCollision()) {
            player.moveUp();
        }
        if (Y > sensitivity && !willCrossDownMargin() && !willBeBottomCollision()) {
            player.moveDown();
        }

        if (isBottomCollect() || isLeftCollect() || isRightCollect() || isTopCollect()) {
            stopGame();
            star.getView().setVisibility(View.INVISIBLE);
        }
    }


    private void stopGame() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("time", String.format("%d:%02d:%03d", minutes, seconds, milisec));
        returnIntent.putExtra("timeMilis", String.valueOf(minutes * 60 * 100 + seconds * 1000 + milisec));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }

}
