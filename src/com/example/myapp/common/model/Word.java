package com.example.myapp.common.model;

import java.util.ArrayList;
import java.util.List;

import com.example.myapp.common.helper.ModelHelper;

public class Word {

    private int x;
    private int y;
    private int length;
    private String tmp;
    private String text;
    private String title;
    private String description;
    private boolean horizontal;
    private boolean completed;

    public Word() {
        this.horizontal = true;
    }

    public void setText(String value) {
        this.text = value;
        this.length = value.length();
    }

    public String getText() {
        return this.text;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTmp(String value) {
        this.tmp = value;
    }

    public String getTmp() {
        return this.tmp;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public void setX(int value) {
        this.x = value;
    }

    public int getX() {
        return this.x;
    }

    public int getXMax() {
        return this.horizontal ? this.x + this.length - 1 : this.x;
    }

    public void setY(int value) {
        this.y = value;
    }

    public int getY() {
        return this.y;
    }

    public int getYMax() {
        return this.horizontal ? this.y : this.y + this.length - 1;
    }

    public int getLength() {
        return this.length;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<GridPos> getGridPositions() {
        List<GridPos> list = new ArrayList<GridPos>();
        for (int i = 0; i < text.length(); i++) {
            if (horizontal) {
                list.add(new GridPos(y, x + i));
            } else {
                list.add(new GridPos(y + i, x));
            }
        }
        return list;
    }

    public List<Integer> getGridIndexes() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < text.length(); i++) {
            if (horizontal) {
                list.add(ModelHelper.getGridIndex(y, x + i));
            } else {
                list.add(ModelHelper.getGridIndex(y + i, x));
            }
        }
        return list;
    }

    public boolean isCorrect() {
        return text.equalsIgnoreCase(tmp);
    }

    public boolean hasPosition(GridPos pos) {
        List<GridPos> positions = getGridPositions();
        for (GridPos position : positions) {
            if (position.getRow() == pos.getRow() && position.getCol() == pos.getCol()) {
                return true;
            }
        }
        return false;
    }

    public String getLetterAtPosition(GridPos pos) {
        List<GridPos> positions = getGridPositions();
        for (int i = 0; i < positions.size(); i++) {
            GridPos position = positions.get(i);
            if (position.getRow() == pos.getRow() && position.getCol() == pos.getCol()) {
                return String.valueOf(text.charAt(i));
            }
        }
        return "";
    }
}
