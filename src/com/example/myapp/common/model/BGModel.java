package com.example.myapp.common.model;

import android.util.Log;

import com.example.myapp.util.ModelHelper;
import com.example.myapp.common.type.BGType;

import java.util.List;

/**
 * Created by Vahagn Hakobyan
 * on 5/6/15- 2:29 PM
 */
public class BGModel {

    private int rows;
    private int cols;
    private BGCell[][] cells;

    public BGModel() {
        init();
    }

    private void init() {
        cols = ModelHelper.getGrid().getWidth();
        rows= ModelHelper.getGrid().getHeight();

        cells = new BGCell[rows][cols];

        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                BGCell bgCell = new BGCell(BGType.EMPTY);
                bgCell.setVal("##");
                cells[r][c] = bgCell;
            }
        }

        List<Word> hw = ModelHelper.getHorizontalWords();
        for (Word entry : hw) {

            int x = entry.getX();
            int y = entry.getY();

            // Log.i("WORD", "hw " + entry.getText());
            for (int i = 0; i < entry.getLength(); i++) {
                if (y >= 0 && y < rows && x + i >= 0 && x + i < cols) {
                    BGCell bgCell = new BGCell(i == 0 ? BGType.NUMBER : BGType.AREA);
                    bgCell.setVal(i == 0 ? entry.getTitle() : "00");
                    cells[y][x + i] = bgCell;
                }
            }
        }

        List<Word> vw = ModelHelper.getVerticalWords();
        for (Word entry : vw) {

            int x = entry.getX();
            int y = entry.getY();

            // Log.i("WORD", "vw " + entry.getText());
            for (int i = 0; i < entry.getLength(); i++) {
                if (y + i >= 0 && y + i < rows && x >= 0 && x < cols) {
                    if (!cells[y + i][x].isNumber()) {
                        BGCell bgCell = new BGCell(i == 0 ? BGType.NUMBER : BGType.AREA);
                        bgCell.setVal(i == 0 ? entry.getTitle() : "00");
                        cells[y + i][x] = bgCell;
                    }
                }
            }
        }

        Log.i("initBGManager", "OK");
        debugBGArray();
    }

    public BGCell[][] getCells() {
        return cells;
    }

    public void setCells(BGCell[][] cells) {
        this.cells = cells;
    }

    public BGCell getBGCell(int index) {
        return cells[index / cols][index % cols];
    }

    public BGCell getBGCell(int row, int col) {
        return cells[col][row];
    }

    public void setBGCellType(int row, int col, BGType type) {
        cells[row][col].setType(type);
    }

    private void debugBGArray() {
        int cols = ModelHelper.getGrid().getWidth();
        int rows= ModelHelper.getGrid().getHeight();
        for (int r = 0; r < rows; r++) {
            StringBuilder builder = new StringBuilder();
            for (int c = 0; c < cols; c++) {
                builder.append(" ").append(cells[r][c].getVal());
            }
            Log.i("#", builder.toString());
        }

    }
}
