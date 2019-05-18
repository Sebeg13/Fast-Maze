package com.test.labirynt;

import android.view.View;

public class Grenade extends Thing {
    private boolean exploded = false;
    Grenade(View view) {
        super(view);
    }

    public void setExploded(boolean exploded){
        this.exploded = exploded;
    }

    public boolean getExploded(){
        return exploded;
    }

}
