package com.example.sequencegame;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game extends AppCompatActivity implements SensorEventListener {
    // Properties
    private int _score = 0;
    private int _sequenceLength = 3;
    private int _pulseInterval = 1000;
    private int _tiltCount = 0;

    private long _lastTiltTime;

    private boolean _inputPhase = false;

    private List<Integer> _userInput;
    private List<Integer> _pattern;
    private SensorManager _sensorManager;
    private Sensor _accelerometer;
    private Handler gameHandler = new Handler();
    private Runnable gameRunnable;

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

        // Set up sensors
        SetUpAccelerometer();

        // Get activity elements
        textViewInstructions = findViewById(R.id.textViewInstructions);
        imageViewTop = findViewById(R.id.imageViewTop);
        imageViewRight = findViewById(R.id.imageViewRight);
        imageViewBottom = findViewById(R.id.imageViewBottom);
        imageViewLeft = findViewById(R.id.imageViewLeft);

        StartGame();
    }

    private void StartGame() {
        gameRunnable = new Runnable() {
            @Override
            public void run() {
                if (!_inputPhase) {
                    // Run a single game round
                    Log.i("ROUND STATUS", "Starting new round");
                    StartRound();

                    // Schedule the next iteration of the game loop
                    gameHandler.postDelayed(this, 2000); // Delay for 2 seconds
                } else {
                    // Keep current round running
                    gameHandler.postDelayed(this, 500);
                }
            }
        };

        // Start the game loop
        gameHandler.post(gameRunnable);
    }

    private void StartRound() {
        // Make sure everything is wiped for new round
        if (_sensorManager.getSensors() != 0) {
            _sensorManager.unregisterListener(this, _accelerometer);
        }
        _pattern = new ArrayList<>();
        _userInput = new ArrayList<>();
        _tiltCount = 0;

        // Generate new pattern
        textViewInstructions.setText(getString(R.string.watch_instructions));
        GenerateRandomSequence();

        // Inform user to start input phase
        textViewInstructions.setText(getString(R.string.replicate_instructions));
        _sensorManager.registerListener(this, _accelerometer, 3);

        // Allow time for the user to input
        _inputPhase = true;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Constants
        final int X_THRESHOLD = 3;
        final int Y_THRESHOLD = 5;
        final int TILT_COOLDOWN_MS = 2000;

        // Get values from sensor
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];

        // Get current time to stop repeated inputs
        long currentTime = System.currentTimeMillis();

        // Check enough time has passed since last tilt to give time for user to reset their phone position
        if ((currentTime - _lastTiltTime) > TILT_COOLDOWN_MS) {
            // Check x axis for significant movement (up/down)
            int tiltDirection = 0;
            if (x < (X_THRESHOLD * -1)) {
                FlashCircle(imageViewTop);
                tiltDirection = 1;
            } else if (x > X_THRESHOLD) {
                FlashCircle(imageViewBottom);
                tiltDirection = 3;
            }

            // Check y axis for significant movement (left/right)
            if (y < (Y_THRESHOLD * -1)) {
                FlashCircle(imageViewLeft);
                tiltDirection = 2;
            } else if (y > Y_THRESHOLD) {
                FlashCircle(imageViewRight);
                tiltDirection = 4;
            }

            // Record any significant tilt from user
            if (tiltDirection != 0) {
                // Record tilt and time of tilt
                _userInput.add(tiltDirection);
                _tiltCount++;
                _lastTiltTime = currentTime;

                // Debugging
                Log.i("Tilt direction:", String.valueOf(tiltDirection));
                Log.i("Tilt count: ", String.valueOf(_tiltCount) + "/" + String.valueOf(_pattern.size()));
            }
        }

        // Check user has entered an entire sequence and their sequence is correct
        if (_tiltCount == _pattern.size()) {
            // End input
            _inputPhase = false;
            _sensorManager.unregisterListener(this, _accelerometer);

            if (CheckInputs()) {
                // Increase user score
                _score += 2;

                textViewInstructions.setText("Correct! Proceeding to next round...");
            } else {
                // End game if pattern incorrect
                ShowGameOverScreen();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // Methods
    private void SetUpAccelerometer() {
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
        textViewInstructions.setText(getString(R.string.watch_instructions));

        for (int i = 0; i < _sequenceLength; i++) {
            int n = GetRandom(_sequenceLength);
            // Record pattern entry
            _pattern.add(n+1);

            // Calculate delay based on the index in the sequence
            int delay = i * _pulseInterval;

            // Set up delayed flash
            Handler handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    Log.i("Pattern entry", String.valueOf(n));

                    // Trigger the flash based on the random number
                    switch (n+1) {
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

    private int GetRandom(int maxValue) {
        return ((int) ((Math.random() * maxValue) + 1));
    }

    private void ShowGameOverScreen() {
        // End runnable so it doesn't continue in the background
        if (gameHandler != null && gameRunnable != null) {
            gameHandler.removeCallbacks(gameRunnable);
        }

        // Set up game over intent/activity
        Intent gameOverIntent = new Intent(Game.this, GameOver.class);

        // Set up values to be passed to next activity
        gameOverIntent.putExtra("username", getIntent().getStringExtra("username"));
        gameOverIntent.putExtra("score", _score);

        // Show game over
        startActivity(gameOverIntent);
    }

    private boolean CheckInputs() {
        boolean patternMatch = true;

        // Check each user input
        for (int i = 0; i < _userInput.size(); i++) {
            // Debugging
            Log.i("Equality", "User Input " + i + ": " + _userInput.get(i) + "; Pattern " + i + ": " + _pattern.get(i));

            // If any input does not match, patternMatch is marked false
            if (_userInput.get(i) != _pattern.get(i)) {
                patternMatch = false;
            }
        }

        return patternMatch;
    }
}