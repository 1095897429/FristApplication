package com.allfree.security;

import android.content.Context;

/**
 * Created by zhangming on 2017/1/22.
 */
public class SecurityManager {
    private static SecurityManager instance;

    public static SecurityManager getInstance() {
        if (instance == null) {
            synchronized (Security.class) {
                if (instance == null) {
                    instance = new SecurityManager();
                }
            }
        }
        return instance;
    }

    private Context context;

    private SecurityManager() {

    }

    public void init(Context applicationContext) {
        this.context = applicationContext;
    }

    public Context getContext() {
        return context;
    }
}
