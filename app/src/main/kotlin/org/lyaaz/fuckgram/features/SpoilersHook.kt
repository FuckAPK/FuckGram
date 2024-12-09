package org.lyaaz.fuckgram.features

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.messageObjectClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookModule.Companion.spoilerEffectClass
import org.lyaaz.fuckgram.HookUtils.hookMethods

object SpoilersHook : HookModule {
    override fun enabled(): Boolean {
        return settings.prohibitSpoilers()
    }

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam): Boolean {
        return hookMethods(
            spoilerEffectClass,
            "addSpoilers",
            XC_MethodReplacement.returnConstant(null)
        ) and hookMethods(
            messageObjectClass,
            "hasMediaSpoilers",
            XC_MethodReplacement.returnConstant(false)
        )
    }
}