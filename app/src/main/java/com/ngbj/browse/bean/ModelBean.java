package com.ngbj.browse.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/***
 * 统计模块用户数据Bean
 */
@Entity
public class ModelBean implements Serializable {
    @Id(autoincrement = true)
    private Long id;
    private String model_id;
    private String date;//当日23:59:59的时间
    private boolean is_clicked;//今天是否点击过了

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
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

    public ModelBean(String model_id, String date, boolean is_clicked) {
        this.model_id = model_id;
        this.date = date;
        this.is_clicked = is_clicked;
    }

    @Generated(hash = 232778778)
    public ModelBean(Long id, String model_id, String date, boolean is_clicked) {
        this.id = id;
        this.model_id = model_id;
        this.date = date;
        this.is_clicked = is_clicked;
    }

    @Generated(hash = 178437845)
    public ModelBean() {
    }
}
