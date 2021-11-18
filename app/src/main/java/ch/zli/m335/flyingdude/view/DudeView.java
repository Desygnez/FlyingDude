package ch.zli.m335.flyingdude.view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;

import ch.zli.m335.flyingdude.model.Background;

public class DudeView {

    private Image.Plane dude;
    private Background bg;

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

    public DudeView(Context context, Image.Plane plane, Background bg) {
        super(context);
        this.dude = plane;
        this.bg = bg;
        this.context = context;
    }
}
