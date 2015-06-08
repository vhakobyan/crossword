package com.example.myapp.data;

import android.content.res.AssetManager;
import android.widget.GridView;

import com.example.myapp.common.ModelHelper;
import com.example.myapp.data.board.BGCell;
import com.example.myapp.data.board.BGManager;
import com.example.myapp.data.board.DataManager;
import com.example.myapp.data.board.GridPos;

import java.util.List;

/**
 * this class is responsible for data management as background and text
 * Created by Vahagn Hakobyan
 * on 5/6/15- 11:29 AM
 */
public class GameManager {

    private BGManager bgManager;
    private DataManager dataManager;

    public GameManager(AssetManager assets, GridView gv) {
        bgManager = new BGManager(gv);
        dataManager = new DataManager(assets);
    }

    public void clearBGSelection() {
        bgManager.clearWordSelection();
    }

    public void setCurrentWord(Word currentWord) {
        dataManager.setCurrentWord(currentWord);
    }

    public void getCurrentWord() {
        dataManager.getCurrentWord();
    }

    public void initGameModel() {
        initDataManager();
        initBGManager();
    }

    private void initDataManager() {
        dataManager.initDataManager();
    }

    private void initBGManager() {
        int width = dataManager.getGridWidth();
        int height = dataManager.getGridHeight();
        List<Word> horizontalWords = dataManager.getHorizontalWords();
        List<Word> verticalWords = dataManager.getVerticalWords();
        bgManager.initBGManager(width, height, horizontalWords, verticalWords);
    }

    public void markBGSelection() {
        bgManager.markWordSelected();
    }

    public void setCurrentPosition(int currentPos) {
        dataManager.setCurrentPosition(currentPos);
    }

    public void onKeyUp(String letter) {

        int gridWidth = dataManager.getGridWidth();
        int gridHeight = dataManager.getGridHeight();
        Word currentWord = dataManager.getCurrentWord();
        int currentPosition = dataManager.getCurrentPosition();
        GridPos currPos = ModelHelper.getGridPosition(currentPosition);

        int row = currPos.getRow();
        int col = currPos.getCol();

        boolean horizontal = currentWord.isHorizontal();

        int newRow = horizontal ? row : row + 1;
        int newCol = horizontal ? col + 1 : col;

        BGCell cell = bgManager.getBGCell(ModelHelper.getGridIndex(newRow, newCol));
        bgManager.markCellSelected(row, col);
        if (row >= 0 && row < gridWidth && col >= 0 && col < gridHeight && !cell.isEmpty()) {
            bgManager.markCellCurrent(newRow, newCol);
            dataManager.setCurrentPosition(ModelHelper.getGridIndex(newRow, newCol));
        }

        //TODO mark if is correct the word

        String tmp = currentWord.getTmp();
        tmp += letter;
        currentWord.setTmp(tmp);
        if (currentWord.isCorrect()) {
            bgManager.markWordComplete(currentWord);
        }
    }

    public BGCell getBGCell(int index) {
        return bgManager.getBGCell(index);
    }

    public Word getWord(int downX, int downY, boolean horizontal) {
        return dataManager.getWord(downX, downY, horizontal);
    }
}
