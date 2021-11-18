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

    SurfaceHolder holder;
    private BackgroundView backgroundView;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private DudeView dudeView;
    private boolean showGameOver;
    private static final int PLAYER_SIZE = 100;
    private MainThread thread;
    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;
    private MainActivity gameActivity;
    private boolean RUNNING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        backgroundView = new BackgroundView(this);

        FrameLayout fl = new FrameLayout(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fl.setLayoutParams(lp);

        dudeView = new DudeView(this, new Dude(0, 0, BitmapFactory.decodeResource(getResources(), R.drawable.hero_icon)), backgroundView.getBackgroundModel());

        fl.addView(backgroundView);
        fl.addView(dudeView);

        setContentView(fl);
    }

    public void onStart(MainActivity gameActivity) {
        this.gameActivity = gameActivity;

        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        RUNNING = true;
        thread.start();

        Constants.CURRENT_Y = 0;
        Constants.SCORE = 0;
        Constants.ADDER = 15;
        showGameOver = false;
        setFocusable(true);

        player = new RectPlayer(new Rect(0,0,PLAYER_SIZE, PLAYER_SIZE), Color.YELLOW);
        playerPoint = new Point(Constants.SCREEN_WIDTH/2-PLAYER_SIZE/2,Constants.SCREEN_HEIGHT-7*PLAYER_SIZE);

        obstacleManager = new ObstacleManager();
    }

    @Override
    protected void onResume() {
        Constants.HIGHSCORE = getHighScore();
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mSensorManager.registerListener(    this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
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
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
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

    public void update() {
        if(RUNNING) {
            Constants.CURRENT_Y++;
            if(!showGameOver)
                Constants.SCORE = Constants.CURRENT_Y;
            if (Constants.CURRENT_Y % 100 == 0) {
                Constants.ADDER++;
            }

            player.update(playerPoint);
            obstacleManager.update();

            int collide = obstacleManager.playerCollide(player);

            // were testing the top
            if ((collide & Constants.TOP_COLLISION) == Constants.TOP_COLLISION) {
                // add the score to it
                playerPoint.set(playerPoint.x, playerPoint.y + Constants.ADDER);
            }

            if (player.getRectangle().bottom > Constants.SCREEN_HEIGHT) {
                showGameOver = true;
            }

            if(showGameOver) {
                if(Constants.CURRENT_Y - Constants.SCORE > 30 * 3)  // 3 seconds
                    RUNNING = false;
            }
        } else {
            try {
                thread.setRunning(false);
                //thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }

            gameActivity.gameOver();
        }
    }

    public void gameOver() {
        // save the game, then make a new intent to
        // switch the game to the main activity

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
}
