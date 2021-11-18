package ch.zli.m335.flyingdude.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import ch.zli.m335.flyingdude.Constants;
import ch.zli.m335.flyingdude.GameObject;
import ch.zli.m335.flyingdude.model.RectPlayer;

public class ObstacleView implements GameObject {

    private Rect rectangle;
    private int color;

    public ObstacleView(Rect rectangle, int color) {
        this.rectangle = rectangle;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
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


    public int playerCollide(RectPlayer player) {
        int collision = 0;

        if(rectangle.contains(player.getRectangle().left, player.getRectangle().top)
                || rectangle.contains(player.getRectangle().left, player.getRectangle().bottom))
            collision |= Constants.LEFT_COLLISION;

        if(rectangle.contains(player.getRectangle().left, player.getRectangle().top)
                || rectangle.contains(player.getRectangle().right, player.getRectangle().top))
            collision |= Constants.TOP_COLLISION;

        if(rectangle.contains(player.getRectangle().right, player.getRectangle().top)
                || rectangle.contains(player.getRectangle().right, player.getRectangle().bottom))
            collision |= Constants.RIGHT_COLLISION;

        if(rectangle.contains(player.getRectangle().left, player.getRectangle().bottom)
                || rectangle.contains(player.getRectangle().right, player.getRectangle().bottom))
            collision |= Constants.BOTTOM_COLLISION;

        return collision;
    }

}
