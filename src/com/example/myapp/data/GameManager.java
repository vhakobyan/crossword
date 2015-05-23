package com.example.myapp.data;

import android.content.res.AssetManager;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.example.myapp.common.JSONHelper;
import com.example.myapp.common.ModelHelper;
import com.example.myapp.data.board.BGManager;
import com.example.myapp.data.board.DataManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * this class is responsible for data management as background and text
 * Created by Vahagn Hakobyan
 * on 5/6/15- 11:29 AM
 */
public class GameManager {

    private BGManager bgManager;
    private DataManager dataManager;

    public GameManager(AssetManager assets, GridView gv) {
        bgManager = new BGManager(gv);
        dataManager = new DataManager(assets);
    }

    public void clearBGSelection() {
        bgManager.clearSelection();
    }

    public void setCurrentWord(Word currentWord) {
        dataManager.setCurrentWord(currentWord);
    }

    public void getCurrentWord() {
        dataManager.getCurrentWord();
    }

    public void initGameModel() {
        initDataManager();
        initBGManager();
    }

    private void initDataManager() {
        dataManager.initDataManager();
    }

    private void initBGManager() {
    	int width = dataManager.getGridWidth();
        int height = dataManager.getGridHeight();
    	List<Word> horizontalWords = dataManager.getHorizontalWords();
        List<Word> verticalWords = dataManager.getVerticalWords();
        bgManager.initBGManager(width, height, horizontalWords, verticalWords);
    }

	public void markBGSelection() {
		bgManager.markBGSelection();
	}

	public void setCurrentPosition(int currentPos) {
		dataManager.setCurrentPosition(currentPos);
	}
}
