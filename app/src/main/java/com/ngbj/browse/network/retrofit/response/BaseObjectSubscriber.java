package com.ngbj.browse.network.retrofit.response;

import android.net.ParseException;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.ngbj.browse.util.AppUtil;
import com.socks.library.KLog;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.HttpException;

/**
 * Date:2018/7/19
 * author:zl
 * 备注：用于解析后台返回的Json对象
 */
public abstract class BaseObjectSubscriber<T> extends ResourceSubscriber<HttpResponse<T>>{

    /** 统一做处理，这里response就是返回的后台数据 */
    @Override
    public void onNext(HttpResponse<T> response) {
        if(response.getCode() == 200){
            if(response.getData() != null){
                onSuccess(response.getData());
            }
        }else{
            onFailure(response.getCode(),response.getMessage());
        }

    }

    /** 服务器返回数据，但响应码不为200*/
    public void onFailure(int code, String message) {
        KLog.d("数据返回错误信息 并且结束对话框",message);
        Toast.makeText(AppUtil.getContext(),message,Toast.LENGTH_SHORT).show();
    }

    /** 返回正确 方法*/
    public abstract void onSuccess(T t);


    /**
     * TODO 请求异常，可以用BaseView中的showError来显示
     * @param reason
     */
    public void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                KLog.d("连接超时");
                break;

            case CONNECT_TIMEOUT:
                KLog.d("连接错误");
                break;

            case BAD_NETWORK:
                KLog.d("HTTP错误");
                break;

            case PARSE_ERROR:
                KLog.d("解析错误");
                break;

            case UNKNOWN_ERROR:
                KLog.d("未知错误");
            default:
                break;
        }
    }

    @Override
    public void onError(Throwable e) {
        KLog.d("错误信息并且结束对话框",e.getMessage());
        if (e instanceof HttpException) {     //   HTTP错误
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {   //   连接错误
            onException(ExceptionReason.CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {   //  连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {   //  解析错误
            onException(ExceptionReason.PARSE_ERROR);
        }else {
            onException(ExceptionReason.UNKNOWN_ERROR);//未知错误
        }
    }

    /**
     * 请求网络失败原因
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }

    @Override
    public void onComplete() {

    }

    @Override
    protected void onStart() {
        super.onStart();
//        KLog.d("显示对话框");
    }
}
