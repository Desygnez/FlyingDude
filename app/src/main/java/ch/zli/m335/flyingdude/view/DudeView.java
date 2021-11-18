package ch.zli.m335.flyingdude.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;

import ch.zli.m335.flyingdude.R;
import ch.zli.m335.flyingdude.model.Background;
import ch.zli.m335.flyingdude.model.Dude;

public class DudeView extends View {

    private Dude dude;
    private Background background;

    private Paint paint;
    private Rect rect;
    private final String textLabel = "Speed: ";
    private Context context;

    private float[] coords = new float[2];

    private int yOffset;
    private int speedXCoordinate;

    public DudeView(Context context) {
        super(context);
    }

    public DudeView(Context context, Dude dude, Background background) {
        super(context);
        this.dude = dude;
        this.background = background;
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        keepDudeVisible(dude);
        canvas.drawBitmap(dude.getImage(), coords[0], coords[1], null);

        String speedText = ""+(-background.getSpeed()*23);
        int fontSize = context.getResources().getDimensionPixelSize(R.dimen.speedFontSize);
        Paint paint = getPaint(fontSize);
        paint.getTextBounds(textLabel, 0, speedText.length(), rect);

        if (yOffset == 0)
            yOffset = rect.height();

        paint.getTextBounds(speedText, 0, speedText.length(), rect);

        int labelPadding = context.getResources().getDimensionPixelSize(R.dimen.labelPadding);

        if (speedXCoordinate == 0)
            speedXCoordinate = (getWidth()-rect.width())-labelPadding;

        paint.getTextBounds(textLabel, 0, textLabel.length(), rect);
        canvas.drawText(textLabel, (speedXCoordinate-rect.width()-labelPadding), yOffset, paint);
        canvas.drawText(speedText, speedXCoordinate, yOffset, paint);
    }

    private Paint getPaint(float fontSize) {
        if (rect == null)
            rect = new Rect();
        if (paint == null) {
            paint = new Paint();
            Typeface typeface = Typeface.create("Droid Sans", Typeface.BOLD);
            paint.setTypeface(typeface);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setColor(context.getResources().getColor(android.R.color.holo_green_dark));
            paint.setTextSize(fontSize);
        }
        return paint;
    }

    public Dude getDude() {
        return dude;
    }

    private void keepDudeVisible(Dude dude) {
        int height = dude.getImage().getHeight();
        int width = dude.getImage().getWidth();
        float dudeX = dude.getX();
        float dudeY = dude.getY();

        if (dudeX < 0)
            coords[0] = 1;
        else if (dudeX > getWidth()-width)
            coords[0] = getWidth()-width;
        else
            coords[0] = dudeX;

        if (dudeY < 0)
            coords[1] = 1;
        else if (dudeY > getHeight()-height)
            coords[1] = getHeight()-height;
        else
            coords[1] = dudeY;

    }
}
