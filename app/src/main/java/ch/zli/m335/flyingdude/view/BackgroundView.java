package ch.zli.m335.flyingdude.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import ch.zli.m335.flyingdude.R;
import ch.zli.m335.flyingdude.model.Background;

public class BackgroundView extends SurfaceView implements Runnable {
    Background background;
    Thread renderThread;
    SurfaceHolder holder;
    volatile boolean running;

    public BackgroundView(Context context) {
        super(context);
        this.background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        this.holder = getHolder();
    }
    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }

    public void pause() {
        running = false;
        while (true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }

    public void run() {
        long startTime = System.nanoTime();
        while (running) {
            if (!holder.getSurface().isValid())
                continue;
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas(null);

                synchronized (background) {
                    if (background.getX()+ background.getBitmap().getWidth() >= 0 || (background.getX() >= 0 && background.getX() <= getWidth()))
                        canvas.drawBitmap(background.getBitmap(), background.getX(),0, null);
                    if (background.getX()+ background.getBitmap().getWidth()+ background.getBitmap().getWidth() >= 0 || (background.getX()+ background.getBitmap().getWidth() >= 0 && background.getX() <= getWidth()))
                        canvas.drawBitmap(background.getBitmap(), (background.getX()+ background.getBitmap().getWidth()),0, null);

                    if (getDeltaTime(startTime) > 0.1) {
                        background.update();
                        startTime = System.nanoTime();
                    }
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public Background getBackgroundModel(){
        return background;
    }

    private float getDeltaTime(long startTime) {
        return (System.nanoTime() - startTime) / 100000000f;
    }
}
