package org.lyaaz.fuckgram.features

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookMoule
import org.lyaaz.fuckgram.HookMoule.Companion.messageObjectClass
import org.lyaaz.fuckgram.HookMoule.Companion.settings
import org.lyaaz.fuckgram.HookMoule.Companion.spoilerEffectClass
import org.lyaaz.fuckgram.HookUtils.hookMethods

object SpoilersHook : HookMoule {
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