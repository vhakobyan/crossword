package com.example.myapp.util;

import android.content.Context;

import com.example.myapp.common.GameState;

import java.io.Serializable;

/**
 * Created by Vahagn Hakobyan
 * on 7/2/15.
 */
public class SerializerHelper {

    private static Serializer serializer = new Serializer();

    private static final String GAME_STATE = "gameState";

    public static void saveGameState(Context context, GameState obj) {
        serializer.storeObject(context, obj, GAME_STATE);
    }

    public static GameState loadGameState(Context context) {
        return (GameState) serializer.loadObject(context, GAME_STATE);
    }
}
