package com.example.sequencegame;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sequencegame.Services.AccelerometerService;

import java.util.ArrayList;
import java.util.List;

public class Game extends AppCompatActivity implements SensorEventListener {
    // Properties
    private int _score = 0;
    private int _sequenceLength = 3;
    private int _pulseInterval = 1000;

    private List<Integer> _pattern = new ArrayList<Integer>();
    private SensorManager _sensorManager;
    private Sensor _accelerometer;

    // Activity elements
    TextView textViewInstructions;
    ImageView imageViewTop;
    ImageView imageViewRight;
    ImageView imageViewBottom;
    ImageView imageViewLeft;

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
//        _sensorManager.registerListener(this, _accelerometer, 3);

        // Get activity elements
        textViewInstructions = findViewById(R.id.textViewInstructions);
        imageViewTop = findViewById(R.id.imageViewTop);
        imageViewRight = findViewById(R.id.imageViewRight);
        imageViewBottom = findViewById(R.id.imageViewBottom);
        imageViewLeft = findViewById(R.id.imageViewLeft);

        // Set up elements
        textViewInstructions.setText(getString(R.string.watch_instructions));
        GenerateRandomSequence();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Get values from sensor
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];

        // Check x axis for significant movement from user (up/down)
        if (x < -3) {
            FlashCircle(imageViewTop);
        } else if (x > 3) {
            FlashCircle(imageViewBottom);
        }

        // Check y axis for significant movement from user (left/right)
        if (y < -5) {
            FlashCircle(imageViewLeft);
        } else if (y > 5) {
            FlashCircle(imageViewRight);
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

    private void FlashCircle(ImageView imageView) {
        // Set up delayed flash
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                // Get circle from drawable
                LayerDrawable layerDrawable = (LayerDrawable) imageView.getDrawable();
                Drawable circle = layerDrawable.findDrawableByLayerId(R.id.circle);

                // Set to grey
                circle.setTint(Color.parseColor("#888888"));

                // Return to default color
                Handler handler1 = new Handler();
                Runnable r1 = new Runnable() {
                    public void run() {
                        circle.setTint(Color.parseColor("#000000"));
                    }
                };
                handler1.postDelayed(r1, 600);

            }
        };

        // Flash
        handler.postDelayed(r, 600);
    }

    private void GenerateRandomSequence() {
        for (int i = 0; i < _sequenceLength; i++) {
            int n = getRandom(_sequenceLength);
            // Record pattern entry
            _pattern.add(n + 1);

            // Calculate delay based on the index in the sequence
            int delay = i * _pulseInterval;

            // Set up delayed flash
            Handler handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    Log.i("Pattern entry", String.valueOf(n));

                    // Trigger the flash based on the random number
                    switch (n) {
                        case 1:
                            FlashCircle(imageViewTop);
                            break;
                        case 2:
                            FlashCircle(imageViewLeft);
                            break;
                        case 3:
                            FlashCircle(imageViewBottom);
                            break;
                        case 4:
                            FlashCircle(imageViewRight);
                            break;
                        default:
                            break;
                    }
                }
            };
            handler.postDelayed(r, delay);
        }
    }

    private int getRandom(int maxValue) {
        return ((int) ((Math.random() * maxValue) + 1));
    }
}