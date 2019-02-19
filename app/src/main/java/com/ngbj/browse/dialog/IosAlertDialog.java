package com.ngbj.browse.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngbj.browse.R;


/***
 * 仿Ios的弹出框
 * 预备了2个按钮 和 1个按钮
 */

public class IosAlertDialog {
    private Context context;
    private Dialog dialog;
    private Display display;
    private TextView txt_title;//标题
    private TextView txt_msg;//内容
    private Button btn_neg;//左边按钮
    private Button btn_pos;//右边按钮
    private ImageView img_line;//分割线
    private boolean showTitle = false;//是否显示标题
    private boolean showMsg = false;//是否显示内容
    private boolean showPosBtn = false;//是否显示右按钮
    private boolean showNegBtn = false;//是否显示左按钮

    public IosAlertDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context
                .WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public IosAlertDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_ios, null);

        // 获取自定义Dialog布局中的控件
        txt_title =  view.findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        txt_msg =  view.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        btn_neg =  view.findViewById(R.id.btn_neg);
        btn_neg.setVisibility(View.GONE);
        btn_pos =  view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        img_line =  view.findViewById(R.id.img_line);
        img_line.setVisibility(View.GONE);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(view);

        // 调整dialog背景大小
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (display.getWidth() * 0.8);

        return this;
    }

    public IosAlertDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            txt_title.setText("标题");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    public IosAlertDialog setMsg(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            txt_msg.setText("内容");
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }

    public IosAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }


    public IosAlertDialog setCanceledOnTouchOutside(boolean b) {
        dialog.setCanceledOnTouchOutside(b);
        return this;
    }


    public IosAlertDialog setPositiveButton(String text, final OnClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new OnClickListener() {
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

    public IosAlertDialog setNegativeButton(String text, final OnClickListener listener) {
        showNegBtn = true;
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new OnClickListener() {
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



    private void setLayout() {
        if (!showTitle && !showMsg) {
            txt_title.setText("提示");
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }

        /** 只有独立的按钮 */
        if (!showPosBtn && !showNegBtn) {
            btn_pos.setText("确定");
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.iosdialog_single_selector);
            btn_pos.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.iosdialog_right_selector);
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.iosdialog_left_selector);
            img_line.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.iosdialog_single_selector);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.iosdialog_single_selector);
        }
    }

    public void show() {
        setLayout();
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }


}
