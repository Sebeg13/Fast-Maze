package com.test.labirynt;

import android.view.View;

public class Thing {
    private View view;
    private int r;

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    private float centerX;
    private float centerY;

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }



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
    private final static int velocity = 10;

    Thing(View view) {
        this.view = view;
        r = view.getWidth()/2;
    }
    public View getView() {
        return view;
    }

    public void setPos(Float left, Float right, Float top, Float bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;

        centerX = (right - left)/2;
        centerY = (bottom-top)/2;
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

}
