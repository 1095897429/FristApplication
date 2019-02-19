package com.ngbj.browse.mvp.presenter.app;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.ngbj.browse.base.RxPresenter;
import com.ngbj.browse.bean.LoginResult;
import com.ngbj.browse.bean.NewsBean;
import com.ngbj.browse.bean.WeatherBean;
import com.ngbj.browse.bean.WeatherSaveBean;
import com.ngbj.browse.constant.ApiConstants;
import com.ngbj.browse.db.DBManager;
import com.ngbj.browse.mvp.contract.app.HomeContract;
import com.ngbj.browse.network.retrofit.helper.RetrofitHelper;
import com.ngbj.browse.network.retrofit.response.BaseObjectSubscriber;
import com.ngbj.browse.network.retrofit.response.BaseSubscriber;
import com.ngbj.browse.network.retrofit.utils.Sha1SignUtils;
import com.ngbj.browse.util.DeviceIdHepler;
import com.socks.library.KLog;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomePresenter extends RxPresenter<HomeContract.View>
                        implements HomeContract.Presenter<HomeContract.View> {



    @Override
    public void getNewsData() {
        long time = System.currentTimeMillis();
        Map<String,Object> map = new HashMap<>();
        map.put("num","20");
        map.put("page","1");
        //map.put("from","browse");//它需要统计来源
        map.put("device_serial", DeviceIdHepler.getUniquePsuedoID());
        map.put("app_key", "llq2db90");
        map.put("auth_timestamp", time);
        map = Sha1SignUtils.reSign(map);

        //获取到签名文件
        String qianming = (String) map.get("auth_signature");

        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("num", "20")
                .add("page", "1")
                .add("device_serial",  DeviceIdHepler.getUniquePsuedoID())
                .add("app_key", "llq2db90")
                .add("auth_timestamp", String.valueOf(time))
                .add("auth_signature",qianming)
                .build();

        final Request request = new Request.Builder()
                .url(ApiConstants.BASEURL)//请求的url
                .post(formBody)
                .build();

        //创建/Call
        Call call = okHttpClient.newCall(request);
        //加入队列 异步操作
        call.enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                KLog.d("连接失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200) {
                   String result = response.body().string();
                   KLog.d(result);
                   NewsBean newsBean = JSONObject.parseObject(result,NewsBean.class);
                   if(newsBean.getReturn_code().equals("200")){
                       mView.showNewsData(newsBean);
                       KLog.d("推荐数量：" + newsBean.getReturn_data().getCom_list().size());
                   }

                }
            }
        });

    }



    @Override
    public void getWeatherData(final Context context,String location) {

        String url = "https://free-api.heweather.com/s6/weather/now?" + "location=" + location + "&" +
                "key=" + ApiConstants.HEKEY;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 注：该回调是子线程，非主线程
//                KLog.d("我是异步线程,线程Name为:" + Thread.currentThread().getName());
                String responseStr = response.body().string();
//                KLog.d(responseStr);
                if(null != responseStr && !TextUtils.isEmpty(responseStr)){
                    Gson gson = new Gson();
                    WeatherBean weatherBean = gson.fromJson(responseStr,WeatherBean.class);
                    if("ok".equals(weatherBean.getHeWeather6().get(0).getStatus())){
                        KLog.d("温度：" + weatherBean.getHeWeather6().get(0).getNow().getTmp());
                        DBManager dbManager = DBManager.getInstance(context);
                        WeatherSaveBean weatherSaveBean = new WeatherSaveBean();
                        weatherSaveBean.setTemp(weatherBean.getHeWeather6().get(0).getNow().getTmp());
                        weatherSaveBean.setCondition(weatherBean.getHeWeather6().get(0).getNow().getCloud());
                        dbManager.insertWeather(weatherSaveBean);

                        mView.showWeatherData(weatherBean);
                    }

                }



            }
        });

    }
}
