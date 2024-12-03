package com.example.sequencegame.Services;

import static java.lang.Math.round;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class AccelerometerService implements SensorEventListener {
    // Properties
    private Sensor _accelerometer;
    public int XAxis = 0;
    public int YAxis = 0;
    public int ZAxis = 0;

    // Constructors
    public AccelerometerService(Sensor accelerometer) {
        _accelerometer = accelerometer;
    }

    // Event listeners
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Vairables
        final double UPPER_MAG_LIMIT = 11.0;
        final double LOWER_MAG_LIMIT = 8.0;

        // Get sensor values
        float x = sensorEvent.values[0];    // get x value from sensor
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        XAxis = (int) x;
        YAxis = (int) y;
        ZAxis = (int) z;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // Methods
}
