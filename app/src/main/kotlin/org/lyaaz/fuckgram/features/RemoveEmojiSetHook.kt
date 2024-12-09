package org.lyaaz.fuckgram.features

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.emojiTabsStripClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookConstructors

object RemoveEmojiSetHook : HookModule {
    override fun enabled(): Boolean {
        return settings.enableRemoveEmojiSet()
    }

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam): Boolean {
        return hookConstructors(emojiTabsStripClass, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                when (param.args.size) {
                    6 -> param.args[3] = false
                    8 -> param.args[4] = false
                }
            }
        })
    }
}