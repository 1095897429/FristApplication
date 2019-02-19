package com.ngbj.browse.util;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ngbj.browse.BuildConfig;
import com.ngbj.browse.MyApplication;
import com.ngbj.browse.R;
import com.socks.library.KLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Android 7.0 权限 不用 Uri.fromFile
 */
public class AppUpdateUtilByN {

	private Context context;
	private ProgressBar mProgress;
	private Dialog downloadDialog;
	private String apkUrl;
	private int progress;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private static final String APK_NAME = "xnllq.apk";
	int contentLength;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				Toast.makeText(context, "下载完毕", Toast.LENGTH_SHORT).show();
				downloadDialog.dismiss();
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public AppUpdateUtilByN(Context context, String apkUrl) {
		this.context = context;
		this.apkUrl = apkUrl;
	}

	public void showUpdateNoticeDialog(String apknote) {
		try {
			Builder builder = new Builder(context);
			builder.setTitle("发现新版本");
			builder.setMessage(apknote);
			builder.setPositiveButton("立即更新",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							showDownloadDialog();
						}
					});
			builder.setCancelable(false);
			builder.create().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showUpdateNoticeDialog(String apknote, boolean calcle) {
		try {
			Builder builder = new Builder(context);
			builder.setTitle("发现新版本");
			builder.setMessage(apknote);
			builder.setCancelable(false);
			builder.setPositiveButton("立即更新",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							showDownloadDialog();
						}
					});
			builder.setNegativeButton("暂不更新",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void showDownloadDialog() {
		Builder builder = new Builder(context);
		builder.setTitle("软件版本更新");
		builder.setCancelable(false);
		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.progress_version_update, null);
		mProgress =  v.findViewById(R.id.progress_version_update_pb);
		builder.setView(v);		
		downloadDialog =builder.create();
		downloadDialog.show();
		//下载apk安装包
		downloadApk();
	}

	private void downloadApk() {
		Thread downLoadThread = new Thread(mdownApk1);
		downLoadThread.start();
	}

	private Runnable mdownApk1 = new Runnable() {
		@Override
		public void run() {
			URL url;
			try {
				url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				//设置超时间为3秒
				conn.setConnectTimeout(3 * 1000);
//				conn.setDoOutput(true);将导致请求以post方式提交
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				//打开连接
				conn.connect();
				//获取内容长度
				contentLength = conn.getContentLength();
				//得到输入流
				InputStream inputStream = conn.getInputStream();
				//获取自己数组
				byte[] getData = readInputStream(inputStream);
				//获取内容长度
				contentLength = conn.getContentLength();

				File file ;
				String apkName = Environment.getExternalStorageDirectory() + File.separator + APK_NAME;//文件保存位置
				if(fileIsExists(apkName)){
					file = new File(apkName);
					//如果存在，则先删除
					file.delete();

				}
				file = new File(apkName);

				FileOutputStream fos = new FileOutputStream(file);
				fos.write(getData);
				if (fos != null) {
					fos.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
				mHandler.sendEmptyMessage(DOWN_OVER);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};


	//判断文件是否存在
	public boolean fileIsExists(String strFile) {
		try
		{
			File f=new File(strFile);
			if(!f.exists())
			{
				return false;
			}
		}
		catch (Exception e)
		{
			return false;
		}

		return true;
	}

	/**
	 * 从输入流中获取字节数组
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public  byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		int count = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
			count += len;
			progress = (int) (((float) count / contentLength) * 100);
			// 更新进展
			if (mProgress.getProgress() < progress) {
				mHandler.sendEmptyMessage(DOWN_UPDATE);
			}
		}
		bos.close();
		return bos.toByteArray();
	}


	public void installApk() {
		//解析包时出现问题 /sdcard/即可解决
//		String path = Environment.getExternalStorageDirectory().getPath();
//		Logger.d("安装地址 path: " + path + APK_NAME);
//		File f = new File(path + APK_NAME);


		File f = new File("/sdcard/" + APK_NAME);
		Uri contentUri = FileProvider.getUriForFile(context,
				BuildConfig.APPLICATION_ID + ".fileprovider", f);
		KLog.d("uri",contentUri.toString());//content://com.ngbj.browse.fileprovider/sdcard/xnllq.apk
		Intent intent = new Intent(Intent.ACTION_VIEW);
		// 由于没有在Activity环境下启动Activity,设置下面的标签
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//添加这一句表示对目标应用临时授权该Uri所代表的文件
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
		context.startActivity(intent);

		//退出
		try {
			Thread.sleep(3000);
			MyApplication.getInstance().exitApp();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



}
