package com.example.eigenaar.trivia;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 *  Handles the questions request.
 */

public class QuestionRequest implements Response.Listener<JSONArray>, Response.ErrorListener{
    private Context context;
    private Callback callbackActivity;
    private String difficulty;

    // callback
    public interface Callback {
        void gotQuestions(ArrayList<String> questions, ArrayList<String> answers, String difficulty);
        void gotQuestionsError(String message);
    }

    // Constructor
    public QuestionRequest (Context aContext){
        context = aContext;
    }

    // attempts to retrieve questions from API
    public void getQuestions(Callback activity, String category){
        callbackActivity = activity;

        // create new queue
        RequestQueue queue = Volley.newRequestQueue(context);

        // choose difficulty
        String[] diff = new String[]{"100", "200", "300", "400", "500"};

        // choose right answer button random
        Random randomGenerator = new Random();
        difficulty = diff[randomGenerator.nextInt(diff.length)];

        // create JSON array
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(
                "http://jservice.io/api/clues?value="+difficulty+"&category="+category,
                this, this);
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        callbackActivity.gotQuestionsError(error.getMessage());
    }



    @Override
    public void onResponse(JSONArray questionArray) {
        ArrayList<String> questions = new ArrayList<>();
        ArrayList<String> answers = new ArrayList<>();

        // get the questions
        for (int i = 0; i < questionArray.length(); i++) {
            try{
                // get the object
                JSONObject objectItem = questionArray.getJSONObject(i);

                String question = objectItem.getString("question");
                String answer = objectItem.getString("answer");

                questions.add(question);
                answers.add(answer);

            } catch (JSONException e) {
                callbackActivity.gotQuestionsError(e.getMessage());
            }
        }

        // send it back
        callbackActivity.gotQuestions(questions, answers, difficulty);
    }
}

