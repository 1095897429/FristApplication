package com.ngbj.browse.base;

/**
 * Created by zl on 2018/5/18.
 * 1.所有View的契约类，用于管理Presenter 和 View
 */

public interface BaseContract {

     interface BaseView {//负责显示数据、提供用户交互友好界面
         void complete();//显示成功
         void showError(String msg);//显示错误
     }

    interface BasePresenter<T> {//关系和那个View绑定
        void attachView(T view);//绑定
        void detachView();//解绑
    }


}
