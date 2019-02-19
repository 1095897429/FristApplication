package com.ngbj.browse.event;

public class History_CollectionEvent {
    String link;
    String index;
    public History_CollectionEvent(String link,String index){
        this.link = link;
        this.index = index;
    }

    public String getLink() {
        return link;
    }

    public String getIndex() {
        return index;
    }
}
