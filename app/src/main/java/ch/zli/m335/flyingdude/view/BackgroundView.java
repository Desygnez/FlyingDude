package ch.zli.m335.flyingdude.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import ch.zli.m335.flyingdude.control.MainThread;
import ch.zli.m335.flyingdude.Constants;
import ch.zli.m335.flyingdude.R;
import ch.zli.m335.flyingdude.control.MainActivity;
import ch.zli.m335.flyingdude.model.Background;
import ch.zli.m335.flyingdude.model.ObstacleManager;
import ch.zli.m335.flyingdude.model.RectPlayer;

public class BackgroundView extends SurfaceView implements Runnable {

    private static final int PLAYER_SIZE = 100;
    private RectPlayer player;
    private Point playerPoint;
    private boolean RUNNING;
    Background background;
    Thread renderThread;
    SurfaceHolder holder;
    volatile boolean running;
    private boolean showGameOver;

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

    @Override
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

        renderThread = new MainThread(getHolder(), this);
        RUNNING = true;
        renderThread.start();

        Constants.CURRENT_Y = 0;
        Constants.SCORE = 0;
        Constants.ADDER = 15;
        showGameOver = false;
        setFocusable(true);

        player = new RectPlayer(new Rect(0,0,PLAYER_SIZE, PLAYER_SIZE), Color.YELLOW);
        playerPoint = new Point(Constants.SCREEN_WIDTH/2-PLAYER_SIZE/2,Constants.SCREEN_HEIGHT-7*PLAYER_SIZE);

    }

    public void update() {
        if(RUNNING) {
            Constants.CURRENT_Y++;
            if(!showGameOver)
                Constants.SCORE = Constants.CURRENT_Y;
            if (Constants.CURRENT_Y % 100 == 0) {
                Constants.ADDER++;
            }

            player.update(playerPoint);


            if (player.getRectangle().bottom > Constants.SCREEN_HEIGHT) {
                showGameOver = true;
            }

            if(showGameOver) {
                if(Constants.CURRENT_Y - Constants.SCORE > 30 * 3)  // 3 seconds
                    RUNNING = false;
            }
        } else {
            try {
            } catch (Exception e) {
                e.printStackTrace();
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
