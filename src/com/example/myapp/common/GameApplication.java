package com.example.myapp.common;

import android.app.Application;

import com.example.myapp.common.model.GameModel;

/**
 * Created: Vahagn Hakobyan
 * on: 04/05/15 - 5:01 PM
 */
public class GameApplication extends Application {

    private GameModel gameModel;

    private static GameApplication singleton = new GameApplication();

    public static GameApplication getInstance() {
        return singleton;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        gameModel = new GameModel();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
