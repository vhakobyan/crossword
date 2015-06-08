package com.example.myapp.data.board;

import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.example.myapp.R;
import com.example.myapp.adapter.GameGridAdapter;
import com.example.myapp.common.ModelHelper;
import com.example.myapp.data.Word;

/**
 * This class is responsible for bg images
 * Created by Vahagn Hakobyan
 * on 5/6/15 - 11:06 AM
 */
public class BGManager {

    private GridView gridView;
    private BGCell[][] bgDataArr;
    private int rows;
    private int cols;

    public BGManager(GridView gv) {
        this.gridView = gv;
    }

    public void initBGManager(int width, int height, List<Word> hw, List<Word> vw) {

        this.rows = height;
        this.cols = width;

        bgDataArr = new BGCell[height][width];

        for (int c = 0; c < width; c++) {
            for (int r = 0; r < height; r++) {
                bgDataArr[r][c] = new BGCell(BGType.EMPTY);
            }
        }

        for (Word entry : hw) {

            int x = entry.getX();
            int y = entry.getY();

            // Log.i("WORD", "hw " + entry.getText());
            for (int i = 0; i < entry.getLength(); i++) {
                if (y >= 0 && y < height && x + i >= 0 && x + i < width) {
                    BGCell bgCell = new BGCell(i == 0 ? BGType.NUMBER : BGType.AREA);
                    bgCell.setVal(i == 0 ? entry.getTitle() : "00");
                    bgDataArr[y][x + i] = bgCell;
                }
            }
        }
        for (Word entry : vw) {

            int x = entry.getX();
            int y = entry.getY();

            // Log.i("WORD", "vw " + entry.getText());
            for (int i = 0; i < entry.getLength(); i++) {
                if (y + i >= 0 && y + i < height && x >= 0 && x < width) {
                    if (!bgDataArr[y + i][x].isNumber()) {
                        BGCell bgCell = new BGCell(i == 0 ? BGType.NUMBER : BGType.AREA);
                        bgCell.setVal(i == 0 ? entry.getTitle() : "00");
                        bgDataArr[y + i][x] = bgCell;
                    }
                }
            }
        }

        Log.i("initBGManager", "OK");

        debugBGArray();
    }

    /*public void initBG() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                markCell(ModelHelper.getGridIndex(r, c));
            }
        }
    }*/

    public BGCell getBGCell(int index) {
//		Log.i("getBGCell", String.valueOf(index));
        return bgDataArr[index / cols][index % cols];
    }

    private void debugBGArray() {

        for (int r = 0; r < rows; r++) {
            StringBuilder builder = new StringBuilder();
            for (int c = 0; c < cols; c++) {
                BGCell cell = bgDataArr[r][c];
                if (cell.isArea() || cell.isNumber()) {
                    builder.append(" ").append(cell.getVal());
                } else {
                    builder.append(" ##");
                }
            }
            Log.i("#", builder.toString());
        }

    }

    public void markCellCurrent(int row, int col) {
        markCell(row, col, "cell_y_");
    }

    public void markCellSelected(int row, int col) {
        markCell(row, col, "cell_b_");
    }

    public void markCellCompleted(int row, int col) {
        markCell(row, col, "cell_g_");
    }

    private void markCell(int x, int y, String color) {
        int index = ModelHelper.getGridIndex(x, y);
        BGCell cell = getBGCell(index);
        if(!cell.isEmpty()) {
            View view = gridView.getChildAt(index);
            view.setBackgroundResource(GameGridAdapter.getId(color + cell.getVal(), R.drawable.class));
        }
    }

    private void clearCell(int index) {

        BGCell cell = getBGCell(index);
        View view = this.gridView.getChildAt(index);
        if (cell.isArea())
            view.setBackgroundResource(R.drawable.area_empty);
        else if (cell.isNumber())
            view.setBackgroundResource(GameGridAdapter.getId("cell_" + cell.getVal(), R.drawable.class));
    }

    public void markWordComplete(Word word) {
        List<GridPos> gridPositions = word.getGridPositions();
        for (GridPos pos : gridPositions) {
            markCellCompleted(pos.getRow(), pos.getCol());
        }
    }

    public void markWordSelected() {

        Word currentWord = ModelHelper.getCurrentWord();
        if (currentWord != null) {
            List<GridPos> gridPositions = currentWord.getGridPositions();
            int currentPos = ModelHelper.getCurrentPosition();
            for (GridPos pos : gridPositions) {
                int index = ModelHelper.getGridIndex(pos.getRow(), pos.getCol());
                if(currentPos == index) markCellCurrent(pos.getRow(), pos.getCol());
                else  markCellSelected(pos.getRow(), pos.getCol());
            }
        }
    }

    public void clearWordSelection() {

        int currentPos = ModelHelper.getCurrentPosition();
        BGCell cell = getBGCell(currentPos);
        if (cell.isEmpty())
            return;

        Word currentWord = ModelHelper.getCurrentWord();
        if (currentWord != null) {
            List<Integer> gridIndexes = currentWord.getGridIndexes();
            for (int i = 0; i < gridIndexes.size(); i++) {
                clearCell(gridIndexes.get(i));
            }
        }
    }
}
