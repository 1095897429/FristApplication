package com.ngbj.browse.bean;

import android.app.Activity;
import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

public class NewWindowBean implements Serializable {

    private Bitmap bitmap;
    private String  type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
