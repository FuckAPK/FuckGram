package org.lyaaz.fuckgram.features

import android.view.View
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookMoule
import org.lyaaz.fuckgram.HookMoule.Companion.chatActivityClass
import org.lyaaz.fuckgram.HookMoule.Companion.localeControllerClass
import org.lyaaz.fuckgram.HookMoule.Companion.rStringClass
import org.lyaaz.fuckgram.HookMoule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookMethods

object ChannelBottomButtonHook : HookMoule {
    override fun enabled(): Boolean {
        return settings.disableChannelBottomButton()
    }

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam): Boolean {
        return hookMethods(chatActivityClass, "updateBottomOverlay", object : XC_MethodHook() {
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
}