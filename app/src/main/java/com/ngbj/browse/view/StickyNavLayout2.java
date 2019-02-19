package com.ngbj.browse.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.ngbj.browse.R;
import com.socks.library.KLog;

public class StickyNavLayout2 extends LinearLayout implements NestedScrollingParent {

    private int mTopViewHeight;
    OverScroller mScroller;
    private View mTop;
    private View mNav;
    private ViewPager mViewPager;
    private Context context;

    public StickyNavLayout2(Context context) {
        super(context);
        this.context = context;
        mScroller = new OverScroller(context);
    }

    public StickyNavLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mScroller = new OverScroller(context);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
//        KLog.e("onNestedScrollAccepted");
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//        KLog.i("onNestedScroll");
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
//        KLog.i("onNestedFling");
        return false;
    }

    @Override
    public void onStopNestedScroll(View child) {
//        KLog.i("onStopNestedScroll");
    }

    @Override
    public int getNestedScrollAxes() {
//        KLog.i("getNestedScrollAxes");
        return 0;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
//        KLog.i("onNestedPreScroll");
        boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight;
//        KLog.d("hiddenTop:" + hiddenTop);
//        KLog.i("scrolly=" + getScrollY() + "  ==  " + mTopViewHeight);
        boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);
//        KLog.d("showTop:" + showTop);
        if (hiddenTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
//            if(hiddenTopListenter != null)hiddenTopListenter.hiddenTopStatus(false);
        }else {
//            KLog.d("what?");
//            if(hiddenTopListenter != null)hiddenTopListenter.hiddenTopStatus(true);
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getScrollY() >= mTopViewHeight) {
            return false;
        }
        fling((int) velocityY);
        return true;
    }

    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        KLog.i("onMeasures");
        getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = getMeasuredHeight();
//        KLog.i("heights=" + params.height);
        setMeasuredDimension(getMeasuredWidth(), mTop.getMeasuredHeight() +  mNav.getMeasuredHeight() +  mViewPager.getMeasuredHeight());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        KLog.i("onFinishInflate");
        mTop = findViewById(R.id.par3);//第一个子View
        mNav = findViewById(R.id.tl_5);//标题栏
        View view = findViewById(R.id.viewPager);//下面的VP
        if (!(view instanceof ViewPager))
        {
            throw new RuntimeException(
                    "id_stickynavlayout_viewpager show used by ViewPager !");
        }
        mViewPager = (ViewPager) view;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTop.getMeasuredHeight();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

}

