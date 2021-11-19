package ch.zli.m335.flyingdude.model;

import android.graphics.Bitmap;

public class Dude {
    private float x,y;
    private Bitmap img;

    public Dude(float x, float y, Bitmap bitmap) {
        this.x = x;
        this.y = y;
        this.img = bitmap;
    }

    public Bitmap getImage() {
        return img;
    }

    public void setPosition(float[] coords) {
        this.x = coords[0];
        this.y = coords[1];
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
