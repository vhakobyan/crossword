package com.example.myapp.data;

import com.example.myapp.data.board.BGManager;
import com.example.myapp.data.board.DataManager;

/**
 * Created by Vahagn Hakobyan
 * on 5/6/15- 11:29 AM
 */
public class GameManager {

    private BGManager bgManager;
    private DataManager dataManager;

    public GameManager() {
    }

    public BGManager getBgManager() {
        return bgManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
