package org.baiyu.fuckgram;

import android.app.Activity;import android.widget.Toast;
import java.util.ArrayList;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    private final static long DEFAULT_MAX_FILE_SIZE = 1024L * 1024L * 2001L;
    private final static int downloadChunkSizeBig = 1024 * 1024;
    private final static int maxDownloadRequests = 8;
    private final static int maxDownloadRequestsBig = 8;
    private final static int maxCdnParts = (int) (DEFAULT_MAX_FILE_SIZE / downloadChunkSizeBig);
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        try {
            // message force forward-able
            XposedBridge.hookAllMethods(
                    XposedHelpers.findClass(
                            "org.telegram.messenger.MessagesController", lpparam.classLoader),
                    "isChatNoForwards",
                    XC_MethodReplacement.returnConstant(false));
            // remove sponsored ads
            XposedBridge.hookAllMethods(
                    XposedHelpers.findClass(
                            "org.telegram.messenger.MessagesController", lpparam.classLoader),
                    "getSponsoredMessages",
                    XC_MethodReplacement.returnConstant(null));
            XposedBridge.hookAllMethods(
                    XposedHelpers.findClass(
                            "org.telegram.ui.ChatActivity", lpparam.classLoader),
                    "addSponsoredMessages",
                    XC_MethodReplacement.returnConstant(null));
            // disable reaction list in context menu
            XposedBridge.hookAllMethods(
                    XposedHelpers.findClass(
                            "org.telegram.messenger.MediaDataController", lpparam.classLoader),
                    "getEnabledReactionsList",
                    XC_MethodReplacement.returnConstant(new ArrayList<>()));
            // disable double tap quick reaction
            XposedBridge.hookAllMethods(
                    XposedHelpers.findClass(
                            "org.telegram.ui.ChatActivity", lpparam.classLoader),
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
            // speed up download
            XposedBridge.hookAllMethods(
                    XposedHelpers.findClass(
                            "org.telegram.messenger.FileLoadOperation", lpparam.classLoader),
                    "updateParams",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            XposedHelpers.setIntField(param.thisObject, "downloadChunkSizeBig", downloadChunkSizeBig);
                            XposedHelpers.setObjectField(param.thisObject, "maxDownloadRequests", maxDownloadRequests);
                            XposedHelpers.setObjectField(param.thisObject, "maxDownloadRequestsBig", maxDownloadRequestsBig);
                            XposedHelpers.setObjectField(param.thisObject, "maxCdnParts", maxCdnParts);
                            param.setResult(null);
                        }
                    });
        } catch (Throwable t) {
            XposedBridge.log(t);
            XposedHelpers.findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    Toast.makeText((Activity) param.thisObject, "FuckGram: Hook failed, check log for details", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
