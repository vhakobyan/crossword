package com.example.myapp.common;

import android.util.Log;
import com.example.myapp.data.Grid;
import com.example.myapp.data.Word;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Vahagn Hakobyan
 * on 5/6/15- 12:07 PM
 */
public class JSONHelper {

    public static List<Word> grtVerticalWords(JSONObject obj) {

        try {
            JSONObject verticalObj = new JSONObject(String.valueOf(obj.get("vertical")));
            JSONArray verticalWords = verticalObj.getJSONArray("word");
            return getWordList(verticalWords);
        } catch (JSONException e) {
            Log.e("JSONHelper", e.getMessage());
        }
        return new ArrayList<>();
    }

    public static List<Word> grtHorizontalWords(JSONObject obj) {

        try {
            JSONObject horizontalObj = new JSONObject(String.valueOf(obj.get("horizontal")));
            JSONArray horizontalWords = horizontalObj.getJSONArray("word");
            return getWordList(horizontalWords);
        } catch (JSONException e) {
            Log.e("JSONHelper", e.getMessage());
        }
        return new ArrayList<>();
    }



    private static List<Word> getWordList(JSONArray verticalWords) throws JSONException {

        List<Word> words = new ArrayList<>();

        Word word;
        for (int i = 0; i < verticalWords.length(); i++) {
            JSONObject obj = verticalWords.getJSONObject(i);
            word = new Word();
            word.setX(Integer.valueOf(String.valueOf(obj.get("x"))));
            word.setY(Integer.valueOf(String.valueOf(obj.get("y"))));
            word.setDescription(String.valueOf(obj.get("description")));
            word.setText(String.valueOf(obj.get("text")));
            word.setTitle(String.valueOf(obj.get("title")));
            word.setHorizontal(false);
            words.add(word);
        }

        return words;
    }

    public static Grid getGrid(JSONObject obj) {

        try {
            Grid grid = new Grid();
            grid.setName(String.valueOf(obj.get("name")));
//            grid.setDescription(String.valueOf(jsonGrid.get("description")));
            grid.setLevel(Integer.valueOf(String.valueOf(obj.get("level"))));
            grid.setWidth(Integer.valueOf(String.valueOf(obj.get("width"))));
            grid.setHeight(Integer.valueOf(String.valueOf(obj.get("height"))));
            grid.setDate((new SimpleDateFormat("dd/MM/yyyy")).parse(String.valueOf(obj.get("date"))));
            return grid;
        } catch (Exception e) {
            Log.e("JSONHelper", e.getMessage());
        }

        return null;
    }
}
