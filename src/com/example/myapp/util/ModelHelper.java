package com.example.myapp.util;

import com.example.myapp.common.GameApplication;
import com.example.myapp.common.model.GameModel;
import com.example.myapp.common.model.Grid;
import com.example.myapp.common.model.Word;
import com.example.myapp.common.model.GridPos;

import java.util.List;

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

    public static void setCurrentWord(Word currentWord) {
        gameModel.setCurrentWord(currentWord);
    }

    public static Word getCurrentWord() {
        return gameModel.getCurrentWord();
    }
    
    public static void setCurrentPosition(int currentPos) {
		gameModel.setCurrentPos(currentPos);
		
	}
    
    public static int getCurrentPosition() {
		return gameModel.getCurrentPos();
		
	}
    
    public static GridPos getGridPosition(int index) {
    	Grid grid = getGrid();
    	int row = index / grid.getWidth();
    	int col = index % grid.getWidth();
    	return new GridPos(row, col);
    }
    
    public static int getGridIndex(int row, int col) {
    	return row * getGrid().getWidth() + col;
    }

}
