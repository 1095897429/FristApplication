package com.ngbj.browse.activity;


import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ngbj.browse.BuildConfig;
import com.ngbj.browse.R;
import com.ngbj.browse.adpter.DownAdapter;
import com.ngbj.browse.adpter.DownFileAdapter;
import com.ngbj.browse.bean.PicBean;
import com.ngbj.browse.db.DBManager;
import com.ngbj.browse.dialog.DeleteAlertDialog;
import com.ngbj.browse.event.CleanHistoryEvent;
import com.ngbj.browse.util.SDCardHelper;
import com.ngbj.browse.view.CustomDecoration;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DownFileActivity extends CommonHeadActivity {

    @BindView(R.id.delete_txt)
    TextView delete_txt;

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    @BindView(R.id.empty_view)
    TextView empty_view;

    DownFileAdapter downAdapter;

    List<String> pathList = new ArrayList<>();
    private List<PicBean> mList = new ArrayList<>();
    PicBean picBean;
    private boolean editorStatus = false;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (pathList.size() > 0) {
                downAdapter = new DownFileAdapter(mList);
                recyclerView.setAdapter(downAdapter);
                initEvent();
            } else {
                recyclerView.setVisibility(View.GONE);
                empty_view.setVisibility(View.VISIBLE);
            }

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_download_file;
    }

    @Override
    protected void initDatas() {
        center_title.setText("文件下载管理");
        initRecycle();


        new Thread(new Runnable() {
            @Override
            public void run() {
                getPathList();
                Message message = Message.obtain();
                handler.sendMessage(message);
            }
        }).start();

    }

    private void initEvent() {
        downAdapter.setOnItemClickListener(new DownFileAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos, List<PicBean> myLiveList) {
                if(editorStatus){
                    PicBean picBean = myLiveList.get(pos);
                    boolean isSelect = picBean.isSelect();
                    if(!isSelect){
                        picBean.setSelect(true);
                        index++;
                    }else {
                        picBean.setSelect(false);
                        index--;
                    }
                    downAdapter.notifyDataSetChanged();
                }else{
                    String apkName = mList.get(pos).getName();
                    if (Build.VERSION.SDK_INT >= 23) {
                        File f = new File("/sdcard/Download" , apkName);
                        Uri contentUri = FileProvider.getUriForFile(DownFileActivity.this,
                                BuildConfig.APPLICATION_ID + ".fileprovider", f);
                        KLog.d("uri",contentUri.toString());//content://downloads/all_downloads/1970
                        Intent intent2 = new Intent(Intent.ACTION_VIEW);
                        // 由于没有在Activity环境下启动Activity,设置下面的标签
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //添加这一句表示对目标应用临时授权该Uri所代表的文件
                        intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent2.setDataAndType(contentUri, "application/vnd.android.package-archive");
                        startActivity(intent2);
                    }else {
                        File apkFile = new File("/sdcard/Download" ,apkName);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//安装App先卸载后安装，不然会出现安装过程中闪退的现象
                        Uri uri = Uri.parse("file://" + apkFile.toString());
                        //解析包时出现问题 /sdcard/即可解决
                        i.setDataAndType(uri, "application/vnd.android.package-archive");
                        startActivity(i);
                    }
                }
            }
        });
    }

    public void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new CustomDecoration(this,
                CustomDecoration.VERTICAL_LIST, R.drawable.divider, 0));
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
    }


    private List<String> getPathList() {
        String dir = SDCardHelper.getSDCardPublicDir(Environment.DIRECTORY_DOWNLOADS);
        File parentFile = new File(dir);
//        KLog.d("是否是文件夹：" + parentFile.isDirectory());
        if (parentFile.isDirectory()) {
            File[] files = parentFile.listFiles();

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.exists()) {
                    //如果是文件的加载进来，不是文件的不要加载
                    if(!file.isDirectory()){
                        picBean = new PicBean();
                        picBean.setName(file.getName());
                        picBean.setPath(file.getAbsolutePath());
                        long time = file.lastModified();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String result = formatter.format(time);
                        picBean.setTime(result);
                        mList.add(picBean);
                        pathList.add(file.getAbsolutePath());
                    }
                }
            }
        }

        return pathList;
    }

    private int mEditMode = MYLIVE_MODE_CHECK;//当前状态
    private static final int MYLIVE_MODE_CHECK = 0;
    private static final int MYLIVE_MODE_EDIT = 1;
    int index;
    @OnClick(R.id.delete)
    public void delete(){
        if(pathList.size() != 0){
            mEditMode = mEditMode == MYLIVE_MODE_CHECK ? MYLIVE_MODE_EDIT:MYLIVE_MODE_CHECK;
            if(mEditMode == MYLIVE_MODE_EDIT ){
                delete_txt.setVisibility(View.VISIBLE);
                editorStatus = true;
            }else{
                delete_txt.setVisibility(View.GONE);
                editorStatus = false;
            }

            downAdapter.setEditMode(mEditMode);
        }
    }


    @OnClick(R.id.delete_txt)
    public void deleteTxt(){
        if(index != 0){
            DeleteAlertDialog deleteAlertDialog =  new DeleteAlertDialog(this).builder();
            String name = "删除选中的记录";
            deleteAlertDialog
                    .setContextText(name)
                    .setDeleteButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<PicBean> list = new ArrayList<>();
                            for (PicBean picBean: mList ) {
                                if(picBean.isSelect()){
                                    SDCardHelper.removeFileFromSDCard(picBean.getPath());//删除
                                    continue;
                                }
                                list.add(picBean);
                            }

                            //删除操作
                            delete_txt.setVisibility(View.GONE);
                            downAdapter.setEditMode(MYLIVE_MODE_CHECK);
                            //重新设置
                            if(list.size() != 0){
                                downAdapter.setNewData(list);
                            }else{
                                recyclerView.setVisibility(View.GONE);
                                empty_view.setVisibility(View.VISIBLE);
                            }
                        }
                    });
            deleteAlertDialog.show();
        }


    }

}

