package com.example.sequencegame;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
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
    private float _baselineXAxis = -999;
    private float _baselineYAxis = -999;
    private float _baselineZAxis = -999;

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
        _sensorManager.registerListener(this, _accelerometer, 3);

        // Get activity elements
        textView3 = findViewById(R.id.textView3);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];    // get x value from sensor
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        if (_baselineXAxis == -999) {
            _baselineXAxis = x;
        }

        if ((x < (_baselineXAxis-10) || (x > (_baselineXAxis+10)))) {
            textView3.setText(String.valueOf(x));
            textView3.setTextColor(Color.parseColor("#D70040"));
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

        // Set up accelerometer
//        _accelerometerService = new AccelerometerService(accelerometer);

    }
}