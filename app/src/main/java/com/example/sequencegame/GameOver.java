package com.example.sequencegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sequencegame.Models.ScoreRecord;
import com.example.sequencegame.Services.DatabaseService;

public class GameOver extends AppCompatActivity {
    // Activity elements
    TextView textviewScore;
    Button buttonViewAndSaveHighScores;
    Button buttonReturnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up instance
        String username = getIntent().getStringExtra("username");
        int score = getIntent().getIntExtra("score", 0);

        // Save score
        DatabaseService databaseService = new DatabaseService(this);
        ScoreRecord scoreRecord = new ScoreRecord(score, username);
        databaseService.CreateNewScoreRecord(scoreRecord);

        // Fetch activity elements
        textviewScore = findViewById(R.id.textViewScore);
        buttonReturnHome = findViewById(R.id.buttonReturnHome);
        buttonViewAndSaveHighScores = findViewById(R.id.buttonViewAndSaveHighScores);

        // Set up activity elements
        textviewScore.setText(String.valueOf(score));

        // Event listeners
        buttonViewAndSaveHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowHighscores();
            }
        });
        buttonReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowHome();
            }
        });
    }

    private void ShowHome() {
        // Create new high scores activity
        Intent mainActivityIntent = new Intent(GameOver.this, MainActivity.class);

        // Display activity
        startActivity(mainActivityIntent);
    }

    private void ShowHighscores() {
        // Create new high scores activity
        Intent highScoreActivityIntent = new Intent(GameOver.this, HighScores.class);

        // Display activity
        startActivity(highScoreActivityIntent);
    }
}