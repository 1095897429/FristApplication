package com.ngbj.browse.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)//API需要在21及以上
public class MyJobService extends JobService {

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            JobParameters param = (JobParameters) msg.obj;
            jobFinished(param, true);
            Intent intent = new Intent(getApplicationContext(), MyJobService.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startService(intent);
            return true;
        }
    });

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        doJob(params);
        doService();
        return true;

    }

    private void doJob(JobParameters params) {
//        KLog.d("-----  doJob ----- ");
        Message m = Message.obtain();
        m.obj = params;
        handler.sendMessage(m);
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        handler.removeCallbacksAndMessages(null);
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void doService() {
        JobScheduler jobScheduler = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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

}