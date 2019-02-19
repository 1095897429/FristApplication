package com.ngbj.browse.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ngbj.browse.R;
import com.ngbj.browse.adpter.HomeFragmentAdapter;
import com.ngbj.browse.base.BaseActivity;
import com.ngbj.browse.bean.HistoryData;
import com.ngbj.browse.bean.KeyBean;
import com.ngbj.browse.bean.LoginBean;
import com.ngbj.browse.bean.LoginThirdBean;
import com.ngbj.browse.bean.UpHistoryBean;
import com.ngbj.browse.bean.UserInfoBean;
import com.ngbj.browse.db.DBManager;
import com.ngbj.browse.event.CloseEvent;
import com.ngbj.browse.event.DataToTopEvent;
import com.ngbj.browse.fragment.CustomerFragment;
import com.ngbj.browse.fragment.HomeFragment;
import com.ngbj.browse.fragment.MessageFragment;
import com.ngbj.browse.fragment.MyFragment;
import com.ngbj.browse.network.retrofit.helper.RetrofitHelper;
import com.ngbj.browse.network.retrofit.response.BaseObjectSubscriber;
import com.ngbj.browse.network.retrofit.response.ResponseSubscriber;
import com.ngbj.browse.util.DeviceIdHepler;
import com.ngbj.browse.util.RegexUtils;
import com.ngbj.browse.util.SPHelper;
import com.ngbj.browse.util.ToastUtil;
import com.socks.library.KLog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.phone_ed)
    EditText editText;

    private String phoneNum = "";
    private String plat;//平台

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initDatas() {
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        UMShareAPI.get(this).release();//解除关联，解决内存泄漏
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCloseEvent(CloseEvent event) {
       finish();
    }



    @OnClick(R.id.back)
    public void ToBack(){
       finish();
    }

    @OnClick(R.id.next_btn)
    public void NextBtn(){
        phoneNum = editText.getText().toString().trim();
        if(checkPhoneNum()){
            Intent intent  = new Intent(this,VerificationActivity.class);
            intent.putExtra("phone",phoneNum);
            startActivity(intent);
        }

    }


    private boolean checkPhoneNum(){
        if(TextUtils.isEmpty(phoneNum)){
            Toast.makeText(LoginActivity.this,"请输入手机号码",Toast.LENGTH_SHORT).show();
            KLog.d("请输入手机号码");
            return false;
        }
        if(!RegexUtils.isMobileExact(phoneNum)){
            Toast.makeText(LoginActivity.this,"输入的手机号码格式不正确",Toast.LENGTH_SHORT).show();
            KLog.d("输入的手机号码格式不正确");
            return false;
        }
        return true;
    }


    /**
     * 检测是否安装微信
     *
     * @param context
     * @return
     */
    public static boolean isWxInstall(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isQQInstall(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isWBInstall(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.sina.weibo")) {
                    return true;
                }
            }
        }
        return false;
    }


    @OnClick(R.id.qqLogin)
    public void QqLogin(){
        if (isQQInstall(this)) {
            authorization(SHARE_MEDIA.QQ);
            plat = "QQ";
        } else {
            ToastUtil.showShort(this, "请先安装QQ客户端");
            return;
        }
    }

    @OnClick(R.id.weiboLogin)
    public void WeiboLogin(){
        if (isWBInstall(this)) {
            authorization(SHARE_MEDIA.SINA);
            plat = "SINA";
        } else {
            ToastUtil.showShort(this, "请先安装微博客户端");
            return;
        }
    }


    @OnClick(R.id.weixinLoging)
    public void WeixinLoging() {
        if (isWxInstall(this)) {
            authorization(SHARE_MEDIA.WEIXIN);
            plat = "WEIXIN";
        } else {
            ToastUtil.showShort(this, "请先安装微信客户端");
            return;
        }
    }

    //授权
    private void authorization(SHARE_MEDIA share_media) {
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);

        UMShareAPI.get(this).getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                KLog.d("tag", "onStart " + "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                KLog.d("tag", "onComplete " + "授权完成");

                //sdk是6.4.4的,但是获取值的时候用的是6.2以前的(access_token)才能获取到值,未知原因
                String uid = map.get("uid");
                String openid = map.get("openid");//微博没有
                String unionid = map.get("unionid");//微博没有
                String access_token = map.get("access_token");
                String refresh_token = map.get("refresh_token");//微信,qq,微博都没有获取到
                String expires_in = map.get("expires_in");
                String name = map.get("name");
                String gender = map.get("gender");
                String iconurl = map.get("iconurl");
                String city = map.get("city");
                String province = map.get("province");
//                Toast.makeText(getApplicationContext(), "name=" + name + ",gender=" + gender, Toast.LENGTH_SHORT).show();
                LoginThirdBean bean = new LoginThirdBean();
                bean.setDevice_id(DeviceIdHepler.getUniquePsuedoID());
                bean.setPlat(plat);
                bean.setOpenid(openid);
                bean.setNickname(name);
                bean.setSex(gender);
                bean.setUnionid(unionid);
                bean.setCity(city);
                bean.setProvince(province);
                bean.setHeadimgurl(iconurl);

                thirdLogin(bean);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                KLog.d("tag",  "授权失败 " + throwable.getMessage());
                ToastUtil.showShort(LoginActivity.this,throwable.getMessage());
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                KLog.d("tag",  "onCancel " + "授权取消");
            }
        });
    }


    private void thirdLogin(LoginThirdBean bean) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(bean);
        KLog.d("jsonString: " + jsonString);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jsonString.toString());

        //初始化
        RetrofitHelper.getAppService()
                .thridPartLogin(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(){

                    @Override
                    public void onSuccess(ResponseBody responseBody) {
                        String jsonString ;
                        try {
                            jsonString = responseBody.string();
                            Gson gson1 = new Gson();
                            LoginBean bean = gson1.fromJson(jsonString,LoginBean.class);

                            UserInfoBean userInfoBean = new UserInfoBean();
                            userInfoBean.setAccess_token(bean.getData().getAccess_token());
                            userInfoBean.setExpire_time(bean.getData().getExpire_time());
                            userInfoBean.setGender(bean.getData().getGender().equals("男")?"1":"0");
                            userInfoBean.setHead_img(bean.getData().getHead_img());
                            userInfoBean.setMobile(bean.getData().getMobile());
                            userInfoBean.setNickname(bean.getData().getNickname());
                            DBManager.getInstance(LoginActivity.this).insertUserInfo(userInfoBean);
                            SPHelper.put(LoginActivity.this,"isLogin",true);
                            SPHelper.put(LoginActivity.this,"token",bean.getData().getAccess_token());
                            Intent intent  = new Intent(LoginActivity.this,UserInfoActivity.class);
                            startActivity(intent);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }



}
