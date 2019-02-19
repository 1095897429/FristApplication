package com.ngbj.browse.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.ngbj.browse.MyApplication;
import com.ngbj.browse.bean.BigModelCountData;
import com.ngbj.browse.bean.CountData;
import com.ngbj.browse.db.DBManager;
import com.ngbj.browse.receiver.NetWorkChangReceiver;
import com.ngbj.browse.util.SPHelper;
import com.socks.library.KLog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Date:2018/7/18
 * author:zl
 * 备注：所有Activity的基类，同时实现了BaseView接口,代表着本身也是个View接口
 *       T 代表着每个界面的业务逻辑P
 *       父类一般作用就是帮助子类完成一些操作，比如绑定P，设置状态栏颜色，将Activity管理起来等等
 *       当Activity销毁时，网络请求应该随之终止 -- 采用RxLifecycle对Retrofit进行生命周期管理
 *
 */
public abstract class BaseActivity<T extends BaseContract.BasePresenter> extends RxAppCompatActivity
        implements BaseContract.BaseView{

    private boolean isRegistered = false;
    private NetWorkChangReceiver netWorkChangReceiver;

    protected  T mPresenter;//某一个界面具体的P
    protected Context mContext;//某一个界面具体的上下文

    String[] mPermissions = new String[]{
            Manifest.permission.CAMERA
            ,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    protected DBManager dbManager = DBManager.getInstance(this);
    CountData countData;
    BigModelCountData bigModelCountData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        //注册网络状态监听广播
        netWorkChangReceiver = new NetWorkChangReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChangReceiver, filter);
        isRegistered = true;


        mContext = this;
        ButterKnife.bind(this);
        initInject();
        initStatusBar();
        initPresenter();
        initVariables();
        initWidget();
        initDatas();
        MyApplication.getInstance().addActivity(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //APP状态统计
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    /**设置具体布局*/
    protected abstract int getLayoutId();

    /**初始化StatusBar*/
    protected void initStatusBar() {}

    /** 初始化控件 */
    protected void initWidget() {}

    /** 加载数据 */
    protected void initDatas() {
        //请求权限
        if (EasyPermissions.hasPermissions(this, mPermissions)){
            goToNextPage();
        }else{//申请权限
            EasyPermissions.requestPermissions(this,"请给予存储卡权限,否则app无法正常运行",998,mPermissions);
        }
    }

    //TODO 权限全部通过才走这里呀，不然一个权限申请了，另一个没有申请，不会走这里的
    @AfterPermissionGranted(998)//请求码
    private void after() {
        if (EasyPermissions.hasPermissions(this, mPermissions)){
            KLog.d("权限已申请 -- after");
            goToNextPage();
        }else{
            KLog.d("权限没有申请 -- after");
            EasyPermissions.requestPermissions(this,"请给予存储卡权限,否则app无法正常运行",998,mPermissions);
        }
    }




    protected  void goToNextPage(){
        KLog.d("子类自己做操作");
    }

    //在请求权限回调中
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    /** 初始化变量*/
    protected void initVariables() {}

    /** 初始化P*/
    protected void initInject(){}


    /** P 绑定 V */
    private void initPresenter() {
        if(mPresenter != null){
            mPresenter.attachView(this);
        }
    }

    @Override
    public void complete() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.detachView();
        }
        //解绑
        if (isRegistered) {
            unregisterReceiver(netWorkChangReceiver);
        }

        MyApplication.getInstance().removeActivity(this);
    }


    //TODO 点击模块进表
    protected void addBigModelClickCountToSql(int model_id) {
        bigModelCountData = dbManager.queryBigModel(model_id);
        if(null == bigModelCountData){
            bigModelCountData = new BigModelCountData(model_id,1,model_id + "",   model_id);
            dbManager.insertBigModel(bigModelCountData);
        }else {
            bigModelCountData.setClick_num(bigModelCountData.getClick_num() + 1);
            dbManager.updateBigModel(bigModelCountData);
        }

        KLog.d("大模块:" + bigModelCountData.getBigModelShowName()
                + " 的点击次数是:：" + bigModelCountData.getClick_num()
                + " 类型是：" + bigModelCountData.getType());
    }



}