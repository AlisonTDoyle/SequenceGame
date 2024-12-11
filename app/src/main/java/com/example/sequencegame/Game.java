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
import android.os.CountDownTimer;
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

public class Game extends AppCompatActivity implements SensorEventListener {
    // Properties
    private int _score = 0;
    private int _sequenceLength = 3;
    private int _pulseInterval = 1000;
    private int _remainingTime = 20000;
    private int _patternIndex = 0;
    private int tiltCount = 0;

    private long _lastTiltTime;

    private boolean _patternOk = true;
    private boolean _inputPhase = false;

    private CountDownTimer _timer;
    private List<Integer> _userInput;
    private List<Integer> _pattern = new ArrayList<>();
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
                if (_patternOk && (!_inputPhase)) {
                    // Run a single game round
                    Log.i("TAG", "in memory phase");
                    StartRound();

                    // Increase user score
                    _score += 2;

                    // Schedule the next iteration of the game loop
                    gameHandler.postDelayed(this, 2000); // Delay for 2 seconds

                } else if (_inputPhase){
                        gameHandler.postDelayed(this, 500); // Check again after a short delay
                } else {
                    // End game if _patternOk becomes false
                    ShowGameOverScreen();
                }
            }
        };

        // Start the game loop
        gameHandler.post(gameRunnable);
    }

    private boolean RunGameRound() {
        if (_patternOk) {
            GenerateRandomSequence();

            // Start round
            textViewInstructions.setText(getString(R.string.replicate_instructions));
            return false;
        } else {
            return true;
        }
    }

    private void StartRound() {
        // Reset for the new round
        _userInput = new ArrayList<>();
        _patternIndex = 0;

        // Generate new pattern
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
        final int TILT_COOLDOWN_MS = 500;

        if (_patternIndex < _pattern.size()) {
            int directionNo = _pattern.get(_patternIndex);
            _patternIndex++;
        }

        // Get values from sensor
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];

        // Get current time to stop repeated inputs
        long currentTime = System.currentTimeMillis();

        // If not in the input phase, do nothing
        if (!_inputPhase) {
            return;
        }

        // Check x axis for significant movement from user (up/down)
        int tiltDirection = 0;
        if (x < (X_THRESHOLD * -1)) {
            FlashCircle(imageViewTop);
            tiltDirection = 1;
        } else if (x > X_THRESHOLD) {
            FlashCircle(imageViewBottom);
            tiltDirection = 3;
        }

        // Check y axis for significant movement from user (left/right)
        if (y < (Y_THRESHOLD * -1)) {
            FlashCircle(imageViewLeft);
            tiltDirection = 2;
        } else if (y > Y_THRESHOLD) {
            FlashCircle(imageViewRight);
            tiltDirection = 4;
        }

        // Handle tilt count with cooldown
        if (tiltDirection != 0 && (currentTime - _lastTiltTime > TILT_COOLDOWN_MS)) {
            // Add the tilt direction to user input
            _userInput.add(tiltDirection);
            tiltCount++;
            _lastTiltTime = currentTime;
            Log.i("tilt direction:", String.valueOf(tiltDirection));

            // Check if the user input matches the pattern
            if (CheckInput()) {
                // Proceed to the next round
                textViewInstructions.setText("Correct! Proceeding to next round...");
                new Handler().postDelayed(() -> StartRound(), 2000);
            } else if (tiltDirection != _pattern.get(_patternIndex - 1)) {
                // If the tilt is wrong, end the game
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

    private int GetRandom(int maxValue) {
        return ((int) ((Math.random() * maxValue) + 1));
    }

    private void ShowGameOverScreen() {
        Intent gameOverIntent = new Intent(Game.this, GameOver.class);
        gameOverIntent.putExtra("username", getIntent().getStringExtra("username"));
        gameOverIntent.putExtra("score", _score);

        startActivity(gameOverIntent);
    }

    private boolean CheckInput() {
        if (_userInput.size() == _pattern.size()) {
            for (int i = 0; i < _userInput.size(); i++) {
                if (_userInput.get(i) != _pattern.get(i)) {
                    return false; // Incorrect input
                }
            }
            return true; // Correct input
        }
        return false; // Not yet finished
    }
}