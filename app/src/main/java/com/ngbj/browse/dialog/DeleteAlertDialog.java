package com.ngbj.browse.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.ngbj.browse.R;
import com.ngbj.browse.activity.LoginActivity;
import com.ngbj.browse.adpter.HomeMenuAdapter;
import com.ngbj.browse.bean.HistoryData;
import com.ngbj.browse.bean.ProductListBean;
import com.ngbj.browse.db.DBManager;
import com.ngbj.browse.event.CleanHistoryEvent;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * Date:2018/8/9
 * author:zl
 * 备注：一般的Dialog
 *
 *      1.给Dialog设置一个风格主体（无边框全透明背景）
 *      2.自定义xml (shape)
 *      3.自定义代码 (链式调用)
 */
public class DeleteAlertDialog {
    private Context mContext;
    private Dialog dialog;
    private Display display;
    private GridView mGridView;
    private TextView delete_btn;
    private TextView cancle_btn;
    public CleanAllListener cleanAllListener;

    public void setCleanAllListener(CleanAllListener cleanAllListener) {
        this.cleanAllListener = cleanAllListener;
    }

    public interface  CleanAllListener{
        void cleanAll();
    }

    public DeleteAlertDialog(Context context){
        mContext = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }



    public DeleteAlertDialog builder(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.delete_dialog,null); // 获取Dialog布局
        // 获取自定义Dialog布局中的控件
        mGridView = view.findViewById(R.id.gridView);
        delete_btn = view.findViewById(R.id.delete_btn);
        cancle_btn = view.findViewById(R.id.cancle_btn);

        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        dialog = new Dialog(mContext, R.style.MyDialog);
        dialog.setContentView(view);
        //默认设置
        dialog.setCanceledOnTouchOutside(false);

        // 调整dialog背景大小
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (display.getWidth());
        lp.gravity = Gravity.BOTTOM;
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);

        return this;
    }

    public DeleteAlertDialog setDeleteButton(final View.OnClickListener listener) {
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        return this;
    }

    public DeleteAlertDialog setContextText(String  contextText) {
        delete_btn.setText(contextText);
        return this;
    }


    public DeleteAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }


    public DeleteAlertDialog setCanceledOnTouchOutside(boolean b) {
        dialog.setCanceledOnTouchOutside(b);
        return this;
    }





    private void setLayuot(){

        setData();






    }

    private void setData(){
    }

    public void show(){
        setLayuot();
        dialog.show();
    }


}
