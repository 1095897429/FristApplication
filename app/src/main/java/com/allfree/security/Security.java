package com.allfree.security;

/**
 * Created by zhangming on 2017/1/22.
 */

public class Security {

    public static Object[] encryptParams(String time) {
        return JNIAllfree.stringFromJNI(time);
    }

}
