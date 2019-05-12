package com.test.labirynt;

import android.view.View;

public class Wall extends Thing {

    public boolean isHorizontal() {
        return isHorizontal;
    }

    private boolean isHorizontal = false;

    Wall(View view) {
        super(view);
    }

    Wall (View view, boolean isHorizontal) {
        super(view);
        this.isHorizontal = isHorizontal;
    }
}
