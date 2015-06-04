package com.example.myapp.data.board;

import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

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

    public void markBGSelection() {

        Word currentWord = ModelHelper.getCurrentWord();
        if (currentWord != null) {
            List<Integer> gridIndexes = currentWord.getGridIndexes();
            for (int i = 0; i < gridIndexes.size(); i++) {
                markCell(gridIndexes.get(i));
            }
        }
    }

    public void clearSelection() {

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

    public void setCurrentWord(Word currentWord) {
        ModelHelper.setCurrentWord(currentWord);
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
                    if (i == 0)
                        bgCell.setVal(entry.getTitle());
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
                        if (i == 0)
                            bgCell.setVal(entry.getTitle());
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
                if (cell.isArea()) {
                    builder.append(" ##");
                } else if (cell.isNumber()) {
                    builder.append(" ").append(cell.getVal());
                } else {
                    builder.append("   ");
                }
            }
            Log.i("#", builder.toString());
        }

    }

    public void markCurrent(int row, int col) {
        int index = ModelHelper.getGridIndex(row, col);
        BGCell cell = getBGCell(index);
        View view = gridView.getChildAt(index);
        if(cell.isNumber())
            view.setBackgroundResource(GameGridAdapter.getId("cell_y_" + cell.getVal(), R.drawable.class));
        else if(cell.isArea())
            view.setBackgroundResource(R.drawable.area_current);
    }

    public void markSelected(int x, int y) {
        int index = ModelHelper.getGridIndex(x, y);
        BGCell cell = getBGCell(index);
        View view = gridView.getChildAt(index);
        if(cell.isNumber())
            view.setBackgroundResource(GameGridAdapter.getId("cell_b_" + cell.getVal(), R.drawable.class));
        else if(cell.isArea())
            view.setBackgroundResource(R.drawable.area_selected);
    }

    public GridPos moveCurrent(int row, int col) {

        Word currentWord = ModelHelper.getCurrentWord();
        boolean horizontal = currentWord.isHorizontal();

        int newRow = horizontal ? row : row + 1;
        int newCol = horizontal ? col + 1 : col;

        int index = ModelHelper.getGridIndex(newRow, newCol);
        BGCell cell = getBGCell(index);

        if (!cell.isEmpty()) {
//            markCell(ModelHelper.getGridIndex(row, col));
//            markCell(ModelHelper.getGridIndex(newRow, newCol));
            markSelected(row, col);
            markCurrent(newRow, newCol);
            return new GridPos(newRow, newCol);
        }

        return new GridPos(row, col);
    }


    private void markCell(int index) {

        BGCell cell = getBGCell(index);
        View view = this.gridView.getChildAt(index);

        int currentPos = ModelHelper.getCurrentPosition();

        if (cell.isArea())
            view.setBackgroundResource(currentPos == index ? R.drawable.area_current : R.drawable.area_selected);
        else if (cell.isNumber())
            view.setBackgroundResource(GameGridAdapter.getId("cell_" + (currentPos == index ? "y_" : "b_") + cell.getVal(), R.drawable.class));

    }

    private void clearCell(int index) {

        BGCell cell = getBGCell(index);
        View view = this.gridView.getChildAt(index);
        if (cell.isArea())
            view.setBackgroundResource(R.drawable.area_empty);
        else if (cell.isNumber())
            view.setBackgroundResource(GameGridAdapter.getId("cell_" + cell.getVal(), R.drawable.class));
    }

}
