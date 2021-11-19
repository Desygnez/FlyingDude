package ch.zli.m335.flyingdude.model;

import java.util.ArrayList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import ch.zli.m335.flyingdude.Constants;
import ch.zli.m335.flyingdude.view.ObstacleView;
import ch.zli.m335.flyingdude.GameObject;

public class ObstacleManager implements GameObject {

    private ArrayList<ObstacleView> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;

    private long startTime;
    private long initTime;

    private int score = 0;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color) {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();

        populateObstacles();
    }

    public boolean playerCollide(RectPlayer player) {
        for(ObstacleView obstacle : obstacles) {
            if(obstacle.playerCollide(player))
                return true;
        }
        return false;
    }

    public void update() {
        if(startTime < Constants.INIT_TIME)
            startTime = Constants.INIT_TIME;
        int elapsedTime = (int)(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float)(Math.sqrt(1 + (startTime - initTime)/2000.0))*Constants.SCREEN_HEIGHT/(10000.0f);
        for(ObstacleView obstacle : obstacles) {
            obstacle.incrementY(speed * elapsedTime);
        }
        if(obstacles.get(obstacles.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(0, new ObstacleView(obstacleHeight, color, xStart, obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap, playerGap));
            obstacles.remove(obstacles.size() - 1);
            score++;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for(ObstacleView obstacle : obstacles)
            obstacle.draw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.MAGENTA);
        canvas.drawText("" + score, 50, 50 + paint.descent() - paint.ascent(), paint);
    }

    private void populateObstacles() {
        int currY = -5*Constants.SCREEN_HEIGHT/4;
        while(currY < 0) {
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(new ObstacleView(obstacleHeight, color, xStart, currY, playerGap));
            currY += obstacleHeight + obstacleGap;
        }
    }

        @Override
        public String toString() {
            return "NumObs: " + obstacles.size() + ", Top: " + obstacles.get(0).getRectangle().top + ", Bottom: " + obstacles.get(0).getRectangle().bottom;
        }

    }
