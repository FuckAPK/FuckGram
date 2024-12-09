package org.lyaaz.fuckgram

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import org.lyaaz.fuckgram.features.*

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        HookMoule.lpparam = lpparam
        modules
            .filter { it.enabled() }
            .forEach {
                it.hook(lpparam)
            }
    }

    companion object {
        private val modules = listOf<HookMoule>(
            ChannelBottomButtonHook,
            ChatSwipeHook,
            ForceForwardHook,
            MessageFilterHook,
            PremiumFeaturesHook,
            QuickReactionHook,
            ReactionPopupHook,
            RemoveEmojiSetHook,
            RemoveSponsoredAdsHook,
            SpeedUpDownloadHook,
            SpoilersHook,
            StoriesHook,
            TrackingHook
        )
    }
}
