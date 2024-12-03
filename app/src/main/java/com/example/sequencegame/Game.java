package com.example.sequencegame;

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
    private Sensor _accelerometer;

    // Activity elements
    TextView textView3;

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

        // Get activity elements
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];    // get x value from sensor
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void SetUpAccelerometer() {
        SensorManager sensorManager;
        Sensor accelerometer = null;

        // Get accelerometer
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        _accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Set up accelerometer
//        _accelerometerService = new AccelerometerService(accelerometer);

    }
}