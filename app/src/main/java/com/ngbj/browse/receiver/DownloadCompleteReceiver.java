package com.ngbj.browse.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.ngbj.browse.BuildConfig;
import com.ngbj.browse.activity.MainActivityNew;
import com.ngbj.browse.util.AppUpdateUtil;
import com.ngbj.browse.util.AppUpdateUtilByN;
import com.ngbj.browse.util.AppUtil;
import com.ngbj.browse.util.ToastUtil;
import com.socks.library.KLog;

import java.io.File;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        KLog.d("onReceive. intent:", intent != null ? intent.toUri(0) : null);
        if (intent != null) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                KLog.d("downloadId:", downloadId);
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                String type = downloadManager.getMimeTypeForDownloadedFile(downloadId);
                KLog.d("getMimeTypeForDownloadedFile:", type);
                if (TextUtils.isEmpty(type)) {
                    type = "*/*";
                }
                Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
                KLog.d("UriForDownloadedFile:", uri);
                if (uri != null) {
                    ToastUtil.showShort(AppUtil.getContext(),"下载完成");

                    if (Build.VERSION.SDK_INT >= 23) {
                        KLog.d("uri",uri.toString());//content://downloads/all_downloads/1970
                        Intent intent2 = new Intent(Intent.ACTION_VIEW);
                        // 由于没有在Activity环境下启动Activity,设置下面的标签
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //添加这一句表示对目标应用临时授权该Uri所代表的文件
                        intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent2.setDataAndType(uri, "application/vnd.android.package-archive");
                        context.startActivity(intent2);
                    }else {
                        //  /mnt/sdcard其实是/storage/emulated/0的一个软链接(soft link)
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//安装App先卸载后安装，不然会出现安装过程中闪退的现象
                        //解析包时出现问题 /sdcard/即可解决
                        i.setDataAndType(uri,
                                "application/vnd.android.package-archive");
                        context.startActivity(i);
                    }
                }

            }
        }
    }
}
