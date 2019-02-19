package com.ngbj.browse.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.ngbj.browse.R;
import com.ngbj.browse.base.BaseActivity;
import com.ngbj.browse.dialog.IosAlertDialog;
import com.ngbj.browse.util.AppUtil;
import com.ngbj.browse.util.SPHelper;
import com.ngbj.browse.util.StringUtils;
import com.socks.library.KLog;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends CommonHeadActivity {


    @BindView(R.id.version_text)
    TextView version_text;

    @BindView(R.id.cache_data)
    TextView cache_data;

    @BindView(R.id.ad_switch)
    SwitchButton switchButton;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initDatas() {
        center_title.setText("设置");

        //TODO 随机数 SP
        Random rand = new Random();
        int i = rand.nextInt(5) + 1;//0 - 4 --> 1 - 5
        KLog.d(i);
        cache_data.setText(i + "M");

        //广告
        Boolean isOutAd = (Boolean) SPHelper.get(this,"is_out_ad",false);
        switchButton.setChecked(isOutAd);

//        Boolean isCached = (Boolean) SPHelper.get(this,"is_cached",false);
//        if(!isCached){
//            Random rand = new Random();
//            int i = rand.nextInt(5);
//            KLog.d(i);
//            cache_data.setText(i + "M");
//        }
        //版本
        version_text.setText(AppUtil.getVersionName(this));


    }

    @OnClick(R.id.clean_cache_layout)
    public void cleanCache(){
        //TODO 清除缓存
        final IosAlertDialog iosAlertDialog = new IosAlertDialog(this).builder();
        iosAlertDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cache_data.setText("0M");
                SPHelper.put(SettingActivity.this,"is_cached",true);
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).setTitle("标题").setMsg("是否清除缓存").setCanceledOnTouchOutside(false);
        iosAlertDialog.show();
    }



    @OnClick(R.id.ad_switch)
    public void ad_switch(){
        //TODO 清除广告
        Boolean isOutAd = (Boolean) SPHelper.get(this,"is_out_ad",false);
        isOutAd = !isOutAd;
        switchButton.setChecked(isOutAd);
        SPHelper.put(this,"is_out_ad",isOutAd);
    }

    @OnClick(R.id.response_layout)
    public void response_layout(){
            startActivity(new Intent(this,ResponseActivity.class));
    }


    @OnClick(R.id.default_browser)
    public void default_browser(){
        startActivity(new Intent(this,SetBrowserActivity.class));
    }




}
