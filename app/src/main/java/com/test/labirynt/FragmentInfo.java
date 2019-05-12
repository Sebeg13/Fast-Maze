package com.test.labirynt;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentInfo extends Fragment implements  SensorEventListener {
    View view;
    onZdarzenieListener aListener;
    MainActivity mainActivity;
    SensorManager mySM;
    Sensor sensor;

    private final int padding =8 ;
    private final int velocity = 10;
    private final double sensitivity = 3d;
    private ArrayList<Wall> walls;

    private Player player;

    private final float playerStartingX=1;
    private final float playerStartingY=1;



    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView textView = mainActivity.findViewById(R.id.textView);
        TextView textView2 = mainActivity.findViewById(R.id.textView2);
        TextView textView3 = mainActivity.findViewById(R.id.textView3);
        TextView textView6 = mainActivity.findViewById(R.id.textView6);


        TextView textView7 = mainActivity.findViewById(R.id.textView7);
        TextView textView8 = mainActivity.findViewById(R.id.textView8);
        TextView textView9 = mainActivity.findViewById(R.id.textView9);
        TextView textView10 = mainActivity.findViewById(R.id.textView10);

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


        if(player.getLeft()==0)
            updateWallsPos();

        playerMove(X,Y);
        //Log.d("MAIN ACTIVITY","X:" + event.values[0] + "Y:" + event.values[1] + "Z:" + event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface onZdarzenieListener {
        public void onZdarzenie();
    }

    public FragmentInfo() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            Activity activity = (Activity) context;
            aListener = (onZdarzenieListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_fragment, container, false);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mySM = mainActivity.getMySM();
        sensor = mainActivity.getMySensor();
        mySM.registerListener(FragmentInfo.this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        wallsAdd();
        player = new Player(mainActivity.findViewById(R.id.player));
    }


    private boolean willBeLeftCollision() {
        updatePlayerPos();
//        updateWallsPos();
        for(Wall wall:walls){
            if(!wall.isHorizontal()) {
                if (player.getLeft() - padding - 2 <= wall.getRight() &&
                        player.getRight() > wall.getRight() &&
                        (player.getTop() <= wall.getBottom() && player.getTop() >= wall.getTop() ||
                                player.getBottom() <= wall.getBottom() && player.getBottom() >= wall.getTop()))
                    return true;
            }else{
                if (player.getLeft() - padding - 2 <= wall.getRight() &&
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
        for(Wall wall:walls){
            if(!wall.isHorizontal()) {
                if (player.getRight() + padding + 2 >= wall.getLeft() &&
                        player.getLeft() < wall.getLeft() &&
                        (player.getTop() <= wall.getBottom() && player.getTop() >= wall.getTop() ||
                                player.getBottom() <= wall.getBottom() && player.getBottom() >= wall.getTop()))
                    return true;
            }else{
                if (player.getRight() + padding + 2>= wall.getLeft() &&
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
        for(Wall wall:walls){
            if(!wall.isHorizontal()) {
                if (player.getBottom() + padding >= wall.getTop() &&
                        player.getTop() < wall.getTop() &&
                        (wall.getLeft() <= player.getRight() && wall.getLeft() >= player.getLeft() ||
                                wall.getRight() <= player.getRight() && wall.getRight() >= player.getLeft()))
                    return true;
            }else{
                if (player.getBottom() + padding >= wall.getTop() &&
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
        for(Wall wall:walls){
            if(!wall.isHorizontal()) {
                if (player.getTop() - padding  <= wall.getBottom() &&
                        player.getBottom() > wall.getBottom() &&
                        (wall.getLeft() <= player.getRight() && wall.getLeft() >= player.getLeft() ||
                                wall.getRight() <= player.getRight() && wall.getRight() >= player.getLeft()))
                    return true;
            }else{
                if (player.getTop() - padding <= wall.getBottom() &&
                        player.getBottom() > wall.getBottom() &&
                        (player.getLeft() <= wall.getRight() && player.getLeft() >= wall.getLeft() ||
                                player.getRight() <= wall.getRight() && player.getRight() >= wall.getLeft()))
                    return true;
            }
        }
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
        float mapWidth = mainActivity.findViewById(R.id.viewpager_id).getWidth();

        if (player.getView().getX() + player.getView().getWidth() + padding > mapWidth)
            return true;
        return false;
    }

    private boolean willCrossDownMargin() {
        float mapHeight = mainActivity.findViewById(R.id.viewpager_id).getHeight();
        if (player.getView().getY() + player.getView().getHeight() + padding > mapHeight)
            return true;
        return false;

    }


    private void updatePlayerPos(){
        player.setPos(player.getView().getX(),
                player.getView().getX()+player.getView().getWidth(),
                player.getView().getY(),
                player.getView().getY()+player.getView().getHeight());
    }


    private void wallsAdd(){
        walls = new ArrayList<>();

        //HERE ADD WALLS
        walls.add(new Wall(mainActivity.findViewById(R.id.wall)));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall2),true));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall3)));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall4)));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall5)));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall6)));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall7),true));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall8),true));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall9),true));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall10),true));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall11),true));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall12),true));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall13),true));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall14),true));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall15),true));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall16),true));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall17),true));
        walls.add(new Wall(mainActivity.findViewById(R.id.wall18),true));


        for(Wall wall : walls){
            wall.setPos(wall.getView().getX(),
                    wall.getView().getX()+wall.getView().getWidth(),
                    wall.getView().getY(),
                    wall.getView().getY()+wall.getView().getHeight());
        }

    }

    public void updateWallsPos(){
        for(Wall wall : walls){
            wall.setPos(wall.getView().getX(),
                    wall.getView().getX()+wall.getView().getWidth(),
                    wall.getView().getY(),
                    wall.getView().getY()+wall.getView().getHeight());
        }
    }

    private void playerMove(double X, double Y){
        if (X < -sensitivity && !willCrossRightMargin() && !willBeRightCollision()) {

            player.moveRight();
        }
        if (X > sensitivity && !willCrossLeftMargin() && !willBeLeftCollision()) {
            player.moveLeft();
        }
        if (Y < -sensitivity && !willCrossUpMargin() && !willBeTopCollision())  {
            player.moveUp();
        }
        if (Y > sensitivity && !willCrossDownMargin() && !willBeBottomCollision()) {
            player.moveDown();
        }
    }

}
