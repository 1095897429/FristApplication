package com.ngbj.browse.bean;

import java.io.Serializable;

/***
 * 初始化数据
 */
public class OriData implements Serializable {
    private String province;//省份
    private String city;//城市
    private String latest_version;//版本
    private String download_url;//下载地址

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLatest_version() {
        return latest_version;
    }

    public void setLatest_version(String latest_version) {
        this.latest_version = latest_version;
    }
}
