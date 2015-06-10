package com.example.myapp.data.board;

import android.content.res.AssetManager;
import android.util.Log;
import android.view.View;

import com.example.myapp.common.JSONHelper;
import com.example.myapp.common.ModelHelper;
import com.example.myapp.data.Grid;
import com.example.myapp.data.Word;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vahagn Hakobyan
 * on 5/6/15- 11:43 AM
 */
public class DataManager {

    private AssetManager assets;

    public DataManager(AssetManager assets) {
        this.assets = assets;
        initDataManager();
    }

    public void setCurrentWord(Word currentWord) {
        ModelHelper.setCurrentWord(currentWord);
    }

    public Word getCurrentWord() {
        return ModelHelper.getCurrentWord();
    }

    public void setVerticalWords(List<Word> words) {
        ModelHelper.setVerticalWords(words);
    }

    public void setHorizontalWords(List<Word> words) {
        ModelHelper.setHorizontalWords(words);
    }

    public List<Word> getVerticalWords() {
        return ModelHelper.getVerticalWords();
    }

    public List<Word> getHorizontalWords() {
        return ModelHelper.getHorizontalWords();
    }

    public int getGridWidth() {
        return ModelHelper.getGrid().getWidth();
    }

    public int getGridHeight() {
        return ModelHelper.getGrid().getHeight();
    }
    
    public void setCurrentPosition(int currentPos) {
		ModelHelper.setCurrentPosition(currentPos);
	}
    
    public int getCurrentPosition() {
		return ModelHelper.getCurrentPosition();
	}
    
    public Grid getGrid() {
        return ModelHelper.getGrid();
    }

    public void initDataManager() {

        try {

            String loadJSONFromAsset = loadJSONFromAsset();
            JSONObject jsonObj = new JSONObject(loadJSONFromAsset);
            JSONObject obj = jsonObj.getJSONObject("grid");
            ModelHelper.setGrid(JSONHelper.getGrid(obj));
            setVerticalWords(JSONHelper.grtVerticalWords(obj));
            setHorizontalWords(JSONHelper.grtHorizontalWords(obj));

        } catch (JSONException e) {
            Log.e("initGameModel", e.getMessage());
        }
    }

    private String loadJSONFromAsset() {

        String json = null;
        InputStream is = null;

        try {

            is = this.assets.open("grid_01.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            Log.d("GA", ex.getMessage());
            return "";
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ignored) {}
        }
        return json;

    }

    public Word getWord(int x, int y, boolean horizontal) {
        Word hw = ModelHelper.getHW(x, y);
        Word vw = ModelHelper.getVW(x, y);
        return horizontal ? (hw == null ? vw : hw) : (vw == null ?  hw : vw);
    }

    public void markWordComplete(Word currentWord) {
        if (currentWord.isHorizontal()) {
            List<Word> hw = getHorizontalWords();
            for (Word word : hw) {
                if(word.getTitle().equals(currentWord.getTitle())) {
                    word.setCompleted(true);
                }
            }
        } else {
            List<Word> vw = getVerticalWords();
            for (Word word : vw) {
                if(word.getTitle().equals(currentWord.getTitle())) {
                    word.setCompleted(true);
                }
            }
        }
    }
}
