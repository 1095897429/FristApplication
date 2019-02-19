package com.ngbj.browse.view.myview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import java.util.ArrayList;

import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;
import static com.ngbj.browse.view.myview.Tool.getScreenWidth;
import static com.ngbj.browse.view.myview.Tool.getTextViewLength;

/**
 * Date:2018/9/17
 * author:zl
 * 备注：
 */
public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

    private ArrayList<TextView> textViews;
    private ViewPagerTitle viewPagerTitle;
    private DynamicLine dynamicLine;
    private ViewPager pager;
    private int pagerCount;
    private int screenWidth;
    private int lastPosition;
    private int dis;
    private int[] location = new int[2];
    private int lineWidth;
    private int everyLength;
    private int fixLeftDis;

    /**
     *
     * @param context
     * @param viewPager
     * @param dynamicLine
     * @param viewPagerTitle
     * @param allLength 所有的TextView的总长度。
     * @param margin TextView的左右边距。
     * @param fixLeftDis TextView的修正的距离
     */
    public MyOnPageChangeListener(Context context, ViewPager viewPager, DynamicLine dynamicLine, ViewPagerTitle viewPagerTitle, int allLength, int margin, int fixLeftDis) {
        this.viewPagerTitle = viewPagerTitle;
        this.pager = viewPager;
        this.dynamicLine = dynamicLine;
        textViews = viewPagerTitle.getTextView();
        pagerCount = textViews.size();
        screenWidth = getScreenWidth(context);
        lineWidth = (int) getTextViewLength(textViews.get(0));
        everyLength = allLength / pagerCount;
        dis = margin;
        this.fixLeftDis = fixLeftDis;
    }





    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (lastPosition > position) {//页面向右滚动
            /**
             * 档页面向右滚动时，dynamicLine的右边的stopX位置不变，startX在变化。
             */
            dynamicLine.updateView((position + positionOffset) * everyLength + dis + fixLeftDis, (lastPosition + 1) * everyLength - dis);


        } else { //页面向左滚动
            /**
             * 档页面向左滚动时，dynamicLine的左边的startX位置不变，stopX在变化。
             */
            if (positionOffset > 0.5f) {
                positionOffset = 0.5f;
            }
            dynamicLine.updateView(lastPosition * everyLength + dis + fixLeftDis, (position + positionOffset * 2) * everyLength + dis + lineWidth);
        }

    }


    @Override
    public void onPageSelected(int position) {
        viewPagerTitle.setCurrentItem(position);
    }


    //0：什么都没做
    //1：开始滑动
    //2：滑动结束
    @Override
    public void onPageScrollStateChanged(int state) {
        boolean scrollRight;//页面向右
        if (state == SCROLL_STATE_SETTLING) {
            scrollRight = lastPosition < pager.getCurrentItem();
            lastPosition = pager.getCurrentItem();
            /**
             * 下面几行代码，解决页面滑到的TAB页时对应的TextView对应，TextView处于屏幕外面，
             * 这个时候就需要将HorizontalScrollView滑动到屏幕中间。
             */
            if (lastPosition + 1 < textViews.size() && lastPosition - 1 >= 0) {
                textViews.get(scrollRight ? lastPosition + 1 : lastPosition - 1).getLocationOnScreen(location);
                if (location[0] > screenWidth) {
                    viewPagerTitle.smoothScrollBy(screenWidth / 2, 0);
                } else if (location[0] < 0) {
                    viewPagerTitle.smoothScrollBy(-screenWidth / 2, 0);
                }
            }

        }

    }

}