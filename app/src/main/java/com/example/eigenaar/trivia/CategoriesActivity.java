package com.example.eigenaar.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CategoriesActivity extends AppCompatActivity {

    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        // retrieve user
        Intent intent = getIntent();
        user = intent.getStringExtra("userName");

        // display user's name
        TextView userNameView = findViewById(R.id.userName);
        userNameView.setText(user);
    }

    public void toQuestions(View view) {
        Button button = (Button) view;

        // pass information to next activity
        Intent intent = new Intent(CategoriesActivity.this, QuestionActivity.class);
        intent.putExtra("userName", user);
        intent.putExtra("categoryNumber", view.getTag().toString());
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CategoriesActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
