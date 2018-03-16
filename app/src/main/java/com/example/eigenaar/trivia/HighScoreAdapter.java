package com.example.eigenaar.trivia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter for high scores.
 */

public class HighScoreAdapter extends ArrayAdapter<Score> {
    private ArrayList<Score> scores;

    public HighScoreAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Score> anList) {
        super(context, resource, anList);
        scores = anList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.entry_row, parent, false);
        }

        // get places
        TextView name = convertView.findViewById(R.id.name);
        TextView score = convertView.findViewById(R.id.score);


        // set right values
        Score item = scores.get(position);
        name.setText(item.user);
        score.setText(String.valueOf(item.score));

        return convertView;
    }
}
