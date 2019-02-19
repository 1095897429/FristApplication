package com.ngbj.browse.bean;

import java.io.Serializable;

public class SlashAdBean implements Serializable{


    /**
     * imgurl : http://pat.farvd.com/uploads/picture/2016/07/20/1532070780.jpg
     * wenzi : null
     * width : 700
     * height : 1280
     * mediaid : 1696
     * planid : 3658
     * gotourl : https://bo.linlusp.com/lmbbss/
     * ip : 180.167.137.215
     * key : f423aa0f1a6669e87faf7a346288767e
     * count_url : http://api.cferw.com/reapi.php?s=ODI4MzMyfDIxNzR8MjIyMTl8N3wxNjk2fDM2NTh8MTgwLjE2Ny4xMzcuMjE1fGltZWk=;bdc562a49c4a53e3ccde53ee4d69d6ef;1&getImageTJ=1532070780&key=f423aa0f1a6669e87faf7a346288767e&lrdst=MTU0MTY0NTEyMi4xNTk1
     * click_url : http://api.cferw.com/reapi.php?s=ODI4MzMyfDIxNzR8MjIyMTl8N3wxNjk2fDM2NTh8MTgwLjE2Ny4xMzcuMjE1fGltZWk=;bdc562a49c4a53e3ccde53ee4d69d6ef;1&getImageTJ=1532070780&clicks=1&key=f423aa0f1a6669e87faf7a346288767e&lrdst=MTU0MTY0NTEyMi4xNTk1
     * sdown_url : https://anquan.luomi.com/adveranaly/downinfo/apiDownDb?dev_appkey=f423aa0f1a6669e87faf7a346288767e&dev_time={dev_time}&dev_flag=1&dev_planid=3658&dev_deviceid=imei&plat=1
     * edown_url : https://anquan.luomi.com/adveranaly/downinfo/apiDownDb?dev_appkey=f423aa0f1a6669e87faf7a346288767e&dev_time={dev_time}&dev_flag=2&dev_planid=3658&dev_deviceid=imei&plat=1
     * finish_url : https://anquan.luomi.com/adveranaly/instalinfo/apiInstalDb?appkey=f423aa0f1a6669e87faf7a346288767e&deviceId=imei&plat=1&packageName={packageName}&dev_flag=2&dev_time={dev_time}
     * is_link : 1
     * succ : 1
     * code : 0
     */

    private String imgurl;
    private Object wenzi;
    private String width;
    private String height;
    private String mediaid;
    private String planid;
    private String gotourl;
    private String ip;
    private String key;
    private String count_url;
    private String click_url;
    private String sdown_url;
    private String edown_url;
    private String finish_url;
    private int is_link;
    private int succ;
    private int code;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Object getWenzi() {
        return wenzi;
    }

    public void setWenzi(Object wenzi) {
        this.wenzi = wenzi;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getMediaid() {
        return mediaid;
    }

    public void setMediaid(String mediaid) {
        this.mediaid = mediaid;
    }

    public String getPlanid() {
        return planid;
    }

    public void setPlanid(String planid) {
        this.planid = planid;
    }

    public String getGotourl() {
        return gotourl;
    }

    public void setGotourl(String gotourl) {
        this.gotourl = gotourl;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCount_url() {
        return count_url;
    }

    public void setCount_url(String count_url) {
        this.count_url = count_url;
    }

    public String getClick_url() {
        return click_url;
    }

    public void setClick_url(String click_url) {
        this.click_url = click_url;
    }

    public String getSdown_url() {
        return sdown_url;
    }

    public void setSdown_url(String sdown_url) {
        this.sdown_url = sdown_url;
    }

    public String getEdown_url() {
        return edown_url;
    }

    public void setEdown_url(String edown_url) {
        this.edown_url = edown_url;
    }

    public String getFinish_url() {
        return finish_url;
    }

    public void setFinish_url(String finish_url) {
        this.finish_url = finish_url;
    }

    public int getIs_link() {
        return is_link;
    }

    public void setIs_link(int is_link) {
        this.is_link = is_link;
    }

    public int getSucc() {
        return succ;
    }

    public void setSucc(int succ) {
        this.succ = succ;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
