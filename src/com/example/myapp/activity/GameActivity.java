package com.example.myapp.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapp.R;
import com.example.myapp.adapter.GameGridAdapter;
import com.example.myapp.data.Grid;
import com.example.myapp.data.Word;
import com.example.myapp.pars.CrosswordParser;
import com.example.myapp.pars.GridParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.view.View.OnTouchListener;

public class GameActivity extends Activity implements OnTouchListener {

    public static final String GRID_LOCAL_PATH = "/data/data/com.example.myapp/grid/%s";
    public static final String GRID_DIRECTORY = "/data/data/ccom.example.myapp/grid/";

    public enum GRID_MODE {NORMAL, CHECK, SOLVE}

    ;
    public GRID_MODE currentMode = GRID_MODE.NORMAL;

    private GridView gridView;
    private GameGridAdapter gridAdapter;
    private TextView txtDescription;
    private TextView keyboardOverlay;

    private Grid grid;
    private ArrayList<Word> entries;        // Liste des mots
    private ArrayList<View> selectedArea = new ArrayList<View>(); // Liste des cases selectionnées

    private boolean downIsPlayable;    // false si le joueur à appuyé sur une case noire
    private int downPos;        // Position ou le joueur à appuyé
    private int downX;            // Ligne ou le joueur à appuyé
    private int downY;            // Colonne ou le joueur à appuyé
    private int currentPos;        // Position actuelle du curseur
    private int currentX;        // Colonne actuelle du curseur
    private int currentY;        // Ligne actuelle du curseur
    private Word currentWord;    // Mot actuellement selectionné
    private boolean horizontal;        // Sens de la selection

    private String filename;        // Nom de la grille

    private boolean solidSelection;    // PREFERENCES: Selection persistante
    private boolean gridIsLower;    // PREFERENCES: Grille en minuscule

    private int width;
    private int height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        readPreferences();

        try {

            String loadJSONFromAsset = loadJSONFromAsset();
            JSONObject jsonObj = new JSONObject(loadJSONFromAsset);
            JSONObject jsonGrid = jsonObj.getJSONObject("grid");

            Grid grid = new Grid();
            grid.setName(String.valueOf(jsonGrid.get("name")));
//            grid.setDescription(String.valueOf(jsonGrid.get("description")));
            grid.setLevel(Integer.valueOf(String.valueOf(jsonGrid.get("level"))));
            grid.setWidth(Integer.valueOf(String.valueOf(jsonGrid.get("width"))));
            grid.setHeight(Integer.valueOf(String.valueOf(jsonGrid.get("height"))));
            grid.setDate((new SimpleDateFormat("dd/MM/yyyy")).parse(String.valueOf(jsonGrid.get("date"))));

            this.grid = grid;

            ArrayList<Word> words = new ArrayList<>();

            JSONObject horizontalObj = new JSONObject(String.valueOf(jsonGrid.get("horizontal")));
            JSONObject verticalObj = new JSONObject(String.valueOf(jsonGrid.get("vertical")));
            JSONArray horizontalWords = horizontalObj.getJSONArray("word");
            JSONArray verticalWords = verticalObj.getJSONArray("word");

            Word word;
            for (int i = 0; i < horizontalWords.length(); i++) {
                JSONObject obj = horizontalWords.getJSONObject(i);
                word = new Word();
                word.setX(Integer.valueOf(String.valueOf(obj.get("x"))));
                word.setY(Integer.valueOf(String.valueOf(obj.get("y"))));
                word.setDescription(String.valueOf(obj.get("text")));
                word.setText(String.valueOf(obj.get("description")));
                word.setHorizontal(true);
                words.add(word);
            }

            for (int i = 0; i < verticalWords.length(); i++) {
                JSONObject obj = verticalWords.getJSONObject(i);
                word = new Word();
                word.setX(Integer.valueOf(String.valueOf(obj.get("x"))));
                word.setY(Integer.valueOf(String.valueOf(obj.get("y"))));
                word.setDescription(String.valueOf(obj.get("text")));
                word.setText(String.valueOf(obj.get("description")));
                word.setHorizontal(false);
                words.add(word);
            }

            this.entries = words;


            this.width = this.grid.getWidth();
            this.height = this.grid.getHeight();

            Display display = getWindowManager().getDefaultDisplay();
            int height = display.getHeight();
            int keyboardHeight = (int) (height / 4.4);

            this.txtDescription = (TextView) findViewById(R.id.description);

            this.gridView = (GridView) findViewById(R.id.grid);
            this.gridView.setOnTouchListener(this);
            this.gridView.setNumColumns(this.width);
            android.view.ViewGroup.LayoutParams gridParams = this.gridView.getLayoutParams();
            gridParams.height = height - keyboardHeight - this.txtDescription.getLayoutParams().height;
            this.gridView.setLayoutParams(gridParams);
            this.gridView.setVerticalScrollBarEnabled(false);
            this.gridAdapter = new GameGridAdapter(this, this.entries, this.width, this.height);
            this.gridView.setAdapter(this.gridAdapter);

//            this.keyboardView = (KeyboardView)findViewById(R.id.keyboard);
//            this.keyboardView.setDelegate(this);
//            android.view.ViewGroup.LayoutParams KeyboardParams = this.keyboardView.getLayoutParams();
//            KeyboardParams.height = keyboardHeight;
//            this.keyboardView.setLayoutParams(KeyboardParams);
//            this.keyboardOverlay = (TextView)findViewById(R.id.keyboard_overlay);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN: {

                int position = this.gridView.pointToPosition((int) event.getX(), (int) event.getY());
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

                int position = this.gridView.pointToPosition((int) event.getX(), (int) event.getY());
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
        Word horizontalWord = null;
        Word verticalWord = null;
        for (Word entry : this.entries) {
            if (x >= entry.getX() && x <= entry.getXMax())
                if (y >= entry.getY() && y <= entry.getYMax()) {
                    if (entry.getHorizontal())
                        horizontalWord = entry;
                    else
                        verticalWord = entry;
                }
        }

        if (horizontal)
            return (horizontalWord != null) ? horizontalWord : verticalWord;
        else
            return (verticalWord != null) ? verticalWord : horizontalWord;
    }
}
