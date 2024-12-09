package org.lyaaz.fuckgram.features

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.dialogCellClass
import org.lyaaz.fuckgram.HookModule.Companion.dialogSwipeControllerClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookModule.Companion.sharedConfigClass
import org.lyaaz.fuckgram.HookUtils.hookMethods

object ChatSwipeHook : HookModule {

    override fun enabled(): Boolean {
        return settings.disableChatSwipe()
    }

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam): Boolean {
        return hookMethods(
            dialogCellClass,
            "getTranslationX",
            XC_MethodReplacement.returnConstant(0f)
        )
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
}