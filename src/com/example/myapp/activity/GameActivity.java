package com.example.myapp.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapp.KeyboardView;
import com.example.myapp.KeyboardViewInterface;
import com.example.myapp.R;
import com.example.myapp.adapter.GameGridAdapter;
import com.example.myapp.common.JSONHelper;
import com.example.myapp.common.ModelHelper;
import com.example.myapp.data.GameManager;
import com.example.myapp.data.Word;
import com.example.myapp.data.board.BGCell;
import com.example.myapp.data.board.GridPos;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends Activity implements OnTouchListener, KeyboardViewInterface {

    private static final String TAG = "GameActivity";

    public enum GRID_MODE {NORMAL, CHECK, SOLVE}
    public GRID_MODE currentMode = GRID_MODE.NORMAL;

    private GridView gridView;
    private GameGridAdapter gridAdapter;
    private TextView txtDescription;


    private int downPos;        // Position or player supported
    private boolean horizontal;        // Direction of selection

    private GameManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game);


        try {
        	

            this.gridView = (GridView) findViewById(R.id.grid);
            KeyboardView keyboardView = (KeyboardView) findViewById(R.id.keyboard);
            this.txtDescription = (TextView) findViewById(R.id.description);
            TextView keyboardOverlay = (TextView) findViewById(R.id.keyboard_overlay);

            this.txtDescription.setText(R.string.lblPleaseTouch);

            readPreferences();

            manager = new GameManager(getAssets(), this.gridView);
            manager.initGameModel();

            Display display = getWindowManager().getDefaultDisplay();
            int keyboardHeight = (int) (display.getHeight() / 4.4);


            this.gridView.setOnTouchListener(this);
            this.gridView.setNumColumns(ModelHelper.getGrid().getWidth());
            android.view.ViewGroup.LayoutParams gridParams = this.gridView.getLayoutParams();
            gridParams.height = display.getHeight() - keyboardHeight - this.txtDescription.getLayoutParams().height;
            this.gridView.setLayoutParams(gridParams);
            this.gridView.setVerticalScrollBarEnabled(false);
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

        int width = ModelHelper.getGrid().getWidth();
        int downX = this.downPos % width;
        int downY = this.downPos / width;

        if (this.downPos == position) this.horizontal = !this.horizontal;
        Word currentWord = manager.getWord(downX, downY, this.horizontal);

        if(currentWord != null) {

            this.horizontal = currentWord.isHorizontal();

            if (this.downPos == position) {

                this.txtDescription.setText(currentWord.getDescription());
                manager.clearBGSelection();
                manager.setCurrentWord(currentWord);
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
        int currentPosition = ModelHelper.getCurrentPosition();
        GridPos pos = ModelHelper.getGridPosition(currentPosition);
        this.gridAdapter.setValue(pos.getCol(), pos.getRow(), value);
        this.manager.onKeyUp();
        this.gridAdapter.notifyDataSetChanged();
    }

    @Override
    public void setDraft(boolean isDraft) {

    }
}
