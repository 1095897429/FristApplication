package com.ngbj.browse.mvp.contract.app;


import android.content.Context;

import com.ngbj.browse.base.BaseContract;
import com.ngbj.browse.bean.LoginResult;
import com.ngbj.browse.bean.NewsBean;
import com.ngbj.browse.bean.WeatherBean;

/**
 * Date:2018/7/18
 * author:zl
 * 备注：
 */
public interface HomeContract {

    interface View extends BaseContract.BaseView{
            void showNewsData(NewsBean newsBean);
            void showWeatherData(WeatherBean weatherBean);
    }


    interface Presenter<T> extends BaseContract.BasePresenter<T>{
            void getNewsData();
            void getWeatherData(Context context,String location);
    }
}
