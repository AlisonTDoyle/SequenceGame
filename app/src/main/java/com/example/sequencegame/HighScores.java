package com.example.sequencegame;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sequencegame.Models.ScoreRecord;
import com.example.sequencegame.Services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class HighScores extends ListActivity {
    // Properties
    DatabaseService _databaseService;
    List<ScoreRecord> _scores = new ArrayList<>();

    // Activity elements
    Button buttonReturnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        // Set up services
        _databaseService = new DatabaseService(this);
//        _databaseService.CreateNewScoreRecord(new ScoreRecord(100,"mary"));
        _scores = _databaseService.ReadAllScoreRecords();

        // Fetch activity elements
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

        // Set up elemetns
        SetListData();
    }

    private void SetListData() {
        // Create adapter
        ArrayAdapter<ScoreRecord> adapter = new ArrayAdapter<ScoreRecord>(this, android.R.layout.simple_list_item_1, _scores);

        // Set contents
        setListAdapter(adapter);
    }
}