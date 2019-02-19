package com.ngbj.browse.network.retrofit.response;

import com.google.gson.JsonSyntaxException;
import com.ngbj.browse.base.BaseContract;
import com.socks.library.KLog;

import java.net.SocketTimeoutException;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by zl on 2018/5/22.
 * T 代表要处理的数据泛型
 * 返回后台整个json字符串中的整体为的泛型 -- Splash为例
 */

public abstract class BaseSubscriber<T> extends ResourceSubscriber<T> {
    private BaseContract.BaseView mView;

    public BaseSubscriber(BaseContract.BaseView view) {
        this.mView = view;
    }

    public abstract void onSuccess(T t);

    @Override
    public void onNext(T response) {
        if (mView == null) return;//防止空指针
        mView.complete();   //获取到数据后，就调用view的方法来显示界面，比如将刷新的视图取消
        onSuccess(response);
    }

    //后台传输过来的异常 JsonSyntaxException
    @Override
    public void onError(Throwable throwable) {
        KLog.d(throwable.getMessage());
        if(mView == null)return;
        mView.complete();//回调，让下拉的父类去取消等等
        if( throwable instanceof JsonSyntaxException){
            mView.showError("数据格式不正确，Json解析错误 ヽ(≧Д≦)ノ");
        }else if(throwable instanceof SocketTimeoutException){
            mView.showError("服务器响应超时ヽ(≧Д≦)ノ");
        }
    }

    @Override
    public void onComplete() {

    }
}
