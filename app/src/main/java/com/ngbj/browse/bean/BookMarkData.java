package com.ngbj.browse.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class BookMarkData implements Serializable {
    @Id(autoincrement = true)
    private Long id;
    private String  visit_link;//链接地址
    private String title;//关键字
    private String icon;//标题
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


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public BookMarkData(String visit_link, String title, String icon, String currentTime) {
        this.visit_link = visit_link;
        this.title = title;
        this.icon = icon;
        this.currentTime = currentTime;
    }

    @Generated(hash = 1943839536)
    public BookMarkData(Long id, String visit_link, String title, String icon,
            String currentTime) {
        this.id = id;
        this.visit_link = visit_link;
        this.title = title;
        this.icon = icon;
        this.currentTime = currentTime;
    }

    @Generated(hash = 1868241036)
    public BookMarkData() {
    }



}
