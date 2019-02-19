package com.ngbj.browse.view;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ngbj.browse.R;
import com.ngbj.browse.util.UIUtils;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by weixiaopan on 2018/1/24.
 */

public class GenderWindow {
    private ViewGroup layout;
    private View genderWindow;
    private Activity activity;
    private boolean isShowing;
    private List<String> genderList = new ArrayList<>();
    private CallBack callBack;
    private WheelView<String> wheelView;
    public interface CallBack{
        void sure(boolean isMan);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public GenderWindow(Activity activity, ViewGroup layout) {
        this.activity = activity;
        this.layout = layout;
        initGenderWindow();
    }

    private void initGenderWindow() {
        genderWindow = LayoutInflater.from(activity).inflate(R.layout.window_modify_gender, null);
        ClickListener clickListener = new ClickListener();

        genderWindow.findViewById(R.id.cancel_txt).setOnClickListener(clickListener);
        genderWindow.findViewById(R.id.dismiss_view).setOnClickListener(clickListener);
        genderWindow.findViewById(R.id.sure_txt).setOnClickListener(clickListener);
        genderList.add("男");
        genderList.add("女");
        wheelView =  genderWindow.findViewById(R.id.wheelview);
        wheelView.setWheelAdapter(new ArrayWheelAdapter(activity)); // 文本数据源
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = ContextCompat.getColor(activity,R.color.font_color);
        style.selectedTextSize = 20;
        style.textColor = ContextCompat.getColor(activity,R.color.hint_color);
        style.textSize = 20;
        style.holoBorderColor = ContextCompat.getColor(activity,R.color.hint_color);
        style.holoBorderWidth = UIUtils.dip2px(activity,1);
        style.holoLineWidth = UIUtils.dip2px(activity,96);
        wheelView.setStyle(style);
        wheelView.setSkin(WheelView.Skin.Holo); // common皮肤
        wheelView.setWheelData(genderList);
    }

    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.cancel_txt:
                case R.id.dismiss_view:
                    dismiss();
                    break;
                case R.id.sure_txt:
                    if(callBack!=null){
                        callBack.sure(wheelView.getCurrentPosition()==0);
                    }
                    dismiss();
                    break;
            }
        }
    }
    public void showGenderWindow() {
        if (isShowing) {
            return;
        }
        if (genderWindow == null) {
            initGenderWindow();
        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layout.addView(genderWindow, params);
        isShowing = true;
    }

    public void dismiss() {
        if (layout != null && isShowing) {
            layout.removeView(genderWindow);
            isShowing = false;
        }
    }

}
