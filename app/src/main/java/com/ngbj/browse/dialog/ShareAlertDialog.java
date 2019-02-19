package com.ngbj.browse.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.ngbj.browse.R;
import com.ngbj.browse.adpter.HomeMenuAdapter;
import com.ngbj.browse.adpter.HomeShareAdapter;
import com.ngbj.browse.bean.MenuBean;
import com.socks.library.KLog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;

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
public class ShareAlertDialog {
    private Context mContext;
    private Dialog dialog;
    private Display display;
    private GridView mGridView;
    private TextView toLogin;


    public ShareAlertDialog(Context context){
        mContext = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }



    public ShareAlertDialog builder(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.share_dialog,null); // 获取Dialog布局
        // 获取自定义Dialog布局中的控件
        mGridView = view.findViewById(R.id.gridView);
        dialog = new Dialog(mContext, R.style.MyDialog);
        dialog.setContentView(view);
        //默认设置
        dialog.setCanceledOnTouchOutside(false);

        // 调整dialog背景大小
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (display.getWidth() * 0.95);
        lp.gravity = Gravity.BOTTOM;
        lp.y = (int) (display.getWidth() * 0.05);//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        return this;
    }




    public ShareAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }


    public ShareAlertDialog setCanceledOnTouchOutside(boolean b) {
        dialog.setCanceledOnTouchOutside(b);
        return this;
    }





    private void setLayuot(){

        setData();
        setEvent();

    }

    private void setEvent() {
    }

    private void setData(){
        String[] proName = {
                "微信","朋友圈","QQ",
                "微博","复制链接"};
        int [] priImg = {
                R.mipmap.share_weixin,R.mipmap.share_pengyouquan,R.mipmap.share_qq,
                R.mipmap.share_weibo,R.mipmap.share_link
        };
        List<MenuBean> listDatas = new ArrayList<>();
        for(int i=0;i<proName.length;i++){
            listDatas.add(new MenuBean(proName[i],priImg[i]));
        }
        mGridView.setAdapter(new HomeShareAdapter(mContext,listDatas));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
//                        new ShareAction(mContext)
//                                .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
//                                .withText("hello")//分享内容
//                                .setCallback(umShareListener)//回调监听器
//                                .share();

                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
            }
        });
    }

    public void show(){
        setLayuot();
        dialog.show();
    }


}
