package com.ngbj.browse.event;

public class RefreshDataEvent {
    private int index;
    public RefreshDataEvent(int index ){
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
