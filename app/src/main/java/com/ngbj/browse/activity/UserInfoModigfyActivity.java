package com.ngbj.browse.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ngbj.browse.R;
import com.ngbj.browse.base.BaseActivity;
import com.ngbj.browse.bean.LoginBean;
import com.ngbj.browse.bean.UserInfoBean;
import com.ngbj.browse.db.DBManager;
import com.ngbj.browse.event.UpdateDataEvent;
import com.ngbj.browse.event.UpdateEvent;
import com.ngbj.browse.network.retrofit.helper.RetrofitHelper;
import com.ngbj.browse.network.retrofit.response.ResponseSubscriber;
import com.ngbj.browse.util.DeviceIdHepler;
import com.ngbj.browse.util.RegexUtils;
import com.ngbj.browse.util.SPHelper;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class UserInfoModigfyActivity extends CommonHeadActivity{

    String type;
    String name;
    String sex;
    String phone;

    @BindView(R.id.nickname_layout)
    LinearLayout nickname_layout;

    @BindView(R.id.sex_layout)
    LinearLayout sex_layout;

    @BindView(R.id.phone_layout)
    LinearLayout phone_layout;

    @BindView(R.id.phoneNum_txt)
    EditText phoneNum_txt;

    @BindView(R.id.nickname_txt)
    EditText nickname_txt;

    DBManager dbManager;
    UserInfoBean userInfoBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_userinfo;
    }

    @Override
    protected void initDatas() {
        dbManager = DBManager.getInstance(this);

        type = getIntent().getStringExtra("type");
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");



        if("1".equals(type)){
            center_title.setText("修改昵称");
            nickname_layout.setVisibility(View.VISIBLE);
            nickname_txt.setText(name);
        }else if("3".equals(type)){
            center_title.setText("修改号码");
            phone_layout.setVisibility(View.VISIBLE);
            phoneNum_txt.setText(phone);
        }

    }


    @OnClick(R.id.save)
    public void save(){
        if("1".equals(type)){
            if(checkName())
                 updateNickName();
        }else if("2".equals(type)){
        }else if("3".equals(type)){
        }
    }



    private boolean checkName(){
        name = nickname_txt.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(UserInfoModigfyActivity.this,"请输入修改昵称",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    private void updateNickName() {

        Map<String,String> map = new HashMap<>();
        map.put("nickname",name);

        Gson gson = new Gson();
        String jsonString = gson.toJson(map);
        KLog.d("jsonString: " + jsonString);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jsonString.toString());
        String token = (String) SPHelper.get(this,"token","");
        //初始化
        RetrofitHelper
                .getAppService()
                .updataUser(token,requestBody)
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

                            UserInfoBean userInfoBean = dbManager.queryUserInfo().get(0);
                            userInfoBean.setNickname(bean.getData().getNickname());
                            dbManager.updateUserInfo(userInfoBean);

                            EventBus.getDefault().post(new UpdateEvent(bean));
                            finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

}
