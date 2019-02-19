package com.ngbj.browse.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;


/***
 * 没有用原先的集合，还没有研究
 */
public class NewsSaveMultiBean implements MultiItemEntity{

    public static final int TYPE_ONE = 1;
    public static final int TYPE_THREE = 3;
    private int itemType;

    /**
     * h5url : http://mp.weixin.qq.com/s?__biz=MjM5MDk1NzQzMQ==&mid=2653269655&idx=3&sn=3da5be2d486df4740226c287b8e1df1a&chksm=bd6de48f8a1a6d990b066bf1c720ee247d29879b8d32cca6127109fdf493753efab2553da368&scene=0
     * nid : 54517
     * signs :
     * show_type : 1
     * chaid : 0
     * title : 中国空间站时代来了
     * fromid : 24
     * pubtime : 1538186965
     * author : 环球时报
     * show_img : ["http://newsimg.xy599.com/15148478115bac804533a342.46669699.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]
     */

    private String h5url;
    private String nid;
    private String signs;
    private String show_type;
    private String chaid;
    private String title;
    private String fromid;
    private int pubtime;
    private String author;
    private List<String> show_img;


    public String getH5url() {
        return h5url;
    }

    public void setH5url(String h5url) {
        this.h5url = h5url;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getSigns() {
        return signs;
    }

    public void setSigns(String signs) {
        this.signs = signs;
    }

    public String getShow_type() {
        return show_type;
    }

    public void setShow_type(String show_type) {
        this.show_type = show_type;
    }

    public String getChaid() {
        return chaid;
    }

    public void setChaid(String chaid) {
        this.chaid = chaid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public int getPubtime() {
        return pubtime;
    }

    public void setPubtime(int pubtime) {
        this.pubtime = pubtime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public  List<String> getShow_img() {
        return show_img;
    }

    public void setShow_img( List<String> show_img) {
        this.show_img = show_img;
    }

    public NewsSaveMultiBean(String h5url, String title, String author,  List<String> show_img) {
        this.h5url = h5url;
        this.title = title;
        this.author = author;
        this.show_img = show_img;
    }

    public NewsSaveMultiBean() {
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return this.itemType;
    }
}
