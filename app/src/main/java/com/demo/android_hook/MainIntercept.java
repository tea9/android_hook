package com.demo.android_hook;

import android.util.Log;

import com.demo.android_hook.util.XPConstant;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * created by tea9 at 2019/1/30
 */
public class MainIntercept implements IXposedHookLoadPackage {

    private static final String TAG = "MainIntercept";

    private ICracker mCracker;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if(lpparam.packageName.equals(XPConstant.ALI_PACKAGE) && lpparam.processName.equals(XPConstant.ALI_PACKAGE)){
            XposedBridge.log("handleLoadPackage equals AlipayCracker.instance()");
//            mCracker = AlipayCracker.instance();
        }

        XposedBridge.log("handleLoadPackage");
        Log.d(TAG, "handleLoadPackage: " + lpparam.classLoader + ", param: " + lpparam);
        Log.d(TAG, String.format("package: %s, process: %s", lpparam.packageName, lpparam.processName));

        XposedBridge.log("handleLoadPackage: " + lpparam.classLoader + ", param: " + lpparam);
        XposedBridge.log(String.format("package: %s, process: %s", lpparam.packageName, lpparam.processName));


        if(mCracker != null && !mCracker.isReadyForUse()){
            XposedBridge.log("handleLoadPackage equals AlipayCracker.mCracker()");
//            mCracker.handleLoadPackage(lpparam.classLoader);
        }
    }
}
