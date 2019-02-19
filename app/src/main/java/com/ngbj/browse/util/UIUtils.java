package com.ngbj.browse.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * UI Util类
 */
public class UIUtils
{



    /**
     * 自适应高度
     * 
     * @param view
     * @param heightMeasureSpec
     * @return
     */
    public static int getHeightSpec(View view, int heightMeasureSpec)
    {
        int heightSpec = 0;
        if (view.getLayoutParams().height == LayoutParams.WRAP_CONTENT)
        {
            // The great Android "hackatlon", the love, the magic.
            // The two leftmost bits in the height measure spec have
            // a special meaning, hence we can't use them to describe height.
            heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        }
        else
        {
            // Any other height should be respected as is.
            heightSpec = heightMeasureSpec;
        }
        return heightSpec;
    }



    public static final <E extends TextView> String getViewText(E view)
    {
        return view.getText().toString().trim();
    }

    public static final <E extends TextView> boolean isViewTextEmpty(E view)
    {
        return TextUtils.isEmpty(getViewText(view));
    }

    public static final <E extends View> E findView(Activity activity, int id)
    {
        return findView(activity.getWindow().getDecorView(), id);
    }

    @SuppressWarnings("unchecked")
    public static final <E extends View> E findView(View view, int id)
    {
        try
        {
            return (E) view.findViewById(id);
        }
        catch (ClassCastException e)
        {
            throw e;
        }
    }


    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Activity ctx)
    {
        int width;
        Display display = ctx.getWindowManager().getDefaultDisplay();
        try
        {
            Method mGetRawW = Display.class.getMethod("getRawWidth");
            width = (Integer) mGetRawW.invoke(display);
        }
        catch (Exception e)
        {
            width = display.getWidth();
        }
        return width;
    }

    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Activity ctx)
    {
        int height;
        Display display = ctx.getWindowManager().getDefaultDisplay();
        try
        {
            Method mGetRawH = Display.class.getMethod("getRawHeight");
            height = (Integer) mGetRawH.invoke(display);
        }
        catch (Exception e)
        {
            height = display.getHeight();
        }
        return height;
    }

    /**
     * 精确获取屏幕尺寸（例如：3.5、4.0、5.0寸屏幕）
     * 
     * @param ctx
     * @return
     */
    public static double getScreenPhysicalSize(Activity ctx)
    {
        DisplayMetrics dm = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2) + Math.pow(dm.heightPixels, 2));
        return diagonalPixels / (160 * dm.density);
    }

    public static void setMargins(View v, int l, int t, int r, int b)
    {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)
        {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    /**
     * dip转px
     * 
     * @param ctx
     * @param dip
     * @return
     */
    public static int dipToPx(final Context ctx, float dip)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, ctx.getResources().getDisplayMetrics());
    }

    /**
     * dip转px
     *
     * @param ctx
     * @param sp
     * @return
     */
    public static int spToPx(final Context ctx, float sp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, ctx.getResources().getDisplayMetrics());
    }

    /**
     * 计算ListView高度
     * 
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getActionBarSize(Context context) {
        int[] attrs = { android.R.attr.actionBarSize };
        TypedArray values = context.getTheme().obtainStyledAttributes(attrs);
        try
        {
            return values.getDimensionPixelSize(0, 0);//第一个参数数组索引，第二个参数 默认值
        }
        finally
        {
            values.recycle();
        }
    }
}
