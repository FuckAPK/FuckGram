package org.lyaaz.fuckgram

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
    override fun handleLoadPackage(lpparam1: LoadPackageParam) {
        lpparam = lpparam1

        if (settings.enableForceForward()) {
            hookMethods(
                messagesControllerClass,
                "isChatNoForwards",
                XC_MethodReplacement.returnConstant(false)
            )
        }

        if (settings.enableRemoveSponsoredAds()) {
            hookMethods(
                messagesControllerClass,
                "getSponsoredMessages",
                XC_MethodReplacement.returnConstant(null)
            )
            hookMethods(
                chatActivityClass,
                "addSponsoredMessages",
                XC_MethodReplacement.returnConstant(null)
            )
        }

        if (settings.disableReactionPopup()) {
            hookMethods(
                mediaDataControllerClass,
                "getEnabledReactionsList",
                XC_MethodReplacement.returnConstant(emptyList<Any>())
            )
        }

        if (settings.disableQuickReaction()) {
            hookMethods(chatActivityClass, "selectReaction", object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (param.args[6] is Boolean && param.args[6] as Boolean) {
                        param.result = null
                    }
                }
            })
        }

        if (settings.lockPremiumFeatures()) {
            hookConstructors(messagesControllerClass, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun afterHookedMethod(param: MethodHookParam) {
                    XposedHelpers.setBooleanField(param.thisObject, "premiumLocked", true)
                }
            })
            hookMethods(messagesControllerClass, "applyAppConfig", object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun afterHookedMethod(param: MethodHookParam) {
                    XposedHelpers.setBooleanField(param.thisObject, "premiumLocked", true)
                }
            })
        }

        if (settings.enableRemoveEmojiSet()) {
            hookConstructors(emojiTabsStripClass, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    when (param.args.size) {
                        6 -> param.args[3] = false
                        8 -> param.args[4] = false
                    }
                }
            })
        }

        if (settings.enableSpeedUpDownload()) {
            hookMethods(fileLoadOperationClass, "updateParams", object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    XposedHelpers.setIntField(
                        param.thisObject,
                        "downloadChunkSizeBig",
                        Settings.DOWNLOAD_CHUNK_SIZE_BIG
                    )
                    XposedHelpers.setIntField(
                        param.thisObject,
                        "maxDownloadRequests",
                        Settings.MAX_DOWNLOAD_REQUESTS
                    )
                    XposedHelpers.setIntField(
                        param.thisObject,
                        "maxDownloadRequestsBig",
                        Settings.MAX_DOWNLOAD_REQUESTS_BIG
                    )
                    XposedHelpers.setIntField(
                        param.thisObject,
                        "maxCdnParts",
                        Settings.MAX_CDN_PARTS
                    )
                    param.result = null
                }
            })
        }

        if (settings.disableTracking()) {
            hookMethods(
                chatActivityClass,
                "logSponsoredClicked",
                XC_MethodReplacement.returnConstant(null)
            )
        }

        if (settings.disableChatSwipe()) {
            hookMethods(dialogCellClass, "getTranslationX", XC_MethodReplacement.returnConstant(0f))
            hookMethods(
                dialogCellClass,
                "setTranslationX",
                XC_MethodReplacement.returnConstant(null)
            )
            hookMethods(
                dialogSwipeControllerClass,
                "onSwiped",
                XC_MethodReplacement.returnConstant(null)
            )
            hookMethods(
                sharedConfigClass,
                "getChatSwipeAction",
                XC_MethodReplacement.returnConstant(-1)
            )
        }

        if (settings.disableChannelBottomButton()) {
            hookMethods(chatActivityClass, "updateBottomOverlay", object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun afterHookedMethod(param: MethodHookParam) {
                    val chatActivity = param.thisObject
                    val bottomOverlayChatText =
                        XposedHelpers.getObjectField(chatActivity, "bottomOverlayChatText")
                            ?: return
                    val text = XposedHelpers.getObjectField(
                        bottomOverlayChatText,
                        "lastText"
                    ) as? CharSequence ?: return

                    val setEnabled by lazy {
                        XposedHelpers.findMethodExact(
                            View::class.java,
                            "setEnabled",
                            Boolean::class.java
                        )
                    }
                    val getString: (String, String) -> String? = { key1, key2 ->
                        XposedHelpers.callStaticMethod(
                            localeControllerClass,
                            "getString",
                            key1,
                            XposedHelpers.getStaticIntField(rStringClass, key2)
                        ) as? String
                    }

                    val unMuteStr by lazy { getString("ChannelUnmute", "ChannelUnmute") }
                    val muteStr by lazy { getString("ChannelMute", "ChannelMute") }

                    if (text.toString() == unMuteStr || text.toString() == muteStr) {
                        setEnabled.invoke(bottomOverlayChatText, false)
                    }
                }
            })
        }

        if (settings.prohibitSpoilers()) {
            hookMethods(
                spoilerEffectClass,
                "addSpoilers",
                XC_MethodReplacement.returnConstant(null)
            )
            hookMethods(
                messageObjectClass,
                "hasMediaSpoilers",
                XC_MethodReplacement.returnConstant(false)
            )
        }
    }

    private fun logHookError(className: String, methodName: String, t: Throwable) {
        XposedBridge.log("Failed to hook $className::$methodName")
        XposedBridge.log(t)
    }

    private fun hookMethods(clazz: Class<*>, methodName: String, callback: XC_MethodHook) {
        try {
            XposedBridge.hookAllMethods(clazz, methodName, callback)
        } catch (t: Throwable) {
            logHookError(clazz.name, methodName, t)
        }
    }

    private fun hookConstructors(clazz: Class<*>, callback: XC_MethodHook) {
        try {
            XposedBridge.hookAllConstructors(clazz, callback)
        } catch (t: Throwable) {
            logHookError(clazz.name, "<init>", t)
        }
    }

    private companion object {
        lateinit var lpparam: LoadPackageParam

        val prefs: SharedPreferences by lazy { XSharedPreferences(BuildConfig.APPLICATION_ID) }
        val settings: Settings by lazy { Settings.getInstance(prefs) }

        fun getClass(className: String): Class<*> {
            return XposedHelpers.findClass(className, lpparam.classLoader)
        }

        val messagesControllerClass: Class<*> by lazy { getClass("org.telegram.messenger.MessagesController") }
        val chatActivityClass: Class<*> by lazy { getClass("org.telegram.ui.ChatActivity") }
        val mediaDataControllerClass: Class<*> by lazy { getClass("org.telegram.messenger.MediaDataController") }
        val fileLoadOperationClass: Class<*> by lazy { getClass("org.telegram.messenger.FileLoadOperation") }
        val dialogCellClass: Class<*> by lazy { getClass("org.telegram.ui.Cells.DialogCell") }
        val emojiTabsStripClass: Class<*> by lazy { getClass("org.telegram.ui.Components.EmojiTabsStrip") }
        val sharedConfigClass: Class<*> by lazy { getClass("org.telegram.messenger.SharedConfig") }
        val spoilerEffectClass: Class<*> by lazy { getClass("org.telegram.ui.Components.spoilers.SpoilerEffect") }
        val localeControllerClass: Class<*> by lazy { getClass("org.telegram.messenger.LocaleController") }
        val rStringClass: Class<*> by lazy { getClass("org.telegram.messenger.R\$string") }
        val messageObjectClass: Class<*> by lazy { getClass("org.telegram.messenger.MessageObject") }
        val dialogSwipeControllerClass: Class<*> by lazy { getClass("org.telegram.ui.DialogsActivity\$SwipeController") }
    }
}
