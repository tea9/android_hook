package com.demo.android_hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * created by tea9 at 2019/1/23
 * https://www.52pojie.cn/thread-688466-1-1.html
 */
public class HookToast implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.demo.myapplication")){
            Class clazz = lpparam.classLoader.loadClass("com.demo.myapplication.MainActivity");
            XposedHelpers.findAndHookMethod(clazz, "toastMessage", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    super.beforeHookedMethod(param);
                    XposedBridge.log("you are been hooked");
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    super.afterHookedMethod(param);
                    param.setResult("hacked by tea");
                }
            });
        }
    }
}
