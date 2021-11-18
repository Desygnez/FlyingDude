package ch.zli.m335.flyingdude.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import ch.zli.m335.flyingdude.R;
import ch.zli.m335.flyingdude.model.Background;

public class BackgroundView {
    Background bg;
    Thread renderThread;
    SurfaceHolder holder;
    volatile boolean running;

    public BackgroundView(Context context) {
        super(context);
        this.bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        this.holder = getHolder();
    }
    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }
    public void run() {
        long startTime = System.nanoTime();
        while (running) {
            if (!holder.getSurface().isValid())
                continue;
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas(null);

                synchronized (bg) {
                    if (bg.getX()+bg.getBitmap().getWidth() >= 0 || (bg.getX() >= 0 && bg.getX() <= getWidth()))
                        canvas.drawBitmap(bg.getBitmap(), bg.getX(),0, null);
                    if (bg.getX()+bg.getBitmap().getWidth()+bg.getBitmap().getWidth() >= 0 || (bg.getX()+bg.getBitmap().getWidth() >= 0 && bg.getX() <= getWidth()))
                        canvas.drawBitmap(bg.getBitmap(), (bg.getX()+bg.getBitmap().getWidth()),0, null);

                    if (getDeltaTime(startTime) > 0.1) {
                        bg.update();
                        startTime = System.nanoTime();
                    }
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private float getDeltaTime(long startTime) {
        return (System.nanoTime() - startTime) / 100000000f;
    }
}
