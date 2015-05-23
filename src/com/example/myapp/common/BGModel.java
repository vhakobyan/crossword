package com.example.myapp.common;

import android.view.View;
import com.example.myapp.data.board.BGCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vahagn Hakobyan
 * on 5/6/15- 2:29 PM
 */
public class BGModel {

    private BGCell[][] cells;

    public BGModel() {
    }

    public BGCell[][] getCells() {
        return cells;
    }

    public void setCells(BGCell[][] cells) {
        this.cells = cells;
    }
}
