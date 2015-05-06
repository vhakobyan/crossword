package com.example.myapp.data.board;

import com.example.myapp.common.ModelHelper;
import com.example.myapp.data.Word;

/**
 * Created by Vahagn Hakobyan
 * on 5/6/15- 11:43 AM
 */
public class DataManager {

    public void setCurrentWord(Word currentWord) {
        ModelHelper.setCurrentWord(currentWord);
    }

    public void getCurrentWord() {
        ModelHelper.getCurrentWord();
    }
}
