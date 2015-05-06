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
import android.widget.*;
import com.example.myapp.KeyboardView;
import com.example.myapp.KeyboardViewInterface;
import com.example.myapp.R;
import com.example.myapp.adapter.GameGridAdapter;
import com.example.myapp.common.JSONHelper;
import com.example.myapp.common.ModelHelper;
import com.example.myapp.data.Grid;
import com.example.myapp.data.Word;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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


    private ArrayList<View> selectedArea = new ArrayList<View>(); // List of selection boxes

    private boolean downIsPlayable;    // false if the player pressed a black box
    private int downPos;        // Position or player supported
    private int downX;            //Line or player supported
    private int downY;            // Column or player supported
    private int currentPos;        // Current cursor position
    private int currentX;        // Current column of the cursor
    private int currentY;        // Current cursor line
    private Word currentWord;    //Currently selected word
    private boolean horizontal;        // Direction of selection

    private String filename;        // Name of the gate

    private boolean solidSelection;    // PREFERENCES: Selection persistante
    private boolean gridIsLower;    // PREFERENCES: Tiny grid

    private int width;
    private int height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game);

        readPreferences();

        try {

            initGameModel();





            this.width = ModelHelper.getGrid().getWidth();
            this.height = ModelHelper.getGrid().getHeight();

            Display display = getWindowManager().getDefaultDisplay();
            int height = display.getHeight();
            int keyboardHeight = (int) (height / 4.4);

            this.txtDescription = (TextView) findViewById(R.id.description);
            this.txtDescription.setText(R.string.lblPleaseTouch);

            this.gridView = (GridView) findViewById(R.id.grid);
            this.gridView.setOnTouchListener(this);
            this.gridView.setNumColumns(ModelHelper.getGrid().getWidth());
            android.view.ViewGroup.LayoutParams gridParams = this.gridView.getLayoutParams();
            gridParams.height = height - keyboardHeight - this.txtDescription.getLayoutParams().height;
            this.gridView.setLayoutParams(gridParams);
            this.gridView.setVerticalScrollBarEnabled(false);
            this.gridAdapter = new GameGridAdapter(this);
            this.gridView.setAdapter(this.gridAdapter);

            this.keyboardView = (KeyboardView)findViewById(R.id.keyboard);
            this.keyboardView.setDelegate(this);
            android.view.ViewGroup.LayoutParams KeyboardParams = this.keyboardView.getLayoutParams();
            KeyboardParams.height = keyboardHeight;
            this.keyboardView.setLayoutParams(KeyboardParams);
            this.keyboardOverlay = (TextView)findViewById(R.id.keyboard_overlay);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void initGameModel() {
        try {
            String loadJSONFromAsset = loadJSONFromAsset();
            JSONObject jsonObj = new JSONObject(loadJSONFromAsset);
            JSONObject obj = jsonObj.getJSONObject("grid");
            ModelHelper.setGrid(JSONHelper.getGrid(obj));
            ModelHelper.setVerticalWords(JSONHelper.grtVerticalWords(obj));
            ModelHelper.setHorizontalWords(JSONHelper.grtHorizontalWords(obj));
        } catch (JSONException e) {
            Log.e("initGameModel", e.getMessage());
        }
    }



    private void readPreferences() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.solidSelection = preferences.getBoolean("solid_selection", false);
        this.gridIsLower = preferences.getBoolean("grid_is_lower", false);
        if (currentMode != GRID_MODE.SOLVE)
            currentMode = preferences.getBoolean("grid_check", false) ? GRID_MODE.CHECK : GRID_MODE.NORMAL;

    }

    public String loadJSONFromAsset() {

        String json = null;
        InputStream is = null;

        try {

            is = getAssets().open("grid_01.json");
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
            } catch (IOException ignored) {
            }
        }
        return json;

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        int x1 = (int) event.getX();
        int y1 = (int) event.getY();
        int position = this.gridView.pointToPosition(x1, y1);


        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN: {


                View child = this.gridView.getChildAt(position);

                // Si pas de mot sur cette case (= case noire), aucun traitement
                if (child == null || child.getTag().equals(GameGridAdapter.AREA_BLOCK)) {
                    if (this.solidSelection == false) {
                        clearSelection();
                        this.gridAdapter.notifyDataSetChanged();
                    }

                    this.downIsPlayable = false;
                    return true;
                }
                this.downIsPlayable = true;

                // Stocke les coordonnees d'appuie sur l'ecran
                this.downPos = position;
                this.downX = this.downPos % this.width;
                this.downY = this.downPos / this.width;
                Log.i("TAG", "ACTION_DOWN, x:" + this.downX + ", y:" + this.downY + ", position: " + this.downPos);
//                Toast.makeText(this, "ACTION_DOWN, x:" + this.downX + ", y:" + this.downY + ", position: " + this.downPos, Toast.LENGTH_LONG).show();

                clearSelection();

                // Colore la case en bleu
                child.setBackgroundResource(R.drawable.area_selected);
                selectedArea.add(child);

                this.gridAdapter.notifyDataSetChanged();
                break;
            }

            case MotionEvent.ACTION_UP: {
                // Si le joueur à appuyé sur une case noire, aucun traitement
                if (this.downIsPlayable == false)
                    return true;

                int x = position % this.width;
                int y = position / this.width;
                Log.i("TAG", "ACTION_DOWN, x:" + x + ", y:" + y + ", position: " + position);
//                Toast.makeText(this, "ACTION_DOWN, x:" + x + ", y:" + y + ", position: " + position, Toast.LENGTH_LONG).show();

                // Si clique sur la case, inversion horizontale <> verticale
                // Si clique sur une autre case (= mouvement) calcul en fonction de la gesture
                if (this.downPos == position && this.currentPos == position) {
                    this.horizontal = !this.horizontal;
                } else if (this.downPos != position) {
                    this.horizontal = (Math.abs(this.downX - x) > Math.abs(this.downY - y));
                }

                // Test si un mot se trouve sur cette case
                this.currentWord = getWord(this.downX, this.downY, this.horizontal);
                if (this.currentWord == null)
                    break;

                // Force la direction a etre dans le meme sens que le mot
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
                    this.currentPos = this.currentY * this.width + this.currentX;
                }

                this.txtDescription.setText(this.currentWord.getDescription());

                // Set background color
                boolean horizontal = this.currentWord.getHorizontal();
                for (int l = 0; l < this.currentWord.getLength(); l++) {
                    int index = this.currentWord.getY() * this.width + this.currentWord.getX() + (l * (horizontal ? 1 : this.width));
                    View currentChild = this.gridView.getChildAt(index);
                    if (currentChild != null) {
                        currentChild.setBackgroundResource(index == this.currentPos ? R.drawable.area_current : R.drawable.area_selected);
                        selectedArea.add(currentChild);
                    }
                }

                this.gridAdapter.notifyDataSetChanged();
                break;
            }
        }
        // if you return false, these actions will not be recorded
        return true;
    }

    private void clearSelection() {
        for (View selected : selectedArea)
            selected.setBackgroundResource(R.drawable.area_empty);
        selectedArea.clear();
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
        if (x >= 0 && x < this.width
                && y >= 0 && y < this.height
                && this.gridAdapter.isBlock(x, y) == false) {
            this.gridView.getChildAt(y * this.width + x).setBackgroundResource(R.drawable.area_current);
            this.gridView.getChildAt(this.currentY * this.width + this.currentX).setBackgroundResource(R.drawable.area_selected);
            this.currentX = x;
            this.currentY = y;
        }

    }

    @Override
    public void setDraft(boolean isDraft) {

    }
}
