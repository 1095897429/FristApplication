package com.ngbj.browse.bean;

import java.io.Serializable;
import java.util.List;

/***
 * 用于上传 广告 + 大模块 的bean，便于封装
 */
public class UploadCountBean implements Serializable{


    /**
     * device_id : 12345678
     * ads : [{"ad_id":1,"show_num":2,"click_num":2,"click_user_num":1},{"ad_id":2,"show_num":3,"click_num":4,"click_user_num":1}]
     * modules : [{"module_id":5,"click_user_num":3,"click_num":4},{"module_id":4,"click_user_num":3,"click_num":4}]
     */

    private String device_id;
    private List<UploadCount> ads;
    private List<BigModelCountData> modules;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public List<UploadCount> getAds() {
        return ads;
    }

    public void setAds(List<UploadCount> ads) {
        this.ads = ads;
    }

    public List<BigModelCountData> getModules() {
        return modules;
    }

    public void setModules(List<BigModelCountData> modules) {
        this.modules = modules;
    }
}
