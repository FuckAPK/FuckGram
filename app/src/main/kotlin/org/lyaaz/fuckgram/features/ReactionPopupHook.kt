package org.lyaaz.fuckgram.features

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookMoule
import org.lyaaz.fuckgram.HookMoule.Companion.mediaDataControllerClass
import org.lyaaz.fuckgram.HookMoule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookMethods

object ReactionPopupHook : HookMoule {
    override fun enabled(): Boolean {
        return settings.disableReactionPopup()
    }

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam): Boolean {
        return hookMethods(
            mediaDataControllerClass,
            "getEnabledReactionsList",
            XC_MethodReplacement.returnConstant(emptyList<Any>())
        )
    }
}