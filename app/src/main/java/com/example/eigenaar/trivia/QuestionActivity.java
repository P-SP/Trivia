package com.example.eigenaar.trivia;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity implements QuestionRequest.Callback {
    String user;
    Integer questionIndex, rightAnswerNumber, score;
    TextView scoreView, questionView, currentQuestionView, totalQuestionsView;
    Button[] buttons;
    ArrayList<String> questions, answers;
    final Integer MAX_QUESTIONS = 5;
    Handler setDelay;
    Runnable startDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // retrieve clicked category and user
        Intent intent = getIntent();
        user = intent.getStringExtra("userName");
        String categoryNumber = intent.getStringExtra("categoryNumber");

        // get questions
        if (savedInstanceState == null) {
            QuestionRequest apiRequest = new QuestionRequest(this);
            apiRequest.getQuestions(this, categoryNumber);
        }

        // get buttons, questionView and scoreView
        scoreView = findViewById(R.id.score);
        questionView = findViewById(R.id.question);
        currentQuestionView = findViewById(R.id.currentQuestion);
        totalQuestionsView = findViewById(R.id.totalQuestions);
        buttons = new Button[]{
                findViewById(R.id.answer1),
                findViewById(R.id.answer2),
                findViewById(R.id.answer3),
                findViewById(R.id.answer4)
        };

        // initialize sleeper
        setDelay = new Handler();
    }

    protected void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);
        outstate.putStringArrayList("answers", answers);
        outstate.putStringArrayList("questions", questions);
        outstate.putInt("score", score);
        outstate.putInt("questionIndex", questionIndex);
    }

    public void onRestoreInstanceState(Bundle instate) {
        super.onRestoreInstanceState(instate);
        answers = instate.getStringArrayList("answers");
        questions = instate.getStringArrayList("questions");
        score = instate.getInt("score");
        questionIndex = instate.getInt("questionIndex");
        update();
    }

    @Override
    public void gotQuestions(ArrayList<String> questionsAPI, ArrayList<String> answersAPI, String diff) {
        Integer difficulty = Integer.valueOf(diff);

        // remember questions and answers
        questions = questionsAPI;
        answers = answersAPI;

        // init values
        score = difficulty/10;
        questionIndex = 0;

        // update GUI
        update();
    }

    @Override
    public void gotQuestionsError(String message) {

        // show error and go back
        Toast.makeText(QuestionActivity.this, "Something went wrong: " +
                message, Toast.LENGTH_SHORT).show();
        startDelay = new Runnable() {
            @Override
            public void run() {
                onBackPressed();
            }
        };
        setDelay.postDelayed(startDelay, 1000);

    }

    public void update() {

        // set right values
        questionView.setText(questions.get(questionIndex));
        scoreView.setText(String.valueOf(score));
        currentQuestionView.setText(String.valueOf(questionIndex + 1));
        if (questions.size() < MAX_QUESTIONS ){
            totalQuestionsView.setText(questions.size());
        } else {
            totalQuestionsView.setText(String.valueOf(MAX_QUESTIONS));
        }


        // choose right answer button random
        Random randomGenerator = new Random();
        rightAnswerNumber = randomGenerator.nextInt(buttons.length);

        // save all the used answers
        ArrayList<Integer> usedAnswersIndices = new ArrayList<Integer>();
        usedAnswersIndices.add(questionIndex);

        // fill the other answers
        for (int i = 0, n = buttons.length; i < n; i++) {
            if (i == rightAnswerNumber) {
                buttons[rightAnswerNumber].setText(answers.get(questionIndex));
            } else {
                int wrongAnswerIndex;

                // choose new unused answer
                do {
                    wrongAnswerIndex = randomGenerator.nextInt(answers.size());
                } while (usedAnswersIndices.contains(wrongAnswerIndex));

                usedAnswersIndices.add(wrongAnswerIndex);
                buttons[i].setText(answers.get(wrongAnswerIndex));
            }
        }
    }

    // check if answer is correct
    public void checkAnswer(View view) {
        // make buttons un-clickable
        for (int j = 0, n = buttons.length; j < n; j++){
            buttons[j].setClickable(false);
        }

        if (Integer.parseInt(view.getTag().toString()) == (rightAnswerNumber)) {
            score = score + 5;
            Toast.makeText(QuestionActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(QuestionActivity.this, "That is the wrong answer. The right answer" +
                    "is answer " + (rightAnswerNumber + 1) + ".", Toast.LENGTH_SHORT).show();
        }

        // go to the next question/score screen after a delay
        startDelay = new Runnable() {
            @Override
            public void run() {
                // make buttons clickable
                for (int j = 0, n = buttons.length; j < n; j++){
                    buttons[j].setClickable(true);
                }

                checkNextQuestion();
            }
        };
        setDelay.postDelayed(startDelay, 2500);

    }

    // check if there is a next question
    public void checkNextQuestion() {
        if ((questionIndex < MAX_QUESTIONS - 1) && (questionIndex < questions.size() - 2)){
            questionIndex++;
            update();
        }
        else {
            toScore();
        }
    }

    // if restart is pressed
    public void toCategories(View view) {
        onBackPressed();
    }

    public void toScore() {

        // pass information to next activity
        Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
        intent.putExtra("scoreObject", new Score(user, score));
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QuestionActivity.this, CategoriesActivity.class);
        intent.putExtra("userName", user);
        startActivity(intent);
        finish();
    }
}
