package org.baiyu.fuckgram;

import java.util.ArrayList;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    private static final long DEFAULT_MAX_FILE_SIZE = 1024L * 1024L * 2001L;
    private static final int downloadChunkSizeBig = 1024 * 1024;
    private static final int maxDownloadRequests = 8;
    private static final int maxDownloadRequestsBig = 8;
    private static final int maxCdnParts = (int) (DEFAULT_MAX_FILE_SIZE / downloadChunkSizeBig);

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        // message force forward-able
        hookAllMethods(
                "org.telegram.messenger.MessagesController",
                lpparam.classLoader,
                "isChatNoForwards",
                XC_MethodReplacement.returnConstant(false));

        // remove sponsored ads
        hookAllMethods(
                "org.telegram.messenger.MessagesController",
                lpparam.classLoader,
                "getSponsoredMessages",
                XC_MethodReplacement.returnConstant(null));
        hookAllMethods(
                "org.telegram.ui.ChatActivity",
                lpparam.classLoader,
                "addSponsoredMessages",
                XC_MethodReplacement.returnConstant(null));

        // disable reaction list in context menu
        hookAllMethods(
                "org.telegram.messenger.MediaDataController",
                lpparam.classLoader,
                "getEnabledReactionsList",
                XC_MethodReplacement.returnConstant(new ArrayList<>()));

        // disable double tap quick reaction
        hookAllMethods(
                "org.telegram.ui.ChatActivity",
                lpparam.classLoader,
                "selectReaction",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        if (param.args[6] instanceof Boolean && (Boolean) param.args[6]) {
                            param.setResult(null);
                        }
                    }
                });

        // lock premium feature
        hookAllConstructors(
                "org.telegram.messenger.MessagesController",
                lpparam.classLoader,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        XposedHelpers.setBooleanField(param.thisObject, "premiumLocked", true);
                    }
                });

        // remove premium emoji set
        // public EmojiTabsStrip(Context context, Theme.ResourcesProvider
        // resourcesProvider, boolean
        // includeStandard, boolean includeAnimated, int type, Runnable onSettingsOpen)
        hookAllConstructors(
                "org.telegram.ui.Components.EmojiTabsStrip",
                lpparam.classLoader,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        param.args[3] = false;
                    }
                });

        // speed up download
        hookAllMethods(
                "org.telegram.messenger.FileLoadOperation",
                lpparam.classLoader,
                "updateParams",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        XposedHelpers.setIntField(
                                param.thisObject, "downloadChunkSizeBig", downloadChunkSizeBig);
                        XposedHelpers.setIntField(
                                param.thisObject, "maxDownloadRequests", maxDownloadRequests);
                        XposedHelpers.setIntField(
                                param.thisObject, "maxDownloadRequestsBig", maxDownloadRequestsBig);
                        XposedHelpers.setIntField(
                                param.thisObject, "maxCdnParts", maxCdnParts);
                        param.setResult(null);
                    }
                });
    }

    private void hookAllMethods(
            String className, ClassLoader classLoader, String methodName, XC_MethodHook callback) {

        Class<?> clazz = XposedHelpers.findClass(className, classLoader);
        if (clazz == null) {
            XposedBridge.log("Class not found: " + className);
            return;
        }
        XposedBridge.hookAllMethods(clazz, methodName, callback);
    }

    private void hookAllConstructors(
            String className, ClassLoader classLoader, XC_MethodHook callback) {

        Class<?> clazz = XposedHelpers.findClass(className, classLoader);
        if (clazz == null) {
            XposedBridge.log("Class not found: " + className);
            return;
        }
        XposedBridge.hookAllConstructors(clazz, callback);
    }
}
