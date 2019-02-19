package com.ngbj.browse.bean;

import java.io.Serializable;

/**
 * AD Bean
 *
 */
public class AdBean implements Serializable {
    /**
     * id : 2
     * title : yitiaoguanggao
     * link : www.baidu.com
     * img_url : http://pgfiz5ht4.bkt.clouddn.com/xnbrowser/ads/20181016/Dp-jDASR-f-3klNM.jpeg
     * begin_time : 0
     * end_time : 0
     * type : 0
     * show_position : 1
     * order : 1
     */
    private String id;
    private String title;
    private String link;
    private String img_url;
    private String begin_time;
    private String end_time;
    private String type;
    private String show_position;
    private String order;
    private int  resId;//这个不是后台返回的，新增它为了更方便的显示 +

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShow_position() {
        return show_position;
    }

    public void setShow_position(String show_position) {
        this.show_position = show_position;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
