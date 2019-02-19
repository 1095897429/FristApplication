package com.ngbj.browse.event;

public class NewsShowFragmentEvent {
    private String link;
    private int type;
    public NewsShowFragmentEvent(String link,int type){
        this.link = link;
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public int getType() {
        return type;
    }
}
