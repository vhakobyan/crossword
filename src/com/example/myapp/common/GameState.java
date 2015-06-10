package com.example.myapp.common;

import com.example.myapp.common.helper.ModelHelper;

import java.io.Serializable;

/**
 * Created by Vahagn Hakobyan
 * on 6/5/15 - 11:34 AM.
 */
public class GameState implements Serializable {

    private CellData[][] datas;

    public GameState() {
        init();
    }

    public void init() {
        int cols = ModelHelper.getGrid().getWidth();
        int rows = ModelHelper.getGrid().getHeight();
        datas = new CellData[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                CellData data = new CellData();
//                data.setState(CellState.DISABLE);
                datas[r][c] = data;
            }
        }
    }

    public CellData[][] getDatas() {
        return datas;
    }

    public void setDatas(CellData[][] datas) {
        this.datas = datas;
    }
}
