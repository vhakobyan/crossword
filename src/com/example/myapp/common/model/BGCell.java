package com.example.myapp.common.model;

import com.example.myapp.common.type.BGType;

import static com.example.myapp.common.type.BGType.*;

/**
 * Created by Vahagn Hakobyan
 * on 5/6/15- 11:08 AM
 */
public class BGCell {

    private BGType type;
    private String val;

    public BGCell(BGType type) {
        this.type = type;
    }

    public boolean isNumber() {
        return this.type == NUMBER;
    }

    public boolean isArea() {
        return this.type == AREA;
    }

    public boolean isEmpty() {
        return this.type == EMPTY;
    }

    public boolean isComplete() {
        return this.type == COMPLETE;
    }

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

    public void setType(BGType type) {
        this.type = type;
    }
}
