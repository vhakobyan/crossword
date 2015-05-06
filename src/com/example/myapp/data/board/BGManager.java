package com.example.myapp.data.board;

/**
 * Created by Vahagn Hakobyan
 * on 5/6/15 - 11:06 AM
 */
public class BGManager {

    private BGCell [][] cells;

    public BGManager(BGCell[][] cells) {
        this.cells = cells;
    }

    public BGManager(int size) {
        this.cells = new BGCell[size][size];
    }

    public BGCell getBGCell(int x, int y) {
        return this.cells[x][y];
    }
}
