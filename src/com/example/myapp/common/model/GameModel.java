package com.example.myapp.common.model;

import com.example.myapp.common.model.Grid;
import com.example.myapp.common.model.Word;

import java.util.List;

/**
 * Created by Vahagn Hakobyan
 * on 5/4/15.- 5:39 PM
 */
public class GameModel {

    private Grid grid;
    private List<Word> verticalWords;
    private List<Word> horizontalWords;
    private Word currentWord;
    private int currentPos;

    public GameModel() {
    }

    public Word getHorizontalWord(int x, int y) {
        for (Word hw : horizontalWords) {
            if(hw.getX() <= x && x <= hw.getX() + hw.getLength() - 1 && hw.getY() == y)
                return hw;
        }
        return null;
    }

    public Word getVerticalWord(int x, int y) {
        for (Word vw : verticalWords) {
            if(vw.getX() == x && vw.getY() <= y && y <= vw.getY() + vw.getLength() - 1)
                return vw;
        }
        return null;
    }

    public void setVerticalWords(List<Word> vws) {
        this.verticalWords = vws;
    }

    public void setHorizontalWords(List<Word> hws) {
        this.horizontalWords = hws;
    }

    public List<Word> getHorizontalWords() {
        return horizontalWords;
    }

    public List<Word> getVerticalWords() {
        return verticalWords;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void setCurrentWord(Word currentWord) {
        this.currentWord = currentWord;
    }

    public Word getCurrentWord() {
        return currentWord;
    }

	public int getCurrentPos() {
		return currentPos;
	}

	public void setCurrentPos(int currentPos) {
		this.currentPos = currentPos;
	}
	
}
