package org.lyaaz.fuckgram.features

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookMoule
import org.lyaaz.fuckgram.HookMoule.Companion.chatActivityClass
import org.lyaaz.fuckgram.HookMoule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookMethods

object QuickReactionHook : HookMoule {
    override fun enabled(): Boolean {
        return settings.disableQuickReaction()
    }

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam): Boolean {
        return hookMethods(chatActivityClass, "selectReaction", object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                if (param.args[6] is Boolean && param.args[6] as Boolean) {
                    param.result = null
                }
            }
        })
    }
}