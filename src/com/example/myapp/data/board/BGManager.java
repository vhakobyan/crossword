package com.example.myapp.data.board;

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

    private String[][] bgData;
    private BGCell[][] bgDataArr;

    public BGManager() {
    }

    public BGManager(int height, int width) {
        bgData = new String[height][width];
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
            if(cell != null) {

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

    public void setBgDataArr(BGCell[][] bgDataArr) {
        this.bgDataArr = bgDataArr;
    }

    public void setBGDataByXY(String[][] bgData){
        this.bgData = bgData;
    }
}
