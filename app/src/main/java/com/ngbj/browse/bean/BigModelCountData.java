package com.ngbj.browse.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/***
 * 大模块的点击量
 * 需要记录的：
 *  点击次数
 */
@Entity
public class BigModelCountData implements Serializable {
    @Id(autoincrement = true)
    private Long id;
    private int module_id;//大模块的一个id
    private int click_num;//大模块的点击次数
    private int click_user_num;//此用户数
    private String bigModelShowName;//大模块的展示名称
    private int type;//大模块类型


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }





    public String getBigModelShowName() {
        return bigModelShowName;
    }

    public void setBigModelShowName(String bigModelShowName) {
        this.bigModelShowName = bigModelShowName;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public int getClick_num() {
        return click_num;
    }

    public void setClick_num(int click_num) {
        this.click_num = click_num;
    }

    public int getClick_user_num() {
        return click_user_num;
    }

    public void setClick_user_num(int click_user_num) {
        this.click_user_num = click_user_num;
    }

    public BigModelCountData(int module_id, int click_num, String bigModelShowName, int type) {
        this.module_id = module_id;
        this.click_num = click_num;
        this.bigModelShowName = bigModelShowName;
        this.type = type;
    }

    @Generated(hash = 358894139)
    public BigModelCountData(Long id, int module_id, int click_num, int click_user_num,
            String bigModelShowName, int type) {
        this.id = id;
        this.module_id = module_id;
        this.click_num = click_num;
        this.click_user_num = click_user_num;
        this.bigModelShowName = bigModelShowName;
        this.type = type;
    }

    @Generated(hash = 1997371266)
    public BigModelCountData() {
    }
}
