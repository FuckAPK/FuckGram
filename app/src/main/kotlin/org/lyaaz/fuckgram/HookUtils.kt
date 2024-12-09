package org.lyaaz.fuckgram

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object HookUtils {
    fun logHookError(className: String, methodName: String, t: Throwable) {
        XposedBridge.log("Failed to hook $className::$methodName")
        XposedBridge.log(t)
    }

    fun hookMethods(clazz: Class<*>, methodName: String, callback: XC_MethodHook): Boolean {
        return runCatching {
            XposedBridge.hookAllMethods(clazz, methodName, callback)
        }.onFailure {
            logHookError(clazz.name, methodName, it)
        }.isSuccess
    }

    fun hookConstructors(clazz: Class<*>, callback: XC_MethodHook): Boolean {
        return runCatching {
            XposedBridge.hookAllConstructors(clazz, callback)
        }.onFailure {
            logHookError(clazz.name, "<init>", it)
        }.isSuccess
    }
}