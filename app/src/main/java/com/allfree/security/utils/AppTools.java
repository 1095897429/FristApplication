package com.allfree.security.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangming on 2017/7/2.
 * email: 1047215352@qq.com
 */

public class AppTools {

    public static boolean[] isAvilibles(Context context, String... packageName) {//本机是否安装了指定程序
        if (packageName == null || packageName.length == 0 || TextUtils.isEmpty(packageName[0])) return new
                boolean[]{false};

        boolean[] avilibles = new boolean[packageName.length];
        for (int index = 0; index < avilibles.length; index++) {
            avilibles[index] = false;
        }

        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        for (int index = 0; index < packageName.length; index++) {
            if (packageNames.contains(packageName[0])) {
                avilibles[index] = true;
            }
        }
        return avilibles;
    }

}
