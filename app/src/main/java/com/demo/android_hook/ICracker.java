package com.demo.android_hook;

/**
 * Created by wy on 2018/11/13.
 */

public interface ICracker {
    void handleLoadPackage(ClassLoader loader) throws Throwable;
    boolean isReadyForUse();
//    IQR getQR();
}
