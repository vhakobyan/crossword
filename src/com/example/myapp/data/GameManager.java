package com.example.myapp.data;

import android.view.View;
import com.example.myapp.data.board.BGManager;
import com.example.myapp.data.board.DataManager;

import java.util.List;

/**
 * Created by Vahagn Hakobyan
 * on 5/6/15- 11:29 AM
 */
public class GameManager {

    private BGManager bgManager;
    private DataManager dataManager;

    public GameManager() {
        bgManager = new BGManager();
        dataManager = new DataManager();
    }

    public void clearBGSelection(int position, String[][] pbgData) {
        bgManager.clearSelection(position, pbgData);
    }

    public void setBGSelection(List<View> views) {
        bgManager.setSelection(views);
    }

    public void setCurrentWord(Word currentWord) {
        dataManager.setCurrentWord(currentWord);
    }

    public void getCurrentWord() {
        dataManager.getCurrentWord();
    }
}
