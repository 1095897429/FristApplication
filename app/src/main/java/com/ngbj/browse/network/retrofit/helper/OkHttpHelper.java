package com.ngbj.browse.network.retrofit.helper;

import android.content.Context;
import android.text.TextUtils;


import com.alibaba.fastjson.JSONObject;
import com.allfree.security.JNIAllfree;
import com.ngbj.browse.network.retrofit.utils.Sha1SignUtils;
import com.ngbj.browse.util.AppUtil;
import com.ngbj.browse.util.AppUtils;
import com.ngbj.browse.util.DeviceIdHepler;
import com.ngbj.browse.util.FileUtils;
import com.ngbj.browse.util.StringUtils;
import com.socks.library.KLog;
import com.umeng.analytics.AnalyticsConfig;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by zl on 2018/5/22.
 * okHttp 帮助类
 * 1.需要Context -- 就把application的context给它
 */

public class OkHttpHelper {
    private static final long DEFAULT_READ_TIMEOUT_MILLIS = 20 * 1000;  //读取时间
    private static final long DEFAULT_WRITE_TIMEOUT_MILLIS = 20 * 1000; //写入时间
    private static final long DEFAULT_CONNECT_TIMEOUT_MILLIS = 20 * 1000; //超时时间
    private static final long HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 20 * 1024 * 1024;//最大缓存 -- 设置20M
    private static final int CACHE_STALE_LONG = 60 * 60 * 24 * 7;  //长缓存有效期为7天

    private static OkHttpHelper sInstance;//单例
    private OkHttpClient mOkHttpClient;//依赖OkHttpClient
    private Context mContext = AppUtils.getmContext();

    public static OkHttpHelper getInstance() {
        if (sInstance == null) {
            synchronized (OkHttpHelper.class) {
                if (sInstance == null) {
                    sInstance = new OkHttpHelper();
                }
            }
        }
        return sInstance;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private OkHttpHelper(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);  //包含header、body数据
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true) // 失败重发
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("deviceType","android")
                                .addHeader("fromPlat", AnalyticsConfig.getChannel(AppUtil.getContext()))
                                .addHeader("appVersion", StringUtils.getVersionCode(AppUtil.getContext()))

                                .build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(loggingInterceptor)    //http数据log，日志中打印出HTTP请求&响应数据
                .build();
    }


    //拼接
    public static String toUrlString(Map params) {
        Set<String> keys = params.keySet();
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : keys) {
            if (!TextUtils.isEmpty(key)) {
                stringBuilder.append(key).append('=').append(params.get(key)).append('&');
            }
        }
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

}
