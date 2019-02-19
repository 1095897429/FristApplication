package com.allfree.security;

/**
 * Created by zhangming on 2017/1/22.
 */
public class JNIAllfree {

    static {
        System.loadLibrary("allfree-jni1.1.0");
    }

    public static native Object[] stringFromJNI(String time);

}
