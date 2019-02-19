package com.ngbj.browse.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 展示天气状态
 */

@Entity
public class WeatherSaveBean implements Serializable {
    @Id
    private Long id;
    private String temp;//温度
    private String area;//地址
    private String condition;//多云

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public WeatherSaveBean(String temp, String area, String condition) {
        this.temp = temp;
        this.area = area;
        this.condition = condition;
    }

    @Generated(hash = 268673271)
    public WeatherSaveBean(Long id, String temp, String area, String condition) {
        this.id = id;
        this.temp = temp;
        this.area = area;
        this.condition = condition;
    }

    @Generated(hash = 1644231941)
    public WeatherSaveBean() {
    }
}
