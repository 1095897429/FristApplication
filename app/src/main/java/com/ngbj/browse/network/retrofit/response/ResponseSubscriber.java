package com.ngbj.browse.network.retrofit.response;

import com.google.gson.JsonSyntaxException;
import com.ngbj.browse.base.BaseContract;
import com.socks.library.KLog;

import java.net.SocketTimeoutException;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by zl on 2018/10.18
 */

public abstract class ResponseSubscriber<T> extends ResourceSubscriber<T> {


    public abstract void onSuccess(T t);

    @Override
    public void onNext(T response) {
        onSuccess(response);
    }

    //后台传输过来的异常 JsonSyntaxException
    @Override
    public void onError(Throwable throwable) {
        KLog.d(throwable.getMessage());
        if( throwable instanceof JsonSyntaxException){
            KLog.d("数据格式不正确，Json解析错误 ヽ(≧Д≦)ノ");
        }else if(throwable instanceof SocketTimeoutException){
            KLog.d("服务器响应超时ヽ(≧Д≦)ノ");
        }
    }

    @Override
    public void onComplete() {

    }
}
