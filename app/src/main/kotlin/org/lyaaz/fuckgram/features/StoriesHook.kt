package org.lyaaz.fuckgram.features

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookMoule
import org.lyaaz.fuckgram.HookMoule.Companion.messagesControllerClass
import org.lyaaz.fuckgram.HookMoule.Companion.settings
import org.lyaaz.fuckgram.HookMoule.Companion.storiesControllerClass
import org.lyaaz.fuckgram.HookUtils.hookMethods

object StoriesHook : HookMoule {
    override fun enabled(): Boolean {
        return settings.disableStories()
    }

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam): Boolean {
        return hookMethods(
            storiesControllerClass,
            "hasStories",
            XC_MethodReplacement.returnConstant(false)
        )
        hookMethods(
            messagesControllerClass,
            "storiesEnabled",
            XC_MethodReplacement.returnConstant(false)
        )
    }
}