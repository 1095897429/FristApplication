package com.ngbj.browse.mvp.presenter.app;

import com.ngbj.browse.base.RxPresenter;
import com.ngbj.browse.bean.LoginResult;
import com.ngbj.browse.mvp.contract.app.LoginContract;
import com.ngbj.browse.network.retrofit.helper.RetrofitHelper;
import com.ngbj.browse.network.retrofit.response.BaseObjectSubscriber;
import com.socks.library.KLog;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Date:2018/7/18
 * author:zl
 * 备注：
 */
public class LoginPresenter extends RxPresenter<LoginContract.View>
                        implements LoginContract.Presenter<LoginContract.View> {


    @Override
    public void getLoginSuccess() {
        addSubscribe(
                RetrofitHelper.getAppService()
                .login("18616541823","123456")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        KLog.d("经纪人的Id: ",loginResult.getAgentId());
                    }
                }));
    }
}
