package com.ngbj.browse.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ngbj.browse.R;
import com.ngbj.browse.base.BaseActivity;
import com.ngbj.browse.bean.LoginBean;
import com.ngbj.browse.bean.NewsBean;
import com.ngbj.browse.bean.NewsSaveMultiBean;
import com.ngbj.browse.bean.OriData;
import com.ngbj.browse.bean.UserInfoBean;
import com.ngbj.browse.bean.VerCodeBean;
import com.ngbj.browse.constant.ApiConstants;
import com.ngbj.browse.db.DBManager;
import com.ngbj.browse.network.retrofit.helper.RetrofitHelper;
import com.ngbj.browse.network.retrofit.response.BaseObjectSubscriber;
import com.ngbj.browse.network.retrofit.response.BaseSubscriber;
import com.ngbj.browse.network.retrofit.response.ResponseSubscriber;
import com.ngbj.browse.network.retrofit.utils.Sha1SignUtils;
import com.ngbj.browse.util.DeviceIdHepler;
import com.ngbj.browse.util.RegexUtils;
import com.ngbj.browse.util.SPHelper;
import com.ngbj.browse.util.StringUtils;
import com.ngbj.browse.util.ToastUtil;
import com.socks.library.KLog;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class VerificationActivity extends BaseActivity {

    @BindView(R.id.get_msg)
    TextView get_msg;

    @BindView(R.id.verty_et)
    EditText verty_et;

    @BindView(R.id.show_msg)
    TextView show_msg;

    private CountDownTimer timer;//倒计时
    private boolean timerOver = true;
    private String phoneNum = "";
    private String vertyNum = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_verification;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer = null;
    }

    @Override
    protected void initDatas() {
        phoneNum = getIntent().getStringExtra("phone");
        timer = new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                get_msg.setText(millisUntilFinished/1000+"秒");
            }

            @Override
            public void onFinish() {
                timerOver = true;
                get_msg.setText("重新获取验证码");
            }
        };
    }


    @OnClick(R.id.get_msg)
    public void getPhoneCode(){
        if(!StringUtils.isFastClick()){
            if(checkPhoneNum() && timerOver){
                timer.start();
                timerOver = false;
                //发送请求
                Map<String,String> map = new HashMap<>();
                map.put("mobile", phoneNum.trim());
                Gson gson = new Gson();
                String jsonString = gson.toJson(map);
                KLog.d("jsonString: " + jsonString);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jsonString.toString());

                //初始化
                RetrofitHelper.getAppService()
                        .getVerCode(requestBody)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new BaseObjectSubscriber<VerCodeBean>(){
                            @Override
                            public void onSuccess(VerCodeBean verCodeBean) {
                                show_msg.setText("已发送到" + phoneNum);
                                KLog.d("验证码:" + verCodeBean.getCode());
                                Toast.makeText(VerificationActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                            }
                        });

//                RetrofitUtils.getInstance().sendValidateCode(phoneNum, 0, new BaseCallBack<String>() {
//                    @Override
//                    public void OnSuccess(String s) {
//                        loadDialog.dismiss();
//                        Toasty.normal(getActivity(),"发送成功").show();
//                        timer.start();
//                        timerOver = false;
//                    }
//
//                    @Override
//                    public void onFailure(int code, String msg) {
//                        loadDialog.dismiss();
//                        if(!TextUtils.isEmpty(msg)){
//                            Toasty.normal(getActivity(),msg).show();
//                        }
//                    }
//                });
            }
        }
    }


    private boolean checkPhoneNum(){
        if(TextUtils.isEmpty(phoneNum)){
            Toast.makeText(VerificationActivity.this,"请输入手机号码",Toast.LENGTH_SHORT).show();
            KLog.d("请输入手机号码");
            return false;
        }
        if(!RegexUtils.isMobileExact(phoneNum)){
            Toast.makeText(VerificationActivity.this,"输入的手机号码格式不正确",Toast.LENGTH_SHORT).show();
            KLog.d("输入的手机号码格式不正确");
            return false;
        }
        return true;
    }


    private boolean checkVertyNum(){
        if(TextUtils.isEmpty(vertyNum)){
            Toast.makeText(VerificationActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
            KLog.d("请输入验证码");
            return false;
        }
        if(vertyNum.length() != 4){
            Toast.makeText(VerificationActivity.this,"输入的验证码数位不正确",Toast.LENGTH_SHORT).show();
            KLog.d("输入的验证码数位不正确");
            return false;
        }
        return true;
    }


    @OnClick(R.id.next_btn)
    public void NextBtn(){
        vertyNum = verty_et.getText().toString().trim();
        if(checkVertyNum()){
            login();
        }
    }


    @SuppressLint("CheckResult")
    private void login() {
        Map<String,String> map = new HashMap<>();
        map.put("device_id", DeviceIdHepler.getUniquePsuedoID());
        map.put("mobile",phoneNum);
        map.put("code", vertyNum);

        Gson gson = new Gson();
        String jsonString = gson.toJson(map);
        KLog.d("jsonString: " + jsonString);
        final RequestBody requestBody = RequestBody
                .create(MediaType.parse("application/json"),jsonString.toString());

//        RetrofitHelper.getAppService()
//                .login(requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObjectSubscriber<LoginBean>(){
//                    @Override
//                    public void onSuccess(LoginBean loginBean) {
//                        LoginBean bean = loginBean;
//                        UserInfoBean userInfoBean = new UserInfoBean();
//                        userInfoBean.setAccess_token(bean.getData().getAccess_token());
//                        userInfoBean.setExpire_time(bean.getData().getExpire_time());
//                        userInfoBean.setGender(bean.getData().getGender()+"");
//                        userInfoBean.setHead_img(bean.getData().getHead_img());
//                        userInfoBean.setMobile(bean.getData().getMobile());
//                        userInfoBean.setNickname(bean.getData().getNickname());
//                        DBManager.getInstance(VerificationActivity.this).insertUserInfo(userInfoBean);
//                        SPHelper.put(VerificationActivity.this,"isLogin",true);
//                        SPHelper.put(VerificationActivity.this,"token",bean.getData().getAccess_token());
//
//                        Intent intent  = new Intent(VerificationActivity.this,UserInfoActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });

        //初始化
        RetrofitHelper.getAppService()
                .login(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(){
                    @Override
                    public void onSuccess(ResponseBody responseBody) {
                        String jsonString ;
                        try {
                            jsonString = responseBody.string();
                            try {
                                org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);
                                int code = jsonObject.optInt("code");
                                String msg = jsonObject.optString("message");
                                if(code == 200){
                                    Gson gson1 = new Gson();
                                    LoginBean bean = gson1.fromJson(jsonString,LoginBean.class);
                                        UserInfoBean userInfoBean = new UserInfoBean();
                                        userInfoBean.setAccess_token(bean.getData().getAccess_token());
                                        userInfoBean.setExpire_time(bean.getData().getExpire_time());
                                        userInfoBean.setGender(bean.getData().getGender()+"");
                                        userInfoBean.setHead_img(bean.getData().getHead_img());
                                        userInfoBean.setMobile(bean.getData().getMobile());
                                        userInfoBean.setNickname(bean.getData().getNickname());
                                        DBManager.getInstance(VerificationActivity.this).insertUserInfo(userInfoBean);
                                        SPHelper.put(VerificationActivity.this,"isLogin",true);
                                        SPHelper.put(VerificationActivity.this,"token",bean.getData().getAccess_token());

                                        Intent intent  = new Intent(VerificationActivity.this,UserInfoActivity.class);
                                        startActivity(intent);
                                        finish();
                                }else{
                                    ToastUtil.showShort(VerificationActivity.this,msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


}
