package com.ngbj.browse.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/***
 * 监听ScrollView的滑动距离
 * 参考：https://blog.csdn.net/u014798175/article/details/51345196
 */
public class MyScrollView extends ScrollView {

    private OnScollChangedListener onScollChangedListener = null;

    public void setOnScollChangedListener(OnScollChangedListener onScollChangedListener) {
        this.onScollChangedListener = onScollChangedListener;
    }

    public interface OnScollChangedListener {

        void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy);

    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (onScollChangedListener != null) {
            onScollChangedListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }



    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
