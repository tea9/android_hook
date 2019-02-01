package com.demo.android_hook;

import android.app.Application;
import android.content.Context;

import com.demo.android_hook.utils.StringConstant;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * created by tea9 at 2019/1/29
 * <p>
 * I/Xposed:
 * <p>
 * logcat|grep "I/Xposed:"
 * I Xposed
 * <p>
 *     this
 * logcat|grep "I Xposed"
 * <p>
 * adb shell am broadcast -a io.va.exposed.CMD -e cmd update -e pkg com.demo.android_hook
 */
public class HookTest implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("xposed hooktest");
        final String packageName = lpparam.packageName;
        final String processName = lpparam.processName;
        XposedBridge.log("xposed hooktest packageName:" + packageName);
        XposedBridge.log("xposed hooktest processName:" + processName);

        if (StringConstant.ALIPAY_PACKAGE.equals(packageName)) {
            XposedBridge.log("Ali detect");
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    super.afterHookedMethod(methodHookParam);
                    XposedBridge.log("xposed hooktest attach:");

                    XposedBridge.log("hookAliPay");
                    Context context = (Context) methodHookParam.args[0];
                    ClassLoader classLoader = context.getClassLoader();
                    if (StringConstant.ALIPAY_PACKAGE.equals(packageName)) {
                        XposedBridge.log("hookAliPay equals");
                        XposedBridge.log("hookAliPay equals 获取到alipay=>>classloader");

                        XposedBridge.log("hookAliPay equals test test test ");
                        XposedBridge.hookAllMethods(XposedHelpers.findClass("com.alipay.android.phone.messageboxstatic.biz.dao.TradeDao", classLoader), "", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                                XposedBridge.log("======start========= TradeDao insertMessageInfo");
                            }
                        });

                        XposedBridge.hookAllMethods(XposedHelpers.findClass("com.alipay.android.phone.messageboxstatic.biz.dao.ServiceDao", classLoader), "insertMessageInfo", new XC_MethodHook() {

                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                super.beforeHookedMethod(param);
                                XposedBridge.log("======商户服务start=========  ServiceDao  insertMessageInfo");
                            }
                        });

                        XposedHelpers.findAndHookMethod("com.alipay.android.phone.messageboxstatic.biz.dao.ServiceDao", classLoader, "queryMsginfoByOffset", String.class, Long.TYPE, Long.TYPE, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                                XposedBridge.log("--------------  收款 ------------  ServiceDao  queryMsginfoByOffset");
                            }
                        });




                    }
                }
            });


        }
//        hook(null,null);
        XposedBridge.log("xposed hooktest end");

    }

    private void hook(final ClassLoader classLoader, final Context context) {
        try {
            XposedBridge.log("aaaaa");
            XposedBridge.log("aaaa");
            XposedBridge.log("aaaa");
            XposedBridge.log("aaaa");
            XposedBridge.log("aaaaaaaaa");
            XposedBridge.log("aaaaa");
            XposedBridge.log("aaaaaa");
            XposedBridge.log("aaaaaaaa");
        } catch (Exception e) {
            XposedBridge.log("xposed hooktest error");
        }
    }
}
