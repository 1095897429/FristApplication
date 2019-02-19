package com.ngbj.browse.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.ngbj.browse.R;
import com.ngbj.browse.activity.SearchActivity;
import com.ngbj.browse.activity.WebViewGetUrlActivity;
import com.ngbj.browse.adpter.HomeFragmentAdapter;
import com.ngbj.browse.adpter.IndexGridViewAdapter;
import com.ngbj.browse.adpter.IndexViewPagerAdapter;
import com.ngbj.browse.adpter.Index_Cool_Adapter;
import com.ngbj.browse.bean.AdBean;
import com.ngbj.browse.bean.AdObjectBean;
import com.ngbj.browse.bean.BookMarkData;
import com.ngbj.browse.bean.CountData;
import com.ngbj.browse.bean.HistoryData;
import com.ngbj.browse.bean.StatisticsBean;
import com.ngbj.browse.bean.WeatherBean;
import com.ngbj.browse.bean.WeatherSaveBean;
import com.ngbj.browse.constant.ApiConstants;
import com.ngbj.browse.db.DBManager;
import com.ngbj.browse.event.CollectEvent;
import com.ngbj.browse.event.DataToTopEvent;
import com.ngbj.browse.event.History_CollectionEvent;
import com.ngbj.browse.event.NewsShowFragmentEvent;
import com.ngbj.browse.event.RefreshDataEvent;
import com.ngbj.browse.event.RefreshDataSecondEvent;
import com.ngbj.browse.event.TypeEvent;
import com.ngbj.browse.network.retrofit.helper.RetrofitHelper;
import com.ngbj.browse.network.retrofit.response.BaseObjectSubscriber;
import com.ngbj.browse.util.AppUtil;
import com.ngbj.browse.util.SPHelper;
import com.ngbj.browse.util.StringUtils;
import com.ngbj.browse.util.ToastUtil;
import com.ngbj.browse.view.StickyNavLayout2;
import com.socks.library.KLog;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.DOWNLOAD_SERVICE;

/***
 * 首页大Fragment -- 第一个
 *
 * 1.是正常
 * 0.是广告
 */
public class Index_Fragment extends BaseFragment {

    @BindView(R.id.degrees)
    TextView degrees;//温度

    @BindView(R.id.location_tv)
    TextView location_tv;//城市

    @BindView(R.id.clound)
    TextView clound;//天气
    @BindView(R.id.top_1_pic)
    ImageView top_1_pic;

    @BindView(R.id.top_2_pic)
    ImageView top_2_pic;

    @BindView(R.id.top_3_pic)
    ImageView top_3_pic;

    @BindView(R.id.top_4_pic)
    ImageView top_4_pic;

    @BindView(R.id.top_5_pic)
    ImageView top_5_pic;

    @BindView(R.id.index_f1_text)
    TextView index_f1_text;

    @BindView(R.id.index_f2_text)
    TextView index_f2_text;

    @BindView(R.id.index_f3_text)
    TextView index_f3_text;

    @BindView(R.id.index_f4_text)
    TextView index_f4_text;

    @BindView(R.id.index_f5_text)
    TextView index_f5_text;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;//显示滑动头下的VP

    @BindView(R.id.viewPager_gridView)
    ViewPager mViewPagerGridView;//显示gridView的VP

    @BindView(R.id.tl_5)
    SlidingTabLayout tabLayout_5;//标题

    @BindView(R.id.stickyNavLayout)
    StickyNavLayout2 stickyNavLayout;//滑动父类

    @BindView(R.id.progressBar)
    ProgressBar pg;

    WebView webview;

    @BindView(R.id.webView_ll)
    LinearLayout webView_ll;

    @BindView(R.id.center_title)
    TextView center_title;

    @BindView(R.id.webView_addpart)
    LinearLayout webView_addpart;

    @BindView(R.id.par3)
    LinearLayout par3;

    @BindView(R.id.part1)
    RelativeLayout part1;

    List<AdBean> adTop1BeanList = new ArrayList<>();
    List<AdBean> adTop2BeanList = new ArrayList<>();
    List<AdBean> adTop3BeanList = new ArrayList<>();
    List<String> list_Title = new ArrayList<>();//标题


    private int totalPage;//总的页数
    private int mPageSize = 8;//每页显示的最大数量
    private List<View> viewPagerList;

    GridView gridView;
    IndexGridViewAdapter mIndexGridViewAdapter;
    HomeFragmentAdapter pagerAdapter;


    SimpleDateFormat simpleDateFormat;
    Date date;
    boolean isRefresh ;
    String saveTitle;
    String saveUrl;
    String currentUrl ;
    String currentTitle;



    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                degrees.setText(weatherBean.getHeWeather6().get(0).getNow().getTmp() + "°");
                location_tv.setText(locationName);
                clound.setText(weatherBean.getHeWeather6().get(0).getNow().getCond_txt() +"");
            }
        }
    };

    protected void getWeatherData(final Context context, final String location) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://free-api.heweather.com/s6/weather/now?" + "location=" + location + "&" +
                        "key=" + ApiConstants.HEKEY;
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // 注：该回调是子线程，非主线程
//                KLog.d("我是异步线程,线程Name为:" + Thread.currentThread().getName());
                        String responseStr = response.body().string();
                        if(null != responseStr && !TextUtils.isEmpty(responseStr)){
                            Gson gson = new Gson();
                            weatherBean = gson.fromJson(responseStr,WeatherBean.class);
                            if("ok".equals(weatherBean.getHeWeather6().get(0).getStatus())){
//                                KLog.d("温度：" + weatherBean.getHeWeather6().get(0).getNow().getTmp());
                                DBManager dbManager = DBManager.getInstance(context.getApplicationContext());
                                WeatherSaveBean weatherSaveBean = new WeatherSaveBean();
                                weatherSaveBean.setArea(locationName);
                                weatherSaveBean.setTemp(weatherBean.getHeWeather6().get(0).getNow().getTmp());
                                weatherSaveBean.setCondition(weatherBean.getHeWeather6().get(0).getNow().getCond_txt());
                                dbManager.insertWeather(weatherSaveBean);

                                //发送消息
                                Message message = Message.obtain();
                                message.what = 1;
                                handler.sendMessage(message);
                            }

                        }



                    }
                });
            }
        }).start();


    }

    //TODO 新增V1.1.0
    /** -----------------------------------------------------------------*/
    @BindView(R.id.cool_recycleView)
    RecyclerView cool_recycleView;
    Index_Cool_Adapter indexCoolAdapter;

    private void initCoolRecycleView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),4);
        //设置布局管理器
        cool_recycleView.setLayoutManager(layoutManager);
        //设置Adapter
        indexCoolAdapter = new Index_Cool_Adapter(adTop2BeanList);
        cool_recycleView.setAdapter(indexCoolAdapter);
        //一行代码开启动画 默认CUSTOM动画
        indexCoolAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置事件
        indexCoolAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AdBean adBean = adTop2BeanList.get(position);
                MobclickAgent.onEvent(getActivity(), "CoolSiteModel");//大模块点击事件
                if(!TextUtils.isEmpty(adBean.getType()) && adBean.getType().equals("0")){//广告
                    map.put("ad_id",adBean.getId());
                    MobclickAgent.onEvent(mContext, "CoolSiteAd", map);//广告点击事件
                    addAdUserClick(adBean.getId(),"CoolSiteAdUserNum");
                }
                addModleUserClick(adBean.getShow_position());//模块用户点击数

                if("1".equals(adBean.getLink())){
                    ToastUtil.customToastGravity(getActivity(),"敬请期待",2, Gravity.CENTER,0,0);
                    return;
                }
                startWebViewRequestLink(adBean.getLink());
            }
        });
    }


    private void getAdData2_2() {
        indexCoolAdapter.setNewData(adTop2BeanList);
    }

    /** -----------------------------------------------------------------*/


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    //home界面的新闻点击返回的逻辑
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewsShowFragmentEvent(NewsShowFragmentEvent event) {
        if(event.getType() == 1){
            part1.setClickable(false);
            startWebViewRequest(event.getLink());
        }
    }


    int type = 1;
    //点击多窗体时获取的type
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTypeEvent(TypeEvent event) {
        type =  event.getType();
    }




    //恢复原样view
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataToTopEvent(DataToTopEvent event) {
        stickyNavLayout.scrollTo(0,0);
    }

    //刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshDataEvent(RefreshDataEvent event) {
        isRefresh = true;
        if(event.getIndex() == 0){
            if(webView_ll.getVisibility() == View.VISIBLE){
//                KLog.d("刷新网页内容");
                webview.reload(); //刷新
                return;
            }else{
                refreshAdData();
                stickyNavLayout.scrollTo(0,0);
                EventBus.getDefault().post(new RefreshDataSecondEvent(0));
            }
        }
    }

    //收藏 将当前页地址记录
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCollectEvent(CollectEvent event) {
        if(webView_ll.getVisibility() == View.VISIBLE){
                if(event.getType() == 0){
                    saveToBookMarkSql( webview.getTitle(),webview.getUrl());
                    Toast.makeText(getActivity(),"收藏成功",Toast.LENGTH_SHORT).show();
                }
        }
    }

    //历史记录 + 收藏 跳转
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHistory_CollectionEvent(History_CollectionEvent event) {
        if(event.getIndex().equals("1"))
            startWebViewRequestLink(event.getLink());
    }



    private void refreshAdData() {
        isNetwork = (boolean) SPHelper.get(getActivity(),"is_network",false);
        if(isNetwork){
            getWeatherData(getActivity(),locationName);
            getHomeData();
        }else{
            List<CountData> list = dbManager.queryCountsListAll();
            getDataBySql(list);
        }
    }


    @SuppressLint("CheckResult")
    private void getHomeData(){
        //初始化
        RetrofitHelper.getAppService()//AnalyticsConfig.getChannel(getActivity())
                .getAdData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<AdObjectBean>(){
                    @Override
                    public void onSuccess(AdObjectBean adObjectBean) {
                    if(null != adObjectBean){
                        adTop1BeanList.clear();
                        adTop2BeanList.clear();
                        adTop3BeanList.clear();
                        adTop1BeanList.addAll(adObjectBean.getCate());
                        adTop2BeanList.addAll(adObjectBean.getCool_site());
                        adTop3BeanList.addAll(adObjectBean.getBanner());
                        changeOriData();
                        sendDataToUm();
                        //TODO 2018.10.18 添加/删除/更新
                        addToSql(adTop1BeanList,adTop2BeanList,adTop3BeanList);
                        }
                    }
                });
    }

    private void sendDataToUm() {
        if(adTop1BeanList != null && adTop1BeanList.size() != 0){
            for (AdBean adBean:adTop1BeanList) {
                if(adBean.getType().equals("0")){
                    map.put("ad_id",adBean.getId());
                    MobclickAgent.onEvent(mContext, "NavigationShowAd", map);//广告展示事件
//                    KLog.d(" -- NavigationShowAd -- ");
                }
            }
        }

        if(adTop2BeanList != null && adTop2BeanList.size() != 0){
            for (AdBean adBean:adTop2BeanList) {
                if(adBean.getType().equals("0")){
                    map.put("ad_id",adBean.getId());
                    MobclickAgent.onEvent(mContext, "CoolSiteShowAd", map);//广告展示事件
//                      KLog.d(" -- CoolSiteShowAd -- ");
                }
            }
        }

        if(adTop3BeanList != null && adTop3BeanList.size() != 0){

            for (AdBean adBean:adTop3BeanList) {
                if(adBean.getType().equals("0")){
                    map.put("ad_id",adBean.getId());
                    MobclickAgent.onEvent(mContext, "TabShowAd", map);//广告展示事件
                }
            }
        }

    }

    //从数据库中得到数据
    protected void getDataBySql(List<CountData> list) {
        AdBean adBean ;
        if(list != null && list.size() != 0){
            for (CountData countData: list) {
                adBean = new AdBean();
                if(!TextUtils.isEmpty(countData.getShow_position()) && countData.getShow_position().equals("1")){//第一块
                    adBean.setTitle(countData.getAdShowName());
                    adBean.setId(countData.getAd_id());
                    adBean.setImg_url(countData.getImg_url());
                    adBean.setLink(countData.getAd_link());
                    adBean.setType(countData.getType());
                    adBean.setShow_position(countData.getShow_position());
                    adTop1BeanList.add(adBean);
                }

                if(!TextUtils.isEmpty(countData.getShow_position()) && countData.getShow_position().equals("2")){//第二块
                    adBean.setId(countData.getAd_id());
                    adBean.setTitle(countData.getAdShowName());
                    adBean.setImg_url(countData.getImg_url());
                    adBean.setLink(countData.getAd_link());
                    adBean.setType(countData.getType());
                    adBean.setShow_position(countData.getShow_position());
                    adTop2BeanList.add(adBean);
                }

                if(!TextUtils.isEmpty(countData.getShow_position()) && countData.getShow_position().equals("3")){//第三块
                    adBean.setTitle(countData.getAdShowName());
                    adBean.setId(countData.getAd_id());
                    adBean.setImg_url(countData.getImg_url());
                    adBean.setLink(countData.getAd_link());
                    adBean.setType(countData.getType());
                    adBean.setShow_position(countData.getShow_position());
                    adTop3BeanList.add(adBean);
                }
            }
            changeOriData();
        }

    }

    // 先删后加  -- 加入数据到数据库中
    protected void addToSql(List<AdBean> adTop1BeanList, List<AdBean> adTop2BeanList, List<AdBean> adTop3BeanList) {
        dbManager.deleteAllCounts();
        for (AdBean adBean:adTop1BeanList) {
            if(adBean.getType().equals("1") || (adBean.getType().equals("0"))){
                transformToCountData(adBean);
            }
        }

        for (AdBean adBean:adTop2BeanList) {
            if(adBean.getType().equals("1") || (adBean.getType().equals("0"))){
                transformToCountData(adBean);
            }
        }

        for (AdBean adBean:adTop3BeanList) {
            if(adBean.getType().equals("1") || (adBean.getType().equals("0"))){
                transformToCountData(adBean);
            }
        }

        List<CountData> list = dbManager.queryCountsListAll();
//        KLog.d("存入成功了吗 " + list.size());
    }

    private void transformToCountData(AdBean adBean) {
        countData = new CountData();
        countData.setAdShowName(adBean.getTitle());
        countData.setAd_id(adBean.getId());
        countData.setImg_url(adBean.getImg_url());
        countData.setAd_link(adBean.getLink());
        countData.setType(adBean.getType());
        countData.setShow_num(1);//默认展示次数为1，之前都获取1即可
        countData.setShow_position(adBean.getShow_position());
        dbManager.insertUser(countData);
    }


    private void addSqlAndToWeb(AdBean adBean, int type) {

        addClickCountToSql(adBean.getTitle(),adBean.getId(),type);

        if("1".equals(adBean.getLink())){
            ToastUtil.customToastGravity(getActivity(),"敬请期待",2, Gravity.CENTER,0,0);
            return;
        }
        startWebViewRequestLink(adBean.getLink());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.top_1)
    public void top_1(){
        //TODO 大模块1
        if(adTop1BeanList != null && adTop1BeanList.size() > 0){
            AdBean adBean = adTop1BeanList.get(0);
            //TODO 2018.11.7 之前的逻辑走后台 -- 发现广告link为空是，后台统计不到点击事件
//            addSqlAndToWeb(adBean,Integer.parseInt(adBean.getShow_position()));
//            MobclickAgent.onEvent(getActivity(), "ad1");//参数二为当前统计的事件ID

            //TODO 2018.11.7 新增逻辑 统计都走友盟
            MobclickAgent.onEvent(getActivity(), "NavigationModel");//大模块点击事件
            KLog.d("NavigationModel");
            if(!TextUtils.isEmpty(adBean.getType()) && adBean.getType().equals("0")){//广告
                map.put("ad_id",adBean.getId());
                MobclickAgent.onEvent(mContext, "NavigationAd", map);//广告点击事件
                addAdUserClick(adBean.getId(),"NavigationAdUserNum");//广告用户点击数
            }
            addModleUserClick(adBean.getShow_position());//模块用户点击数
            KLog.d("ModleUserClick");

            if("1".equals(adBean.getLink())){
                ToastUtil.customToastGravity(getActivity(),"敬请期待",2, Gravity.CENTER,0,0);
                return;
            }
            startWebViewRequestLink(adBean.getLink());

            //TODO 11.12 测试环境测试唤醒
//            if (hasApplication()) {
//                Intent action = new Intent(Intent.ACTION_VIEW);
//                StringBuilder builder = new StringBuilder();
//                builder.append("vipshop://goHome?tra_from=tra%3AC01V4kcilzug74hn%3A%3A29hocf0v%3Along9%3Ac074d74d02034c13a2353064b7ff6500\n");
//                action.setData(Uri.parse(builder.toString()));
//                startActivity(action);
//            }else{
//                KLog.d("去安装唯品会app");
//            }

        }
    }


    /**
     * 判断是否安装了应用
     * @return true 为已经安装
     */
    private boolean hasApplication() {
        PackageManager manager = mContext.getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("vipshop://"));
        List list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }

    @OnClick(R.id.top_2)
    public void top_2(){
        if(adTop1BeanList != null && adTop1BeanList.size() > 1){
            AdBean adBean = adTop1BeanList.get(1);
//            addSqlAndToWeb(adBean,Integer.parseInt(adBean.getShow_position()));
//            MobclickAgent.onEvent(getActivity(), "ad2");//参数二为当前统计的事件ID

            //TODO 2018.11.7 新增逻辑 统计都走友盟
            MobclickAgent.onEvent(getActivity(), "NavigationModel");//大模块点击事件
            if(!TextUtils.isEmpty(adBean.getType()) && adBean.getType().equals("0")){//广告
                map.put("ad_id",adBean.getId());
                MobclickAgent.onEvent(mContext, "NavigationAd", map);//广告点击事件
                addAdUserClick(adBean.getId(),"NavigationAdUserNum");
            }
            addModleUserClick(adBean.getShow_position());//模块用户点击数

            if("1".equals(adBean.getLink())){
                ToastUtil.customToastGravity(getActivity(),"敬请期待",2, Gravity.CENTER,0,0);
                return;
            }
            startWebViewRequestLink(adBean.getLink());
        }

    }

    @OnClick(R.id.top_3)
    public void top_3(){
        if(adTop1BeanList != null && adTop1BeanList.size() > 2){
            AdBean adBean = adTop1BeanList.get(2);
//            addSqlAndToWeb(adBean,Integer.parseInt(adBean.getShow_position()));
//            MobclickAgent.onEvent(getActivity(), "ad3");//参数二为当前统计的事件ID

            //TODO 2018.11.7 新增逻辑 统计都走友盟
            MobclickAgent.onEvent(getActivity(), "NavigationModel");//大模块点击事件
            if(!TextUtils.isEmpty(adBean.getType()) && adBean.getType().equals("0")){//广告
                map.put("ad_id",adBean.getId());
                MobclickAgent.onEvent(mContext, "NavigationAd", map);//广告点击事件
                addAdUserClick(adBean.getId(),"NavigationAdUserNum");
            }
            addModleUserClick(adBean.getShow_position());//模块用户点击数

            if("1".equals(adBean.getLink())){
                ToastUtil.customToastGravity(getActivity(),"敬请期待",2, Gravity.CENTER,0,0);
                return;
            }
            startWebViewRequestLink(adBean.getLink());
        }

    }

    @OnClick(R.id.top_4)
    public void top_4(){
        if(adTop1BeanList != null && adTop1BeanList.size() > 3){
            AdBean adBean = adTop1BeanList.get(3);
//            addSqlAndToWeb(adBean,Integer.parseInt(adBean.getShow_position()));
//            MobclickAgent.onEvent(getActivity(), "ad4");//参数二为当前统计的事件ID

            //TODO 2018.11.7 新增逻辑 统计都走友盟
            MobclickAgent.onEvent(getActivity(), "NavigationModel");//大模块点击事件
            if(!TextUtils.isEmpty(adBean.getType()) && adBean.getType().equals("0")){//广告
                map.put("ad_id",adBean.getId());
                MobclickAgent.onEvent(mContext, "NavigationAd", map);//广告点击事件
                addAdUserClick(adBean.getId(),"NavigationAdUserNum");
            }
            addModleUserClick(adBean.getShow_position());//模块用户点击数

            if("1".equals(adBean.getLink())){
                ToastUtil.customToastGravity(getActivity(),"敬请期待",2, Gravity.CENTER,0,0);
                return;
            }
            startWebViewRequestLink(adBean.getLink());
        }

    }

    @OnClick(R.id.top_5)
    public void top_5(){
      if(adTop1BeanList != null && adTop1BeanList.size() > 4){
          AdBean adBean = adTop1BeanList.get(4);
//          addSqlAndToWeb(adBean,Integer.parseInt(adBean.getShow_position()));
//          MobclickAgent.onEvent(getActivity(), "ad5");//参数二为当前统计的事件ID

          //TODO 2018.11.7 新增逻辑 统计都走友盟
          MobclickAgent.onEvent(getActivity(), "NavigationModel");//大模块点击事件
          if(!TextUtils.isEmpty(adBean.getType()) && adBean.getType().equals("0")){//广告
              map.put("ad_id",adBean.getId());
              MobclickAgent.onEvent(mContext, "NavigationAd", map);//广告点击事件
              addAdUserClick(adBean.getId(),"NavigationAdUserNum");
          }
          addModleUserClick(adBean.getShow_position());//模块用户点击数


          if("1".equals(adBean.getLink())){
              ToastUtil.customToastGravity(getActivity(),"敬请期待",2, Gravity.CENTER,0,0);
              return;
          }
          startWebViewRequestLink(adBean.getLink());

        }
    }



    private void changeOriData() {
        //第一部分
        getAdData1();
        //第二部分
        getAdData2_2();
        //第三部分
        getAdData3();
        //第四部分
        getAdData4();
        initIndicator();
    }


    public static Index_Fragment getInstance(){
        return new Index_Fragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.index_big_fragment;
    }

    @Override
    protected void initData() {
        initCoolRecycleView();
        refreshAdData();
    }


    private void getAdData1() {
        if(adTop1BeanList != null && !adTop1BeanList.isEmpty()){
            if(adTop1BeanList.get(0) != null){
                Glide.with(mContext)
                        .load(adTop1BeanList.get(0).getImg_url())
                        .into(top_1_pic);
                index_f1_text.setText(adTop1BeanList.get(0).getTitle());
            }
            if(adTop1BeanList.get(1) != null){
                Glide.with(mContext)
                        .load(adTop1BeanList.get(1).getImg_url())
                        .into(top_2_pic);
                index_f2_text.setText(adTop1BeanList.get(1).getTitle());
            }
            if(adTop1BeanList.get(2) != null){
                Glide.with(mContext)
                        .load(adTop1BeanList.get(2).getImg_url())
                        .into(top_3_pic);
                index_f3_text.setText(adTop1BeanList.get(2).getTitle());
            }
            if(adTop1BeanList.get(3) != null){
                Glide.with(mContext)
                        .load(adTop1BeanList.get(3).getImg_url())
                        .into(top_4_pic);
                index_f4_text.setText(adTop1BeanList.get(3).getTitle());
            }
            if(adTop1BeanList.get(4) != null){
                Glide.with(mContext)
                        .load(adTop1BeanList.get(4).getImg_url())
                        .into(top_5_pic);
                index_f5_text.setText(adTop1BeanList.get(4).getTitle());
            }
        }
    }

    private void getAdData2() {

        //总的页数，取整（这里有三种类型：Math.ceil(3.5)=4:向上取整，只要有小数都+1  Math.floor(3.5)=3：向下取整  Math.round(3.5)=4:四舍五入）
        totalPage = (int) Math.ceil(adTop2BeanList.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<>();

        for(int i=0;i<totalPage;i++){
            //每个页面都是inflate出一个新实例
            gridView = (GridView) LayoutInflater.from(getActivity()).inflate(R.layout.index_tag2_item,mViewPagerGridView,false);
            mIndexGridViewAdapter = new IndexGridViewAdapter(getActivity(),adTop2BeanList,i,mPageSize);
            gridView.setAdapter(mIndexGridViewAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    KLog.d("大模块2 ：" + adTop2BeanList.get(position).getTitle() + " ");

                    AdBean adBean = adTop2BeanList.get(position);

                    MobclickAgent.onEvent(getActivity(), "CoolSiteModel");//大模块点击事件
                    if(!TextUtils.isEmpty(adBean.getType()) && adBean.getType().equals("0")){//广告
                        map.put("ad_id",adBean.getId());
                        MobclickAgent.onEvent(mContext, "CoolSiteAd", map);//广告点击事件
                        addAdUserClick(adBean.getId(),"CoolSiteAdUserNum");
                    }
                    addModleUserClick(adBean.getShow_position());//模块用户点击数

                    if("1".equals(adBean.getLink())){
                        ToastUtil.customToastGravity(getActivity(),"敬请期待",2, Gravity.CENTER,0,0);
                        return;
                    }
                    startWebViewRequestLink(adBean.getLink());

//                    addSqlAndToWeb(adBean,Integer.parseInt(adBean.getShow_position()));
//                    MobclickAgent.onEvent(getActivity(), "cool_ad" + position + 1);//参数二为当前统计的事件ID

                }
            });
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(gridView);
        }

        //设置ViewPager适配器
        mViewPagerGridView.setAdapter(new IndexViewPagerAdapter(viewPagerList));
    }

    private void getAdData3() {
        list_Title.clear();
        for (int i = 0; i < adTop3BeanList.size(); i++) {
            list_Title.add(adTop3BeanList.get(i).getTitle());
        }
    }

    private void getAdData4() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.getInstance());
        fragments.add(CustomerFragment.getInstance());
        fragments.add(MessageFragment.getInstance());
        fragments.add(MyFragment.getInstance());

        pagerAdapter = new HomeFragmentAdapter(getChildFragmentManager(),fragments,list_Title);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(3);//ViewPager设置预加载页面的个数方法,防止销毁
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {}

            @Override
            public void onPageSelected(int position) {
                //TODO 大模块3
                AdBean adBean = adTop3BeanList.get(position);

                MobclickAgent.onEvent(getActivity(), "TabModel");//大模块点击事件
                if(!TextUtils.isEmpty(adBean.getType()) && adBean.getType().equals("0")){//广告
                    map.put("ad_id",adBean.getId());
                    MobclickAgent.onEvent(mContext, "TabAd", map);//广告点击事件
                    addAdUserClick(adBean.getId(),"TabAdUserNum");
                }
                addModleUserClick(adBean.getShow_position());//模块用户点击数

//                addClickCountToSql(adBean.getTitle(),adBean.getId(),Integer.parseInt(adBean.getShow_position()));
//                MobclickAgent.onEvent(getActivity(), "card_ad" + position + 1);//参数二为当前统计的事件ID
                //TODO 第三部分设置sp，当第三个按键触发时，对应相应的fragment
                SPHelper.put(getActivity(),"home_fragment_posotion",position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {}
        });


    }

    private void initIndicator() {
        tabLayout_5.setViewPager(mViewPager);
        tabLayout_5.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }


    @OnClick(R.id.search_text)
    public void Opensearch(){
        if(StringUtils.isFastClick()){
            return;
        }
        startActivityForResult(new Intent(getActivity(),SearchActivity.class),100);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100){
            if (data != null){

                String content = data.getStringExtra("content");
//                KLog.d("返回给fragment的内容是：" + content);
                //TODO 显示布局，加载内容
                 part1.setClickable(true);
                 startWebViewRequest(content);

            }
        }else if(resultCode == 200){
            if(data != null){
                String content = data.getStringExtra("content");
                startWebViewRequestNoClean(content);
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if(webview != null){
            webview.onPause();
            webview.pauseTimers();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        sendDataToUm();
        //TODO 界面展示，则展示次数加1
//        addToSql(adTop1BeanList,adTop2BeanList,adTop3BeanList);

        if(webview != null){
            webview.resumeTimers();
            webview.onResume();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(webview != null){
            webview.destroy();
            webview.removeAllViews();
            webview = null;
        }
    }

    private void endWebView() {
        center_title.setText("");
        if (webview != null) {
            webview.stopLoading();

//            webview.clearHistory();
//            webview.clearCache(true);
//            webview.loadUrl("about:blank");
//            webview.clearFormData();
//            webview.clearSslPreferences();
//            webview.clearMatches();
//            webview.pauseTimers();
//            webview.removeAllViews();
//            webview.clearView();
//
//            //清空所有Cookie
//            CookieSyncManager.createInstance(AppUtil.getContext());  //Create a singleton CookieSyncManager within a context
//            CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
//            cookieManager.removeAllCookie();// Removes all cookies.
//            CookieSyncManager.getInstance().sync(); // forces sync manager to sync now

//            webview.setWebChromeClient(null);
//            webview.setWebViewClient(null);
//            webview.getSettings().setJavaScriptEnabled(true);
//            webview.clearCache(true);
            webview.clearFormData();
//            webview.clearMatches();
//            webview.pauseTimers();
            webview.clearHistory();
            webview.clearView();
            webview.destroy();
            webview = null;
            webView_addpart.removeAllViews();
        }
    }


    private void startWebViewRequestLink(String urlLink) {
        endWebView();
        webview = new WebView(getActivity());
        webview.setId(R.id.webview);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        webView_addpart.addView(webview,lp);
        setSetting();
        initWebViewClient();
        initWebChromeClient();
        webview.loadUrl(urlLink);
        webView_ll.setVisibility(View.VISIBLE);
    }


    private void startWebViewRequestNoClean(String content) {
        urlLogin(content);
    }

    private void startWebViewRequest(String content) {
//        KLog.d("搜索返回的内容是：" + content);
        endWebView();
        webview = new WebView(AppUtil.getContext());
        webview.setId(R.id.webview);
        webView_addpart.addView(webview);
        setSetting();
        initWebViewClient();
        initWebChromeClient();
        urlLogin(content);
        webView_ll.setVisibility(View.VISIBLE);
    }

    private void urlLogin(String url) {
        //如果输入的url包含协议地址
        if (url.length() >= 4 && url.substring(0, 4).equals("http")) {
            if (StringUtils.isUrl(url)) {
               //加载
                webview.loadUrl(url);
            } else {
              //通过百度搜索关键字
                url = ApiConstants.SOUGOU + "web/sl?keyword=" + url;
                webview.loadUrl(url);
            }
        } else {//如果输入的url不包含协议地址
            String url1 = "http://" + url;
            String url2 = "https://" + url;
            if (StringUtils.isUrl(url1)) {
                //加载
                url1 = ApiConstants.SOUGOU + "web/sl?keyword=" + url;
                webview.loadUrl(url1);
            } else if (StringUtils.isUrl(url2)) {
                //加载
                webview.loadUrl(url2);
            } else {
                //通过百度搜索关键字
//                url = ApiConstants.BAIDUURL + "s?wd=" + url;//URL是根据使用百度搜索某个关键字得到的url截取得到的
                url = ApiConstants.SOUGOU + "web/sl?keyword=" + url;//URL是根据使用百度搜索某个关键字得到的url截取得到的
                webview.loadUrl(url);
            }
        }
    }

    WebSettings webSettings;
    @SuppressLint({"NewApi"})
    private void setSetting() {
        webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webSettings.setSupportZoom(true); //支持屏幕缩放
        webSettings.setBuiltInZoomControls(true);
        //设置是否允许通过 file url 加载的 Javascript 可以访问其他的源(包括http、https等源)
        webview.getSettings().setAllowUniversalAccessFromFileURLs(false);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webview.getSettings().setBlockNetworkImage(false); // 解决图片不显示
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //其他细节操作
        webSettings.setDatabaseEnabled(true);
        String dir = getActivity().getApplicationContext().getDir("database",Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDomStorageEnabled(true);//支持DOM API

        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式


        //一般情况是由于协议不同引起的，添加下面的设置，如果不是HTTP或HTTPS协议则由浏览器进行解读
//        webview.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:54.0) Gecko/20100101 Firefox/54.0");
//        webview.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
        registerForContextMenu(webview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置当一个安全站点企图加载来自一个不安全站点资源时WebView的行为,
            // 在这种模式下,WebView将允许一个安全的起源从其他来源加载内容，即使那是不安全的.
            // 如果app需要安全性比较高，不应该设置此模式
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//解决app中部分页面非https导致的问题
        }
        //TODO 下载
        webview.setDownloadListener(new MyWebViewDownLoadListener());
    }


    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                    ContextMenu.ContextMenuInfo contextMenuInfo) {
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
        final WebView.HitTestResult webViewHitTestResult = webview.getHitTestResult();
        if (webViewHitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                webViewHitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            contextMenu.setHeaderTitle("网页中下载图片");
            contextMenu.add(0, 1, 0, "点击保存")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            //DownloadImageURL 是在网页中图片的路径
                            String DownloadImageURL = webViewHitTestResult.getExtra();
                            if (URLUtil.isValidUrl(DownloadImageURL)) {
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownloadImageURL));
                                request.allowScanningByMediaScanner();
                                //设置图片的保存路径
                                request.setDestinationInExternalPublicDir("SmallBrowse/Pic/",System.currentTimeMillis() + "." + "png");
                                DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                                downloadManager.enqueue(request);
                                Toast.makeText(getActivity(), "图片保存到" + "/SmallBrowse/Pic/目录下", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "下载失败", Toast.LENGTH_LONG).show();
                            }
                            return false;
                        }
                    });
        }
    }


    @BindView(R.id.part3)
    LinearLayout part3;

    @BindView(R.id.edit_title)
    EditText edit_title;


      //显示输入框的地址
    @OnClick(R.id.part1)
    public void part1(){

        String myUrl = "";
        if(!TextUtils.isEmpty(currentUrl)){
            myUrl= currentUrl;
        }else if(!TextUtils.isEmpty(currentTitle)){
            myUrl = currentTitle;
        }

        if(!TextUtils.isEmpty(myUrl)){
            Intent intent = new Intent(getActivity(), WebViewGetUrlActivity.class);
            intent.putExtra("weburl",myUrl);
            startActivityForResult(intent,200);
        }
    }


    private void initWebViewClient() {
        //复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        webview.setWebViewClient(new WebViewClient(){
            boolean if_load;
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if_load = false;
                currentUrl = url;
//                view.loadUrl(url);//在这里设置对应的操作
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();// 接受所有网站的证书
                //super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if_load = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(if_load ){
//               KLog.d("url: " + view.getUrl() + " 标题："  + view.getTitle() + " 原始链接： " + view.getOriginalUrl());

                    if(webview.getSettings().getCacheMode() == WebSettings.LOAD_DEFAULT){

                        if(null != view.copyBackForwardList().getCurrentItem()){
                            saveTitle = view.copyBackForwardList().getCurrentItem().getTitle();
                            saveUrl = view.copyBackForwardList().getCurrentItem().getUrl();

                            if(!TextUtils.isEmpty(saveTitle) &&
                                    !TextUtils.isEmpty(saveUrl)){
                                //TODO 添加到数据库
                                saveToHistorySql(saveTitle,saveUrl);
                            }
                        }
                        if_load = false;
                    }
                }
             }

        });
    }

    private void saveToHistorySql(String saveTitle, String saveUrl) {
        if(!TextUtils.isEmpty(saveTitle) && !TextUtils.isEmpty(saveUrl)){
            HistoryData historyData = new HistoryData();
            historyData.setVisit_link(saveUrl);
            historyData.setTitle(saveTitle);
            historyData.setKeyword(saveTitle);
            historyData.setType("1");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
            //获取当前时间
            Date date = new Date(System.currentTimeMillis());
            historyData.setCurrentTime(simpleDateFormat.format(date));
            dbManager.insertHistrory(historyData);
        }
    }

    private void saveToBookMarkSql(String saveTitle, String saveUrl) {
        if(!TextUtils.isEmpty(saveTitle) && !TextUtils.isEmpty(saveUrl)){
            BookMarkData bookMarkData = new BookMarkData();
            bookMarkData.setVisit_link(saveUrl);
            bookMarkData.setTitle(saveTitle);
            simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
            date = new Date(System.currentTimeMillis());//当前时间
            KLog.d("Date获取当前日期时间"+ simpleDateFormat.format(date));
            bookMarkData.setCurrentTime(simpleDateFormat.format(date));
            dbManager.insertBookMark(bookMarkData);
        }
    }


    private void initWebChromeClient() {
        //获取网页进度
        webview.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100){
                    pg.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    pg.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pg.setProgress(newProgress);//设置进度值
                }
                super.onProgressChanged(view, newProgress);
            }

            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                currentTitle = title;
                center_title.setText(title);
                currentUrl = view.getUrl();
            }

        });
    }





}
