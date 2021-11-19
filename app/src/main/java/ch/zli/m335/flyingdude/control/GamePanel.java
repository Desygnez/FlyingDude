package ch.zli.m335.flyingdude.control;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import ch.zli.m335.flyingdude.Constants;
import ch.zli.m335.flyingdude.model.ObstacleManager;
import ch.zli.m335.flyingdude.model.Orientation;
import ch.zli.m335.flyingdude.model.RectPlayer;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;
    private boolean gameOver = false;

    private Orientation orientationData;
    private long frameTime;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);
        player = new RectPlayer(new Rect(100, 100, 200, 200), Color.rgb(255, 0, 0));
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, 3 * Constants.SCREEN_HEIGHT / 4);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);

        orientationData = new Orientation();
        orientationData.register();
        frameTime = System.currentTimeMillis();

        setFocusable(true);
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {


        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        while (true) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }
}
