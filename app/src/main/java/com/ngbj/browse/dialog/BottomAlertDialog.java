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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ngbj.browse.R;
import com.ngbj.browse.activity.DownActivity;
import com.ngbj.browse.activity.HistoryCollecteActivity;
import com.ngbj.browse.activity.LoginActivity;
import com.ngbj.browse.activity.SettingActivity;
import com.ngbj.browse.activity.UserInfoActivity;
import com.ngbj.browse.activity.VerificationActivity;
import com.ngbj.browse.adpter.HomeMenuAdapter;
import com.ngbj.browse.bean.MenuBean;
import com.ngbj.browse.bean.UserInfoBean;
import com.ngbj.browse.db.DBManager;
import com.ngbj.browse.event.CollectEvent;
import com.ngbj.browse.event.CollectHomeEvent;
import com.ngbj.browse.event.RefreshDataEvent;
import com.ngbj.browse.event.RefreshHomeDataEvent;
import com.ngbj.browse.util.SPHelper;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Date:2018/8/9
 * author:zl
 * 备注：一般的Dialog
 *
 *      1.给Dialog设置一个风格主体（无边框全透明背景）
 *      2.自定义xml (shape)
 *      3.自定义代码 (链式调用)
 */
public class BottomAlertDialog {
    private Context mContext;
    private Dialog dialog;
    private Display display;
    private GridView mGridView;
    private TextView toLogin;
    private LinearLayout login_ll;
    private String type;

    private CircleImageView head_small_icon;
    boolean isLogin;


    public BottomAlertDialog(Context context){
        mContext = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }




    public BottomAlertDialog builder(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_dialog,null); // 获取Dialog布局
        // 获取自定义Dialog布局中的控件
        login_ll = view.findViewById(R.id.login_ll);
        toLogin = view.findViewById(R.id.toLogin);
        head_small_icon = view.findViewById(R.id.head_small_icon);
        mGridView = view.findViewById(R.id.gridView);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://历史记录
                        dialog.dismiss();
                        Intent intent = new Intent(mContext, HistoryCollecteActivity.class);
                        intent.putExtra("type",type);
                        mContext.startActivity(intent);
                        break;
                    case 1://收藏网址
                        dialog.dismiss();
                        EventBus.getDefault().post(new CollectHomeEvent());  //TODO 状态判断
                        break;
                    case 2:
                        dialog.dismiss();
                        mContext.startActivity(new Intent(mContext, DownActivity.class));
                        break;
                    case 3:
                        dialog.dismiss();
                        mContext.startActivity(new Intent(mContext, SettingActivity.class));
                        break;

                    case 4:
                        dialog.dismiss();
                        EventBus.getDefault().post(new RefreshHomeDataEvent());
                        break;

                }
            }
        });

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


        initData();

        return this;
    }

    private void initData() {
        isLogin = (boolean) SPHelper.get(mContext,"isLogin",false);
      if(isLogin){
          if(DBManager.getInstance(mContext).queryUserInfo()!= null &&
                  DBManager.getInstance(mContext).queryUserInfo().size() != 0){
              UserInfoBean userInfoBean = DBManager.getInstance(mContext).queryUserInfo().get(0);
              toLogin.setText(userInfoBean.getNickname());
              //加载
              Glide.with(mContext)
                      .load(userInfoBean.getHead_img())
                      .crossFade()
                      .into(head_small_icon);
          }
      }
    }


    public BottomAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }


    public BottomAlertDialog setCanceledOnTouchOutside(boolean b) {
        dialog.setCanceledOnTouchOutside(b);
        return this;
    }


    public BottomAlertDialog setType(String type ) {
        this.type = type;
        return this;
    }



    private void setLayuot(){

        setData();
        setEvent();

    }

    private void setEvent() {
        login_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.d( "去登陆 ");
                dialog.dismiss();
                isLogin = (boolean) SPHelper.get(mContext,"isLogin",false);
                if(isLogin){
                    mContext.startActivity(new Intent(mContext,UserInfoActivity.class));
                }else
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
            }
        });
    }

    private void setData(){
        //全部
//        String[] proName = {
//                "收藏/历史","收藏网站","下载管理",
//                "夜间模式","无痕模式","无图模式",
//                "设置","刷新","分享页面"};
//        int [] priImg = {
//                R.mipmap.menu_icon_history,R.mipmap.menu_icon_love,R.mipmap.menu_icon_download,
//                R.mipmap.menu_icon_nigth,R.mipmap.menu_icon_clean,R.mipmap.menu_icon_nophoto,
//                R.mipmap.menu_icon_set,R.mipmap.menu_icon_update,R.mipmap.menu_icon_share
//        };

        //部分
        String[] proName = {
                "收藏/历史","收藏网站","下载管理",
                "设置","刷新"};
        int [] priImg = {
                R.mipmap.menu_icon_history, R.mipmap.menu_icon_love, R.mipmap.menu_icon_download,
                R.mipmap.menu_icon_set, R.mipmap.menu_icon_update
        };

        List<MenuBean> listDatas = new ArrayList<>();
        for(int i=0;i<proName.length;i++){
            listDatas.add(new MenuBean(proName[i],priImg[i]));
        }
        mGridView.setAdapter(new HomeMenuAdapter(mContext,listDatas));
    }

    public void show(){
        setLayuot();
        dialog.show();
    }


}
