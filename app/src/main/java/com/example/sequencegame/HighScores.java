package com.example.sequencegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HighScores extends AppCompatActivity {
    // Properties

    // Activity elements
    ListView listViewScores;
    Button buttonReturnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_high_scores);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Fetch activity elements
        listViewScores = findViewById(R.id.listViewScores);
        buttonReturnHome = findViewById(R.id.buttonReturnHome);

        // Event listeners
        buttonReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new high scores activity
                Intent mainActivityIntent = new Intent(HighScores.this, MainActivity.class);

                // Display activity
                startActivity(mainActivityIntent);
            }
        });
    }
}