package com.demo.android_hook;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.demo.android_hook.hook.AliPayHook;
import com.demo.android_hook.utils.StringConstant;
import com.demo.android_hook.utils.StringUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
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
                    XposedBridge.log("you are after been hooked");
                    param.setResult("hacked by tea9");
                }
            });
        }

        XposedBridge.log("xposed main");
        String packageName = lpparam.packageName;
        String processName = lpparam.processName;
        XposedBridge.log("xposed main"+packageName);
        XposedBridge.log("xposed main processName"+processName);
        if (StringConstant.ALIPAY_PACKAGE.equals(packageName)) {
            XposedBridge.log("xposed main ddddd"+packageName);
            hookAliPay(lpparam, packageName);
        }
    }

    private void hookAliPay(XC_LoadPackage.LoadPackageParam loadPackageParam, final String str) {
        try {
            XposedBridge.log("Ali detect");
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    super.afterHookedMethod(methodHookParam);
                    XposedBridge.log("hookAliPay");
                    Context context = (Context) methodHookParam.args[0];
                    ClassLoader classLoader = context.getClassLoader();
                    if (StringConstant.ALIPAY_PACKAGE.equals(str)) {
                        XposedBridge.log("hookAliPay equals");
//                        ALIPAY_PACKAGE_ISHOOK = true;
//                        BroadcastReceiver startAlipayReceived = new StartAlipayReceived();
//                        IntentFilter intentFilter = new IntentFilter();
//                        intentFilter.addAction(StringConstant.ALIPAYSTART_ACTION);
//                        context.registerReceiver(startAlipayReceived, intentFilter);
                        XposedBridge.log("hookAliPay equals 获取到alipay=>>classloader");
                        XposedBridge.log("hookAliPay equals 获取到alipay=>>classloader"+classLoader.toString());
                        Toast.makeText(context, "获取到alipay=>>classloader", Toast.LENGTH_LONG).show();
                        hook(classLoader, context);
                        Toast.makeText(context, "获取到alipay=>>classloader hook", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Throwable th) {
            XposedBridge.log(th);
        }
    }




    //////////////////////////////////
    public void hook(final ClassLoader classLoader, final Context context) {
        Toast.makeText(context, "获取到alipay=>>classloader", Toast.LENGTH_LONG).show();
        XposedBridge.log("test");
        XposedBridge.log("test");
        XposedBridge.log("test");
        XposedBridge.hookAllMethods(XposedHelpers.findClass("com.alipay.android.phone.messageboxstatic.biz.dao.TradeDao", classLoader), "", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                try {
                    XposedBridge.log("======start========= TradeDao insertMessageInfo");
                    String str = (String) XposedHelpers.getObjectField(methodHookParam.args[0], "content");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("tradeDAO :: content1  --->  ");
                    stringBuilder.append(str);
                    XposedBridge.log(stringBuilder.toString());
                    String str2 = (String) XposedHelpers.callMethod(methodHookParam.args[0], "toString", new Object[0]);
                    XposedBridge.log("此处放款");
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("MessageInfo   =》 ");
                    stringBuilder.append(str2);
                    XposedBridge.log(stringBuilder.toString());
                    if (str.contains("二维码收款") || str.contains("收款到账")) {
                        JSONObject jSONObject = new JSONObject(str);
                        str = jSONObject.getString("content");
                        String string = jSONObject.getString("assistMsg2");
                        str2 = StringUtils.getTextCenter(str2, "tradeNO=", "&");
                        XposedBridge.log("AliPayHook hook tea9 str=>" + str);
                        XposedBridge.log("AliPayHook hook tea9 string=>" + string);
                        XposedBridge.log("AliPayHook hook tea9 str2=>" + str2);

                        //下面是发送广播
//                    Intent intent = new Intent();
//                    intent.putExtra("bill_no", str2);
//                    intent.putExtra("bill_money", str);
//                    intent.putExtra("bill_mark", string);
//                    intent.putExtra("bill_type", PayAccountManager.TYPE_ALIPAY);
//                    intent.setAction(StringConstant.BILLRECEIVED_ACTION);
//                    StringBuilder stringBuilder2 = new StringBuilder();
//                    stringBuilder2.append("===收款===  bill_money  == ");
//                    stringBuilder2.append(str);
//                    stringBuilder2.append("   bill_mark  ");
//                    stringBuilder2.append(string);
//                    stringBuilder2.append("  bill_type  alipay   bill_no is ");
//                    stringBuilder2.append(str2);
//                    XposedBridge.log(stringBuilder2.toString());
//                    context.sendBroadcast(intent);
                    }
                    XposedBridge.log("======end=========");
                } catch (Exception e) {
                    XposedBridge.log(e.getMessage());
                }
                super.beforeHookedMethod(methodHookParam);
            }
        });

        XposedBridge.hookAllMethods(XposedHelpers.findClass("com.alipay.android.phone.messageboxstatic.biz.dao.ServiceDao", classLoader), "insertMessageInfo", new XC_MethodHook() {
            StringBuilder stringBuilder;
            @Override
            protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                try{
                    XposedBridge.log("======商户服务start=========  ServiceDao  insertMessageInfo");
                    String str=AliPayHook.getTextCenter((String) XposedHelpers.callMethod(methodHookParam.args[0], "toString", new Object[0]), "extraInfo='", "'").replace("\\", "");
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("商户服务");
                    stringBuilder.append(str);
                    XposedBridge.log(stringBuilder.toString());
                    if (str.contains("收钱到账") || str.contains("收款到账")) {
                        AliPayHook.tradeOrderQuery(context, AliPayHook.getCookie(classLoader));
                    }
                    XposedBridge.log("======商户服务end=========");

                }catch (Exception e){
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("商家服务回调异常:");
                    stringBuilder.append(e.getMessage());
                    XposedBridge.log(stringBuilder.toString());
                    XposedBridge.log(e.getMessage());
                }

                super.beforeHookedMethod(methodHookParam);
            }
        });

        XposedHelpers.findAndHookMethod("com.alipay.android.phone.messageboxstatic.biz.dao.ServiceDao", classLoader, "queryMsginfoByOffset", String.class, Long.TYPE, Long.TYPE, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
//                super.afterHookedMethod(param);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(methodHookParam.args[1]);
                stringBuilder.append("");
                String stringBuilder2 = stringBuilder.toString();
                XposedBridge.log("--------------  收款 ------------  ServiceDao  queryMsginfoByOffset");
                if (stringBuilder2.equals("0")) {
                    for (Object next : (List) XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.alipay.mbxsgsg.d.b", classLoader), "a", methodHookParam.getResult())) {
                        String str = (String) XposedHelpers.getObjectField(next, "content");
                        String str2 = (String) XposedHelpers.getObjectField(next, "msgState");
                        str2 = (String) XposedHelpers.getObjectField(next, "msgId");
                        str2 = new Gson().toJson(next);
                        XposedBridge.log(str);
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(str2.contains("收款到账"));
                        stringBuilder3.append(" 消息 ");
                        stringBuilder3.append(str2);
                        XposedBridge.log(stringBuilder3.toString());
                        if (str2.contains("收款到账")) {
                            stringBuilder2 = (String) XposedHelpers.getObjectField(next, "extraInfo");
                            stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("extraInfo:::   ===>  ");
                            stringBuilder3.append(stringBuilder2);
                            XposedBridge.log(stringBuilder3.toString());
                            new JSONObject(stringBuilder2).put("action", "transferMessage");
                            AliPayHook.tradeOrderQuery(context, AliPayHook.getCookie(classLoader));
                        }
                    }
                }
            }
        });
        XposedHelpers.findAndHookMethod("com.alipay.mobile.antui.basic.AUDialog", classLoader, "show", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
//                super.beforeHookedMethod(param);
//                PayHelperUtils.sendmsg(context, "本日交易受限，请明日再申请交易");
                String simpleName = XposedHelpers.getObjectField(methodHookParam.thisObject, "mContext").getClass().getSimpleName();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(" --- ");
                stringBuilder.append(simpleName);
                stringBuilder.append("  --  ");
                XposedBridge.log(stringBuilder.toString());
                if (Arrays.asList(new String[]{"PayeeQRSetMoneyActivity", "TransparentActivity"}).indexOf(simpleName) != -1) {
                    XposedHelpers.setObjectField(methodHookParam.thisObject, "mContext", null);
                }

            }
        });
//        XposedHelpers.findAndHookMethod("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity", classLoader, "onCreate", Bundle.class, new C09475());
        XposedHelpers.findAndHookMethod("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity", classLoader, "a", XposedHelpers.findClass("com.alipay.transferprod.rpc.result.ConsultSetAmountRes", classLoader), new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                String str = (String) XposedHelpers.findField(methodHookParam.thisObject.getClass(), "g").get(methodHookParam.thisObject);
                String str2 = (String) XposedHelpers.callMethod(XposedHelpers.findField(methodHookParam.thisObject.getClass(), "c").get(methodHookParam.thisObject), "getUbbStr", new Object[0]);
                /**
                 *  methodHookParam = methodHookParam.args[0];
                 *                     String str3 = (String) XposedHelpers.findField(methodHookParam.getClass(), "qrCodeUrl").get(methodHookParam);
                 *                     StringBuilder stringBuilder = new StringBuilder();
                 * */
                String str3 = (String) XposedHelpers.findField(methodHookParam.getClass(), "qrCodeUrl").get(methodHookParam.args[0]);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append("  ");
                stringBuilder.append(str2);
                stringBuilder.append("  ");
                stringBuilder.append(str3);
                XposedBridge.log(stringBuilder.toString());
                XposedBridge.log("调用增加数据方法==>支付宝");
//                Intent intent = new Intent();
//                intent.putExtra("money", str);
//                intent.putExtra("mark", str2);
//                intent.putExtra("type", PayAccountManager.TYPE_ALIPAY);
//                intent.putExtra("payurl", str3);
//                intent.setAction(StringConstant.QRCODERECEIVED_ACTION);
//                context.sendBroadcast(intent);
            }
        });
        XposedHelpers.findAndHookMethod("com.alipay.mobile.security.authcenter.service.AuthServiceImpl", classLoader, "getUserInfo", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                super.afterHookedMethod(methodHookParam);
//                methodHookParam = methodHookParam.getResult();
//                PayHelperUtils.sendLoginIdAndMobile((String) XposedHelpers.callMethod(methodHookParam, "getLogonId", new Object[0]), (String) XposedHelpers.callMethod(methodHookParam, "getMobileNumber", new Object[0]), PayAccountManager.TYPE_ALIPAY, context);
            }
        });

        XposedHelpers.findAndHookMethod("com.alipay.mobile.quinox.LauncherActivity", classLoader, "onResume", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                super.afterHookedMethod(methodHookParam);
//                AliPayHook.getUUID(classLoader, context);
//                AliPayHook.this.checkTransferWork(context, classLoader);
            }
        });


    }
    private void checkTransferWork(Context context, ClassLoader classLoader) {
//        int i = PayHelperUtils.getXSharedPreferences().getInt(SettingSpUtil.TRANSFER_MODE, -1);
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("TRANSFERMODE ");
//        stringBuilder.append(i);
//        XposedBridge.log(stringBuilder.toString());
//        if (i == 0) {
//            XposedBridge.log("start transfer hook");
//            new AlipayHook_transfer().hook(context, classLoader);
//        }
    }


    private void getUUID(ClassLoader classLoader, Context context) {
        try {
            Class findClass = XposedHelpers.findClass("com.alipay.mobile.framework.AlipayApplication", classLoader);
            Class<?> xx= XposedHelpers.findClass("com.alipay.mobile.personalbase.service.SocialSdkContactService", classLoader);
            Object ox = XposedHelpers.getObjectField(XposedHelpers.callMethod(XposedHelpers.callMethod(XposedHelpers.callMethod(XposedHelpers.callStaticMethod(findClass, "getInstance", new Object[0]), "getMicroApplicationContext", new Object[0]), "findServiceByInterface", xx.getName()), "getMyAccountInfoModelByLocal", new Object[0]), "userId").toString();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("UUID :: ");
            stringBuilder.append(ox);
            XposedBridge.log(stringBuilder.toString());
//            PayHelperUtils.sendLoginId(classLoader, "alipay_uuid", context);
        } catch (Exception classLoader2) {
            XposedBridge.log(classLoader2.getMessage());
        }
    }

    private void securityCheckHook(ClassLoader classLoader) {
        XposedHelpers.findClass("com.alipay.mobile.base.security.CI", classLoader);

        final Class<?> clazz = XposedHelpers.findClass("", classLoader);
        XposedHelpers.findAndHookMethod(clazz, "a", String.class, String.class, new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                Object result = methodHookParam.getResult();
                XposedBridge.log("securityCheckHook  -- > ");
                XposedHelpers.setBooleanField(result, "a", false);
                methodHookParam.setResult(result);
                super.afterHookedMethod(methodHookParam);
            }
        });
        XposedHelpers.findAndHookMethod(clazz, "a", Class.class, String.class, String.class, new XC_MethodReplacement() {
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) {
                return Byte.valueOf((byte) 1);
            }
        });
        XposedHelpers.findAndHookMethod(clazz, "a", ClassLoader.class, String.class, new XC_MethodReplacement() {
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) {
                return Byte.valueOf((byte) 1);
            }
        });
        XposedHelpers.findAndHookMethod(clazz, "a", new XC_MethodReplacement() {
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) {
                return Boolean.valueOf(null);
            }
        });

    }

    public static String getTextCenter(String str,String str2,String str3){
        try {
            int indexOf = str.indexOf(str2) + str2.length();
            return str.substring(indexOf, str.indexOf(str3, indexOf));
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    public static String formatDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
    }

    public static void tradeOrderQuery(final Context context, final String str) {
        if (TextUtils.isEmpty(str)) {
            XposedBridge.log("获取cookie异常");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("cookie:");
        stringBuilder.append(str);
        XposedBridge.log(stringBuilder.toString());
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - 864000000;
        String formatDate = formatDate();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("https://mbillexprod.alipay.com/enterprise/simpleTradeOrderQuery.json?beginTime=");
        stringBuilder2.append(j);
        stringBuilder2.append("&limitTime=");
        stringBuilder2.append(currentTimeMillis);
        stringBuilder2.append("&pageSize=20&pageNum=1&channelType=ALL");
        String stringBuilder3 = stringBuilder2.toString();
//        HttpUtils httpUtils = new HttpUtils(15000);
//        httpUtils.configResponseTextCharset("GBK");
//        RequestParams requestParams = new RequestParams();
//        requestParams.addHeader(SM.COOKIE, str);
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("https://render.alipay.com/p/z/merchant-mgnt/simple-order.html?beginTime=");
        stringBuilder2.append(formatDate);
        stringBuilder2.append("&endTime=");
        stringBuilder2.append(formatDate);
        stringBuilder2.append("&fromBill=true&channelType=ALL");
//        requestParams.addHeader("Referer", stringBuilder2.toString());
//        httpUtils.send(HttpMethod.GET, stringBuilder3, requestParams, new RequestCallBack<String>() {
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                String str = (String) responseInfo.result;
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append("历史订单:");
//                stringBuilder.append(str);
//                XposedBridge.log(stringBuilder.toString());
//                try {
//                    JSONObject jSONObject = new JSONObject(str);
//                    if (jSONObject.has("result") && jSONObject.getJSONObject("result").has("list")) {
//                        responseInfo = new JSONObject(str).getJSONObject("result").getJSONArray("list");
//                        String str2 = "";
//                        if (responseInfo != null && responseInfo.length() > 0) {
//                            StringBuilder stringBuilder2 = new StringBuilder();
//                            stringBuilder2.append("最近历史订单数量:");
//                            stringBuilder2.append(responseInfo.length());
//                            XposedBridge.log(stringBuilder2.toString());
//                            responseInfo = responseInfo.optJSONObject(0).optString("tradeNo");
//                            if (jSONObject.has("orderTradeForm") && jSONObject.getJSONObject("orderTradeForm").has("billUserId")) {
//                                str2 = jSONObject.getJSONObject("orderTradeForm").getString("billUserId");
//                            }
//                            AliPayHook.getPayedDetial(context, responseInfo, str2, str);
//                        }
//                    }
//                } catch (ResponseInfo<String> responseInfo2) {
//                    stringBuilder = new StringBuilder();
//                    stringBuilder.append("获取tradeNo异常");
//                    stringBuilder.append(responseInfo2.getMessage());
//                    XposedBridge.log(stringBuilder.toString());
//                }
//            }
//
//            public void onFailure(HttpException httpException, String str) {
//                XposedBridge.log(str);
//            }
//        });
    }

    public static String getCookie(ClassLoader classLoader) {
        String str = "";
        Object callStaticMethod = XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.alipay.mobile.common.transportext.biz.appevent.AmnetUserInfo", classLoader), "getSessionid", new Object[0]);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getSessionid   --- > ");
        stringBuilder.append(callStaticMethod.toString());
        XposedBridge.log(stringBuilder.toString());
        if (((Context) XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.alipay.mobile.common.transportext.biz.shared.ExtTransportEnv", classLoader), "getAppContext", new Object[0])) == null) {
            XposedBridge.log(" context为空");
        } else if (XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.alipay.mobile.common.helper.ReadSettingServerUrl", classLoader), "getInstance", new Object[0]) != null) {
            String str2 = (String) XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.alipay.mobile.common.transport.http.GwCookieCacheHelper", classLoader), "getCookie", ".alipay.com");
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("getCookie :::   ==== > ");
            stringBuilder2.append(str2);
            XposedBridge.log(stringBuilder2.toString());
            return str2;
        } else {
            XposedBridge.log(" readSettingServerUrl为空");
            XposedBridge.log("readSettingServerUrl为空");
        }
        return str;
    }
}
