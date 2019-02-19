package com.ngbj.browse.view.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static com.ngbj.browse.view.myview.Tool.dip2px;
import static com.ngbj.browse.view.myview.Tool.getScreenWidth;


/**
 * Date:2018/9/17
 * author:zl
 * 备注：画底部的黄色的线 -- 圆角矩形
 * 参考：http://www.sohu.com/a/168314330_659256
 */
public class DynamicLine extends View {

    private float startX,stopX;
    private Paint paint;
    private RectF rectF = new RectF(startX,0,stopX,0);
    private Context context;

    public DynamicLine(Context context) {
        this(context,null);
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStopX() {
        return stopX;
    }

    public void setStopX(float stopX) {
        this.stopX = stopX;
    }

    public DynamicLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DynamicLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);//填充
        paint.setStrokeWidth(dip2px(context,1));//画笔宽度
        paint.setColor( Color.parseColor("#0162FF"));
//        paint.setShader(new LinearGradient(0, 100, getScreenWidth(getContext()), 100,
//                Color.parseColor("#0162FF"), Color.parseColor("#0162FF"), Shader.TileMode.MIRROR));//设置画笔渐变色

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rectF.set(startX,0,stopX,10);
        canvas.drawRoundRect(rectF,5,5,paint);
    }

    /** 重新绘制 */
    public void updateView(float startX,float stopX){
        this.startX = startX;
        this.stopX = stopX;
        invalidate();
    }

}
