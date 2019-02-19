package com.ngbj.browse.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.ngbj.browse.R;
import com.ngbj.browse.adpter.HomeFragmentAdapter;
import com.ngbj.browse.adpter.KeyHistoryAdapter;
import com.ngbj.browse.adpter.MyRecyclerAdapter;
import com.ngbj.browse.base.BaseActivity;
import com.ngbj.browse.bean.AdBean;
import com.ngbj.browse.bean.AdObjectBean;
import com.ngbj.browse.bean.BigModelCountData;
import com.ngbj.browse.bean.CountData;
import com.ngbj.browse.bean.HistoryData;
import com.ngbj.browse.bean.KeyBean;
import com.ngbj.browse.bean.LoginBean;
import com.ngbj.browse.bean.ModelBean;
import com.ngbj.browse.bean.OriData;
import com.ngbj.browse.bean.StatisticsBean;
import com.ngbj.browse.bean.UpHistoryBean;
import com.ngbj.browse.bean.UploadCountBean;
import com.ngbj.browse.constant.ApiConstants;
import com.ngbj.browse.db.DBManager;
import com.ngbj.browse.dialog.IosAlertDialog;
import com.ngbj.browse.event.ChangeFragmentEvent;
import com.ngbj.browse.event.UpdateEvent;
import com.ngbj.browse.fragment.BaseFragment;
import com.ngbj.browse.network.retrofit.helper.RetrofitHelper;
import com.ngbj.browse.network.retrofit.response.BaseListSubscriber;
import com.ngbj.browse.network.retrofit.response.BaseObjectSubscriber;
import com.ngbj.browse.network.retrofit.response.ResponseSubscriber;
import com.ngbj.browse.util.DeviceIdHepler;
import com.ngbj.browse.util.SPHelper;
import com.ngbj.browse.util.StringUtils;
import com.ngbj.browse.util.ToastUtil;
import com.ngbj.browse.view.CustomDecoration;
import com.ngbj.browse.view.myview.Tool;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/***
 * 1.历史记录从 数据库中 取出 -- 暂定
 * 2.监听 -- 调用百度api 搜索 -- 此界面销毁 --
 * 3.将软键盘 改变搜索键
 * 4.刚进入页面设置软键盘不因为EditText而自动弹出
 */

public class SearchActivity extends BaseActivity {

    DBManager dbManager ;
    @BindView(R.id.center_title)
    EditText et_search;

    @BindView(R.id.history_ll)
    LinearLayout history_ll;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;


    @BindView(R.id.id_flowlayout)
    TagFlowLayout tagFlowLayout;



    AdBean adBean;
    List<AdBean> adBeanList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    List<KeyBean> keyList = new ArrayList<>();
    KeyHistoryAdapter myRecyclerAdapter;

    SimpleDateFormat simpleDateFormat;
    Date date;
    HashMap<String,String> map = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }


    @Override
    protected void initDatas() {
        dbManager = DBManager.getInstance(getApplicationContext());
        //热搜
        getData();
        initRecycleView();
        initEvent();
    }


    /**
     * 通过Base32将Bitmap转换成Base64字符串
     * @param bit
     * @return
     */
    public String Bitmap2StrByBase64(Bitmap bit) {
        try {  ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.PNG, 40, bos);//参数100表示不压缩
            bos.close();
            byte[] bytes = bos.toByteArray();
            return Base64.encodeToString(bytes,0,bytes.length,Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
            return null;
    }






    private void getData() {
//        adBean = new AdBean();
//        for (int i = 0; i < 5; i++) {
//            adBean.setTitle("小说");
//            adBeanList.add(adBean);
//        }

        // {"success":true,"code":200,"data":[{"id":"36","title":"小鸟浏览器","link":"1","img_url":"","begin_time":"0","end_time":"0","type":"2","show_position":"4","order":"1"}],"message":"OK"}
        RetrofitHelper.getAppService()
                .getAdHotData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseListSubscriber<AdBean>() {
                    @Override
                    public void onSuccess(List<AdBean> t) {
                      adBeanList.addAll(t);
                      sendDataToUm();
                      initData();
                    }
                });
    }

    private void sendDataToUm() {
        if(adBeanList != null && adBeanList.size() != 0){
            for (AdBean adBean:adBeanList) {
                if(adBean.getType().equals("0")){
                    map.put("ad_id",adBean.getId());
                    MobclickAgent.onEvent(mContext, "HotSearchShowAd", map);//广告展示事件
                }
            }
        }

    }

    private void initRecycleView() {
        layoutManager = new LinearLayoutManager(SearchActivity.this);
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        //设置Adapter
        myRecyclerAdapter = new KeyHistoryAdapter(keyList);
        mRecyclerView.setAdapter(myRecyclerAdapter);
        //下划线
        mRecyclerView.addItemDecoration(new CustomDecoration(this,
                CustomDecoration.VERTICAL_LIST,R.drawable.divider,0));

        //获取数据库的数据
        List<KeyBean> list =  DBManager.getInstance(getApplicationContext()).queryKeyList();
        if(null != list && list.size() != 0){
            keyList.addAll(list);
            myRecyclerAdapter = new KeyHistoryAdapter(keyList);
            mRecyclerView.setAdapter(myRecyclerAdapter);
        }else{
            history_ll.setVisibility(View.GONE);
        }
    }



    private void initEvent() {
        // 搜索框的文本变化实时监听
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
//                    KLog.d("搜索历史");
                } else {
//                    KLog.d("搜索结果");
                }
                String tempName = et_search.getText().toString();
//                KLog.d("搜索的文本 :" + tempName);

            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {//搜索按键action
                        //TODO 隐藏软键盘
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        if (TextUtils.isEmpty(et_search.getText().toString().trim())){
                            return true;
                        }

                        queryData( et_search.getText().toString().trim(),"","1");

                        return true;
                    }
                    return false;
                }
            });


        myRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if(StringUtils.isFastClick()){
                    KLog.d("点击的太快啦");
                    return;
                }
                queryData(keyList.get(position).getKeyName(),"","1");


            }
        });

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

    boolean isNetwork;
    private void queryData(String content,String link,String type) {
        isNetwork = (boolean) SPHelper.get(SearchActivity.this,"is_network",false);
        if(isNetwork){
            insertKeyData(content);
            Intent data = new Intent();
            if("0".equals(type)){
                data.putExtra("content",link);
            }else
                data.putExtra("content",content);
            setResult(100,data);
            finish();
        }else {
            ToastUtil.showShort(SearchActivity.this,"网络异常");
            return;
        }

    }



    private void initData() {

//        final String [] titls = new String[]{"欧美影视","崔永元 范冰冰","奇遇人生","闻香释女人","欧美视"};
//        final String [] titls = new String[]{"欧美影视","崔永元 范冰冰"};
        tagFlowLayout.setAdapter(new TagAdapter<AdBean>(adBeanList) {
            @Override
            public View getView(FlowLayout parent, int position, AdBean adBean) {
                TextView tag_txt ;
                tag_txt = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.house_type_tag,
                        tagFlowLayout, false);
                tag_txt.setText(adBean.getTitle());
                return tag_txt;
            }
        });

        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                if(StringUtils.isFastClick()){
                    return true;
                }

                KLog.d("选择的标签是：" + adBeanList.get(position).toString());
                AdBean adBean = adBeanList.get(position);

                //TODO 大模块4
                //TODO 2018.11.7 新增逻辑 统计都走友盟
                MobclickAgent.onEvent(SearchActivity.this, "HotSearchModel");//大模块点击事件
                if(!TextUtils.isEmpty(adBean.getType()) && adBean.getType().equals("0")){//广告
                    map.put("ad_id",adBean.getId());
                    MobclickAgent.onEvent(mContext, "HotSearchAd", map);//广告点击事件
                    addAdUserClick(adBean.getId(),"HotSearchAdUserNum");
                }
                  addModleUserClick(adBean.getShow_position());//模块用户点击数
                //TODO type 为0 是广告
                queryData(adBean.getTitle(),adBean.getLink().toString(),adBean.getType());

                    //之前的逻辑
//                addBigModelClickCountToSql(4);

                return true;
            }
        });

    }


    @OnClick(R.id.cancle)
    public void cancle(){
        finish();
    }

    @OnClick(R.id.tv_deleteAll)
    public void tv_deleteAll(){

        final IosAlertDialog iosAlertDialog = new IosAlertDialog(this).builder();
        iosAlertDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBManager.getInstance(SearchActivity.this).deleteAllKeyData();
                //手动设置
                history_ll.setVisibility(View.GONE);
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).setTitle("标题").setMsg("是否删除历史记录").setCanceledOnTouchOutside(false);
        iosAlertDialog.show();


    }




    public void addAdUserClick(String adId,String adModelName){
        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        date = new Date(System.currentTimeMillis());  //获取当前时间
        String currentYear_Month_Day = simpleDateFormat.format(date);
        KLog.d("当前年月日:",currentYear_Month_Day);


        String str2 = (String) SPHelper.get(SearchActivity.this,"last_today_time","");
        KLog.d("之前记录的最后年月日:",str2);

        if(!TextUtils.isEmpty(str2) && currentYear_Month_Day.compareTo(str2) > 0){//第二天
            StatisticsBean statisticsBean = dbManager.queryAdUser(adId);
            if(null == statisticsBean){
                statisticsBean = new StatisticsBean(adId,currentYear_Month_Day,false);
                dbManager.insertAdUser(statisticsBean);
            }else{
                statisticsBean.setIs_clicked(false);
            }
            KLog.d("statisticsBean: " + statisticsBean.getIs_clicked());

            if(!statisticsBean.isIs_clicked()){//今天还没有上传，去上传,修改标志位为true,和记录当日的时间
                //更新
                statisticsBean.setIs_clicked(true);
                statisticsBean.setDate(currentYear_Month_Day);
                dbManager.updateAdUser(statisticsBean);
                //更新xml
                SPHelper.put(SearchActivity.this,"last_today_time",currentYear_Month_Day);

                MobclickAgent.onEvent(mContext, adModelName, map);
                KLog.d("去上传" + adModelName);
            }

        }else{//当日 取数据库的标志 -- 看是否已经上传过了
            StatisticsBean statisticsBean = dbManager.queryAdUser(adId);
            if(null == statisticsBean){
                statisticsBean = new StatisticsBean(adId,str2,false);
                dbManager.insertAdUser(statisticsBean);
            }
            KLog.d("statisticsBean: " + statisticsBean.getIs_clicked());


            if(currentYear_Month_Day.compareTo(statisticsBean.getDate()) > 0){
                statisticsBean.setIs_clicked(true);
                statisticsBean.setDate(currentYear_Month_Day);
                dbManager.updateAdUser(statisticsBean);
                MobclickAgent.onEvent(mContext, adModelName, map);
                KLog.d("去上传" + adModelName);
            }else{
                if(!statisticsBean.isIs_clicked()){//今天还没有上传，去上传，修改标志位为true
                    statisticsBean.setIs_clicked(true);
                    dbManager.updateAdUser(statisticsBean);
                    MobclickAgent.onEvent(mContext, adModelName, map);
                    KLog.d("去上传" + adModelName);
                }
            }
        }
    }



    public void addModleUserClick(String modelId){
        String modelName = "";
        if(modelId.equals("1")){
            modelName = "NavigationModelUserNum";
        }else  if(modelId.equals("2")){
            modelName = "CoolSiteModelUserNum";
        }else  if(modelId.equals("3")){
            modelName = "TabModelUserNum";
        }else  if(modelId.equals("4")){
            modelName = "HotSearchModelUserNum";
        }


        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        date = new Date(System.currentTimeMillis());  //获取当前时间
        String currentYear_Month_Day = simpleDateFormat.format(date);
        KLog.d("当前年月日:",currentYear_Month_Day);

        String str2 = (String) SPHelper.get(SearchActivity.this,"last_today_time","");
        KLog.d("之前记录的最后年月日:",str2);

        if(!TextUtils.isEmpty(str2) && currentYear_Month_Day.compareTo(str2) > 0){//第二天
            ModelBean modelBean = dbManager.queryModelUser(modelId);
            if(null == modelBean){
                modelBean = new ModelBean(modelId,currentYear_Month_Day,false);
                dbManager.insertModelUser(modelBean);
            }else{
                modelBean.setIs_clicked(false);
            }
            KLog.d("modelBean: " + modelBean.getIs_clicked());

            if(!modelBean.isIs_clicked()){//今天还没有上传，去上传,修改标志位为true,和记录当日的时间
                //更新
                modelBean.setIs_clicked(true);
                modelBean.setDate(currentYear_Month_Day);
                dbManager.updateModelUser(modelBean);
                //更新xml
                SPHelper.put(SearchActivity.this,"last_today_time",currentYear_Month_Day);

                MobclickAgent.onEvent(mContext, modelName);
                KLog.d("去上传" + modelName);
            }

        }else{//当日 取数据库的标志 -- 看是否已经上传过了
            ModelBean modelBean = dbManager.queryModelUser(modelId);
            if(null == modelBean){
                modelBean = new ModelBean(modelId,str2,false);
                dbManager.insertModelUser(modelBean);
            }
            KLog.d("modelBean: " + modelBean.getIs_clicked());

            if(currentYear_Month_Day.compareTo(modelBean.getDate()) > 0){
                modelBean.setIs_clicked(true);
                modelBean.setDate(currentYear_Month_Day);
                dbManager.updateModelUser(modelBean);
                MobclickAgent.onEvent(mContext, modelName);
                KLog.d("去上传" + modelName);
            }else{
                if(!modelBean.isIs_clicked()){//今天还没有上传，去上传，修改标志位为true

                    modelBean.setIs_clicked(true);
                    dbManager.updateModelUser(modelBean);
                    MobclickAgent.onEvent(mContext, modelName);
                    KLog.d("去上传" + modelName);
                }
            }
        }
    }




}
