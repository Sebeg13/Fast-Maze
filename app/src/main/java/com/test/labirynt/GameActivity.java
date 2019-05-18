package com.test.labirynt;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements SensorEventListener {


    View view;
    FragmentInfo.onZdarzenieListener aListener;
    SensorManager mySM;
    Sensor sensor;

    private final int padding = 8;
    private final int velocity = 10;
    private final double sensitivity = 3d;
    private ArrayList<Wall> walls;
    private int grenadesIncrease = 10;
    private int grenadesGenerationFreq = 2000;

    private boolean playing = true;

    private Player player;

    private final float playerStartingX = 1;
    private final float playerStartingY = 1;

    //TO TIME
    TextView timerTextView;
    long startTime = 0;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long milis = System.currentTimeMillis() - startTime;
            int seconds = (int) (milis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };
    //-TO TIME

    ArrayList<Grenade> grenades = new ArrayList<>();

    int grenadesCounterPut = 0;
    int grenadesCounterGet = 0;
    int grenadesCounterInvis = 0;
    Handler generatorHandler = new Handler();
    Runnable generatorRunnable = new Runnable() {
        @Override
        public void run() {
//            timerTextView.setText("DZIALA CZY NIE");

//            timerHandler.postDelayed(this,2000);


            double x = Math.random() * findViewById(R.id.game_layout).getWidth() - grenades.get(grenadesCounterPut).getView().getWidth();
            double y = Math.random() * findViewById(R.id.game_layout).getHeight() - grenades.get(grenadesCounterPut).getView().getHeight();
            if (x < 0)
                x = 0;
            if (y < 0)
                y = 0;

            grenades.get(grenadesCounterPut).getView().setVisibility(View.VISIBLE);
            grenades.get(grenadesCounterPut).getView().setX((float) x);
            grenades.get(grenadesCounterPut).getView().setY((float) y);

            grenadesGenerationFreq -= grenadesIncrease;
            if (grenadesGenerationFreq < 100)
                grenadesGenerationFreq = 100;

            generatorHandler.postDelayed(this, grenadesGenerationFreq);

            //explode in 1 sec
            explodeHandler.postDelayed(explodeRunnable, 1000);


            grenadesCounterPut++;
            if (grenadesCounterPut == grenades.size())
                grenadesCounterPut = 0;

        }
    };

    Handler explodeHandler = new Handler();
    Runnable explodeRunnable = new Runnable() {
        @Override
        public void run() {

            ImageView grenadeView = (ImageView) grenades.get(grenadesCounterGet).getView();
            grenadeView.setImageResource(R.drawable.explosion);
            Grenade grenade = grenades.get(grenadesCounterGet);
            grenade.setExploded(true);
            grenade.setPos(grenadeView.getX(),
                    grenadeView.getX() + grenadeView.getWidth(),
                    grenadeView.getY(),
                    grenadeView.getY() + grenadeView.getHeight());

            willBeKilled();
//            if (willBeKilled()) {
//                Toast.makeText(getApplicationContext(), "HEHEHE", Toast.LENGTH_SHORT).show();
//                onDestroy();
//            }
            invisibleHandler.postDelayed(invisibleRunnable, 500);

            grenadesCounterGet++;
            if (grenadesCounterGet == grenades.size())
                grenadesCounterGet = 0;

        }
    };

    Handler invisibleHandler = new Handler();
    Runnable invisibleRunnable = new Runnable() {
        @Override
        public void run() {

            ImageView grenadeView = (ImageView) grenades.get(grenadesCounterGet).getView();
            grenadeView.setImageResource(R.drawable.grenade);
            Grenade grenade = grenades.get(grenadesCounterInvis);
            grenade.setExploded(false);
            grenadeView.setVisibility(View.INVISIBLE);

            grenadesCounterInvis++;
            if (grenadesCounterInvis == grenades.size())
                grenadesCounterInvis = 0;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        mySM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = mySM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mySM.registerListener(GameActivity.this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

      //  wallsAdd();
        addGrenades();
        player = new Player(findViewById(R.id.player));

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        generatorHandler.postDelayed(generatorRunnable, 0);

    }


    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        TextView textView = mainActivity.findViewById(R.id.textView);
//        TextView textView2 = mainActivity.findViewById(R.id.textView2);
//        TextView textView3 = mainActivity.findViewById(R.id.textView3);
//        TextView textView6 = mainActivity.findViewById(R.id.textView6);
//
//
//        TextView textView7 = mainActivity.findViewById(R.id.textView7);
//        TextView textView8 = mainActivity.findViewById(R.id.textView8);
//        TextView textView9 = mainActivity.findViewById(R.id.textView9);
//        TextView textView10 = mainActivity.findViewById(R.id.textView10);

        double X = event.values[0];
        double Y = event.values[1];
        double Z = event.values[2];


//         textView.setText("Left" + player.getLeft());
//         textView2.setText("Right" + player.getRight());
//         textView3.setText("Top" + player.getTop());
//         textView6.setText("Bottom" + player.getBottom());
//
//        textView7.setText("Left" + walls.get(0).getLeft());
//        textView8.setText("Right" + walls.get(0).getRight());
//        textView9.setText("Top" + walls.get(0).getTop());
//        textView10.setText("Bottom" + walls.get(0).getBottom());


//        if (player.getLeft() == 0)
//            updateWallsPos();

        playerMove(X, Y);
        //Log.d("MAIN ACTIVITY","X:" + event.values[0] + "Y:" + event.values[1] + "Z:" + event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private boolean willBeLeftCollision() {
//        updatePlayerPos();
//        for (Wall wall : walls) {
//            if (!wall.isHorizontal()) {
//                if (player.getLeft() - padding - 2 <= wall.getRight() &&
//                        player.getRight() > wall.getRight() &&
//                        (player.getTop() <= wall.getBottom() && player.getTop() >= wall.getTop() ||
//                                player.getBottom() <= wall.getBottom() && player.getBottom() >= wall.getTop()))
//                    return true;
//            } else {
//                if (player.getLeft() - padding - 2 <= wall.getRight() &&
//                        player.getRight() > wall.getRight() &&
//                        (wall.getTop() <= player.getBottom() && wall.getTop() >= player.getTop() ||
//                                wall.getBottom() <= player.getBottom() && wall.getBottom() >= player.getTop()))
//                    return true;
//            }
//        }
        return false;
    }

    private boolean willBeRightCollision() {
//        updatePlayerPos();
//
//        for (Wall wall : walls) {
//            if (!wall.isHorizontal()) {
//                if (player.getRight() + padding + 2 >= wall.getLeft() &&
//                        player.getLeft() < wall.getLeft() &&
//                        (player.getTop() <= wall.getBottom() && player.getTop() >= wall.getTop() ||
//                                player.getBottom() <= wall.getBottom() && player.getBottom() >= wall.getTop()))
//                    return true;
//            } else {
//                if (player.getRight() + padding + 2 >= wall.getLeft() &&
//                        player.getLeft() < wall.getLeft() &&
//                        (wall.getTop() <= player.getBottom() && wall.getTop() >= player.getTop() ||
//                                wall.getBottom() <= player.getBottom() && wall.getBottom() >= player.getTop()))
//                    return true;
//            }
//        }
        return false;
    }

    private boolean willBeBottomCollision() {
//        updatePlayerPos();
//        for (Wall wall : walls) {
//            if (!wall.isHorizontal()) {
//                if (player.getBottom() + padding >= wall.getTop() &&
//                        player.getTop() < wall.getTop() &&
//                        (wall.getLeft() <= player.getRight() && wall.getLeft() >= player.getLeft() ||
//                                wall.getRight() <= player.getRight() && wall.getRight() >= player.getLeft()))
//                    return true;
//            } else {
//                if (player.getBottom() + padding >= wall.getTop() &&
//                        player.getTop() < wall.getTop() &&
//                        (player.getLeft() <= wall.getRight() && player.getLeft() >= wall.getLeft() ||
//                                player.getRight() <= wall.getRight() && player.getRight() >= wall.getLeft()))
//                    return true;
//            }
//        }
        return false;
    }


    private boolean willBeTopCollision() {
//        updatePlayerPos();
//        for (Wall wall : walls) {
//            if (!wall.isHorizontal()) {
//                if (player.getTop() - padding <= wall.getBottom() &&
//                        player.getBottom() > wall.getBottom() &&
//                        (wall.getLeft() <= player.getRight() && wall.getLeft() >= player.getLeft() ||
//                                wall.getRight() <= player.getRight() && wall.getRight() >= player.getLeft()))
//                    return true;
//            } else {
//                if (player.getTop() - padding <= wall.getBottom() &&
//                        player.getBottom() > wall.getBottom() &&
//                        (player.getLeft() <= wall.getRight() && player.getLeft() >= wall.getLeft() ||
//                                player.getRight() <= wall.getRight() && player.getRight() >= wall.getLeft()))
//                    return true;
//            }
//        }
        return false;
    }


    private boolean willCrossLeftMargin() {
        if (player.getView().getX() - padding < 0)
            return true;
        return false;
    }

    private boolean willCrossUpMargin() {
        if (player.getView().getY() - padding < 0)
            return true;
        return false;
    }

    private boolean willCrossRightMargin() {
//        float mapWidth = mainActivity.findViewById(R.id.viewpager_id).getWidth();
        float mapWidth = findViewById(R.id.game_layout).getWidth();

        if (player.getView().getX() + player.getView().getWidth() + padding > mapWidth)
            return true;
        return false;
    }

    private boolean willCrossDownMargin() {
//        float mapHeight = mainActivity.findViewById(R.id.viewpager_id).getHeight();
        float mapHeight = findViewById(R.id.game_layout).getHeight();


        if (player.getView().getY() + player.getView().getHeight() + padding > mapHeight)
            return true;
        return false;

    }


    private void updatePlayerPos() {
        player.setPos(player.getView().getX(),
                player.getView().getX() + player.getView().getWidth(),
                player.getView().getY(),
                player.getView().getY() + player.getView().getHeight());
    }


    private void addGrenades() {

        grenades.add(new Grenade(findViewById(R.id.grenade1)));


    }

    private boolean willBeKilled() {


        updatePlayerPos();
        for (Grenade grenade : grenades) {
            updateGrenadePos(grenade);
           // Toast.makeText(getApplicationContext(),String.valueOf(countDistance(player,grenade)),Toast.LENGTH_SHORT).show();
            if (countDistance(player, grenade) <= player.getR())
                return true;
        }
        return false;
    }

    private double countDistance(Thing thing1, Thing thing2) {
        float x1 = thing1.getCenterX();
        float x2 = thing2.getCenterX();
        float y1 = thing1.getCenterY();
        float y2 = thing2.getCenterY();

        Toast.makeText(getApplicationContext(),String.valueOf(x2),Toast.LENGTH_SHORT).show();


        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }

    private void wallsAdd() {
        walls = new ArrayList<>();

        //HERE ADD WALLS
        walls.add(new Wall(findViewById(R.id.wall)));
        walls.add(new Wall(findViewById(R.id.wall2), true));
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

    public void updateGrenadePos(Grenade wall) {
        wall.setPos(wall.getView().getX(),
                wall.getView().getX() + wall.getView().getWidth(),
                wall.getView().getY(),
                wall.getView().getY() + wall.getView().getHeight());
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
    }
}
