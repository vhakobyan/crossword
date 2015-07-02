package com.example.myapp.common.manager;

import android.content.res.AssetManager;
import android.widget.GridView;

import com.example.myapp.util.ModelHelper;
import com.example.myapp.common.model.Word;
import com.example.myapp.common.model.BGCell;
import com.example.myapp.common.model.GridPos;

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
        dataManager = new DataManager(assets);
        bgManager = new BGManager(gv);
    }

    public void clearBGSelection() {
        bgManager.clearWordSelection();
    }

    public void setCurrentWord(Word currentWord) {
        dataManager.setCurrentWord(currentWord);
    }

    public Word getCurrentWord() {
        return dataManager.getCurrentWord();
    }

    public void markBGSelection() {
        Word currentWord = ModelHelper.getCurrentWord();
        if (currentWord != null) {
            List<GridPos> gridPositions = currentWord.getGridPositions();
            int currentPos = ModelHelper.getCurrentPosition();
            for (GridPos pos : gridPositions) {
                int index = ModelHelper.getGridIndex(pos.getRow(), pos.getCol());
                if (currentPos == index) bgManager.markCellCurrent(pos.getRow(), pos.getCol());
                else bgManager.markCellSelected(pos.getRow(), pos.getCol());
            }
        }
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

        boolean isDeletePressed = letter.equals(" ");
        int step = isDeletePressed ? -1 : 1;
        int newRow = horizontal ? row : row + step;
        int newCol = horizontal ? col + step : col;


        int gridIndex = ModelHelper.getGridIndex(newRow, newCol);
        BGCell cell = bgManager.getBGCell(gridIndex);

        if (newRow >= 0 && newRow < gridHeight && newCol >= 0 && newCol < gridWidth && !cell.isEmpty()) {

            bgManager.markCellSelected(row, col);
            bgManager.markCellCurrent(newRow, newCol);
            dataManager.setCurrentPosition(gridIndex);
        }

        //check the word is correct
        if (isDeletePressed) {
            currentWord.removeLastFromTmp();
        } else {
            currentWord.addLastToTmp(letter);
        }

        if (currentWord.isCorrect()) {
            dataManager.markWordComplete(currentWord);
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
