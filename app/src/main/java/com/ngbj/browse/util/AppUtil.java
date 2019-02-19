package com.ngbj.browse.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Date:2018/7/19
 * author:zl
 * 备注：封装全局Context为基准
 */
public class AppUtil {
    private static Context mContext;

    public static void init(Context context){
        mContext = context;
    }

    public static Context getContext(){
        return mContext;
    }


    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static  String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
