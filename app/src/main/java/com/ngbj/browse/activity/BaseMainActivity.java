package com.ngbj.browse.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.ngbj.browse.base.BaseActivity;
import com.ngbj.browse.base.BaseContract;
import com.ngbj.browse.bean.BigModelCountData;
import com.ngbj.browse.bean.CountData;
import com.ngbj.browse.bean.HistoryData;
import com.ngbj.browse.bean.KeyBean;
import com.ngbj.browse.bean.LoginBean;
import com.ngbj.browse.bean.UpHistoryBean;
import com.ngbj.browse.bean.UploadCount;
import com.ngbj.browse.bean.UploadCountBean;
import com.ngbj.browse.bean.UserInfoBean;
import com.ngbj.browse.network.retrofit.helper.RetrofitHelper;
import com.ngbj.browse.network.retrofit.response.BaseObjectSubscriber;
import com.ngbj.browse.network.retrofit.response.HttpResponse;
import com.ngbj.browse.network.retrofit.response.ResponseSubscriber;
import com.ngbj.browse.util.BitmapHelper;
import com.ngbj.browse.util.DeviceIdHepler;
import com.ngbj.browse.util.SPHelper;
import com.ngbj.browse.util.ScreenHelper;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/***
 * 这个类是4个主Activity的父类，便于统计页面的展示次数
 * 思路：来源是SlashActivity的时候，表示是第一次进入，那么保存到数据库中
 *       来源是其他或者后台的话，表示不是第一次进入，查询 -- 循环更新
 */

public abstract class BaseMainActivity<T extends BaseContract.BasePresenter> extends BaseActivity<T> {

    HandlerThread mHandlerThread;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){

            }else{
                KLog.d("数据更新完成");
            }
        }
    };

    @Override
    protected void initDatas() {
        super.initDatas();

        initHandlerThread();
    }

    private void initHandlerThread() {
        mHandlerThread = new HandlerThread("BaseMainActivity");
        mHandlerThread.start();//创建一个HandlerThread并启动它
        mHandler = new Handler(mHandlerThread.getLooper());//使用HandlerThread的looper对象创建Handler，如果使用默认的构造方法，很有可能阻塞UI线程

    }


    @SuppressLint("CheckResult")
    protected void uploadAdBigModel() {
        UploadCountBean uploadCountBean = new UploadCountBean();
        uploadCountBean.setDevice_id(DeviceIdHepler.getUniquePsuedoID());
        //获取广告集合
        List<UploadCount> adList = new ArrayList<>();
        List<CountData> countDataList = dbManager.queryCountsListByType();//只查询type = 0
        UploadCount uploadCount ;
        for (CountData countData:countDataList) {
            uploadCount = new UploadCount();
            uploadCount.setAd_id(countData.getAd_id());
            uploadCount.setClick_num(countData.getClick_num());
            uploadCount.setClick_user_num(countData.getClick_user_num());
            uploadCount.setShow_num(countData.getShow_num());
            adList.add(uploadCount);
        }
        uploadCountBean.setAds(adList);

        //获取模块集合
        List<BigModelCountData> modules = new ArrayList<>();
        List<BigModelCountData> bigModelCountDataList = dbManager.queryBigModelCountDataList();
        for (BigModelCountData bigModelCountData:bigModelCountDataList) {
            bigModelCountData.setClick_user_num((Integer) SPHelper.get(this,"click_user_num",0));
            modules.add(bigModelCountData);
        }
        uploadCountBean.setModules(modules);


        Gson gson = new Gson();
        String jsonString = gson.toJson(uploadCountBean);
        KLog.d("jsonString: " + jsonString);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jsonString.toString());

        //初始化
        RetrofitHelper.getAppService()
                .uploadCountData(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(){
                    @Override
                    public void onSuccess(ResponseBody responseBody) {
                        try {
                            String jsonString = responseBody.string();
                            Gson gson1 = new Gson();
                            HttpResponse bean = gson1.fromJson(jsonString,HttpResponse.class);
                            if(bean.getCode() == 200){
                                KLog.d("写入成功");
//                                dbManager.updateAllCountData();
                                dbManager.deleteAllCounts();
                                //TODO
                                List<CountData> list = dbManager.queryCountsListByType();
                                KLog.d(list.size());
                                dbManager.deleteAllBigModelCountData();
                                //重新设置状态位
//                                SPHelper.put(BaseMainActivity.this,"is_write_ok",true);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    protected void uploadHistoryList() {
        UpHistoryBean upHistoryBean = new UpHistoryBean();
        upHistoryBean.setDevice_id(DeviceIdHepler.getUniquePsuedoID());
        //获取关键字集合
        List<String> stringList = new ArrayList<>();
        List<KeyBean> keyBeanList = dbManager.queryKeyList();
        for (KeyBean keyBean:keyBeanList) {
            stringList.add(keyBean.getKeyName());
        }
        upHistoryBean.setSearch_word(stringList);

        //获取链接集合
        List<String> mLinkList = new ArrayList<>();
        List<HistoryData> linkList = dbManager.queryHistoryLimitList();
        for (HistoryData historyData:linkList) {
            mLinkList.add(historyData.getVisit_link());
        }
        upHistoryBean.setVisit_link(mLinkList);


        Gson gson = new Gson();
        String jsonString = gson.toJson(upHistoryBean);
        KLog.d("jsonString: " + jsonString);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jsonString.toString());

        //初始化
        RetrofitHelper.getAppService()
                .uploadHistory(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<String>(){
                    @Override
                    public void onSuccess(String string) {
                        KLog.d("数据: " + string);
                    }
                });
    }



}
