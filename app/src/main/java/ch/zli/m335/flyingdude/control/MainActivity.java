package ch.zli.m335.flyingdude.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.annotation.MainThread;

import ch.zli.m335.flyingdude.model.ObstacleManager;
import ch.zli.m335.flyingdude.model.RectPlayer;
import ch.zli.m335.flyingdude.Constants;
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
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        backgroundView = new BackgroundView(this);

        FrameLayout fl = new FrameLayout(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fl.setLayoutParams(lp);

        dudeView = new DudeView(this, new Dude(0, 0, BitmapFactory.decodeResource(getResources(), R.drawable.hero_icon)), backgroundView.getBackgroundModel());

        fl.addView(backgroundView);
        fl.addView(dudeView);

        setContentView(fl);
    }


    @Override
    protected void onResume() {
        Constants.HIGHSCORE = getHighScore();
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mSensorManager.registerListener(    this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        backgroundView.resume();
        SharedPreferences sharedPref = getSharedPreferences(Constants.HIGH_SCORE_FILE, 0);
        int highScore = sharedPref.getInt("highScore", 0); // default is 0
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        backgroundView.pause();
    }



    public float[] getDudePosition(float y, float z ) {
        float[] coordinates = new float[2];

        coordinates[0] = (dudeView.getWidth() / 2) + ((y) * dudeView.getWidth() / 2) - (dudeView.getDude().getImage().getWidth() / 2);
        coordinates[1] = (dudeView.getHeight() / 2) + ((z) * dudeView.getHeight() / 2) - (dudeView.getDude().getImage().getHeight() / 2);

        return coordinates;
    }

    @Override
    public void onAccuracyChanged(Sensor s, int acc) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float y = -event.values[1];
            float z = event.values[2] - 345;

            float[] coords = getDudePosition(y, z);

            int backgroundBaseSpeed = 15;

            int backgroundSpeed = (int) (backgroundBaseSpeed + ((50) * (y)));
            backgroundView.getBackgroundModel().setSpeed(backgroundSpeed < 3 ? 3 : backgroundSpeed);

            dudeView.getDude().setPosition(coords);
            dudeView.invalidate();

        }
    }



    public void gameOver() {
        // save the game, then make a new intent to
        int highScore = Constants.HIGHSCORE;
        int score = Constants.SCORE;

        if(score > highScore) {
            setHighScore(score);
        }
        // display the home screen
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private int getHighScore() {
        SharedPreferences sharedPref = getSharedPreferences(Constants.HIGH_SCORE_FILE, 0);
        int highScore = sharedPref.getInt("highScore", 0); // default is 0
        return highScore;
    }

    public void setHighScore(int highScore) {
        SharedPreferences sharedPref = getSharedPreferences(Constants.HIGH_SCORE_FILE, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("highScore", highScore);
        editor.commit();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
