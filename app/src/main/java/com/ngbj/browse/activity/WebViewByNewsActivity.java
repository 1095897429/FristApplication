package com.ngbj.browse.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ngbj.browse.R;
import com.ngbj.browse.base.BaseActivity;
import com.ngbj.browse.bean.HistoryData;
import com.ngbj.browse.db.DBManager;
import com.ngbj.browse.receiver.DownloadCompleteReceiver;
import com.ngbj.browse.util.SDCardHelper;
import com.socks.library.KLog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 展示网页的界面 -- 通过浏览新闻进入
 */

public class WebViewByNewsActivity extends BaseActivity {

    private static final String TAG = "WebViewByNewsActivity";

    @BindView(R.id.center_title)
    TextView center_title;

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.progressBar)
    ProgressBar pg;

    private String url;//刚开始加载的url
    private String lastTitle;//标题


    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview_news;
    }

    @Override
    protected void initDatas() {
        url = getIntent().getStringExtra("url");
        if(TextUtils.isEmpty(url)){
            return;
        }

        initData();
        setSetting();
        initWebViewClient();
        initWebChromeClient();
        webView.loadUrl(url);//http://e.firefoxchina.cn/

        webView.setDownloadListener(new MyWebViewDownLoadListener());
    }


    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            // 动态广播使用
            DownloadCompleteReceiver receiver = new DownloadCompleteReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            registerReceiver(receiver, intentFilter);

            downloadBySystem(url,contentDisposition,mimetype);
        }

    }



    @SuppressLint("NewApi")
    private void downloadBySystem(String url, String contentDisposition, String mimeType) {
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
        KLog.debug("下载文件的文件名:", fileName);
        //系统下载在公共目录
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//        另外可选一下方法，自定义下载路径

//        File file = new File(SDCardHelper.getSDCardBaseDir() + "/"+ "SmallBrowse");
//        request.setDestinationUri(Uri.fromFile(file));
//        request.setDestinationInExternalFilesDir(mContext,file.getAbsolutePath(),fileName);
        final DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        // 添加一个下载任务
        long downloadId = downloadManager.enqueue(request);
        KLog.debug("downloadId:", downloadId);
    }

    private void initWebChromeClient() {
        //获取网页进度
        webView.setWebChromeClient(new WebChromeClient(){

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
                KLog.d(TAG,"网页标题:"+ title);
                lastTitle = title;
                center_title.setText(title);
            }


        });
    }

    private void initWebViewClient() {
        //复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        webView.setWebViewClient(new WebViewClient(){
            boolean if_load;
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                KLog.d("url: " + url);
                if_load = false;
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
                KLog.d("url: " + url);

            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
            if (webView.canGoBack()) {
                webView.goBack(); //goBack()表示返回WebView的上一页面
                return true;
            }else {
                finish();
                return true;
            }
        }
        return false;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setSetting() {
        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setDatabaseEnabled(true);
        String dir = getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDomStorageEnabled(true);//支持DOM API

        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置当一个安全站点企图加载来自一个不安全站点资源时WebView的行为,
            // 在这种模式下,WebView将允许一个安全的起源从其他来源加载内容，即使那是不安全的.
            // 如果app需要安全性比较高，不应该设置此模式
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//解决app中部分页面非https导致的问题

        }



    }



    private void initData() {


    }


    //重新载入
    @OnClick(R.id.center_title)
    public void refresh(){
//        String current = historyList.lastElement();
//        webView.loadUrl(current);
    }


    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
        final WebView.HitTestResult webViewHitTestResult = webView.getHitTestResult();
        if (webViewHitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                webViewHitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            contextMenu.setHeaderTitle("网页中下载图片");
            contextMenu.add(0, 1, 0, "点击保存")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            String DownloadImageURL = webViewHitTestResult.getExtra();
                            String prefix = DownloadImageURL.substring(DownloadImageURL.lastIndexOf(".")+1);
                            KLog.d("后缀名为：" + prefix);
                            if (URLUtil.isValidUrl(DownloadImageURL)) {
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownloadImageURL));
                                request.allowScanningByMediaScanner();
                                //设置图片的保存路径
//                                request.setDestinationInExternalFilesDir(WebViewByNewsActivity.this, "/img", "/a.png");

                                request.setDestinationInExternalPublicDir("SmallBrowse/Pic/",System.currentTimeMillis() + "." + prefix);
                                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                downloadManager.enqueue(request);
                                Toast.makeText(WebViewByNewsActivity.this, "图片保存到" + "/SmallBrowse/Pic/目录下", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(WebViewByNewsActivity.this, "下载失败", Toast.LENGTH_LONG).show();
                            }
                            return false;
                        }
                    });
        }
    }




}
