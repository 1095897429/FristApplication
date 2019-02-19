package com.ngbj.browse.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.socks.library.KLog;

public class MsgPushService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        KLog.d("onStartCommand");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}