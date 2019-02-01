package com.demo.android_hook;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.demo.android_hook.hook.AliPayHook;
import com.demo.android_hook.utils.StringConstant;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * created by tea9 at 2019/1/23
 */
public class Main implements IXposedHookLoadPackage {
    private boolean ALIPAY_PACKAGE_ISHOOK = false;
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("xposed main");
        String packageName = lpparam.packageName;
        String processName = lpparam.processName;
        XposedBridge.log("xposed main"+packageName);
        XposedBridge.log("xposed main"+processName);
        if (StringConstant.ALIPAY_PACKAGE.equals(packageName)) {
            XposedBridge.log("xposed main ddddd"+packageName);
            hookAliPay(lpparam, processName);
        }
    }

    private void hookAliPay(XC_LoadPackage.LoadPackageParam loadPackageParam, final String str) {
        try {
            XposedBridge.log("Ali detect");
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    super.afterHookedMethod(methodHookParam);
                    Context context = (Context) methodHookParam.args[0];
                    ClassLoader classLoader = context.getClassLoader();
                    if (StringConstant.ALIPAY_PACKAGE.equals(str) && !Main.this.ALIPAY_PACKAGE_ISHOOK) {
                        Main.this.ALIPAY_PACKAGE_ISHOOK = true;
//                        BroadcastReceiver startAlipayReceived = new StartAlipayReceived();
//                        IntentFilter intentFilter = new IntentFilter();
//                        intentFilter.addAction(StringConstant.ALIPAYSTART_ACTION);
//                        context.registerReceiver(startAlipayReceived, intentFilter);
                        Toast.makeText(context, "获取到alipay=>>classloader", Toast.LENGTH_LONG).show();
                        new AliPayHook().hook(classLoader, context);
                    }
                }
            });
        } catch (Throwable th) {
            XposedBridge.log(th);
        }
    }
}
