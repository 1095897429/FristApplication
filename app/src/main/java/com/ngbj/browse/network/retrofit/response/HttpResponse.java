package com.ngbj.browse.network.retrofit.response;

import java.io.Serializable;

/**
 * Date:2018/7/18
 * author:zl
 * 备注：统一处理后台返回 T 表示为后台Data的整体返回
 *       基本上后台返回的数据格式如下：
 *       {
 *          "Success":true,
            "code":300,
            "msg":"成功",
            "Data":[]
            }
 或

         {
            "Success":true,
             "code":300,
             "msg":"成功",
             "Data":{}
            }
 */
public class HttpResponse<T> implements Serializable{
    private boolean success;
    private int code;
    private String message;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
