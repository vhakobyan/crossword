package com.example.myapp.common;

import android.view.View;

import com.example.myapp.data.Grid;
import com.example.myapp.data.Word;
import com.example.myapp.data.board.GridPos;

import java.util.List;
import java.util.Map;

/**
 * Created by Vahagn Hakobyan
 * on 5/6/15- 11:40 AM
 */
public class ModelHelper {

    private static BGModel bgModel;
    private static GameModel gameModel;

    static {
        bgModel = GameApplication.getInstance().getBGModel();
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

    public static List<Word> getHorizontalWords() {
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

    public static void setSelection(List<View> views) {
        bgModel.setSelection(views);
    }

    public static List<View> getSelection() {
        return bgModel.getSelection();
    }

    public static void setCurrentWord(Word currentWord) {
        gameModel.setCurrentWord(currentWord);
    }

    public static Word getCurrentWord() {
        return gameModel.getCurrentWord();
    }
    
    public static GridPos getGridPosition(int index) {
    	Grid grid = getGrid();
    	int row = index / grid.getWidth();
    	int col = index % grid.getHeight();
    	return new GridPos(row, col);
    }
    
    public static int getGridIndex(int row, int col) {
    	Grid grid = getGrid();
    	return col * grid.getWidth() + row;
    }
}
