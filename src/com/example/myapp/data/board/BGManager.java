package com.example.myapp.data.board;

import android.view.View;
import com.example.myapp.R;
import com.example.myapp.common.ModelHelper;
import com.example.myapp.data.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vahagn Hakobyan
 * on 5/6/15 - 11:06 AM
 */
public class BGManager {

    public BGManager() {
    }

    public void clearSelection() {
        List<View> selection = ModelHelper.getSelection();
        for (View view : selection) {
            view.setBackgroundResource(R.drawable.area_empty);
        }
        selection.clear();
        ModelHelper.setSelection(selection);
    }

    public void setSelection(List<View> views) {
        ModelHelper.setSelection(views);
    }

    public void setCurrentWord(Word currentWord) {

    }
}
