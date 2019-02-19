package com.ngbj.browse.network.retrofit.utils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.ngbj.browse.network.retrofit.helper.OkHttpHelper;
import com.ngbj.browse.util.AppUtil;
import com.socks.library.KLog;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Date:2018/7/19
 * author:zl
 * 备注：同时配置OkHttp,构建Retrofit,构建动态代理对象
 */
public class RetrofitUtil {

    public static OkHttpClient getOkhttpClient(){
        //设置日志拦截器，拦截服务器返回的json数据
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                KLog.d("OkHttp 数据",message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //OkHttp配置缓存
        File cacheFile = new File(AppUtil.getContext().getCacheDir(),"cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 2); //2Mb

        return new OkHttpClient.Builder()
                    .readTimeout(10000, TimeUnit.MILLISECONDS)
                    .connectTimeout(10000,TimeUnit.MILLISECONDS)
                    .addInterceptor(loggingInterceptor)
                    .cache(cache)
                    .build();
    }


    public static Retrofit getRetrofit(String baseUrl){
        return new Retrofit.Builder()
                .client(OkHttpHelper.getInstance().getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public static <T> T getApiService(Class<T> cls,String baseUrl){
        return getRetrofit(baseUrl).create(cls);
    }
}
