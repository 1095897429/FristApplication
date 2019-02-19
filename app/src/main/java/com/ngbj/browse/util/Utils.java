package com.ngbj.browse.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 16/12/08
 *     desc  : Utils 初始化相关
 * </pre>
 * 　　　　　　　　　瓦瓦　　　　　　　　　　　　十
 * 　　　　　　　　十齱龠己　　　　　　　　　亅瓦車己
 * 　　　　　　　　乙龍龠毋日丶　　　　　　丶乙己毋毋丶
 * 　　　　　　　　十龠馬鬼車瓦　　　　　　己十瓦毋毋
 * 　　　　　　　　　鬼馬龠馬龠十　　　　己己毋車毋瓦
 * 　　　　　　　　　毋龠龠龍龠鬼乙丶丶乙車乙毋鬼車己
 * 　　　　　　　　　乙龠龍龍鬼龍瓦　十瓦毋乙瓦龠瓦亅
 * 　　　　　　　　　　馬齱龍馬鬼十丶日己己己毋車乙丶
 * 　　　　　　　　　　己齱馬鬼車十十毋日乙己己乙乙
 * 　　　　　　　　　　　車馬齱齱日乙毋瓦己乙瓦日亅
 * 　　　　　　　　　　　亅車齺龖瓦乙車龖龍乙乙十
 * 　　　　　　　　　　　　日龠龠十亅車龍毋十十
 * 　　　　　　　　　　　　日毋己亅　己己十亅亅
 * 　　　　　　　　　　　丶己十十乙　　丶丶丶丶丶
 * 　　　　　　　　　　　亅己十龍龖瓦　　丶　丶　乙十
 * 　　　　　　　　　　　亅己十龠龖毋　丶丶　　丶己鬼鬼瓦亅
 * 　　　　　　　　　　　十日十十日亅丶亅丶　丶十日毋鬼馬馬車乙
 * 　　　　　　　　　　　十日乙十亅亅亅丶　　十乙己毋鬼鬼鬼龍齺馬乙
 * 　　　　　　　　　　　丶瓦己乙十十亅丶亅乙乙乙己毋鬼鬼鬼龍齱齺齺鬼十
 * 　　　　　　　　　　　　乙乙十十十亅乙瓦瓦己日瓦毋鬼鬼龠齱齱龍龍齱齱毋丶
 * 　　　　　　　　　　　　亅十十十十乙瓦車毋瓦瓦日車馬龠龍龍龍龍龍龠龠龠馬亅
 * 　　　　　　　　　　　　　十十十十己毋車瓦瓦瓦瓦鬼馬龠龍龠龠龍龠龠龠馬龠車
 * 　　　　　　　　　　　　　　亅十十日毋瓦日日瓦鬼鬼鬼龠龠馬馬龠龍龍龠馬馬車
 * 　　　　　　　　　　　　　　亅亅亅乙瓦瓦毋車車車馬龍龠鬼鬼馬龠龍龍龠馬馬鬼
 * 　　　　　　　　　　　　丶丶乙亅亅乙車鬼鬼鬼毋車龍龍龠鬼馬馬龠龍齱齱龍馬鬼
 * 　　　　　　　　　　　亅己十十己十日鬼鬼車瓦毋龠龍龠馬馬龠龠龠齱齺齺齱龠鬼
 * 　　　　　　　　　　　　亅乙乙乙十車馬車毋馬齱齱龍龠龠龠馬龠龍齱龍龠龠鬼瓦
 * 　　　　　　　　　　　　　　　　丶毋龠鬼車瓦車馬龠龍龠龠龍齱齱龠馬馬鬼毋日
 * 　　　　　　　　　　　　　　　　十乙己日十　　丶己鬼龍齱齺齱龍馬馬馬車毋己
 * 　　　　　　　　　　　　　　丶十己乙亅丶　　　　　　亅瓦馬龠龍龠龠馬毋瓦乙
 * 　　　　　　　　　　　　　丶十十乙亅十　　　　　　　　亅己瓦車馬龠鬼車瓦乙
 * 　　　　　　　　　　　　　丶十乙十十丶　　　　　　　　　丶丶亅十瓦鬼車瓦己
 * 　　　　　　　　　　　　　　丶亅亅丶　　　　　　　　　　　　　　　亅日瓦日
 * 　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　丶
 */
public final class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    static WeakReference<Activity> sTopActivityWeakRef;
    static List<Activity> sActivityList = new LinkedList<>();

    private static ActivityLifecycleCallbacks mCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            sActivityList.add(activity);
            setTopActivityWeakRef(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            setTopActivityWeakRef(activity);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            setTopActivityWeakRef(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            sActivityList.remove(activity);
        }
    };

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param app 应用
     */
    public static void init(@NonNull final Application app) {
        Utils.sApplication = app;
        app.registerActivityLifecycleCallbacks(mCallbacks);
    }

    /**
     * 获取 Application
     *
     * @return Application
     */
    public static Application getApp() {
        if (sApplication != null) return sApplication;
        throw new NullPointerException("u should init first");
    }

    private static void setTopActivityWeakRef(final Activity activity) {
        //if (activity.getClass() == PermissionUtils.PermissionActivity.class) return;
        if (sTopActivityWeakRef == null || !activity.equals(sTopActivityWeakRef.get())) {
            sTopActivityWeakRef = new WeakReference<>(activity);
        }
    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


    //获得独一无二的Psuedo ID  b3f2ca2d1cc25621dd4c722832877ed5
    //测试机 c1989cc6a8c603af25649a0ee3cade11
    public static String getUniquePsuedoID() {

        String serial ;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            String result = new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
            //为了统一格式对设备的唯一标识进行md5加密 最终生成32位字符串
            String md5 = getMD5(result.toString(), false);
            return md5;
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        String result =  new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        //为了统一格式对设备的唯一标识进行md5加密 最终生成32位字符串
        String md5 = getMD5(result.toString(), false);
        return md5;

    }


    /**
     * 对挺特定的 内容进行 md5 加密
     * @param message  加密明文
     * @param upperCase  加密以后的字符串是是大写还是小写  true 大写  false 小写
     * @return
     */
    public static String getMD5(String message, boolean upperCase) {
        String md5str = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] input = message.getBytes();
            byte[] buff = md.digest(input);
            md5str = bytesToHex(buff, upperCase);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }

    public static String bytesToHex(byte[] bytes, boolean upperCase) {
        StringBuffer md5str = new StringBuffer();
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        if (upperCase) {
            return md5str.toString().toUpperCase();
        }
        return md5str.toString().toLowerCase();
    }


}
