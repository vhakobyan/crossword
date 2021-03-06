package com.example.myapp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapp.KeyboardView;
import com.example.myapp.KeyboardViewInterface;
import com.example.myapp.R;
import com.example.myapp.adapter.GameGridAdapter;
import com.example.myapp.common.GameState;
import com.example.myapp.util.ModelHelper;
import com.example.myapp.common.manager.GameManager;
import com.example.myapp.common.model.Word;
import com.example.myapp.common.model.BGCell;
import com.example.myapp.common.model.GridPos;
import com.example.myapp.util.SerializerHelper;

public class GameActivity extends BaseActivity implements OnTouchListener, KeyboardViewInterface {

    private static final String TAG = "GameActivity";

    public enum GRID_MODE {NORMAL, CHECK, SOLVE}
    public GRID_MODE currentMode = GRID_MODE.NORMAL;

    private GridView gridView;
    private GameGridAdapter gridAdapter;
    private TextView txtDescription;


    private int downPos;        // Position or player supported
    private boolean horizontal;        // Direction of selection

    private GameState gameState;
    private GameManager manager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game);

        try {

            this.gridView = (GridView) findViewById(R.id.grid);
            KeyboardView keyboardView = (KeyboardView) findViewById(R.id.keyboard);
            this.txtDescription = (TextView) findViewById(R.id.description);
            TextView keyboardOverlay = (TextView) findViewById(R.id.keyboard_overlay);

            this.txtDescription.setText(R.string.lblPleaseTouch);

            readPreferences();

            manager = new GameManager(getAssets(), this.gridView);

            Display display = getWindowManager().getDefaultDisplay();
            int keyboardHeight = (int) (display.getHeight() / 4.4);


            this.gridView.setOnTouchListener(this);
            this.gridView.setNumColumns(ModelHelper.getGrid().getWidth());
            android.view.ViewGroup.LayoutParams gridParams = this.gridView.getLayoutParams();
            gridParams.height = display.getHeight() - keyboardHeight - this.txtDescription.getLayoutParams().height;
            this.gridView.setLayoutParams(gridParams);
//            this.gridView.setVerticalScrollBarEnabled(false);
            this.gridAdapter = new GameGridAdapter(this);
            this.gridView.setAdapter(this.gridAdapter);


            keyboardView.setDelegate(this);
            android.view.ViewGroup.LayoutParams KeyboardParams = keyboardView.getLayoutParams();
            KeyboardParams.height = keyboardHeight;
            keyboardView.setLayoutParams(KeyboardParams);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        Settings.load(getFileIO());
        gameState = SerializerHelper.loadGameState(getBaseContext());
        if(gameState == null) {
            gameState = new GameState();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Settings.save(getFileIO());
        SerializerHelper.saveGameState(getBaseContext(), gameState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void readPreferences() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        this.solidSelection = preferences.getBoolean("solid_selection", false);
//        this.gridIsLower = preferences.getBoolean("grid_is_lower", false);
        if (currentMode != GRID_MODE.SOLVE)
            currentMode = preferences.getBoolean("grid_check", false) ? GRID_MODE.CHECK : GRID_MODE.NORMAL;

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        int position = this.gridView.pointToPosition((int) event.getX(), (int) event.getY());
        BGCell bgCell = manager.getBGCell(position);
        if(bgCell.isEmpty()) return true;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN : onActionDown(position); break;
            case MotionEvent.ACTION_UP   : onActionUp(position);   break;
        }

        this.gridAdapter.notifyDataSetChanged();

        return true;
    }

    private void onActionUp(int position) {

        if (this.downPos != position) return;

        int width = ModelHelper.getGrid().getWidth();
        int downX = this.downPos % width;
        int downY = this.downPos / width;

        Word word = manager.getWord(downX, downY, this.horizontal);

        if(!word.isCompleted()) {

            this.horizontal = !this.horizontal;
            Word newCurrentWord = manager.getWord(downX, downY, this.horizontal);

            if(newCurrentWord != null && !newCurrentWord.isCompleted()) {

                this.horizontal = newCurrentWord.isHorizontal();
                this.txtDescription.setText(newCurrentWord.getDescription());
                // TODO correct tmp on crossing
                newCurrentWord.setTmp("");

                // clean bg only for not completed words
                Word currentWord = manager.getCurrentWord();
                if(currentWord != null && !currentWord.isCompleted())
                    manager.clearBGSelection();

                manager.setCurrentWord(newCurrentWord);
                manager.setCurrentPosition(position);
                manager.markBGSelection();
            }

        }
    }

    private void onActionDown(int position) {
        this.downPos = position;
    }

    @Override
    public void onKeyDown(String value, int[] location, int width) {
    }

    @Override
    public void onKeyUp(String value) {
        try {
            int currentPosition = ModelHelper.getCurrentPosition();
            GridPos pos = ModelHelper.getGridPosition(currentPosition);
            this.gridAdapter.setValue(pos.getCol(), pos.getRow(), value);
            this.manager.onKeyUp(value);
            this.gridAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setDraft(boolean isDraft) {

    }

    public GameManager getManager() {
        return manager;
    }
}
