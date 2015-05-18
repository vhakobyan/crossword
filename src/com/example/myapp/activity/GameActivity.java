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
    private int downX;            //Line or player supported
    private int downY;            // Column or player supported
    private int currentPos;        // Current cursor position
    private int currentX;        // Current column of the cursor
    private int currentY;        // Current cursor line
    private Word currentWord;    //Currently selected word
    private boolean horizontal;        // Direction of selection

    private GameManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game);


        try {

            readPreferences();

            manager = new GameManager(getAssets());
            manager.initGameModel();

            this.gridView = (GridView) findViewById(R.id.grid);
            this.keyboardView = (KeyboardView)findViewById(R.id.keyboard);
            this.txtDescription = (TextView) findViewById(R.id.description);
            this.keyboardOverlay = (TextView)findViewById(R.id.keyboard_overlay);

            this.txtDescription.setText(R.string.lblPleaseTouch);

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

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                this.downPos = position;
                this.downX = this.downPos % width;
                this.downY = this.downPos / width;
                Log.i("TAG", "ACTION_DOWN, x:" + this.downX + ", y:" + this.downY + ", position: " + this.downPos);
                manager.clearBGSelection(position, this.gridAdapter.getbgData());
                break;
            case MotionEvent.ACTION_UP: {

                // rejecting click on empty (black) area
                View child = this.gridView.getChildAt(position);
                if (child == null || child.getTag().equals(GameGridAdapter.AREA_BLOCK)) return true;
                // using horizontal or vertical words depend on click count
                if (this.downPos == position && this.currentPos == position) this.horizontal = !this.horizontal;
                //retrieving current word
                this.currentWord = getWord(this.downX, this.downY, this.horizontal);
                if(this.currentWord == null) return true;
                manager.setCurrentWord(currentWord);
                // getWord van return any direction
                this.horizontal = this.currentWord.getHorizontal();

                // Si clique sur la case, place le curseur sur le mot
                // Sinon place le curseur au debut du mot
                if (this.downPos == position) {
                    this.currentX = this.downX;
                    this.currentY = this.downY;
                    this.currentPos = position;
                } else {
                    this.currentX = this.currentWord.getX();
                    this.currentY = this.currentWord.getY();
                    this.currentPos = this.currentY * width + this.currentX;
                }

                this.txtDescription.setText(this.currentWord.getDescription());

                // Set background color
                List<View> views = new ArrayList<>();
                boolean horizontal = this.currentWord.getHorizontal();
                for (int l = 0; l < this.currentWord.getLength(); l++) {
                    int index = this.currentWord.getY() * width + this.currentWord.getX() + (l * (horizontal ? 1 : width));
                    View currentChild = this.gridView.getChildAt(index);
                    if (currentChild != null) {
                        currentChild.setBackgroundResource(index == this.currentPos ? R.drawable.area_current : R.drawable.area_selected);
                        views.add(currentChild);
                    }
                }
                manager.setBGSelection(views);

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

        System.out.println("onKeyDown: " + value + ", insert in: " + currentX + "x" + currentY);

        // Deplace l'overlay du clavier
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

        System.out.println("onKeyUp: " + value + ", insert in: " + currentX + "x" + currentY);

        // Efface l'overlay du clavier
        if (value.equals(" ") == false) {
            this.keyboardOverlay.setAnimation(AnimationUtils.loadAnimation(this, R.anim.keyboard_overlay_fade_out));
            this.keyboardOverlay.setVisibility(View.INVISIBLE);
        }

        // Si aucun mot selectionne, retour
        if (this.currentWord == null)
            return;

        // Case actuelle
        int x = this.currentX;
        int y = this.currentY;

        // Si la case est noire => retour
        if (this.gridAdapter.isBlock(x, y))
            return;

        // Ecrit la lettre sur le "curseur"
        this.gridAdapter.setValue(x, y, value);
        this.gridAdapter.notifyDataSetChanged();

        // Deplace sur le "curseur" sur la case precendante (effacer), ou suivante (lettres)
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
            this.gridView.getChildAt(this.currentY * width + this.currentX).setBackgroundResource(R.drawable.area_selected);
            this.currentX = x;
            this.currentY = y;
        }

    }

    @Override
    public void setDraft(boolean isDraft) {

    }
}
