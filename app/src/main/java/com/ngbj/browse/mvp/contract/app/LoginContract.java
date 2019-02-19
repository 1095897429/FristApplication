package com.ngbj.browse.mvp.contract.app;


import com.ngbj.browse.base.BaseContract;
import com.ngbj.browse.bean.LoginResult;

/**
 * Date:2018/7/18
 * author:zl
 * 备注：
 */
public interface LoginContract {

    interface View extends BaseContract.BaseView{
            void showLoginSuccess(LoginResult loginResult);
    }


    interface Presenter<T> extends BaseContract.BasePresenter<T>{
            void getLoginSuccess();
    }
}
