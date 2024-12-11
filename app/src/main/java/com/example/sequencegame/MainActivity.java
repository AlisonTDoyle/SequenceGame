package com.example.sequencegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    // Properties

    // Activity elements
    EditText editTextUserName;
    Button buttonStartGame;
    Button buttonViewHighScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Fetch activity elements
        editTextUserName = findViewById(R.id.editTextUserName);
        buttonStartGame = findViewById(R.id.buttonStartGame);
        buttonViewHighScores = findViewById(R.id.buttonViewHighScores);

        // Event listeners
        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Capture username
                String username = editTextUserName.getText().toString();

                if (!username.isEmpty()) {
                    // Create new game activity
                    Intent gameActivityIntent = new Intent(MainActivity.this, Game.class);
                    gameActivityIntent.putExtra("username", username);

                    // Display activity
                    startActivity(gameActivityIntent);
                } else {
                    ShowToast("Enter a username before proceeding");
                }
            }
        });

        buttonViewHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new high scores activity
                Intent highScoreActivityIntent = new Intent(MainActivity.this, HighScores.class);

                // Display activity
                startActivity(highScoreActivityIntent);
            }
        });
    }

    // Method
    private void ShowToast(String message) {
        int toastDuration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, message, toastDuration);
        toast.show();
    }
}