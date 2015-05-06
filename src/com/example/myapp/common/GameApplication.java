package com.example.myapp.common;

import android.app.Application;

/**
 * Created: Vahagn Hakobyan
 * on: 04/05/15 - 5:01 PM
 */
public class GameApplication extends Application {

    private BGModel bgModel;
    private GameModel gameModel;

    private static GameApplication singleton = new GameApplication();

    public static GameApplication getInstance() {
        return singleton;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public BGModel getBGModel() {
        return bgModel;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        bgModel = new BGModel();
        gameModel = new GameModel();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
