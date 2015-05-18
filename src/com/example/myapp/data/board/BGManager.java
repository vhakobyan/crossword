package com.example.myapp.data.board;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myapp.R;
import com.example.myapp.common.ModelHelper;
import com.example.myapp.data.Word;

import java.util.ArrayList;
import java.util.List;

import com.example.myapp.adapter.*;

/**
 * Created by Vahagn Hakobyan
 * on 5/6/15 - 11:06 AM
 */
public class BGManager {

    private BGCell[][] bgDataArr;

    public BGManager() {
    }

    public void clearSelection(int position, String[][] pbgData) {
        List<View> selection = ModelHelper.getSelection();
        //TextView v = this.views.get(position);
        int y = (int) (position / ModelHelper.getGrid().getWidth());
        int x = (int) (position % ModelHelper.getGrid().getWidth());

        String stringbgData = pbgData[y][x];
        BGCell cell = bgDataArr[y][x];
        for (View v : selection) {
            //String stringbgData = pbgData[v.getHeight()][v.getWidth()];
            //view.setBackgroundResource(R.drawable.area_empty);
            if (cell.isArea()) {

                v.setBackgroundResource(R.drawable.area_empty);
                v.setTag(GameGridAdapter.AREA_WRITABLE);
            } else if (cell.isNumber()) {
                v.setBackgroundResource(GameGridAdapter.getId("cell_" + stringbgData, R.drawable.class));
                v.setTag(GameGridAdapter.AREA_WRITABLE);
            } else {
                v.setBackgroundResource(R.drawable.area_block);
                v.setTag(GameGridAdapter.AREA_BLOCK);
            }
        }
        selection.clear();
        ModelHelper.setSelection(selection);


        /*
        if (bgData != null && "".equals(bgData)) {
                v.setBackgroundResource(R.drawable.area_empty);
                v.setTag(AREA_WRITABLE);
            } else if (bgData != null && bgData.trim().length() > 0) {
                v.setBackgroundResource(getId("cell_" + bgData, R.drawable.class));
                v.setTag(AREA_WRITABLE);
            } else {
                v.setBackgroundResource(R.drawable.area_block);
                v.setTag(AREA_BLOCK);
            }
         */
    }

    public void setSelection(List<View> views) {
        ModelHelper.setSelection(views);
    }

    public void setCurrentWord(Word currentWord) {

    }

    public void initBGManager(int width, int height, List<Word> hw, List<Word> vw) {

        bgDataArr = new BGCell[height][width];

        for (int c = 0; c < width; c++) {
            for (int r = 0; r < height; r++) {
                bgDataArr[r][c] = new BGCell(BGType.EMPTY);
            }
        }

        for (Word entry : hw) {

            int x = entry.getX();
            int y = entry.getY();

//            Log.i("WORD", "hw " + entry.getText());
            for (int i = 0; i < entry.getLength(); i++) {
                if (y >= 0 && y < height && x + i >= 0 && x + i < width) {
                    bgDataArr[y][x + i] = new BGCell(i == 0 ? BGType.NUMBER : BGType.AREA);
                }
            }
        }
        for (Word entry : vw) {

            int x = entry.getX();
            int y = entry.getY();

//            Log.i("WORD", "vw " + entry.getText());
            for (int i = 0; i < entry.getLength(); i++) {
                if (y + i >= 0 && y + i < height && x >= 0 && x < width) {
                    if(!bgDataArr[y + i][x].isNumber())
                        bgDataArr[y + i][x] = new BGCell(i == 0 ? BGType.NUMBER : BGType.AREA);
                }
            }
        }

        System.out.println("OK");
    }
}
