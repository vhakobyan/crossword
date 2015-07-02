package com.example.myapp.common.manager;

import java.util.List;

import android.view.View;
import android.widget.GridView;

import com.example.myapp.R;
import com.example.myapp.adapter.GameGridAdapter;
import com.example.myapp.common.model.BGModel;
import com.example.myapp.util.ModelHelper;
import com.example.myapp.common.model.Word;
import com.example.myapp.common.model.BGCell;
import com.example.myapp.common.model.GridPos;
import com.example.myapp.common.type.BGType;

/**
 * This class is responsible for bg images
 * Created by Vahagn Hakobyan
 * on 5/6/15 - 11:06 AM
 */
public class BGManager {

    private GridView gridView;
    private BGModel bgModel;

    public BGManager(GridView gv) {
        this.gridView = gv;
        this.bgModel = new BGModel();
    }

    public BGCell getBGCell(int index) {
        return bgModel.getBGCell(index);
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
        if(!cell.isEmpty() && !cell.isComplete()) {
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
            bgModel.setBGCellType(pos.getRow(), pos.getCol(), BGType.COMPLETE);
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
