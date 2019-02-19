package com.ngbj.browse.view.myview;

/**
 *
 * Created by xiangpan on 2017/8/1.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ngbj.browse.event.UpdateDataEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import static com.ngbj.browse.view.myview.Tool.getScreenWidth;
import static com.ngbj.browse.view.myview.Tool.getTextViewLength;


public class ViewPagerTitle extends HorizontalScrollView {
    private String[] titles;
    private ArrayList<TextView> textViews = new ArrayList<>();
    private DynamicLine dynamicLine;
    private ViewPager viewPager;
    private MyOnPageChangeListener onPageChangeListener;
    private int margin;
    private LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private float defaultTextSize = 18;
    private float selectedTextSize = 18;
    private int defaultTextColor = Color.GRAY;
    private int selectedTextColor = Color.BLACK;
    private int allTextViewLength;


    public ViewPagerTitle(Context context) {
        this(context, null);
    }

    public ViewPagerTitle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    /**
     * 初始化时，调用这个方法。ViewPager需要设置Adapter，且titles的数据长度需要与Adapter中的数据长度一置。
     *
     * @param titles
     * @param viewPager
     * @param defaultIndex 默认选择的第几个页面
     */
    public void initData(String[] titles, ViewPager viewPager, int defaultIndex) {
        this.titles = titles;
        this.viewPager = viewPager;
        createDynamicLine();
        createTextViews(titles);

        int fixLeftDis = getFixLeftDis();
        onPageChangeListener = new MyOnPageChangeListener(getContext(), viewPager, dynamicLine, this, allTextViewLength, margin, fixLeftDis);
        setDefaultIndex(defaultIndex);

        viewPager.addOnPageChangeListener(onPageChangeListener);

    }

    public void setDefaultIndex(int index) {
        setCurrentItem(index);
    }

    private int getFixLeftDis() {
        TextView textView = new TextView(getContext());
        textView.setTextSize(defaultTextSize);
        textView.setText(titles[0]);
        float defaultTextSize = getTextViewLength(textView);
        textView.setTextSize(selectedTextSize);
        float selectTextSize = getTextViewLength(textView);
        return (int) (selectTextSize - defaultTextSize) / 2;
    }

    public ArrayList<TextView> getTextView() {
        return textViews;
    }


    private void createDynamicLine() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dynamicLine = new DynamicLine(getContext());
        dynamicLine.setLayoutParams(params);
    }


    private void createTextViews(String[] titles) {
        LinearLayout contentLl = new LinearLayout(getContext());
//        contentLl.setBackgroundColor(Color.parseColor("#ffffff"));//#303F9F
        contentLl.setLayoutParams(contentParams);
        contentLl.setOrientation(LinearLayout.VERTICAL);
        contentLl.setGravity(Gravity.CENTER_VERTICAL);
        addView(contentLl);


        LinearLayout textViewLl = new LinearLayout(getContext());
        textViewLl.setLayoutParams(contentParams);
        textViewLl.setOrientation(LinearLayout.HORIZONTAL);


        margin = getTextViewMargins(titles);

        textViewParams.setMargins(margin, 0, margin, 0);

        for (int i = 0; i < titles.length; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(titles[i]);
            textView.setTextColor(Color.GRAY);
            textView.setTextSize(defaultTextSize);
            textView.setLayoutParams(textViewParams);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setOnClickListener(onClickListener);
            textView.setTag(i);
            textViews.add(textView);
            textViewLl.addView(textView);
        }
        contentLl.addView(textViewLl);  //将所有的TextView所在的LinerLayout添加到HorizontalScrollView的contentLl中
        contentLl.addView(dynamicLine);//dynamicLine是左右跑动的黄色的线
    }


    /**
     *
     * @param titleAry TextView的String字符串 “关注” “推荐”
     * @return
     */
    private int getTextViewMargins(String[] titleAry) {
        int defaultMargins = 30;
        float countLength = 0;
        TextView textView = new TextView(getContext());
        textView.setTextSize(defaultTextSize);
        TextPaint paint = textView.getPaint();


        for (int i = 0; i < titleAry.length; i++) {
            countLength = countLength + defaultMargins + paint.measureText(titleAry[i]) + defaultMargins;
        }
        int screenWidth = getScreenWidth(getContext());

        if (countLength <= screenWidth) { //TextView总长度小于屏幕宽度
            allTextViewLength = screenWidth;
            return (screenWidth / titleAry.length - (int) paint.measureText(titleAry[0])) / 2;
        } else { //TextView总长度大于屏幕宽度
            allTextViewLength = (int) countLength;
            return defaultMargins;
        }
    }



    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            setCurrentItem((int) v.getTag());
            viewPager.setCurrentItem((int) v.getTag());
        }
    };


    public void setCurrentItem(int index) {
        //TODO 点击发送事件
        EventBus.getDefault().post(new UpdateDataEvent(index));

        for (int i = 0; i < textViews.size(); i++) {
            if (i == index) {
                textViews.get(i).setTextColor(selectedTextColor);
                textViews.get(i).setTextSize(selectedTextSize);
            } else {
                textViews.get(i).setTextColor(defaultTextColor);
                textViews.get(i).setTextSize(defaultTextSize);
            }
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        viewPager.removeOnPageChangeListener(onPageChangeListener);
    }


}