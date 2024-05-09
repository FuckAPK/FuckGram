package org.baiyu.fuckgram

import android.content.SharedPreferences
import android.view.View
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        // message force forward-able
        if (settings.enableForceForward()) {
            hookAllMethods(
                "org.telegram.messenger.MessagesController",
                lpparam.classLoader,
                "isChatNoForwards",
                XC_MethodReplacement.returnConstant(false)
            )
        }

        // remove sponsored ads
        if (settings.enableRemoveSponsoredAds()) {
            hookAllMethods(
                "org.telegram.messenger.MessagesController",
                lpparam.classLoader,
                "getSponsoredMessages",
                XC_MethodReplacement.returnConstant(null)
            )
            hookAllMethods(
                "org.telegram.ui.ChatActivity",
                lpparam.classLoader,
                "addSponsoredMessages",
                XC_MethodReplacement.returnConstant(null)
            )
        }

        // disable reaction list in context menu
        if (settings.disableReactionPopup()) {
            hookAllMethods(
                "org.telegram.messenger.MediaDataController",
                lpparam.classLoader,
                "getEnabledReactionsList",
                XC_MethodReplacement.returnConstant(ArrayList<Any>())
            )
        }

        // disable double tap quick reaction
        if (settings.disableQuickReaction()) {
            hookAllMethods(
                "org.telegram.ui.ChatActivity",
                lpparam.classLoader,
                "selectReaction",
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (param.args[6] is Boolean && param.args[6] as Boolean) {
                            param.result = null
                        }
                    }
                })
        }

        // lock premium feature
        if (settings.lockPremiumFeatures()) {
            hookAllConstructors(
                "org.telegram.messenger.MessagesController",
                lpparam.classLoader,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: MethodHookParam) {
                        XposedHelpers.setBooleanField(param.thisObject, "premiumLocked", true)
                    }
                })
        }

        // remove premium emoji set
        // public EmojiTabsStrip(Context context, Theme.ResourcesProvider
        // resourcesProvider, boolean
        // includeStandard, boolean includeAnimated, int type, Runnable onSettingsOpen)
        if (settings.enableRemoveEmojiSet()) {
            hookAllConstructors(
                "org.telegram.ui.Components.EmojiTabsStrip",
                lpparam.classLoader,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        param.args[3] = false
                    }
                })
        }

        // speed up download
        if (settings.enableSpeedUpDownload()) {
            hookAllMethods(
                "org.telegram.messenger.FileLoadOperation",
                lpparam.classLoader,
                "updateParams",
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        XposedHelpers.setIntField(
                            param.thisObject, "downloadChunkSizeBig", downloadChunkSizeBig
                        )
                        XposedHelpers.setIntField(
                            param.thisObject, "maxDownloadRequests", maxDownloadRequests
                        )
                        XposedHelpers.setIntField(
                            param.thisObject, "maxDownloadRequestsBig", maxDownloadRequestsBig
                        )
                        XposedHelpers.setIntField(
                            param.thisObject, "maxCdnParts", maxCdnParts
                        )
                        param.result = null
                    }
                })
        }

        // disable tracking
        if (settings.disableTracking()) {
            hookAllMethods(
                "org.telegram.ui.ChatActivity",
                lpparam.classLoader,
                "logSponsoredClicked",
                XC_MethodReplacement.returnConstant(null)
            )
        }

        // disable swipe
        if (settings.disableChatSwipe()) {
            hookAllMethods(
                "org.telegram.ui.Cells.DialogCell",
                lpparam.classLoader,
                "getTranslationX",
                XC_MethodReplacement.returnConstant(0f)
            )
            hookAllMethods(
                "org.telegram.ui.Cells.DialogCell",
                lpparam.classLoader,
                "setTranslationX",
                XC_MethodReplacement.returnConstant(null)
            )
            hookAllMethods(
                "org.telegram.ui.DialogsActivity.SwipeController",
                lpparam.classLoader,
                "onSwiped",
                XC_MethodReplacement.returnConstant(null)
            )
            hookAllMethods(
                "org.telegram.messenger.SharedConfig",
                lpparam.classLoader,
                "getChatSwipeAction",
                XC_MethodReplacement.returnConstant(-1)
            )
        }

        // disable channel bottom button
        if (settings.disableChannelBottomButton()) {
            hookAllMethods(
                "org.telegram.ui.ChatActivity",
                lpparam.classLoader,
                "updateBottomOverlay",
//                XC_MethodReplacement.returnConstant(null)
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val chatActivity = param.thisObject
                        val bottomOverlayChatText = XposedHelpers.getObjectField(
                            chatActivity, "bottomOverlayChatText"
                        ) ?: return
                        val text = XposedHelpers.getObjectField(
                            bottomOverlayChatText, "lastText"
                        ) as CharSequence?
                        if (text.isNullOrBlank()) {
                            return
                        }
                        val setEnabled by lazy {
                            XposedHelpers.findMethodExact(
                                View::class.java, "setEnabled", Boolean::class.java
                            )
                        }
                        val localeController = XposedHelpers.findClass(
                            "org.telegram.messenger.LocaleController",
                            lpparam.classLoader
                        )
                        val string = XposedHelpers.findClass(
                            "org.telegram.messenger.R\$string",
                            lpparam.classLoader
                        )
                        val getString: (String, String) -> String? = { key1, key2 ->
                            XposedHelpers.callStaticMethod(
                                localeController,
                                "getString",
                                key1,
                                XposedHelpers.getStaticIntField(string, key2)
                            ) as String?
                        }
                        val unMuteStr by lazy {
                            getString("ChannelUnmute", "ChannelUnmute")
                        }
                        val muteStr by lazy {
                            getString("ChannelMute", "ChannelMute")
                        }
                        if (text.toString() == unMuteStr || text.toString() == muteStr) {
                            setEnabled.invoke(bottomOverlayChatText, false)
                        }
                    }
                }
            )
        }
    }

    private fun hookAllMethods(
        className: String, classLoader: ClassLoader, methodName: String, callback: XC_MethodHook
    ) {
        val clazz = XposedHelpers.findClass(className, classLoader)
        if (clazz == null) {
            XposedBridge.log("Class not found: $className")
            return
        }
        try {
            XposedBridge.hookAllMethods(clazz, methodName, callback)
        } catch (t: Throwable) {
            XposedBridge.log(t)
        }
    }

    private fun hookAllConstructors(
        className: String, classLoader: ClassLoader, callback: XC_MethodHook
    ) {
        val clazz = XposedHelpers.findClass(className, classLoader)
        if (clazz == null) {
            XposedBridge.log("Class not found: $className")
            return
        }
        try {
            XposedBridge.hookAllConstructors(clazz, callback)
        } catch (t: Throwable) {
            XposedBridge.log(t)
        }
    }

    companion object {
        private val prefs: SharedPreferences by lazy {
            XSharedPreferences(BuildConfig.APPLICATION_ID)
        }
        private val settings: Settings by lazy {
            Settings.getInstance(prefs)
        }
        private const val DEFAULT_MAX_FILE_SIZE = 1024L * 1024L * 2001L
        private const val downloadChunkSizeBig = 1024 * 1024
        private const val maxDownloadRequests = 8
        private const val maxDownloadRequestsBig = 8
        private const val maxCdnParts = (DEFAULT_MAX_FILE_SIZE / downloadChunkSizeBig).toInt()
    }
}
