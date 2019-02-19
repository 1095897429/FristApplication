package com.ngbj.browse.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ngbj.browse.R;
import com.ngbj.browse.base.BaseActivity;
import com.ngbj.browse.bean.KeyBean;
import com.ngbj.browse.util.SPHelper;
import com.ngbj.browse.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class WebViewGetUrlActivity extends BaseActivity {

    @BindView(R.id.center_title)
    EditText editText;

    private String weburl;
    SimpleDateFormat simpleDateFormat;
    Date date;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview_url;
    }

    @Override
    protected void initDatas() {
        weburl = getIntent().getStringExtra("weburl");
        editText.setText(weburl);

        initEvent();
    }

    private void initEvent() {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//搜索按键action
                    //TODO 隐藏软键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if (TextUtils.isEmpty(editText.getText().toString().trim())){
                        return true;
                    }

                    queryData(editText.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
    }

    boolean isNetwork;
    private void queryData(String content) {
        isNetwork = (boolean) SPHelper.get(WebViewGetUrlActivity.this,"is_network",false);
        if(isNetwork){
            insertKeyData(content);
            Intent data = new Intent();
            data.putExtra("content",content);
            setResult(200,data);
            finish();
        }else {
            ToastUtil.showShort(WebViewGetUrlActivity.this,"网络异常");
            return;
        }

    }



    private void insertKeyData(String keyName) {
        //先判断数据库有没有，有的话，updata
        KeyBean keyBean = dbManager.queryKey(keyName);
        simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        //获取当前时间
        date = new Date(System.currentTimeMillis());
        if(null == keyBean){
            keyBean = new KeyBean(keyName);
            keyBean.setCurrentTime(simpleDateFormat.format(date));
            dbManager.insertKey(keyBean);
        }else {
            keyBean.setCurrentTime(simpleDateFormat.format(date));
            dbManager.updateKeyBean(keyBean);
        }

    }



    @OnClick(R.id.web_back)
    public void webBack(){
        finish();
    }


}
