package org.lyaaz.fuckgram.features

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.mediaDataControllerClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookMethods

object ReactionPopupHook : HookModule {
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