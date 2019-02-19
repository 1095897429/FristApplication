package com.ngbj.browse.util;

import android.content.Context;

/**
 * Created by zl on 2018/5/22.
 * APP工具类 -- 以全局的context为基准
 */

public class AppUtils {
    private static Context mContext;

    public static void init(Context context){
        mContext = context;
    }

    public static Context getmContext(){
        return mContext;
    }

    public static String[] getStringArray(int resId){
        return mContext.getResources().getStringArray(resId);
    }

    public static int getColor(int resId) {
        return mContext.getResources().getColor(resId);
    }


}
