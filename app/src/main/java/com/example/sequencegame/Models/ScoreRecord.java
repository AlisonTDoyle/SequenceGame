package com.example.sequencegame.Models;

public class ScoreRecord {
    // Properties
    private int _id;
    private int _score;
    private String _username;

    // Getters and setters
    public int getId() {
        return this._id;
    }
    public void setId(int id) {
        this._id = id;
    }

    public int getScore() {
        return this._score;
    }
    public void setScore(int score) {
        this._score = score;
    }

    public String getUsername() {
        return this._username;
    }
    public void setUsername(String username) {
        this._username = username;
    }

    // Constructors
    public ScoreRecord() {

    }

    public ScoreRecord(int score, String username) {
        this._score = score;
        this._username = username;
    }

    public ScoreRecord(int id, int score, String username) {
        this._id = id;
        this._score = score;
        this._username = username;
    }

    @Override
    public String toString() {
        return _username + "               " + _score;
    }
}
