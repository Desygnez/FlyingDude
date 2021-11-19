package ch.zli.m335.flyingdude.model;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import ch.zli.m335.flyingdude.Constants;

public class Orientation {
    private SensorManager manager;
    private Sensor accelerometer;
    private Sensor magnometer;

    private float[] orientation = new float[3];
    public float[] getOrientation() {
        return orientation;
    }

    private float[] startOrientation = null;
    public float[] getStartOrientation() {
        return startOrientation;
    }
    public void newGame() {
        startOrientation = null;
    }

    public Orientation() {
        manager = (SensorManager) Constants.CURRENT_CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void register() {
        manager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener((SensorEventListener) this, magnometer, SensorManager.SENSOR_DELAY_GAME);
    }


}
