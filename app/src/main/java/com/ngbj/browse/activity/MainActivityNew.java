package com.ngbj.browse.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.browse.MyApplication;
import com.ngbj.browse.R;
import com.ngbj.browse.adpter.NewWindowAdapter;
import com.ngbj.browse.bean.NewWindowBean;
import com.ngbj.browse.bean.NewsBean;
import com.ngbj.browse.bean.WeatherBean;
import com.ngbj.browse.dialog.BottomAlertDialog;
import com.ngbj.browse.dialog.DeleteAlertDialog;
import com.ngbj.browse.dialog.IosAlertDialog;
import com.ngbj.browse.event.ChangeFragmentEvent;
import com.ngbj.browse.event.CollectEvent;
import com.ngbj.browse.event.CollectHomeEvent;
import com.ngbj.browse.event.DataToTopEvent;
import com.ngbj.browse.event.RefreshDataEvent;
import com.ngbj.browse.event.RefreshHomeDataEvent;
import com.ngbj.browse.event.TypeEvent;
import com.ngbj.browse.fragment.Index_Fragment;
import com.ngbj.browse.fragment.Index_Fragment_2;
import com.ngbj.browse.fragment.Index_Fragment_3;
import com.ngbj.browse.fragment.Index_Fragment_4;
import com.ngbj.browse.mvp.contract.app.HomeContract;
import com.ngbj.browse.mvp.presenter.app.HomePresenter;
import com.ngbj.browse.service.MyJobService;
import com.ngbj.browse.util.AppUpdateUtil;
import com.ngbj.browse.util.AppUpdateUtilByN;
import com.ngbj.browse.util.AppUtil;
import com.ngbj.browse.util.BitmapHelper;
import com.ngbj.browse.util.SDCardHelper;
import com.ngbj.browse.util.SPHelper;
import com.ngbj.browse.util.ScreenHelper;
import com.ngbj.browse.util.StringUtils;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivityNew extends BaseMainActivity<HomePresenter> implements HomeContract.View {

    @BindView(R.id.window_count)
    TextView window_count;

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    @BindView(R.id.new_window_ll)
    LinearLayout new_window_ll;


    Index_Fragment index_fragment;
    Index_Fragment_2 index_fragment2;
    Index_Fragment_3 index_fragment_3;
    Index_Fragment_4 index_fragment_4;
    Fragment currentFragment;
    String type;//当前类型

    private boolean mReturningWithResult = false;
    int resultCode1;//返回码 200:搜索界面返回
    private long mExitTime; //退出时的时间

    NewWindowAdapter newWindowAdapter;
    List<NewWindowBean> newWindowBeanList = new ArrayList<>();

    LinearLayout ll;//fragment中webview布局
    WebView webview;//fragment中webview控件

    String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
            ,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
    List<String> mPermissionList = new ArrayList<>();
    private final int mRequestCode = 100;//权限请求码


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_new;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {//6.0才用动态权限
            initPermission();
        }
    }

    //权限判断和申请
    private void initPermission() {
        mPermissionList.clear();//清空没有通过的权限
        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i])
                    != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }

        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        }
    }


    //请求权限后回调的方法
    //参数： requestCode  是我们自己定义的权限请求码
    //参数： permissions  是我们请求的权限名称数组
    //参数： grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (mRequestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat
                            .shouldShowRequestPermissionRationale(MainActivityNew.this, permissions[i]);
                    KLog.d(showRequestPermission);
                }
            }
            //如果有权限没有被允许 -- 别人的逻辑
//            if (hasPermissionDismiss) {
//                showPermissionDialog();//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
//                KLog.d("what?");
//            }else{
//                //全部权限通过，可以进行下一步操作。。。
//                goToNextPage();
//            }
        }

    }



    public void toOutLinkForDefaultBrowser(Intent intent){
        //获取到数据
        String webUri;//外部传过来的数据
        Uri uri = intent.getData();
        if(null != uri) {
//            KLog.d("uri: " + uri);
            if (!TextUtils.isEmpty(uri.toString())) {
                webUri = uri.toString();
                Intent intent2 = new Intent(MainActivityNew.this, WebViewHao123Activity.class);
                intent2.putExtra("url", webUri);
                startActivity(intent2);

            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //TODO 二次
        toOutLinkForDefaultBrowser(intent);
    }

    private void doService() {
        JobScheduler jobScheduler = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(this, MyJobService.class));  //指定哪个JobService执行操作
            builder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS); //执行的最小延迟时间
            builder.setOverrideDeadline(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);  //执行的最长延时时间
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NOT_ROAMING);  //非漫游网络状态
            builder.setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS, JobInfo.BACKOFF_POLICY_LINEAR);//线性重试方案
            builder.setRequiresCharging(false); // 未充电状态
            jobScheduler.schedule(builder.build());
        }
    }

    //启动服务
    public void bindLife(){
        doService();
    }

    @Override
    protected void initDatas() {

        //TODO 一次
        toOutLinkForDefaultBrowser(getIntent());
        //TODO
        bindLife();

        //第一次初始化首页默认显示第一个fragment
        initFragment1();
        initRecycleView();
        initEvent();
        EventBus.getDefault().register(this);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//               boolean isg =  hasMyDefault(MainActivityNew.this);
//               Message message = new Message();
//               message.what = 100;
//               message.obj = isg;
//               handler.sendMessage(message);
//
//            }
//        }).start();
    }






    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    //首页主请求刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshHomeDataEvent(RefreshHomeDataEvent event) {
        //获取当前Fragment
        if(currentFragment != null && currentFragment instanceof Index_Fragment){
            EventBus.getDefault().post(new RefreshDataEvent(0));
        }else if(currentFragment != null && currentFragment instanceof Index_Fragment_2){
            EventBus.getDefault().post(new RefreshDataEvent(1));
        }else if(currentFragment != null && currentFragment instanceof Index_Fragment_3){
            EventBus.getDefault().post(new RefreshDataEvent(2));
        }else if(currentFragment != null && currentFragment instanceof Index_Fragment_4){
            EventBus.getDefault().post(new RefreshDataEvent(3));
        }
    }

    //首页收藏中转站
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCollectHomeEvent(CollectHomeEvent event) {
        if(currentFragment != null && currentFragment instanceof Index_Fragment){
            EventBus.getDefault().post(new CollectEvent(0));
        }else if(currentFragment != null && currentFragment instanceof Index_Fragment_2){
            EventBus.getDefault().post(new CollectEvent(1));
        }else if(currentFragment != null && currentFragment instanceof Index_Fragment_3){
            EventBus.getDefault().post(new CollectEvent(2));
        }else if(currentFragment != null && currentFragment instanceof Index_Fragment_4){
            EventBus.getDefault().post(new CollectEvent(3));
        }
    }

    //默认的Fragment
    private void initFragment1() {
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(index_fragment == null){
            index_fragment = Index_Fragment.getInstance();
            transaction.add(R.id.frameLayout, index_fragment);
        }
        currentFragment = index_fragment;
        //默认赋值
        window_count.setText("1");
        //提交事务
        transaction.commit();
    }

     //展示Fragment
      private void showFragment(Fragment fragment) {
            if (currentFragment != fragment) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.hide(currentFragment);
                     currentFragment = fragment;
                     if (!fragment.isAdded()) {
                             transaction.add(R.id.frameLayout, fragment).show(fragment).commit();
                         } else {
                             transaction.show(fragment).commit();
                         }
                 }
             }

    @Override
    protected void initInject() {
//        String s1 = "1.0.1";
//        String s2 = "1.0";
//       if(  s1.compareTo(s2) > 0){//t1 > t2
//           KLog.d("更新");
//       }
      //TODO 检测更新
      String myVersion = AppUtil.getVersionName(this);
      String lastVersion = (String) SPHelper.get(this,"lastVersion","1.0");
//      KLog.d("myVersion: " + myVersion + " " + "lastVersion:"+ lastVersion );
        String apkUrl = (String) SPHelper.get(this,"downlink","");

        //TODO 测试
//        apkUrl = "http://qn.xnapp.com/1015975425bce7558d5ddf8.00204010.apk";
        if(lastVersion.compareTo(myVersion) > 0){
          //TODO 强制更新
            if (Build.VERSION.SDK_INT >= 23) {
                AppUpdateUtilByN appUpdateUtilByN = new AppUpdateUtilByN(MainActivityNew.this,apkUrl);
                appUpdateUtilByN.showUpdateNoticeDialog("新版本更新",true);
            }else {
                AppUpdateUtil appUpdateUtil = new AppUpdateUtil(MainActivityNew.this,apkUrl);
                appUpdateUtil.showUpdateNoticeDialog("新版本更新",true);
            }
      }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //TODO 上传广告 2018.10.21 逻辑是：上传成功后，清空数据库，然后再次进入时，重新加入数据库中，是新的一个逻辑
//        uploadAdBigModel();
        //TODO 上传历史记录
        uploadHistoryList();
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        mReturningWithResult = true;
//        resultCode1 = resultCode;
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        if (mReturningWithResult) {
//            // Commit your transactions here.
//        }
//            // Reset the boolean flag back to false for next time.
//            mReturningWithResult = false;
//        }

    private void change201() {
        Fragment myCurrentFragment = null;
        if(index_fragment == null){
            index_fragment = Index_Fragment.getInstance();
            myCurrentFragment = index_fragment;
        }else if(index_fragment2 == null){
            index_fragment2 = Index_Fragment_2.getInstance();
            myCurrentFragment = index_fragment2;
        }else if(index_fragment_3 == null){
            index_fragment_3 = Index_Fragment_3.getInstance();
            myCurrentFragment = index_fragment_3;
        }else if(index_fragment_4 == null){
            index_fragment_4 = Index_Fragment_4.getInstance();
            myCurrentFragment = index_fragment_4;
        }
        showFragment(myCurrentFragment);
    }

    //type -- bitmap -- fragment
    @OnClick(R.id.index_new)
    public void openNewWindow(){
        Bitmap bitmap = ScreenHelper.takeScreenShot(this);
        String name = StringUtils.getCurrentFragmentName(currentFragment);
        BitmapHelper.storeImageCache(bitmap, name,this);
        //TODO 新增Bean,每次浏览的都放在第一个
        NewWindowBean newWindowBean = new NewWindowBean();
        newWindowBean.setType(StringUtils.getFragmentTyep(name));
        newWindowBeanList.add(0,newWindowBean);
        newWindowAdapter.notifyDataSetChanged();
        new_window_ll.setVisibility(View.VISIBLE);

    }


    private void initRecycleView() {
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivityNew.this,2);
        recyclerView.setLayoutManager(layoutManager);//设置布局管理器
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置增加或删除条目动画
        newWindowAdapter = new NewWindowAdapter(newWindowBeanList);
        recyclerView.setAdapter(newWindowAdapter);
    }


    private void initEvent() {
        // 删除
        newWindowAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //点击谁，则从集合中删除  -- 并且从activity移除fragment
                String myType = newWindowBeanList.get(position).getType();
                removeFragment(myType);
                newWindowBeanList.remove(position);
                newWindowAdapter.notifyDataSetChanged();
                if(newWindowBeanList.size() == 0){
                    //创建新的fragment
                    initFragment1();
                    new_window_ll.setVisibility(View.GONE);
                }
            }
        });
        //条目点击事件
        newWindowAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String myType = newWindowBeanList.get(position).getType();
                //TODO
                window_count.setText( getFragmentSize() + "");
                EventBus.getDefault().post(new TypeEvent(Integer.parseInt(myType)));
                Fragment mFragment = getFragment(myType);
                showFragment(mFragment);
                //点击谁，则从集合中删除
                newWindowBeanList.remove(position);
                new_window_ll.setVisibility(View.GONE);
            }
        });
    }


    //通过type获取到对应的Fragment
    private  Fragment getFragment(String myType){
        Fragment mFragment = null;
        if(myType.equals("1")){
            mFragment = index_fragment;
        }else if(myType.equals("2")){
            mFragment = index_fragment2;
        }else if(myType.equals("3")){
            mFragment = index_fragment_3;
        }else if(myType.equals("4")){
            mFragment = index_fragment_4;
        }
        KLog.d("mFragment: " + mFragment);
        return mFragment;
    }

    //从activity中移除
    private void removeFragment(String type) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getFragment(type);
        transaction.remove(fragment);
        transaction.commit();
        //TODO 上述的并没有清除，手动设置null
        if(fragment == index_fragment){
            index_fragment = null;
        }else if(fragment == index_fragment2){
            index_fragment2 = null;
        }else if(fragment == index_fragment_3){
            index_fragment_3 = null;
        }else if(fragment == index_fragment_4){
            index_fragment_4 = null;
        }
    }


    //删除
    @OnClick(R.id.delete_window)
    public void deleteWindow(){
        DeleteAlertDialog deleteAlertDialog =  new DeleteAlertDialog(this).builder().setContextText("关闭所有窗口");
        deleteAlertDialog.setDeleteButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < newWindowBeanList.size(); i++) {
                    removeFragment(newWindowBeanList.get(i).getType());
                }
                newWindowBeanList.clear();
                initFragment1();
                new_window_ll.setVisibility(View.GONE);
            }
        });
        deleteAlertDialog.show();
    }

    //加号
    @OnClick(R.id.new_window_btn)
    public void new_window_btn(){
        if(StringUtils.isFastClick()){
            return;
        }

        if(newWindowBeanList != null && newWindowBeanList.size() == 4){
            Toast.makeText(this,"超过最大展开数量",Toast.LENGTH_SHORT).show();
            return;
        }
         new_window_ll.setVisibility(View.GONE);
         change201();
         window_count.setText(  getFragmentSize() + "");
    }

    @OnClick(R.id.index_menu)
    public void openMenu(){
        if(StringUtils.isFastClick()){
            return;
        }

        String name = StringUtils.getCurrentFragmentName(currentFragment);
        String type = StringUtils.getFragmentTyep(name);
        new BottomAlertDialog(this)
                .builder()
                .setCanceledOnTouchOutside(true)
                .setType(type)
                .show();
    }


    private void endWebView() {
        if (webview != null) {
            webview.stopLoading();
            webview.destroy();
            webview = null;
        }
    }


    @OnClick(R.id.index_home)
    public void index_home(){
        //如果有WebView，则隐藏
        getSubWebViewLayout();
        //TODO ll是当前fragment下的放置WebView的容器
        if(ll.getVisibility() == View.VISIBLE){
            endWebView();
            ll.setVisibility(View.GONE);
            return;
        }else{
            int index = (int) SPHelper.get(this,"home_fragment_posotion",0);
            EventBus.getDefault().post(new DataToTopEvent(index));
        }
    }

    private void getSubWebViewLayout() {
         ll =  currentFragment.getView().findViewById(R.id.webView_ll);
         webview =  currentFragment.getView().findViewById(R.id.webview);
    }


    @Override
    public void showNewsData( NewsBean newsBean) {
    }

    @Override
    public void showWeatherData(final WeatherBean weatherBean) { }


    //新建窗体的返回 :当前fragment 与 通过type获取的fragment作比较
    @OnClick(R.id.new_window_back)
    public void newWindowBack() {
        if(new_window_ll.getVisibility() == View.VISIBLE){
            String name = StringUtils.getCurrentFragmentName(currentFragment);
            String fragment_type = StringUtils.getFragmentTyep(name);
            boolean isExit  = false;
            for (int i = 0; i < newWindowBeanList.size(); i++) {
                if(newWindowBeanList.get(i).getType().equals(fragment_type)){
                    isExit = true;
                    break;
                }
            }

            if(!isExit){//如果集合条目中不包含当前的fragment,则用最后一个
                showFragment(getFragment(newWindowBeanList.get(newWindowBeanList.size() - 1).getType()));
            }

            new_window_ll.setVisibility(View.GONE);
            newWindowBeanList.remove(newWindowBeanList.size() - 1);
            window_count.setText( getFragmentSize() + "");
            return ;
        }
    }



    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            //选项卡
            if(new_window_ll.getVisibility() == View.VISIBLE){
                String name = StringUtils.getCurrentFragmentName(currentFragment);
                String fragment_type = StringUtils.getFragmentTyep(name);
                boolean isExit  = false;
                for (int i = 0; i < newWindowBeanList.size(); i++) {
                    if(newWindowBeanList.get(i).getType().equals(fragment_type)){
                        isExit = true;
                        break;
                    }
                }

                if(!isExit){//如果集合条目中不包含当前的fragment,则用最后一个
                    showFragment(getFragment(newWindowBeanList.get(newWindowBeanList.size() - 1).getType()));
                }

                new_window_ll.setVisibility(View.GONE);
                newWindowBeanList.remove(newWindowBeanList.size() - 1);
                //TODO 新建显示数量文本
                window_count.setText( getFragmentSize() + "");
                SDCardHelper.removeFileFromSDCard(SDCardHelper.getSDCardPrivateCacheDir(this) + "/" +  name + ".jpg");
                return true;
            }

            //Fragment1 + Fragment2 + ....
             return goBackLogic(currentFragment);

        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.index_back)
    public void index_back(){
        backLogic();
    }

    private void backLogic() {
        //选项卡
        if(new_window_ll.getVisibility() == View.VISIBLE){
            //先放着这里新增
            window_count.setText( getFragmentSize() + "");

            String name = StringUtils.getCurrentFragmentName(currentFragment);
            String fragment_type = StringUtils.getFragmentTyep(name);
            boolean isExit  = false;
            for (int i = 0; i < newWindowBeanList.size(); i++) {
                if(newWindowBeanList.get(i).getType().equals(fragment_type)){
                    isExit = true;
                    break;
                }
            }

            if(!isExit){//如果集合条目中不包含当前的fragment,则用第一个
                showFragment(getFragment(newWindowBeanList.get(0).getType()));
            }

            new_window_ll.setVisibility(View.GONE);
            //TODO 这个是将当前创建显示的Bean的资源销毁
            newWindowBeanList.remove(newWindowBeanList.size() - 1);
            SDCardHelper.removeFileFromSDCard(SDCardHelper.getSDCardPrivateCacheDir(this) + "/" +  name + ".jpg");
            return ;
        }

        //Fragment1 + Fragment2 + ....
        goBackNotLogic(currentFragment);
    }

    //获取fragment的数量

    private int getFragmentSize() {
        int count = 0;
        if(index_fragment != null){
            count++;
        }
        if(index_fragment2 != null){
            count++;
        }
        if(index_fragment_3 != null){
            count++;
        }
        if(index_fragment_4 != null){
            count++;
        }
        return count;
    }

    private void goBackNotLogic(Fragment currentFragment) {
        //Fragment1
        if (currentFragment != null && currentFragment instanceof Index_Fragment){

            getSubWebViewLayout();

            if(ll.getVisibility() == View.VISIBLE){
                if(webview != null && webview.canGoBack()){
                    webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                    webview.goBack();
                    return ;
                }else{
                    ll.setVisibility(View.GONE);
                    return ;
                }
            }else{
                exit();
                return ;
            }
        }

        //Fragment2
        if (currentFragment != null && currentFragment instanceof Index_Fragment_2){
            getSubWebViewLayout();
            if(ll.getVisibility() == View.VISIBLE){
                if(webview != null && webview.canGoBack()){
                    webview.goBack();
                    return ;
                }else{
                    ll.setVisibility(View.GONE);
                    return ;
                }
            }else{
                exit();
                return ;
            }
        }

        //Fragment3
        if (currentFragment != null && currentFragment instanceof Index_Fragment_3){
            getSubWebViewLayout();
            if(ll.getVisibility() == View.VISIBLE){
                if(webview != null && webview.canGoBack()){
                    webview.goBack();
                    return ;
                }else{
                    ll.setVisibility(View.GONE);
                    return ;
                }
            }else{
                exit();
                return ;
            }
        }

        //Fragment4
        if (currentFragment != null && currentFragment instanceof Index_Fragment_4){
            getSubWebViewLayout();
            if(ll.getVisibility() == View.VISIBLE){
                if(webview != null && webview.canGoBack()){
                    webview.goBack();
                    return ;
                }else{
                    ll.setVisibility(View.GONE);
                    return ;
                }
            }else{
                exit();
                return ;
            }
        }
    }


    private boolean goBackLogic(Fragment currentFragment) {
        //Fragment1
        if (currentFragment != null && currentFragment instanceof Index_Fragment){

            getSubWebViewLayout();

            if(ll.getVisibility() == View.VISIBLE){
                if(webview != null && webview.canGoBack()){
                    webview.goBack();
                    return true;
                }else{
                    ll.setVisibility(View.GONE);
                    return true;
                }
            }else{
                exit();
                return true;
            }
        }

        //Fragment2
        if (currentFragment != null && currentFragment instanceof Index_Fragment_2){
            getSubWebViewLayout();
            if(ll.getVisibility() == View.VISIBLE){
                if(webview != null && webview.canGoBack()){
                    webview.goBack();
                    return true;
                }else{
                    ll.setVisibility(View.GONE);
                    return true;
                }
            }else{
                exit();
                return true;
            }
        }

        //Fragment3
        if (currentFragment != null && currentFragment instanceof Index_Fragment_3){
            getSubWebViewLayout();
            if(ll.getVisibility() == View.VISIBLE){
                if(webview != null && webview.canGoBack()){
                    webview.goBack();
                    return true;
                }else{
                    ll.setVisibility(View.GONE);
                    return true;
                }
            }else{
                exit();
                return true;
            }
        }

        //Fragment4
        if (currentFragment != null && currentFragment instanceof Index_Fragment_4){
            getSubWebViewLayout();
            if(ll.getVisibility() == View.VISIBLE){
                if(webview != null && webview.canGoBack()){
                    webview.goBack();
                    return true;
                }else{
                    ll.setVisibility(View.GONE);
                    return true;
                }
            }else{
                exit();
                return true;
            }
        }

        return false;
    }

    long currentTime;
    public void exit() {
        currentTime = System.currentTimeMillis();
        if ((currentTime - mExitTime) > 2000) {
            Toast.makeText(MainActivityNew.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mExitTime = currentTime;
        } else {

            int count = (int) SPHelper.get(this,"count",0);
//            KLog.d("退出count: "+ count + " count % 2 " + (count % 2) );
            if(count % 5 == 0 ){
                boolean isg =  hasMyDefault(MainActivityNew.this);
                Message message = new Message();
                message.what = 100;
                message.obj = isg;
                handler.sendMessage(message);
            }else{
                MyApplication.getInstance().exitApp();
            }


        }
    }

    //检查是否是自己设置为默认
    public final boolean hasMyDefault( Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.niaowifi.com/"));
        PackageManager pm = context.getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // 找出手机当前安装的所有浏览器程序
        @SuppressLint("WrongConstant")
        List<ResolveInfo> resolveInfoList = pm
                .queryIntentActivities(intent,
                        PackageManager.GET_INTENT_FILTERS);
        KLog.d(resolveInfoList.size());
        for (int i = 0; i < resolveInfoList.size(); i++) {
            ActivityInfo activityInfo = resolveInfoList.get(i).activityInfo;
            String packageName = activityInfo.packageName;
            if(packageName.equals(info.activityInfo.packageName)){
                if(packageName.equals(getPackageName())){//如果是自己的话，存本地
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private void setDefaultBrowser() {
        final IosAlertDialog iosAlertDialog = new IosAlertDialog(this).builder();
        iosAlertDialog.setPositiveButton("去设置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityNew.this,SetBrowserActivity.class));
            }
        }).setNegativeButton("退出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 清除一些信息 ，上传一些东西
                MyApplication.getInstance().exitApp();
            }
        }).setTitle("标题").setMsg("是否设置"+ getString(R.string.app_name)
                + "为默认浏览器").setCanceledOnTouchOutside(false);
        iosAlertDialog.show();
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 100){
                boolean isg = (boolean) msg.obj;
                if(!isg){
                    setDefaultBrowser();
                }else
                    MyApplication.getInstance().exitApp();
            }
        }
    };


}
