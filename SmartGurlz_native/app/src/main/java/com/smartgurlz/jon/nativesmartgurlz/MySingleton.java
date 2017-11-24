package com.smartgurlz.jon.nativesmartgurlz;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Jon Tvermose Nielsen on 17-11-2017.
 */

class MySingleton {
    private static final MySingleton ourInstance = new MySingleton();

    static MySingleton getInstance() {
        return ourInstance;
    }

    private ArrayList<UserScore> highscores;

    private MySingleton() {
    }

    public ArrayList<UserScore> gethighScores(){
        if(highscores == null)
            return null;

        highscores.sort(new Comparator<UserScore>() {
            @Override
            public int compare(UserScore u1, UserScore u2) {
                return u2.score - u1.score;
            }
        });
        return highscores;
    }

    public void setHighscores(ArrayList<UserScore> scores){
        this.highscores = scores;
    }
}
