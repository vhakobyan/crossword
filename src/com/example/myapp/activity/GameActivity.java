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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends Activity implements OnTouchListener, KeyboardViewInterface {

    public static final float 	KEYBOARD_OVERLAY_OFFSET = 90;
    private static final String TAG = "GameActivity";

    public enum GRID_MODE {NORMAL, CHECK, SOLVE};
    public GRID_MODE currentMode = GRID_MODE.NORMAL;

    private GridView gridView;
    private KeyboardView 	keyboardView;
    private GameGridAdapter gridAdapter;
    private TextView txtDescription;
    private TextView keyboardOverlay;


    private int downPos;        // Position or player supported
    private int currentPos;        // Current cursor position
    private Word currentWord;    //Currently selected word
    private boolean horizontal;        // Direction of selection

    private GameManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game);


        try {
        	

            this.gridView = (GridView) findViewById(R.id.grid);
            this.keyboardView = (KeyboardView)findViewById(R.id.keyboard);
            this.txtDescription = (TextView) findViewById(R.id.description);
            this.keyboardOverlay = (TextView)findViewById(R.id.keyboard_overlay);

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


            this.keyboardView.setDelegate(this);
            android.view.ViewGroup.LayoutParams KeyboardParams = this.keyboardView.getLayoutParams();
            KeyboardParams.height = keyboardHeight;
            this.keyboardView.setLayoutParams(KeyboardParams);

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

        int x1 = (int) event.getX();
        int y1 = (int) event.getY();
        int position = this.gridView.pointToPosition(x1, y1);

        int width = ModelHelper.getGrid().getWidth();
        int downX = this.downPos % width;
        int downY = this.downPos / width;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
            	Log.i("TAG", "ACTION_DOWN, x:" + downX + ", y:" + downY + ", position: " + position);
                this.downPos = position;
                break;
            case MotionEvent.ACTION_UP: {
            	Log.i("TAG", "ACTION_UP, x:" + downX + ", y:" + downY + ", position: " + position);
                // rejecting click on empty (black) area
                View child = this.gridView.getChildAt(position);
                if (child == null || child.getTag().equals(GameGridAdapter.AREA_BLOCK)) return true;
                // using horizontal or vertical words depend on click count
                if (this.downPos == position && this.currentPos == position) this.horizontal = !this.horizontal;
                
                //retrieving current word
                this.currentWord = getWord(downX, downY, this.horizontal);
                if(this.currentWord == null) return true;
 
                this.horizontal = this.currentWord.getHorizontal();

                if (this.downPos == position) {
                	
                    this.currentPos = position;
                    this.txtDescription.setText(this.currentWord.getDescription());

                    manager.clearBGSelection();
                    manager.setCurrentWord(this.currentWord);
                    manager.setCurrentPosition(this.currentPos);
                    manager.markBGSelection();
                }

                break;
            }
        }

        this.gridAdapter.notifyDataSetChanged();

        return true;
    }

    private Word getWord(int x, int y, boolean horizontal) {
        Word hw = ModelHelper.getHW(x, y);
        Word vw = ModelHelper.getVW(x, y);
        return horizontal ? (hw == null ? vw : hw) : (vw == null ?  hw : vw);
    }

    @Override
    public void onKeyDown(String value, int[] location, int width) {

        int currentX = this.downPos % width;
        int currentY = this.downPos / width;
        System.out.println("onKeyDown: " + value + ", insert in: " + currentX + "x" + currentY);

        if (value.equals(" ") == false) {
            int offsetX = (this.keyboardOverlay.getWidth() - width) / 2;
            int offsetY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, KEYBOARD_OVERLAY_OFFSET, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)this.keyboardOverlay.getLayoutParams();
//            lp.leftMargin = location[0] - offsetX;
//            lp.topMargin = location[1] - offsetY;
//            this.keyboardOverlay.setLayoutParams(lp);
//            this.keyboardOverlay.setText(value);
//            this.keyboardOverlay.clearAnimation();
//            this.keyboardOverlay.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onKeyUp(String value) {

        int width = ModelHelper.getGrid().getWidth();
        int height = ModelHelper.getGrid().getHeight();

        int currentX = this.downPos % width;
        int currentY = this.downPos / width;
        System.out.println("onKeyUp: " + value + ", insert in: " + currentX + "x" + currentY);

        if (value.equals(" ") == false) {
            this.keyboardOverlay.setAnimation(AnimationUtils.loadAnimation(this, R.anim.keyboard_overlay_fade_out));
            this.keyboardOverlay.setVisibility(View.INVISIBLE);
        }

        // Si aucun mot selectionne, retour
        if (this.currentWord == null)
            return;

        // Case actuelle
        int x = currentX;
        int y = currentY;

        if (this.gridAdapter.isBlock(x, y))
            return;

        this.gridAdapter.setValue(x, y, value);
        this.gridAdapter.notifyDataSetChanged();

        if (value.equals(" ")) {
            x = (this.horizontal ? x - 1 : x);
            y = (this.horizontal ? y: y - 1);
        }
        else
        {
            x = (this.horizontal ? x + 1 : x);
            y = (this.horizontal ? y: y + 1);
        }

        // Si la case suivante est disponible, met la case en jaune, remet l'ancienne en bleu, et set la nouvelle position
        if (x >= 0 && x < width && y >= 0 && y < height && this.gridAdapter.isBlock(x, y) == false) {
            this.gridView.getChildAt(y * width + x).setBackgroundResource(R.drawable.area_current);
            this.gridView.getChildAt(currentY * width + currentX).setBackgroundResource(R.drawable.area_selected);
            currentX = x;
            currentY = y;
        }

    }

    @Override
    public void setDraft(boolean isDraft) {

    }
}
