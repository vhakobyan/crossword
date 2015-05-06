package com.example.myapp.common;

import com.example.myapp.data.Grid;
import com.example.myapp.data.Word;

import java.util.List;
import java.util.Map;

/**
 * Created by Vahagn Hakobyan
 * on 5/6/15- 11:40 AM
 */
public class ModelHelper {

    private static GameModel gameModel;

    static {
        gameModel = GameApplication.getInstance().getGameModel();
    }

    public static Word getHW(int x, int y) {
        return gameModel.getHorizontalWord(x, y);
    }

    public static Word getVW(int x, int y) {
        return gameModel.getVerticalWord(x, y);
    }

    public static void setHorizontalWords(List<Word> hws) {
        gameModel.setHorizontalWords(hws);
    }

    public static void setVerticalWords(List<Word> vws) {
        gameModel.setVerticalWords(vws);
    }

    public static List<Word> gtHorizontalWords() {
        return gameModel.getHorizontalWords();
    }

    public static List<Word> getVerticalWords() {
        return gameModel.getVerticalWords();
    }

    public static void setGrid(Grid grid) {
        gameModel.setGrid(grid);
    }

    public static Grid getGrid() {
        return gameModel.getGrid();
    }
}
