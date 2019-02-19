package com.ngbj.browse.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ngbj.browse.R;
import com.ngbj.browse.activity.WebViewHao123Activity;
import com.ngbj.browse.bean.AdBean;
import com.ngbj.browse.bean.BigModelCountData;
import com.ngbj.browse.bean.CountData;
import com.ngbj.browse.bean.HistoryData;
import com.ngbj.browse.bean.ModelBean;
import com.ngbj.browse.bean.NewsBean;
import com.ngbj.browse.bean.NewsSaveMultiBean;
import com.ngbj.browse.bean.StatisticsBean;
import com.ngbj.browse.bean.WeatherBean;
import com.ngbj.browse.bean.WeatherSaveBean;
import com.ngbj.browse.constant.ApiConstants;
import com.ngbj.browse.db.DBManager;
import com.ngbj.browse.event.DataToTopEvent;
import com.ngbj.browse.receiver.DownloadCompleteReceiver;
import com.ngbj.browse.util.SPHelper;
import com.ngbj.browse.util.StringUtils;
import com.ngbj.browse.util.ToastUtil;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Date:2018/8/20
 * author:zl
 * 备注：基类的Fragment
 *       1.懒加载 -- 在状态可见 + 控件初始化完成
 */
public abstract class BaseFragment extends Fragment{

    protected static int myType;//哪个fragment

    protected boolean isVisible;//fragment是否可见
    protected boolean isPrepared;//控件初始化完成
    protected boolean isFirst = true;//确保ViewPager来回切换时BaseFragment的lazyLoadData方法不会被重复调用
    protected View mRootView;//加载的View
    protected Context mContext;//上下文

    protected DBManager dbManager;
    CountData countData;
    BigModelCountData bigModelCountData;

    boolean isNetwork;
    WeatherBean weatherBean;
    String locationName;

    private Unbinder unbinder;
    SimpleDateFormat simpleDateFormat;
    Date date;
    HashMap<String,String> map = new HashMap<>();


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            isVisible = true;
            onVisible();
        }else{
            isVisible = false;
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(null == mRootView){
          mRootView = LayoutInflater.from(mContext).inflate(getLayoutId(),container,false);
        }
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    /**
     * onDestroyView中进行解绑操作
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO 和风天C气
        HeConfig.init("HE1809290936241272","ee990762134744d9922aa4b7b2b2df1e");
        dbManager = DBManager.getInstance(getActivity());
        locationName = (String) SPHelper.get(getActivity(),"location","上海");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

    /** 初始化控件方法 */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVidget();
        initData();
        finishCreateView();
    }

    protected  void initData(){}


    /** 当状态和控件都可见时才加载数据 */
    private void finishCreateView() {
        isPrepared = true;
        lazyLoad();
    }

    /** 对各种控件进行设置、适配、填充数据 */
    protected void initVidget() {}

    protected abstract int getLayoutId();

    private void onVisible() {
        lazyLoad();
    }

    /**  懒加载方法 需要状态可见 + 控件初始化完成 */
    private void lazyLoad() {
        if(!isVisible || !isPrepared || !isFirst) return;
        lazyLoadData();
        isPrepared = false;
        isFirst = false;
    }

    /**  加载数据 */
    protected void lazyLoadData() {}


    //TODO 点击广告次数进表 adName为了自己方便看
    //type 是大模块的标志 1.功能导航 2.酷站导航 3.选项卡 4.热搜
    public void addClickCountToSql(String adName,String adId,int model_id){
        KLog.d("adName :"  + adName);
        countData = dbManager.queryUser(adId);
        if(!TextUtils.isEmpty(countData.getType())  && countData.getType().equals("0")){
            if(null == countData){
                countData = new CountData(adId,1,0, 1,  adName);
                dbManager.insertUser(countData);
            }else {//更新多点东西
                countData.setClick_num(countData.getClick_num() + 1);
                countData.setClick_user_num(1);
                dbManager.updateUser(countData);
            }
            KLog.d("广告: " + countData.getAdShowName() + " 的点击次数是：" + countData.getClick_num());
            //一个用户下点击每个广告都设置为1
            SPHelper.put(getActivity(),"click_user_num",1);
        }

        //TODO 大模块
        addBigModelClickCountToSql(model_id);
    }

    //TODO 点击模块进表 通过模块的名字找到具体的bean  -- 名字也用model_id
    protected void addBigModelClickCountToSql(int model_id) {
        bigModelCountData = dbManager.queryBigModel(model_id);
        if(null == bigModelCountData){
            bigModelCountData = new BigModelCountData(model_id,1,model_id + "",   model_id);
            bigModelCountData.setClick_user_num(1);
            dbManager.insertBigModel(bigModelCountData);
        }else {
            bigModelCountData.setClick_num(bigModelCountData.getClick_num() + 1);
            bigModelCountData.setClick_user_num(1);
            dbManager.updateBigModel(bigModelCountData);
        }

        KLog.d("大模块:" + bigModelCountData.getBigModelShowName()
                + " 的点击次数是:：" + bigModelCountData.getClick_num()
                + " 类型是：" + bigModelCountData.getType());
    }


    //跳转hao123网址
//    protected void startToHtml() {
//        Intent intent = new Intent(getActivity(), WebViewHao123Activity.class);
//        String url = ApiConstants.HAOURL ;//URL是根据使用百度搜索某个关键字得到的url截取得到的
//        intent.putExtra("url",url);
//        intent.putExtra("type","1");
//        startActivity(intent);
//    }


    protected class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            KLog.d("MyWebViewDownLoadListener");
            // 动态广播使用
            DownloadCompleteReceiver receiver = new DownloadCompleteReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            getActivity().registerReceiver(receiver, intentFilter);
            if(StringUtils.isFastClick()){
                return;
            }
            ToastUtil.showShort(getActivity(),"正在下载...");
            downloadBySystem(url,contentDisposition,mimetype);
        }

    }

    @SuppressLint("NewApi")
    protected void downloadBySystem(String url, String contentDisposition, String mimeType) {
        // 指定下载地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
        request.allowScanningByMediaScanner();
        // 设置通知的显示类型，下载进行时和完成后显示通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // 设置通知栏的标题，如果不设置，默认使用文件名
        request.setTitle("下载标题");
        // 设置通知栏的描述
        request.setDescription("下载内容");
        // 允许在计费流量下下载
        request.setAllowedOverMetered(false);
        // 允许该记录在下载管理界面可见
        request.setVisibleInDownloadsUi(false);
        // 允许漫游时下载
        request.setAllowedOverRoaming(true);
        // 允许下载的网路类型
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        // 设置下载文件保存的路径和文件名
        String fileName  = URLUtil.guessFileName(url, contentDisposition, mimeType);
//        String fileName  = contentDisposition.replaceFirst(
//                "attachment; filename=", "");
        KLog.debug("下载文件的文件名:  ", fileName);
        //系统下载在公共目录  /storage/emulated/0/Download
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//        另外可选一下方法，自定义下载路径

//        request.setDestinationInExternalPublicDir("SmallBrowse","/File/" + System.currentTimeMillis() + ".png");
//

//        File file = new File(SDCardHelper.getSDCardBaseDir() + "/"+ "SmallBrowse");
//        request.setDestinationUri(Uri.fromFile(file));
//        request.setDestinationInExternalFilesDir(mContext,file.getAbsolutePath(),fileName);
        final DownloadManager downloadManager = (DownloadManager)getActivity(). getSystemService(DOWNLOAD_SERVICE);
        // 添加一个下载任务
        long downloadId = downloadManager.enqueue(request);
        KLog.debug("downloadId:", downloadId);
    }



    protected List<NewsSaveMultiBean> transToList(NewsBean newsBean) {
        List<NewsSaveMultiBean> saveBeanList = new ArrayList<>();
        NewsSaveMultiBean newsSaveMultiBean;
        for (NewsBean.ReturnDataBean.ComListBean comListBean : newsBean.getReturn_data().getCom_list()) {
            newsSaveMultiBean = new NewsSaveMultiBean();
            newsSaveMultiBean.setAuthor(comListBean.getAuthor());
            newsSaveMultiBean.setH5url(comListBean.getH5url());
            newsSaveMultiBean.setTitle(comListBean.getTitle());
            newsSaveMultiBean.setPubtime(comListBean.getPubtime());
            newsSaveMultiBean.setShow_type(comListBean.getShow_type());
            newsSaveMultiBean.setShow_img(comListBean.getShow_img());
            newsSaveMultiBean.setItemType(Integer.parseInt(comListBean.getShow_type()));
            saveBeanList.add(newsSaveMultiBean);
        }
        return saveBeanList;
    }

    protected void saveToSql(NewsSaveMultiBean newsSaveBean) {

        HistoryData historyData = new HistoryData();
        historyData.setVisit_link(newsSaveBean.getH5url());
        historyData.setKeyword("");
        historyData.setTitle(newsSaveBean.getTitle());
        historyData.setType("0");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        KLog.d("Date获取当前日期时间"+ simpleDateFormat.format(date));
        historyData.setCurrentTime(simpleDateFormat.format(date));
        dbManager.insertHistrory(historyData);
    }




    public static int getParentFragmentToType(Fragment fragment) {
        int myType = 1;
        Fragment parentFragment = fragment.getParentFragment();
        if(parentFragment != null && parentFragment instanceof Index_Fragment){
            KLog.d("我是第一个Fragment");
            myType = 1;
        }else if(parentFragment != null && parentFragment instanceof Index_Fragment_2){
            KLog.d("我是第二个Fragment");
            myType = 2;
        }else if(parentFragment != null && parentFragment instanceof Index_Fragment_3){
            myType = 3;
        }else if(parentFragment != null && parentFragment instanceof Index_Fragment_4){
            myType = 4;
        }
        return myType;
    }




    public void addAdUserClick(String adId,String adModelName){
        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        date = new Date(System.currentTimeMillis());  //获取当前时间
        String currentYear_Month_Day = simpleDateFormat.format(date);
        KLog.d("当前年月日:",currentYear_Month_Day);//20181109


        String str2 = (String) SPHelper.get(getActivity(),"last_today_time","");
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
                statisticsBean.setIs_clicked(true);
                statisticsBean.setDate(currentYear_Month_Day);
                dbManager.updateAdUser(statisticsBean);
                //更新xml
                SPHelper.put(getActivity(),"last_today_time",currentYear_Month_Day);
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

        String str2 = (String) SPHelper.get(getActivity(),"last_today_time","");
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
                SPHelper.put(getActivity(),"last_today_time",currentYear_Month_Day);

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
