package com.ngbj.browse.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.webkit.URLUtil;

import com.ngbj.browse.fragment.Index_Fragment;
import com.ngbj.browse.fragment.Index_Fragment_2;
import com.ngbj.browse.fragment.Index_Fragment_3;
import com.ngbj.browse.fragment.Index_Fragment_4;
import com.socks.library.KLog;

import org.apache.commons.validator.routines.UrlValidator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringUtils {

    private static final int MIN_DELAY_TIME = 1000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }


    /**
     * 获取url对应的域名
     *
     * @param url
     * @return
     */
    public static String getDomain(String url) {
        String result = "";
        int j = 0, startIndex = 0, endIndex = 0;
        for (int i = 0; i < url.length(); i++) {
            if (url.charAt(i) == '/') {
                j++;
                if (j == 2)
                    startIndex = i;
                else if (j == 3)
                    endIndex = i;
            }

        }
        result = url.substring(startIndex + 1, endIndex);
        return result;
    }

    //版本号 1.0
    public static String getVersionCode(Context context) {
        return getPackageInfo(context).versionName;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        KLog.d("版本号：" + pi);
        return pi;
    }

    public static String getFragmentTyep(String name){
        if("Index_Fragment_1".equals(name)){
            return "1";
        }else  if("Index_Fragment_2".equals(name)){
            return "2";
        }else  if("Index_Fragment_3".equals(name)){
            return "3";
        }else  if("Index_Fragment_4".equals(name)){
            return "4";
        }
        return null;
    }

    public static String getFragmentName(String type){
        if("1".equals(type)){
            return "Index_Fragment_1";
        }else  if("2".equals(type)){
            return "Index_Fragment_2";
        }else  if("3".equals(type)){
            return "Index_Fragment_3";
        }else  if("4".equals(type)){
            return "Index_Fragment_4";
        }
        return null;
    }


    public static String getCurrentFragmentName(Fragment currentFragment) {
        String name = "";
        if(currentFragment != null && currentFragment instanceof Index_Fragment){
            name = "Index_Fragment_1";
        }

        if(currentFragment != null && currentFragment instanceof Index_Fragment_2){
            name = "Index_Fragment_2";
        }

        if(currentFragment != null && currentFragment instanceof Index_Fragment_3){
            name = "Index_Fragment_3";
        }

        if(currentFragment != null && currentFragment instanceof Index_Fragment_4){
            name = "Index_Fragment_4";
        }
        return name;
    }


    //检测用户输入是否是合法Uri的需求
    public static boolean isUrl(String url){
        String[] schemas = {"http","https"};
        UrlValidator urlValidator = new UrlValidator(schemas);
        return urlValidator.isValid(url);
    }

    //将time : 1447830272000转化为yyyy-MM-dd
    public static String longToDateStr(long strDate) {
        strDate = strDate * 1000L;
        Date date = new Date(strDate);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String strs = sdf.format(date);
        return strs;
    }

    public static String getLastTodayTime(){
        Calendar now = Calendar.getInstance();
        KLog.d("年: " + now.get(Calendar.YEAR));
        KLog.d("月: " + (now.get(Calendar.MONTH) + 1) + "");
        KLog.d("日: " + now.get(Calendar.DAY_OF_MONTH));
        int  year = now.get(Calendar.YEAR);
        int month = (now.get(Calendar.MONTH) + 1);
        int day = now.get(Calendar.DAY_OF_MONTH);
        String str1 = year+"/"+month+"/"+day+" "+"00:00:00";
        String result = year+"年"+month+"月"+day+ "日"+ " "+"23:59:59";
        return  result;
    }


    public static String getYear_Month_Day(){
        Calendar now = Calendar.getInstance();
        KLog.d("年: " + now.get(Calendar.YEAR));
        KLog.d("月: " + (now.get(Calendar.MONTH) + 1) + "");
        KLog.d("日: " + now.get(Calendar.DAY_OF_MONTH));
        int  year = now.get(Calendar.YEAR);
        int month = (now.get(Calendar.MONTH) + 1);
        int day = now.get(Calendar.DAY_OF_MONTH);
        String newDay = "";
        if(day < 10 ){
            newDay = "0" + day;
        } else{
            newDay = day + "";
        }
        String result = year +  "" + month  + "" + newDay ;
        return  result;
    }



}
