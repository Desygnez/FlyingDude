package ch.zli.m335.flyingdude.control;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import ch.zli.m335.flyingdude.R;
import ch.zli.m335.flyingdude.model.Dude;
import ch.zli.m335.flyingdude.view.BackgroundView;
import ch.zli.m335.flyingdude.view.DudeView;

public class MainActivity extends Activity implements SensorEventListener {

    private BackgroundView backgroundView;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private DudeView dudeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        backgroundView = new BackgroundView(this);

        FrameLayout fl = new FrameLayout(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fl.setLayoutParams(lp);

        dudeView = new DudeView(this, new Dude(0, 0, BitmapFactory.decodeResource(getResources(), R.drawable.plane_icon)), backgroundView.getBackgroundModel());

        fl.addView(backgroundView);
        fl.addView(dudeView);

        setContentView(fl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        backgroundView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        backgroundView.pause();
    }

    public float[] getDudePosition(float y, float z, float maxAngle) {
        float[] coordinates = new float[2];

        coordinates[0] = (dudeView.getWidth() / 2) + ((y / maxAngle) * dudeView.getWidth() / 2) - (dudeView.getDude().getImage().getWidth() / 2);
        coordinates[1] = (dudeView.getHeight() / 2) + ((z / maxAngle) * dudeView.getHeight() / 2) - (dudeView.getDude().getImage().getHeight() / 2);

        return coordinates;
    }

    @Override
    public void onAccuracyChanged(Sensor s, int acc) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float y = -event.values[1];
            float z = event.values[2] - 45;

            float maxAngle = 30;

            if (y > maxAngle)
                y = maxAngle;
            if (y < -maxAngle)
                y = -maxAngle;

            if (z > maxAngle)
                z = maxAngle;
            if (z < -maxAngle)
                z = -maxAngle;

            float[] coords = getDudePosition(y, z, maxAngle);

            int bgBaseSpeed = 12;

            int bgSpeed = (int) (bgBaseSpeed + ((24) * (y / maxAngle)));
            backgroundView.getBackgroundModel().setSpeed(bgSpeed < 3 ? 3 : bgSpeed);

            dudeView.getDude().setPosition(coords);
            dudeView.invalidate();

        }
    }
}
