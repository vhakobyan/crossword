package com.example.myapp.data.board;

/**
 * Created by Vahagn Hakobyan
 * on 5/6/15- 11:08 AM
 */
public class BGCell {

    private BGType type;

    public BGCell(BGType type) {
        this.type = type;
    }

    public boolean isNumber() {
        return this.type == BGType.NUMBER;
    }

    public boolean isArea() {
        return this.type == BGType.AREA;
    }

    public boolean isEmpty() {
        return this.type == BGType.EMPTY;
    }
}
