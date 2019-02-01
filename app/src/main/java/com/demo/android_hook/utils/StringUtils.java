package com.demo.android_hook.utils;

/**
 * created by tea9 at 2019/1/28
 */
public class StringUtils {
    public static void main(String[] strArr) {
    }

    public static String getTextCenter(String str, String str2, String str3) {
        int indexOf = str.indexOf(str2) + str2.length();
        return str.substring(indexOf, str.indexOf(str3, indexOf));
    }
}
