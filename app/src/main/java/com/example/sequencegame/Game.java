package com.example.sequencegame;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
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
        _sensorManager.registerListener(this, _accelerometer, 3);

        // Get activity elements
        textViewInstructions = findViewById(R.id.textViewInstructions);
        imageViewTop = findViewById(R.id.imageViewTop);
        imageViewRight = findViewById(R.id.imageViewRight);
        imageViewBottom = findViewById(R.id.imageViewBottom);
        imageViewLeft = findViewById(R.id.imageViewLeft);

        // Set up elements
        textViewInstructions.setText(getString(R.string.watch_instructions));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Get values from sensor
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];

        // Check x axis for significant movement from user (up/down)
        if ((x < 0)) {
            LayerDrawable imageLayersBottom = (LayerDrawable) imageViewBottom.getDrawable();
            Drawable circleBottom = imageLayersBottom.findDrawableByLayerId(R.id.circle);
            circleBottom.setTint(Color.parseColor("#000000"));

            LayerDrawable imageLayersTop = (LayerDrawable) imageViewTop.getDrawable();
            Drawable circleTop = imageLayersTop.findDrawableByLayerId(R.id.circle);
            circleTop.setTint(Color.parseColor("#888888"));
        } else if (x > 0) {
            LayerDrawable imageLayersBottom = (LayerDrawable) imageViewBottom.getDrawable();
            Drawable circleBottom = imageLayersBottom.findDrawableByLayerId(R.id.circle);
            circleBottom.setTint(Color.parseColor("#888888"));

            LayerDrawable imageLayersTop = (LayerDrawable) imageViewTop.getDrawable();
            Drawable circleTop = imageLayersTop.findDrawableByLayerId(R.id.circle);
            circleTop.setTint(Color.parseColor("#000000"));
        }

        // Check y axis for significant movement from user (left/right)
        if ((y < (Y_AXIS_THRESHOLD * -1)) || (y > Y_AXIS_THRESHOLD)) {

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