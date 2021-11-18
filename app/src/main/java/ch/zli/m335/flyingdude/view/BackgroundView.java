package ch.zli.m335.flyingdude.view;

import android.content.Context;
import android.graphics.BitmapFactory;
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
}
