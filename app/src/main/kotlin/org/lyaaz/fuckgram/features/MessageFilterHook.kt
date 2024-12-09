package org.lyaaz.fuckgram.features

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookMoule
import org.lyaaz.fuckgram.HookMoule.Companion.messageObjectClass
import org.lyaaz.fuckgram.HookMoule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.logHookError

object MessageFilterHook : HookMoule {

    override fun enabled(): Boolean {
        return settings.enableMessageFilter()
    }

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam): Boolean {
        val blacklistPattern by lazy {
            settings.messageFilterPattern().toRegex(RegexOption.IGNORE_CASE)
        }
        return runCatching {
            XposedBridge.hookMethod(
                java.util.ArrayList::class.java.getDeclaredMethod(
                    "add",
                    Object::class.java
                ), object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val arg = param.args[0] ?: return
                        if (arg.javaClass.name == messageObjectClass.name) {
                            val text by lazy {
                                XposedHelpers.getObjectField(
                                    arg, "messageText"
                                ) as CharSequence?
                            }
                            val caption by lazy {
                                XposedHelpers.getObjectField(
                                    arg, "caption"
                                ) as CharSequence?
                            }
                            val layoutCreated by lazy {
                                XposedHelpers.getBooleanField(
                                    arg, "layoutCreated"
                                )
                            }
                            // avoid block pinned message
                            if (!layoutCreated) return
                            text?.let {
                                if (blacklistPattern.containsMatchIn(it)) {
                                    param.result = false
                                    return
                                }
                            }
                            caption?.let {
                                if (blacklistPattern.containsMatchIn(it)) {
                                    param.result = false
                                    return
                                }
                            }
                        }
                    }
                }
            )
        }.onFailure {
            logHookError(java.util.ArrayList::class.java.name, "add", it)
        }.isSuccess
    }
}