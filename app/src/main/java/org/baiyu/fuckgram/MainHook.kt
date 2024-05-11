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

        val hookAllMethods: (String, String, XC_MethodHook) -> Unit =
            { className, methodName, callback ->
                try {
                    XposedHelpers.findClass(className, lpparam.classLoader).let {
                        XposedBridge.hookAllMethods(it, methodName, callback)
                    }
                } catch (t: Throwable) {
                    XposedBridge.log("Failed to hook $className::$methodName(*)")
                    XposedBridge.log(t)
                }
            }

        val hookAllConstructors: (String, XC_MethodHook) -> Unit =
            { className, callback ->
                try {
                    XposedHelpers.findClass(className, lpparam.classLoader).let {
                        XposedBridge.hookAllConstructors(it, callback)
                    }
                } catch (t: Throwable) {
                    XposedBridge.log("Failed to hook $className::$className(*)")
                    XposedBridge.log(t)
                }
            }

        // message force forward-able
        if (settings.enableForceForward()) {
            hookAllMethods(
                "org.telegram.messenger.MessagesController",
                "isChatNoForwards",
                XC_MethodReplacement.returnConstant(false)
            )
        }

        // remove sponsored ads
        if (settings.enableRemoveSponsoredAds()) {
            hookAllMethods(
                "org.telegram.messenger.MessagesController",
                "getSponsoredMessages",
                XC_MethodReplacement.returnConstant(null)
            )
            hookAllMethods(
                "org.telegram.ui.ChatActivity",
                "addSponsoredMessages",
                XC_MethodReplacement.returnConstant(null)
            )
        }

        // disable reaction list in context menu
        if (settings.disableReactionPopup()) {
            hookAllMethods(
                "org.telegram.messenger.MediaDataController",
                "getEnabledReactionsList",
                XC_MethodReplacement.returnConstant(ArrayList<Any>())
            )
        }

        // disable double tap quick reaction
        if (settings.disableQuickReaction()) {
            hookAllMethods(
                "org.telegram.ui.ChatActivity",
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
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: MethodHookParam) {
                        XposedHelpers.setBooleanField(param.thisObject, "premiumLocked", true)
                    }
                })
        }

        // remove premium emoji set
        // public EmojiTabsStrip(
        //     Context context,
        //     Theme.ResourcesProvider resourcesProvider,
        //     boolean includeStandard,
        //     boolean includeAnimated,
        //     int type,
        //     Runnable onSettingsOpen)
        // or
        // public EmojiTabsStrip(
        //     Context context,
        //     Theme.ResourcesProvider resourcesProvider,
        //     boolean includeRecent,
        //     boolean includeStandard,
        //     boolean includeAnimated,
        //     int type,
        //     Runnable onSettingsOpen,
        //     int accentColor)
        if (settings.enableRemoveEmojiSet()) {
            hookAllConstructors(
                "org.telegram.ui.Components.EmojiTabsStrip",
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        when (param.args.size) {
                            6 -> param.args[3] = false
                            8 -> param.args[4] = false
                        }
                    }
                })
        }

        // speed up download
        if (settings.enableSpeedUpDownload()) {
            hookAllMethods(
                "org.telegram.messenger.FileLoadOperation",
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
                "logSponsoredClicked",
                XC_MethodReplacement.returnConstant(null)
            )
        }

        // disable swipe
        if (settings.disableChatSwipe()) {
            hookAllMethods(
                "org.telegram.ui.Cells.DialogCell",
                "getTranslationX",
                XC_MethodReplacement.returnConstant(0f)
            )
            hookAllMethods(
                "org.telegram.ui.Cells.DialogCell",
                "setTranslationX",
                XC_MethodReplacement.returnConstant(null)
            )
            hookAllMethods(
                "org.telegram.ui.DialogsActivity.SwipeController",
                "onSwiped",
                XC_MethodReplacement.returnConstant(null)
            )
            hookAllMethods(
                "org.telegram.messenger.SharedConfig",
                "getChatSwipeAction",
                XC_MethodReplacement.returnConstant(-1)
            )
        }

        // disable channel bottom button
        if (settings.disableChannelBottomButton()) {
            hookAllMethods(
                "org.telegram.ui.ChatActivity",
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

        // prohibit spoilers
        if (settings.prohibitSpoilers()) {
            hookAllMethods(
                "org.telegram.ui.Components.spoilers.SpoilerEffect",
                "addSpoilers",
                XC_MethodReplacement.returnConstant(null)
            )
            hookAllMethods(
                "org.telegram.messenger.MessageObject",
                "hasMediaSpoilers",
                XC_MethodReplacement.returnConstant(false)
            )
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
