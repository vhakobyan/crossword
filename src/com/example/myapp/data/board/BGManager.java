package com.example.myapp.data.board;

import java.util.Iterator;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.example.myapp.R;
import com.example.myapp.adapter.GameGridAdapter;
import com.example.myapp.common.ModelHelper;
import com.example.myapp.data.Word;

/**
 * Created by Vahagn Hakobyan
 * on 5/6/15 - 11:06 AM
 */
public class BGManager {

	private GridView gridView;
    private BGCell[][] bgDataArr;
    private int rows;
    private int cols;

    public BGManager(GridView gv) {
    	this.gridView = gv;
    }

    public void clearSelection(int position, String[][] pbgData) {
    	
    	Word currentWord = ModelHelper.getCurrentWord();
    	if(currentWord != null) {
	    	List<Integer> gridIndexes = currentWord.getGridIndexes();
	    	for (int i = 0; i < gridIndexes.size(); i++) {
	    		int index = gridIndexes.get(i);
	    		BGCell cell = getBGCell(index);
	    		View view = this.gridView.getChildAt(index);
	    		if (cell.isArea()) {
	    			view.setBackgroundResource(R.drawable.area_empty);
	            } else if (cell.isNumber()) {
	            	view.setBackgroundResource(GameGridAdapter.getId("cell_" + cell.getVal(), R.drawable.class));
	            } else {
	            	view.setBackgroundResource(R.drawable.area_block);
	            }
			}
    	}
    }

    public void setSelection(List<View> views) {
        ModelHelper.setSelection(views);
    }

    public void setCurrentWord(Word currentWord) {
    	ModelHelper.setCurrentWord(currentWord);
    }

    public void initBGManager(int width, int height, List<Word> hw, List<Word> vw) {
    	
    	this.rows = height;
    	this.cols = width;
    	
        bgDataArr = new BGCell[height][width];

        for (int c = 0; c < width; c++) {
            for (int r = 0; r < height; r++) {
                bgDataArr[r][c] = new BGCell(BGType.EMPTY);
            }
        }

        for (Word entry : hw) {

            int x = entry.getX();
            int y = entry.getY();

//            Log.i("WORD", "hw " + entry.getText());
            for (int i = 0; i < entry.getLength(); i++) {
                if (y >= 0 && y < height && x + i >= 0 && x + i < width) {
                    BGCell bgCell = new BGCell(i == 0 ? BGType.NUMBER : BGType.AREA);
                    if(i == 0) bgCell.setVal(entry.getTitle());
					bgDataArr[y][x + i] = bgCell;
                }
            }
        }
        for (Word entry : vw) {

            int x = entry.getX();
            int y = entry.getY();

//            Log.i("WORD", "vw " + entry.getText());
            for (int i = 0; i < entry.getLength(); i++) {
                if (y + i >= 0 && y + i < height && x >= 0 && x < width) {
                    if(!bgDataArr[y + i][x].isNumber()) {
						BGCell bgCell = new BGCell(i == 0 ? BGType.NUMBER : BGType.AREA);
						if(i == 0) bgCell.setVal(entry.getTitle());
						bgDataArr[y + i][x] = bgCell;
					}
                }
            }
        }
        
        Log.i("initBGManager", "OK");

    }
    
    public BGCell getBGCell(int index) {
    	return bgDataArr[index / cols][index % cols];
    } 
}
