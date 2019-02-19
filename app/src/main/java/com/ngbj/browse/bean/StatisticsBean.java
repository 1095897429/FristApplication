package com.ngbj.browse.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/***
 * 统计用户数据Bean
 */
@Entity
public class StatisticsBean implements Serializable {
    @Id(autoincrement = true)
    private Long id;
    private String ad_id;
    private String date;//当日23:59:59的时间
    private boolean is_clicked;//今天是否点击过了

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isIs_clicked() {
        return is_clicked;
    }

    public void setIs_clicked(boolean is_clicked) {
        this.is_clicked = is_clicked;
    }

    public boolean getIs_clicked() {
        return this.is_clicked;
    }

    public StatisticsBean(String ad_id, String date, boolean is_clicked) {
        this.ad_id = ad_id;
        this.date = date;
        this.is_clicked = is_clicked;
    }

    @Generated(hash = 456789906)
    public StatisticsBean(Long id, String ad_id, String date, boolean is_clicked) {
        this.id = id;
        this.ad_id = ad_id;
        this.date = date;
        this.is_clicked = is_clicked;
    }

    @Generated(hash = 1303279983)
    public StatisticsBean() {
    }

    

}
