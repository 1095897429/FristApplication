package com.ngbj.browse.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.ngbj.browse.event.NewWindowEvent;
import com.ngbj.browse.view.myview.Tool;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

public class ScreenHelper {


    // 获取指定Activity的截屏
     public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        //        L.e(""+statusBarHeight);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        int hh = statusBarHeight;
        KLog.d("状态栏的高--- "+ statusBarHeight+"屏幕的高--"+ height);
        //从hh下开始截图,截300dp的view
        Bitmap b = Bitmap.createBitmap(b1, 0, hh, width,  hh + Tool.dip2px(activity,500));
//        Bitmap b = Bitmap.createBitmap(b1, 0, hh, width,  height);
        view.destroyDrawingCache();
        KLog.d("Thresh"+ "takeScreenShot:            截图成功");
        return b;
    }


   /**     * 截取除了导航栏之外的整个屏幕     */
   public static Bitmap screenShotWholeScreen(Activity activity) {
      View dView = activity.getWindow().getDecorView();
      dView.setDrawingCacheEnabled(true);
      dView.buildDrawingCache();
      Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
      return bitmap;
   }


   public static int getScreenWidth(Activity activity){
      WindowManager manager = activity.getWindowManager();
      DisplayMetrics outMetrics = new DisplayMetrics();
      manager.getDefaultDisplay().getMetrics(outMetrics);
      int width = outMetrics.widthPixels;
      return width;
   }


   public static int getScreenHeight(Activity activity){
      WindowManager manager = activity.getWindowManager();
      DisplayMetrics outMetrics = new DisplayMetrics();
      manager.getDefaultDisplay().getMetrics(outMetrics);
      int height = outMetrics.heightPixels;
      return height;
   }




}

