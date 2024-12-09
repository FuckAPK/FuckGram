package org.lyaaz.fuckgram.features

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookMoule
import org.lyaaz.fuckgram.HookMoule.Companion.fileLoadOperationClass
import org.lyaaz.fuckgram.HookMoule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookMethods
import org.lyaaz.fuckgram.Settings

object SpeedUpDownloadHook : HookMoule {
    override fun enabled(): Boolean {
        return settings.enableSpeedUpDownload()
    }

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam): Boolean {
        return hookMethods(fileLoadOperationClass, "updateParams", object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                XposedHelpers.setIntField(
                    param.thisObject,
                    "downloadChunkSizeBig",
                    Settings.DOWNLOAD_CHUNK_SIZE_BIG
                )
                XposedHelpers.setIntField(
                    param.thisObject,
                    "maxDownloadRequests",
                    Settings.MAX_DOWNLOAD_REQUESTS
                )
                XposedHelpers.setIntField(
                    param.thisObject,
                    "maxDownloadRequestsBig",
                    Settings.MAX_DOWNLOAD_REQUESTS_BIG
                )
                XposedHelpers.setIntField(
                    param.thisObject,
                    "maxCdnParts",
                    Settings.MAX_CDN_PARTS
                )
                param.result = null
            }
        })
    }
}