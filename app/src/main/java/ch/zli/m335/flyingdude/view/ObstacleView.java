package ch.zli.m335.flyingdude.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import ch.zli.m335.flyingdude.Constants;
import ch.zli.m335.flyingdude.GameObject;
import ch.zli.m335.flyingdude.model.RectPlayer;

public class ObstacleView implements GameObject {

    private Rect rectangle;
    private Rect rectangle2;
    private int color;

    public ObstacleView(int rectHeight, int color, int startX, int startY, int playerGap) {
        this.color = color;
        rectangle = new Rect(0, startY, startX, startY + rectHeight);
        rectangle2 = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY + rectHeight);
    }

    public void incrementY(float y){
        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom += y;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
        canvas.drawRect(rectangle2, paint);
    }

    @Override
    public void update() {
        // make rectangle move down the screen
        int adder = Constants.ADDER;
        rectangle.top += adder;
        rectangle.bottom += adder;
    }

    public Rect getRectangle() {
        return rectangle;
    }


    public boolean playerCollide(RectPlayer player) {
        if(rectangle.contains(player.getRectangle().left,player.getRectangle().top)
            || rectangle.contains(player.getRectangle().right, player.getRectangle().top)
            || rectangle.contains(player.getRectangle().left, player.getRectangle().bottom)
            || rectangle.contains(player.getRectangle().right, player.getRectangle().bottom))
                return true;
        return false;
    }

}
