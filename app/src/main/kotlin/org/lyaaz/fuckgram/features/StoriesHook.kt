package org.lyaaz.fuckgram.features

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.messagesControllerClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookModule.Companion.storiesControllerClass
import org.lyaaz.fuckgram.HookUtils.hookMethods

object StoriesHook : HookModule {
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