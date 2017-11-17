package com.smartgurlz.jon.nativesmartgurlz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

public class GameEndedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_ended);

        TextView highScoreTextView = (TextView) findViewById(R.id.highScoreTextView);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        final EditText playerNameText = (EditText) findViewById(R.id.nameTextInputEditText);

        // Retrieve the score from the intent
        Intent intent = getIntent();
        final int score = intent.getIntExtra("highScore", -999);
        highScoreTextView.setText(Integer.toString(score));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // playerName & score
                String playerName = playerNameText.getText().toString();

                Properties p = new Properties();
                File file = new File(getFilesDir() + "score.properties");
                try {
                    // Set score back to -999 and save file
                    p.load(new FileInputStream(file));
                    p.setProperty("score", "-999");
                    // Save the new highscores
                    UserScore newScore = new UserScore(score, playerName);
                    MySingleton.getInstance().highscores.add(newScore);
                    UserScores highscoreHolder = new UserScores();
                    highscoreHolder.highScoreList = MySingleton.getInstance().highscores;
                    String highscoresJson = new Gson().toJson(highscoreHolder);
                    p.setProperty("highscores", highscoresJson);
                    p.store(new FileOutputStream(getFilesDir() + "score.properties"), null);
                    // end current activity
                    finish();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                    finish();
                }
            }
        });
    }

}
