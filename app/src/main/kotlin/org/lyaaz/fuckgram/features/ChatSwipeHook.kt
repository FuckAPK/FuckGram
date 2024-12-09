package org.lyaaz.fuckgram.features

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookMoule
import org.lyaaz.fuckgram.HookMoule.Companion.dialogCellClass
import org.lyaaz.fuckgram.HookMoule.Companion.dialogSwipeControllerClass
import org.lyaaz.fuckgram.HookMoule.Companion.settings
import org.lyaaz.fuckgram.HookMoule.Companion.sharedConfigClass
import org.lyaaz.fuckgram.HookUtils.hookMethods

object ChatSwipeHook : HookMoule {

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