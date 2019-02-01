package com.demo.android_hook.utils;

/**
 * created by tea9 at 2019/1/23
 */
public class StringConstant {
    public static final String ALIPAYSTART_ACTION = "com.sytpay.alipay.start";
    public static final String ALIPAY_PACKAGE = "com.eg.android.AlipayGphone";
    public static final String BILLRECEIVED_ACTION = "com.sytpay.billreceived";
    public static final String BROADCAST_BASE = "com.sytpay.";
    public static final String LOGINIDRECEIVED_ACTION = "com.sytpay.loginidreceived";
    public static final String MSGRECEIVED_ACTION = "com.sytpay.msgreceived";
    public static final String NOTIFY_ACTION = "com.sytpay.notify";
    public static final String NOTIFY_TRANSFER_ACTION = "notify_transfer";
    public static final String QQSTART_ACTION = "com.sytpay.qq.start";
    public static String QQ_PACKAGE = "com.tencent.mobileqq";
    public static String QQ_WALLET_PACKAGE = "com.qwallet";
    public static final String QRCODERECEIVED_ACTION = "com.sytpay.qrcodereceived";
    public static final String TRADENORECEIVED_ACTION = "com.sytpay.tradenoreceived";
    public static final String WECHATSTART_ACTION = "com.sytpay.wechat.start";
    public static final String WECHAT_PACKAGE = "com.tencent.mm";
    public static final String XShareFlag = "bbplayer";
    public static final String YSESTART_ACTION = "com.sytpay.yse.start";
    public static final String YSE_GETPAY = "com.ysepay.mobileswipcard.activity.PosMakeCollections_V2";
    public static final String YSE_HTML_ACTIVITY = "com.ysepay.mobileportal.activity.html.JsBridgeHtml";
    public static final String YSE_INDEX_FRAGMENT = "com.ysepay.mobileportal.activity.FragmentPage1";
    public static final String YSE_MainTabActivity = "com.ysepay.mobileportal.activity.MainTabActivity";
    public static final String YSE_PACKAGE_NAME = "com.ysepay.mobileportal.activity";
    public static final String YSE_PAYRES = "com.ysepay.mobileportal.util.VoicePromptTools";

    public interface TRANSFER {
        public static final String BALANCE = "balance";
        public static final String MAXLOAD = "maxload";
        public static final String TOACCOUNT = "transfer_account";
        public static final String WORKFLAG = "checkBalance";
    }
}
