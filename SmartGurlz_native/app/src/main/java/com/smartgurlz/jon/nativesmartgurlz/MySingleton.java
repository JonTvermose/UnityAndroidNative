package com.smartgurlz.jon.nativesmartgurlz;

import java.util.ArrayList;

/**
 * Created by Jon Tvermose Nielsen on 17-11-2017.
 */

class MySingleton {
    private static final MySingleton ourInstance = new MySingleton();

    static MySingleton getInstance() {
        return ourInstance;
    }

    public ArrayList<UserScore> highscores;

    private MySingleton() {
    }
}
