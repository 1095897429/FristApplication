package com.ngbj.browse.activity;


import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.ngbj.browse.R;
import com.ngbj.browse.util.SDCardHelper;
import com.socks.library.KLog;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class DownActivity extends CommonHeadActivity {

    List<String> pathList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_download;
    }

    @Override
    protected void initDatas() {
        center_title.setText("下载管理");
    }



    @OnClick(R.id.down_pic)
    public void opemPic(){
        Intent i = new Intent(this,DownPicActivity.class);
        startActivity(i);
    }

    @OnClick({R.id.down_file,R.id.down_giz,R.id.other})
    public void down_file(){
//        Intent i = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
//        startActivity(i);
        Intent i = new Intent(this,DownFileActivity.class);
        startActivity(i);
    }


}
