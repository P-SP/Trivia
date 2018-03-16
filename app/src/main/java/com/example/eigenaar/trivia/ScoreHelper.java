package com.example.eigenaar.trivia;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Helper for writing and reading to the database.
 */

public class ScoreHelper {
    private Context context;
    private DatabaseReference triviaDB;
    private ScoreHelper.Callback callbackActivity;

    // callback
    public interface Callback {
        void gotScoresError(String message);
        void gotScores(ArrayList<Score> scores);
    }

    // Constructor
    public ScoreHelper (Context aContext, DatabaseReference reference, Callback activity){
        callbackActivity = activity;
        context = aContext;
        triviaDB = reference;
    }

    public void getScores(final Score newScore) {
        // set listener
        ValueEventListener scoreListener = new ValueEventListener() {
            @Override
            public void onCancelled(DatabaseError databaseError) {
                callbackActivity.gotScoresError(databaseError.getMessage());
            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Score> scores = new ArrayList<Score>();

                try {
                    scores.add(newScore);
                    Score scoreObject = dataSnapshot.child("personal").child(newScore.user).getValue(Score.class);
                    if (scoreObject == null){
                        triviaDB.child("personal").child(newScore.user).setValue(newScore);
                        scores.add(newScore);
                    } else {
                        scores.add(scoreObject);
                    }
                } catch (NullPointerException error) {
                    triviaDB.child("personal").child(newScore.user).setValue(newScore);
                    scores.add(newScore);
                    scores.add(newScore);
                }

                try {
                    Score scoreObject = dataSnapshot.child("general").child("nr1").getValue(Score.class);
                    scores.add(scoreObject);
                    Score scoreObject2 = dataSnapshot.child("general").child("nr2").getValue(Score.class);
                    scores.add(scoreObject2);
                    Score scoreObject3 = dataSnapshot.child("general").child("nr3").getValue(Score.class);
                    scores.add(scoreObject3);
                } catch (NullPointerException error) {
                    triviaDB.child("general").child("nr1").setValue(newScore);
                    scores.add(newScore);
                    Score emptyScore = new Score("No one", 0);
                    scores.add(emptyScore);
                    scores.add(emptyScore);
                }
                callbackActivity.gotScores(scores);
            }
        };
        triviaDB.addValueEventListener(scoreListener);
    }

    public void setNewScore(Score newScore, String place, String name) {
        triviaDB.child(place).child(name).setValue(newScore);
    }




}