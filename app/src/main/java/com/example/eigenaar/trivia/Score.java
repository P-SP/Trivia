package com.example.eigenaar.trivia;

import java.io.Serializable;

/**
 * Class for the scores.
 */

public class Score implements Serializable{
    public String user;
    public int score;

    public Score() {}

    public Score(String anUser, int aScore) {
        user = anUser;
        score = aScore;
    }
}
