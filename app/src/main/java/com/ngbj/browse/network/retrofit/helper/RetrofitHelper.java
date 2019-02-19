package com.ngbj.browse.network.retrofit.helper;


import com.ngbj.browse.bean.AdBean;
import com.ngbj.browse.bean.AdObjectBean;
import com.ngbj.browse.bean.BuildingBean;
import com.ngbj.browse.bean.LoginBean;
import com.ngbj.browse.bean.LoginResult;
import com.ngbj.browse.bean.OriData;
import com.ngbj.browse.bean.VerCodeBean;
import com.ngbj.browse.constant.ApiConstants;
import com.ngbj.browse.network.retrofit.api.AppService;
import com.ngbj.browse.network.retrofit.response.HttpResponse;
import com.ngbj.browse.network.retrofit.utils.RetrofitUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Date:2018/7/18
 * author:zl
 * 备注：管理不同的网络接口 -- 内部用Retrofit获取代理对象
 */
public class RetrofitHelper {

    private static AppService mAppService;

    static {
        mAppService = RetrofitUtil.getApiService(AppService.class, ApiConstants.URL);
    }

    public static AppService getAppService(){
        return mAppService;
    }

    //初始化
    public Flowable<HttpResponse<OriData>> initOriData(RequestBody requestBody){
        return mAppService.initOriData(requestBody);
    }

    //首页广告
    public Flowable<HttpResponse<AdObjectBean>> getAdData(String from_plat){
        return mAppService.getAdData();
    }

    //热搜
    public Flowable<HttpResponse<List<AdBean>>> getAdHotData(){
        return mAppService.getAdHotData();
    }

    //上传广告 + 大模块
    public Flowable<ResponseBody> uploadCountData(RequestBody requestBody){
        return mAppService.uploadCountData(requestBody);
    }

    //获取验证码
    public Flowable<HttpResponse<VerCodeBean>> getVerCode(RequestBody requestBody){
        return mAppService.getVerCode(requestBody);
    }

    //login
    public Flowable<ResponseBody> login(RequestBody requestBody){
        return mAppService.login(requestBody);
    }

//    public Flowable<HttpResponse<LoginBean>> login(RequestBody requestBody){
//        return mAppService.login(requestBody);
//    }

    //update信息
    public Flowable<ResponseBody> updataUser(String access_token,RequestBody requestBody){
        return mAppService.updataUser(access_token,requestBody);
    }

    //update头像信息
    public Flowable<ResponseBody> updataHeadUser(String access_token,RequestBody requestBody){
        return mAppService.updataHeadUser(access_token,requestBody);
    }

    //logout
    public Flowable<ResponseBody> logout(String access_token){
        return mAppService.logout(access_token);
    }

    //历史记录
    public Flowable<HttpResponse<String>> uploadHistory(RequestBody requestBody){
        return mAppService.uploadHistory(requestBody);
    }


    //第三方登录
    public Flowable<ResponseBody> thridPartLogin(RequestBody requestBody){
        return mAppService.thridPartLogin(requestBody);
    }


    public Flowable<ResponseBody> getNewsData(int num , int page){
        return mAppService.getNewsData(num,page);
    }

    public Flowable<HttpResponse<LoginResult>> login(String phoneNum,String password){
        return mAppService.login(phoneNum,password);
    }


    public Flowable<HttpResponse<List<BuildingBean>>> getAgentBuildingRecommendList(int agentId, int pageIndex, String provinceId){
        return mAppService.getAgentBuildingRecommendList(agentId,pageIndex,provinceId);
    }

}
