package com.example.myapp.data;

import java.util.ArrayList;
import java.util.List;

import com.example.myapp.common.ModelHelper;
import com.example.myapp.data.board.GridPos;

public class Word {
	private int		x;
	private int		y;
	private int		length;
	private String	tmp;
	private String	text;
    private String	title;
	private String	description;
	private boolean	horizontal = true;
	
	public void		setText(String value) { this.text = value; this.length = value.length(); }
	public String	getText() { return this.text; }

    public void		setTitle(String value) { this.title = value; }
    public String	getTitle() { return this.title; }

	public void		setTmp(String value) { this.tmp = value; }
	public String	getTmp() { return this.tmp; }
	
	public void		setDescription(String value) { this.description= value; }
	public String	getDescription() { return this.description; }
	
	public boolean	getHorizontal() { return this.horizontal; }
	public void		setHorizontal(boolean value) { this.horizontal = value; }

	public void		setX(int value) { this.x = value; }
	public int		getX() { return this.x; }
	public int 		getXMax() { return this.horizontal ? this.x + this.length - 1: this.x; }
	
	public void		setY(int value) { this.y = value; }
	public int		getY() { return this.y; }
	public int 		getYMax() { return this.horizontal ? this.y : this.y + this.length - 1; }

	public int 		getLength() { return this.length; }
	
	public List<GridPos> getGridPositions() {
		List<GridPos> list = new ArrayList<GridPos>();
		for(int i = 0; i < text.length(); i++) {
			if(horizontal) {
				list.add(new GridPos(x + i, y));
			} else {
				list.add(new GridPos(x, y + i));
			}
		}
		return list;
	}
	
	public List<Integer> getGridIndexes() {
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < text.length(); i++) {
			if(horizontal) {
				list.add(ModelHelper.getGridIndex(x + i, y));
			} else {
				list.add(ModelHelper.getGridIndex(x, y + i));
			}
		}
		return list;
	}

}
