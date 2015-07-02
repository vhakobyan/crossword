package com.example.myapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.example.myapp.activity.GameActivity;
import com.example.myapp.R;
import com.example.myapp.util.ModelHelper;
import com.example.myapp.common.manager.GameManager;
import com.example.myapp.common.model.Word;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.example.myapp.common.model.BGCell;

public class GameGridAdapter extends BaseAdapter {



    public static final int AREA_BLOCK = -1;
    public static final int AREA_WRITABLE = 0;
    private Map<Integer, TextView> views;
    private Context context;
    // user letters
    private String[][] area;            // Tableau représentant les lettres du joueur

    private int displayHeight;
    private int width;
    private int height;


    public GameGridAdapter(Activity act) {

        views = new HashMap<Integer, TextView>();

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(act);
        this.context = act;
        this.width = ModelHelper.getGrid().getWidth();
        this.height = ModelHelper.getGrid().getHeight();

        // Calcul area height
        Display display = act.getWindowManager().getDefaultDisplay();
        this.displayHeight = display.getWidth() / this.width;

        // Fill area and areaCorrection
        this.area = new String[this.height][this.width];

        for (Word words : ModelHelper.getHorizontalWords()) {

            String tmp = words.getTmp();
            String text = words.getText();

            int x = words.getX();
            int y = words.getY();

            for (int i = 0; i < words.getLength(); i++) {

                if (y >= 0 && y < this.height && x + i >= 0 && x + i < this.width) {
                    //this.area[y][x + i] = tmp != null ? String.valueOf(tmp.charAt(i)) : (k == 1) ? title : " ";
                    if(tmp != null){
                        //BGCell cell = new BGCell(BGType.NUMBER);
                        this.area[y][x + i] = String.valueOf(tmp.charAt(i));
                        //bgDataArr[y][x + i] = cell;
                    } else {
                        this.area[y][x + i] = " ";

                    }

                }
            }
        }

        for (Word words : ModelHelper.getVerticalWords()) {

            String tmp = words.getTmp();
            String text = words.getText();

            int x = words.getX();
            int y = words.getY();

            for (int i = 0; i < words.getLength(); i++) {

                if (y + i >= 0 && y + i < this.height && x >= 0 && x < this.width) {
                    //this.area[y + i][x] = tmp != null ? String.valueOf(tmp.charAt(i)) : (k == 1) ? title : " ";
                    if(tmp != null){
                        this.area[y + i][x] = String.valueOf(tmp.charAt(i));
                    } else {
                        this.area[y + i][x] = " ";

                    }
                }
            }
        }

    }

    @Override
    public int getCount() {
        return this.height * this.width;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView v = this.views.get(position);

        if (v == null) {

            v = new TextView(context);
            v.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.FILL_PARENT, this.displayHeight));
            v.setTextSize((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4 ? 30 : 20);
            v.setGravity(Gravity.CENTER);

            GameManager manager = ((GameActivity) context).getManager();
            BGCell bgCell = manager.getBGCell(position);

            if (bgCell.isArea()) {
                v.setBackgroundResource(R.drawable.area_empty);
                v.setTag(AREA_WRITABLE);
            } else if (bgCell.isNumber()) {
                v.setBackgroundResource(getId("cell_" + bgCell.getVal(), R.drawable.class));
                v.setTag(AREA_WRITABLE);
            } else {
                v.setBackgroundResource(R.drawable.area_block);
                v.setTag(AREA_BLOCK);
            }

            this.views.put(position, v);
        }

        int row = position / this.width;
        int col = position % this.width;
        String data = this.area[row][col];

        if (data != null) {
            v.setTextColor(context.getResources().getColor(R.color.normal));
            v.setText(data);
        }

        return v;
    }

    public int getPercent() {
        int filled = 0;
        int empty = 0;

        for (int y = 0; y < this.height; y++)
            for (int x = 0; x < this.width; x++)
                if (this.area[y][x] != null) {
                    if (this.area[y][x].equals(" "))
                        empty++;
                    else
                        filled++;
                }
        return filled * 100 / (empty + filled);
    }

    public boolean isBlock(int x, int y) {
        return (this.area[y][x] == null);
    }

    public void setValue(int x, int y, String value) {
        if (this.area[y][x] != null)
            this.area[y][x] = value;
    }

    public String getWord(int x, int y, int length, boolean isHorizontal) {
        StringBuffer word = new StringBuffer();
        for (int i = 0; i < length; i++) {
            if (isHorizontal) {
                if (y < this.height && x + i < this.width)
                    word.append(this.area[y][x + i] != null ? this.area[y][x + i] : " ");
            } else {
                if (y + i < this.height && x < this.width)
                    word.append(this.area[y + i][x] != null ? this.area[y + i][x] : " ");
            }
        }
        return word.toString();
    }

    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: " + resourceName + " / " + c, e);
        }
    }

}
