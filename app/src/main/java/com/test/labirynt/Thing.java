package com.test.labirynt;

import android.view.View;

public class Thing {
    private View view;

    public Float getLeft() {
        return left;
    }

    public Float getRight() {
        return right;
    }

    public Float getBottom() {
        return bottom;
    }

    public Float getTop() {
        return top;
    }

    private Float left=0f;
    private Float right=0f;
    private Float bottom=0f;
    private Float top=0f;


    public static int getMainVelocity() {
        return mainVelocity;
    }

    private static final int mainVelocity = 4;
    private static int velocity = 4;

    Thing(View view) {
        this.view = view;
    }
    public View getView() {
        return view;
    }

    public void setPos(Float left, Float right, Float top, Float bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }
    public void moveRight(){
        view.setX(view.getX() + velocity);
    }
    public void moveLeft(){
        view.setX(view.getX() - velocity);
    }
    public void moveUp(){
        view.setY(view.getY() - velocity);
    }
    public void moveDown(){
        view.setY(view.getY() + velocity);
    }

    public static void setVelocity(int velocity) {
        Thing.velocity = velocity;
    }

    public static int getVelocity() {
        return velocity;
    }
}
