package com.ngbj.browse.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * 历史搜索关键字记录
 *
 */
@Entity
public class KeyBean implements Serializable {
    @Id(autoincrement = true)
    private Long id;

    private String keyName;
    private String currentTime;//点击的时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public KeyBean(String keyName) {
        this.keyName = keyName;
    }

    @Generated(hash = 2088928649)
    public KeyBean(Long id, String keyName, String currentTime) {
        this.id = id;
        this.keyName = keyName;
        this.currentTime = currentTime;
    }

    @Generated(hash = 217750680)
    public KeyBean() {
    }



    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
