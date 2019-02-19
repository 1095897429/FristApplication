package com.ngbj.browse.network.retrofit.api;


import com.ngbj.browse.bean.AdBean;
import com.ngbj.browse.bean.AdObjectBean;
import com.ngbj.browse.bean.BuildingBean;
import com.ngbj.browse.bean.LoginBean;
import com.ngbj.browse.bean.LoginResult;
import com.ngbj.browse.bean.OriData;
import com.ngbj.browse.bean.VerCodeBean;
import com.ngbj.browse.network.retrofit.response.HttpResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Date:2018/7/18
 * author:zl
 * 备注：网络请求接口
 *      POST方式 -- 浏览器默认原生form表单，默认为enctype以application/x-www-form-urlencoded方式提交数据,格式如下key1=val1&key2=val2
 *               -- enctype以multipart/form-data方式，格式每部分都以--boundary开始，内容描述，然后回车||文件还要包括文件名和文件类型
 */
public interface AppService {

    /** 初始化信息 */
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("users/init-user-info")
    Flowable<HttpResponse<OriData>> initOriData(@Body RequestBody requestBody);


    @GET("ads")
    Flowable<HttpResponse<AdObjectBean>> getAdData();

    @GET("ads/hot-search")
    Flowable<HttpResponse<List<AdBean>>> getAdHotData();





    @POST("mobile-code/send-mobile-code")
    Flowable<HttpResponse<VerCodeBean>> getVerCode(@Body RequestBody requestBody);


    /** login信息 */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("users/login")
    Flowable<ResponseBody> login(@Body RequestBody requestBody);
//    Flowable<HttpResponse<LoginBean>> login(@Body RequestBody requestBody);


    /** 修改头像信息 */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("users/update-user-head-image")
    Flowable<ResponseBody> updataHeadUser(@Query("access-token") String access_token,
                                               @Body RequestBody requestBody);


    /** 修改用户信息 */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("users/update-user-info")
    Flowable<ResponseBody> updataUser(@Query("access-token") String access_token,
                                      @Body RequestBody requestBody);


    @GET("users/logout")
    Flowable<ResponseBody> logout(@Query("access-token") String access_token);





    /** 历史记录 */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("history/visit-history")
    Flowable<HttpResponse<String>> uploadHistory(@Body RequestBody requestBody);



    /** 广告 + 大模块 （没有上传时，记得缓存）*/
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("ad-statistics/statistics")
    Flowable<ResponseBody> uploadCountData(@Body RequestBody requestBody);



    /** 第三方登录（没有上传时，记得缓存）*/
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("users/third-plat-login")
    Flowable<ResponseBody> thridPartLogin(@Body RequestBody requestBody);


    /** 新闻精选展示列表 */
    @POST("api/News/newsColList")
    @FormUrlEncoded
    Flowable<ResponseBody> getNewsData(
            @Field("num") int num,
            @Field("page") int page);


    /** 登录界面 */
    @POST("AgentLogin/Login")
    @FormUrlEncoded
    Flowable<HttpResponse<LoginResult>> login(@Field("tel")String phoneNum, @Field("pwd")String password);


    /** 首页楼盘列表 */
    @POST("AgentBuilding/GetAgentBuildingRecommendList")
    @FormUrlEncoded
    Flowable<HttpResponse<List<BuildingBean>>> getAgentBuildingRecommendList(@Field("AgentId") int agentId,
                                                                             @Field("PageIndex") int pageIndex,
                                                                             @Field("ProvinceId") String cityId);
}
