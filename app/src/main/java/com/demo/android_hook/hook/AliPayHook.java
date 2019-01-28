package com.demo.android_hook.hook;

import android.content.Context;

import de.robv.android.xposed.XposedBridge;

/**
 * created by tea9 at 2019/1/23
 */
public class AliPayHook {
    public void hook(final ClassLoader classLoader, final Context context) {
        XposedBridge.log("test");

    }
}
