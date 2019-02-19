package com.ngbj.browse.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class HistoryData implements Serializable {
    @Id(autoincrement = true)
    private Long id;
    private String  visit_link;//链接地址
    private String type;//来源哪里  1.关键字  0.浏览
    private String keyword;//关键字
    private String title;//标题
    private String currentTime;//点击的时间


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVisit_link() {
        return visit_link;
    }

    public void setVisit_link(String visit_link) {
        this.visit_link = visit_link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public HistoryData(String visit_link, String keyword,String type) {
        this.visit_link = visit_link;
        this.type = type;
        this.keyword = keyword;
    }


    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Generated(hash = 1124441852)
    public HistoryData(Long id, String visit_link, String type, String keyword, String title,
            String currentTime) {
        this.id = id;
        this.visit_link = visit_link;
        this.type = type;
        this.keyword = keyword;
        this.title = title;
        this.currentTime = currentTime;
    }

    public HistoryData(String visit_link, String type, String keyword, String currentTime) {
        this.visit_link = visit_link;
        this.type = type;
        this.keyword = keyword;
        this.currentTime = currentTime;
    }

    public HistoryData() {
    }


    public boolean equals(Object obj) {
        if (obj instanceof HistoryData) {
            HistoryData historyData = (HistoryData) obj;
            return (title.equals(historyData.getTitle()));
        }
        return super.equals(obj);
    }

    public int hashCode() {
        return title.hashCode();

    }


}
