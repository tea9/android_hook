package com.demo.android_hook.alipay;

import android.content.Context;
import android.util.Log;

import com.demo.android_hook.ICracker;
import com.demo.android_hook.util.XPConstant;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * created by tea9 at 2019/1/30
 */
public class AlipayCracker implements ICracker {

    private static final String TAG = "AlipayCracker";

    private static AlipayCracker _instance;
    private Context mAlipayContext;
    private ClassLoader mLoader;
    private boolean mIsLoaded = false;
    private Object mCollectMoneyRpc;

    // 从hy来的安全检查
    private void securityCheckHook(ClassLoader classLoader) {
        final Class<?> clazz =XposedHelpers.findClass("com.alipay.mobile.base.security.CI", classLoader);

//         = XposedHelpers.findClass("a", classLoader);
        XposedHelpers.findAndHookMethod(clazz, "a", String.class, String.class, new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                Object result = methodHookParam.getResult();
                XposedBridge.log("securityCheckHookaaaa  -- > ");
                XposedHelpers.setBooleanField(result, "a", false);
                methodHookParam.setResult(result);
                super.afterHookedMethod(methodHookParam);
            }
        });
        XposedHelpers.findAndHookMethod(clazz, "a", Class.class, String.class, String.class, new XC_MethodReplacement() {
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) {
                XposedBridge.log("securityCheckHookaaaa  -- > ");
                return Byte.valueOf((byte) 1);
            }
        });
        XposedHelpers.findAndHookMethod(clazz, "a", ClassLoader.class, String.class, new XC_MethodReplacement() {
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) {
                XposedBridge.log("securityCheckHookaaaa  -- > ");
                return Byte.valueOf((byte) 1);
            }
        });
        XposedHelpers.findAndHookMethod(clazz, "a", new XC_MethodReplacement() {
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) {
                return Boolean.valueOf(null);
            }
        });

    }

    public static AlipayCracker instance() {
        XposedBridge.log("AlipayCracker instance");
        if (_instance == null) {
            synchronized (AlipayCracker.class) {
                if (_instance == null) {
                    _instance = new AlipayCracker();
                }
            }
        }
        return _instance;
    }

    private void hookAppContext(){
        try {
            Log.d(TAG, "hookAppContext");
            final Class<?> launcherClazz = mLoader.loadClass(XPConstant.LAUNCHER_AGENT_CLASS);
            findAndHookMethod(launcherClazz, "init", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Log.d(TAG, "LauncherApplicationAgent init()");
                    Object launcherInstance = param.thisObject;
                    Field applicationContext = launcherClazz.getDeclaredField("mMicroApplicationContext");
                    applicationContext.setAccessible(true);
                    Object microApplicationContext = applicationContext.get(launcherInstance);
                    Log.d(TAG, "get mMicroApplicationContext: " + microApplicationContext);
                    hookRpcService(microApplicationContext);
                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void hookRpcService(Object microApplicationContext){
        Log.d(TAG, "hookQRMoneyRpcService");
        try {
            Class<?> applicationClazz = mLoader.loadClass(XPConstant.CONTEXT_IMPL_CLASS);
            Class<?> collectMoneyRpcClazz = mLoader.loadClass(XPConstant.MONEY_RPC_CLASS);
            Method getRpcMethod = applicationClazz.getMethod("findServiceByInterface", String.class);
            getRpcMethod.setAccessible(true);
            Log.d(TAG, "before invoke");
            Object rpcServiceInstance = getRpcMethod.invoke(microApplicationContext, XPConstant.RPC_SERVICE_CLASS);
            Log.d(TAG, "after invoke");
            Method getCollectMoneyMethod = rpcServiceInstance.getClass().getDeclaredMethod("getRpcProxy", Class.class);
            getCollectMoneyMethod.setAccessible(true);
            mCollectMoneyRpc = getCollectMoneyMethod.invoke(rpcServiceInstance, collectMoneyRpcClazz);
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }
    private int index=1;

    private AlipayCracker() {
        XposedBridge.log("AlipayCracker: ()");
        String consultReq = "com.alipay.transferprod.rpc.req.ConsultSetAmountReq";
        String consultRes = "com.alipay.transferprod.rpc.result.ConsultSetAmountRes";
        Observable.interval(10,5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .map(aLong -> {

                    XposedBridge.log("AlipayCracker: () timer timer timer timer"+index);

                    // ConsultSetAmountReq
                    Class<?> consultReqClazz = mLoader.loadClass(consultReq);
                    Object consultReqInstance = consultReqClazz.newInstance();
                    // Field amount
                    Field amountField = consultReqClazz.getField("amount");
//                    amountField.set(consultReqInstance, "" + money);
                    amountField.set(consultReqInstance, "" + 1.0);
                    // Field desc
                    Field descField = consultReqClazz.getField("desc");
//                    descField.set(consultReqInstance, des);
                    descField.set(consultReqInstance, "tea desc");

                    // Method consultSetAmount
                    Method method = mCollectMoneyRpc.getClass().getMethod("consultSetAmount", consultReqClazz);
                    method.setAccessible(true);
                    Object resInstance = method.invoke(mCollectMoneyRpc, consultReqInstance);

                    // ConsultSetAmountRes
                    Class<?> consultResClazz = mLoader.loadClass(consultRes);
                    Field codeIdField = consultResClazz.getDeclaredField("codeId");
                    codeIdField.setAccessible(true);
                    String codeId = (String) codeIdField.get(resInstance);
                    Field printQrCodeUrlField = consultResClazz.getDeclaredField("printQrCodeUrl");
                    String printQrCodeUrl = (String) printQrCodeUrlField.get(resInstance);
                    Field qrCodeUrlField = consultResClazz.getDeclaredField("qrCodeUrl");
                    String qrCodeUrl = (String) qrCodeUrlField.get(resInstance);
                    Log.d(TAG, String.format("RPC request collect money, codeId: %s, printQrCodeUrl: %s, qrCodeUrl: %s->",
                            codeId, printQrCodeUrl, qrCodeUrl)+index);

                    XposedBridge.log("AlipayCracker: " +String.format("RPC request collect money, codeId: %s, printQrCodeUrl: %s, qrCodeUrl: %s->",
                            codeId, printQrCodeUrl, qrCodeUrl)+index);

                    //printQrCodeUrl
                    // TODO THIS
                    index++;
                    return qrCodeUrl;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(codeUrl -> {
                    Log.e(TAG,"codeUrl:"+codeUrl);
//                    Bitmap qrImage = createQRBitmap(icon, codeUrl);
//                    postProcessQRImage(qrImage);
                }, throwable -> Log.e(TAG, throwable.getMessage()));
    }

    @Override
    public void handleLoadPackage(ClassLoader loader) throws Throwable {
        if (isReadyForUse()){
            return;
        }
        mIsLoaded = true;

        hookStartCommandService(loader);
    }



    @Override
    public boolean isReadyForUse() {
        return false;
    }

    private void hookStartCommandService(ClassLoader loader){
        findAndHookMethod(XPConstant.APPLICATION_CLASS, loader, "attachBaseContext", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(TAG, "hookStartCommandService->afterHookedMethod: " + param.args[0]);
                mAlipayContext = (Context) param.args[0];
//                QRTool.instance().init(mAlipayContext);
                mLoader = mAlipayContext.getClassLoader();
//                mQRCode = new AlipayQR(AlipayCracker.this);
//                mQRCode.handleLoadPackage(mLoader);
//                connectToServer();


//
//

                //applicationContext赋值
                hookAppContext();
//                securityCheckHook(mLoader);

                // 168
//                XposedHelpers.findAndHookMethod("com.alipay.mobile.antui.basic.AUDialog", mLoader, "show", new XC_MethodHook() {
//                    //170
//                    protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
//                        Object obj = XposedHelpers.findField(methodHookParam.thisObject.getClass(), "b").get(methodHookParam.thisObject);
//                        Object obj2 = XposedHelpers.findField(methodHookParam.thisObject.getClass(), "c").get(methodHookParam.thisObject);
//                        Intent intent = ((Activity) methodHookParam.thisObject).getIntent();
//                        String stringExtra = intent.getStringExtra("mark");
//                        String stringExtra2 = intent.getStringExtra("money");
//                        XposedHelpers.callMethod(obj, "setText", stringExtra2);
//                        XposedHelpers.callMethod(obj2, "setText", stringExtra);
//                        ((Button) XposedHelpers.findField(methodHookParam.thisObject.getClass(), "e").get(methodHookParam.thisObject)).performClick();
//                    }
//                    protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
////                        PayHelperUtils.sendmsg(context, "本日交易受限，请明日再申请交易");
//                        XposedBridge.log("securityCheckHookaaaa  -- > 交易受限");
//                        XposedBridge.log("本日交易受限，请明日再申请交易");
//                        String simpleName = XposedHelpers.getObjectField(methodHookParam.thisObject, "mContext").getClass().getSimpleName();
//                        StringBuilder stringBuilder = new StringBuilder();
//                        stringBuilder.append(" --- ");
//                        stringBuilder.append(simpleName);
//                        stringBuilder.append("  --  ");
//                        XposedBridge.log(stringBuilder.toString());
//                        if (Arrays.asList(new String[]{"PayeeQRSetMoneyActivity", "TransparentActivity"}).indexOf(simpleName) != -1) {
//                            XposedHelpers.setObjectField(methodHookParam.thisObject, "mContext", null);
//                        }
//                    }
//                });
            }
        });
    }
}
