package com.example.eigenaar.trivia;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity implements ScoreHelper.Callback {
    ScoreHelper helper;
    Score scoreObject;
    Handler setDelay;
    Runnable startDelay;
    private HighScoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // retrieve category and ScoreObject
        Intent intent = getIntent();
        scoreObject = (Score) intent.getSerializableExtra("scoreObject");

        // set database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference triviaDB = database.getReference();

        // check the scores
        helper = new ScoreHelper(this, triviaDB, this);
        helper.getScores(scoreObject);

        // initialize sleeper
        setDelay = new Handler();
    }


    @Override
    public void gotScores(ArrayList<Score> scores) {
        Score newScore = scores.get(0);
        Score currentHighScore = scores.get(1);
        Score oldNr1 = scores.get(2);
        Score oldNr2 = scores.get(3);
        Score oldNr3 = scores.get(4);

        // check personal high score
            if (newScore.score > currentHighScore.score) {

            // update personal high score en toast dat nw high score
            helper.setNewScore(newScore, "personal", currentHighScore.user);
            Toast.makeText(ScoreActivity.this, "You have a new personal high score! " +
                    "Congratulations!", Toast.LENGTH_SHORT).show();
        }

        // check top 3
        if (newScore.score > oldNr1.score) {
            helper.setNewScore(oldNr2, "general", "nr3");
            helper.setNewScore(oldNr1, "general", "nr2");
            helper.setNewScore(newScore, "general", "nr1");

            Toast.makeText(ScoreActivity.this, "You are the new number 1! " +
                    "Congratulations!", Toast.LENGTH_SHORT).show();

        } else if (newScore.score > oldNr2.score && newScore.score < oldNr1.score) {
            helper.setNewScore(oldNr2, "general", "nr3");
            helper.setNewScore(newScore, "general", "nr2");

            Toast.makeText(ScoreActivity.this, "You are the new number 2! " +
                    "Congratulations!", Toast.LENGTH_SHORT).show();
        } else if (newScore.score > oldNr3.score && newScore.score < oldNr2.score) {
            helper.setNewScore(newScore, "general", "nr3");

            Toast.makeText(ScoreActivity.this, "You are the new number 3! " +
                    "Congratulations!", Toast.LENGTH_SHORT).show();
        }

        // attach adapter to listView
        ArrayList<Score> highScores = new ArrayList<Score>();
        highScores.add(oldNr1);
        highScores.add(oldNr2);
        highScores.add(oldNr3);
        ListView list = findViewById(R.id.listViewHighScores);
        adapter = new HighScoreAdapter(this, R.layout.entry_row, highScores);
        list.setAdapter(adapter);

        // get right places
        TextView newScoreView = findViewById(R.id.currentScore);
        TextView currentHighScoreView = findViewById(R.id.currentHighScore);

        // set right values
        newScoreView.setText(String.valueOf(newScore.score));
        currentHighScoreView.setText(String.valueOf(currentHighScore.score));

    }

    @Override
    public void gotScoresError(String message) {

        // show error and go back
        Toast.makeText(ScoreActivity.this, "Something went wrong: " +
                message, Toast.LENGTH_SHORT).show();
        startDelay = new Runnable() {
            @Override
            public void run() {
                onBackPressed();
            }
        };
        setDelay.postDelayed(startDelay, 100);
    }

    public void toCategories(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ScoreActivity.this, CategoriesActivity.class);
        intent.putExtra("userName", scoreObject.user);
        startActivity(intent);
        finish();
    }
}

