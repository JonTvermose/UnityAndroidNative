package com.smartgurlz.jon.nativesmartgurlz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.jon.smartGurlz.app.UnityPlayerActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UnityPlayerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateHighScoreListView(){
        this.loadHighScore();
        ListView highscore = (ListView) findViewById(R.id.HighscoreList);
        String[] strings = getHighScoresAsStringArray();
        if(strings.length == 0){
            strings = new String[]{"No entries"};
        }
        highscore.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, strings));
    }

    public String[] getHighScoresAsStringArray(){
        int length = MySingleton.getInstance().highscores.size();
        String[] strings = new String[length];
        for(int i = 0; i < length; i++){
            UserScore obj = MySingleton.getInstance().highscores.get(i);
            strings[i] = obj.userName + " : " + obj.score;
        }
        return strings;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        this.updateHighScoreListView();
        Properties p = new Properties();
        try {
            // Find the last session score
            File file = new File(getFilesDir() + "score.properties");
            p.load(new FileInputStream(file));
            String score = p.getProperty("score");
            // parse to an int
            if(score != null && !score.isEmpty()){
                int scoreInt = Integer.parseInt(score);

                if(scoreInt != -999){

                    if(this.isOnHighScore(scoreInt, MySingleton.getInstance().highscores)){
                        // Start the activity where user can enter their name
                        Intent intent = new Intent(getApplicationContext(), GameEndedActivity.class);
                        intent.putExtra("highScore", scoreInt);
                        startActivity(intent);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    private void loadHighScore(){
        Properties p = new Properties();
        File file = new File(getFilesDir() + "score.properties");
        try {
            p.load(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserScores highscoreHolder = new UserScores();
        if(MySingleton.getInstance().highscores == null){
            // Retrieve the highscores from file and parse to java object
            String highscore = p.getProperty("highscores");
            if(highscore != null && !highscore.isEmpty()){
                highscoreHolder = new Gson().fromJson(highscore, UserScores.class);
                MySingleton.getInstance().highscores = highscoreHolder.highScoreList;
            } else {
                // No highscore saved on phone
                MySingleton.getInstance().highscores = new ArrayList<UserScore>();
            }
        }
    }
    private boolean isOnHighScore(int score, ArrayList<UserScore> highscoreList){
        if(highscoreList.size() < 3){
            return true;
        }
        UserScore lowestScore = new UserScore(-999, "");
        for(int i=0; i<highscoreList.size(); i++){
            UserScore item = highscoreList.get(i);
            if(item.score < lowestScore.score){
                lowestScore = item;
            }
        }
        if(score > lowestScore.score){
            highscoreList.remove(lowestScore);
            return true;
        }
        return false;
    }

}
