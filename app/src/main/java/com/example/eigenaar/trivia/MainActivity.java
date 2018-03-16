package com.example.eigenaar.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toCategories(View view) {

        // get name
        TextView nameView = findViewById(R.id.name);

        // pass information to next activity
        Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
        intent.putExtra("userName", nameView.getText().toString());
        startActivity(intent);
        finish();
    }
}
