package com.example.sequencegame.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sequencegame.Models.ScoreRecord;

import java.util.ArrayList;
import java.util.List;

public class DatabaseService extends SQLiteOpenHelper {
    // Properties
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Sequence_Game_DB";
    private static final String SCORE_TABLE = "scores";
    private static final String PRIMARY_KEY_COL = "id";
    private static final String USERNAME_COL = "username";
    private static final String SCORE_COL = "score";

    // Constructor

    public DatabaseService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Event listeners
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Command to create table w/ desired cols
        String createTableCommand = "CREATE TABLE " + SCORE_TABLE + "(" + PRIMARY_KEY_COL + " INTEGER PRIMARY KEY," + USERNAME_COL + " TEXT," + SCORE_COL + " INTEGER" + ")";

        // Exec. command
        sqLiteDatabase.execSQL(createTableCommand);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SCORE_TABLE);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    // Methods
    public void CreateNewScoreRecord(ScoreRecord scoreRecord) {
        // Get database
        SQLiteDatabase database = this.getWritableDatabase();

        // Parse passed data into suitable format
        ContentValues values = new ContentValues();
        values.put(USERNAME_COL, scoreRecord.getUsername());
        values.put(SCORE_COL, scoreRecord.getScore());

        // Add to database
        database.insert(SCORE_TABLE, null, values);
        database.close();
    }

    public List<ScoreRecord> ReadAllScoreRecords() {
        List<ScoreRecord> scoreRecords = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        // Set up and run query
        String selectAllQuery = "SELECT * FROM " + SCORE_TABLE + " ORDER BY " + SCORE_COL + " DESC LIMIT " + 5;
        Cursor cursor = database.rawQuery(selectAllQuery, null);

        // Cycle through and add results to list
        if (cursor.moveToFirst()) {
            do {
                // Create new score record object to hold data
                ScoreRecord scoreRecord = new ScoreRecord();
                scoreRecord.setId(Integer.parseInt(cursor.getString(0)));
                scoreRecord.setScore(Integer.parseInt(cursor.getString(2)));
                scoreRecord.setUsername(cursor.getString(1));

                // Add to list
                scoreRecords.add(scoreRecord);
            } while (cursor.moveToNext());
        }

        return scoreRecords;
    }
}
