package com.ngbj.browse.bean;

import java.io.Serializable;

/**
 * 菜单Bean
 */
public class MenuBean implements Serializable {
    private String menuName;
    private int imgUrl;

    public MenuBean(String menuName, int imgUrl) {
        this.menuName = menuName;
        this.imgUrl = imgUrl;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }
}
