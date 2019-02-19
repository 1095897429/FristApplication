package com.ngbj.browse.event;

public class DataToTopEvent {
    private int index;
    public DataToTopEvent(int index ){
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
