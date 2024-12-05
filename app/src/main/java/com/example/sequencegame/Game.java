package com.example.sequencegame;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sequencegame.Services.AccelerometerService;

public class Game extends AppCompatActivity implements SensorEventListener {
    // Properties
    private AccelerometerService _accelerometerService;
    private SensorManager _sensorManager;
    private Sensor _accelerometer;
    private final double X_AXIS_THRESHOLD = 9;
    private final double Y_AXIS_THRESHOLD = 0.75;

    // Activity elements
    TextView textView3;
    TextView textView4;

    // Event listeners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up services
        SetUpAccelerometer();
        _sensorManager.registerListener(this, _accelerometer, 3);

        // Get activity elements
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Get values from sensor
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];

        // Check x axis for significant movement from user
        if ((x < X_AXIS_THRESHOLD)) {
            // If movement is significant...
        }

        // Check y axis for significant movement from user
        if ((y < (Y_AXIS_THRESHOLD * -1)) || (y > Y_AXIS_THRESHOLD)) {
            // If movement is significant...
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void SetUpAccelerometer() {
        SensorManager sensorManager;
        Sensor accelerometer = null;

        // Get accelerometer
        _sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        _accelerometer = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
}