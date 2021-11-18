package ch.zli.m335.flyingdude.control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ch.zli.m335.flyingdude.R;
import ch.zli.m335.flyingdude.view.BackgroundView;
import ch.zli.m335.flyingdude.view.DudeView;

public class MainActivity extends AppCompatActivity {

    private BackgroundView backgroundView;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private DudeView dudeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        backgroundView = new BackgroundView(this);

        FrameLayout fl = new FrameLayout(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fl.setLayoutParams(lp);

        dudeView = new DudeView(this, new Image.Plane(0, 0, BitmapFactory.decodeResource(getResources(), R.drawable.plane_icon)), backgroundView.getBackgroundModel());

        fl.addView(backgroundView);
        fl.addView(dudeView);

        setContentView(fl);
    }
}