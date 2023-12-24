package org.baiyu.fuckgram

import android.content.SharedPreferences

class Settings private constructor(private val prefs: SharedPreferences) {

    fun enableForceForward(): Boolean {
        return prefs.getBoolean(PREF_ENABLE_FORCE_FORWARD, true)
    }

    fun enableRemoveSponsoredAds(): Boolean {
        return prefs.getBoolean(PREF_ENABLE_REMOVE_SPONSORED_ADS, true)
    }

    fun disableReactionPopup(): Boolean {
        return prefs.getBoolean(PREF_DISABLE_REACTION_POPUP, true)
    }

    fun disableQuickReaction(): Boolean {
        return prefs.getBoolean(PREF_DISABLE_QUICK_REACTION, true)
    }

    fun lockPremiumFeatures(): Boolean {
        return prefs.getBoolean(PREF_LOCK_PREMIUM_FEATURES, true)
    }

    fun enableRemoveEmojiSet(): Boolean {
        return prefs.getBoolean(PREF_ENABLE_REMOVE_EMOJI_SET, true)
    }

    fun enableSpeedUpDownload(): Boolean {
        return prefs.getBoolean(PREF_ENABLE_SPEED_UP_DOWNLOAD, true)
    }

    fun disableTracking(): Boolean {
        return prefs.getBoolean(PREF_DISABLE_TRACKING, true)
    }

    fun disableChatSwipe(): Boolean {
        return prefs.getBoolean(PREF_DISABLE_CHAT_SWIPE, true)
    }

    companion object {

        const val PREF_ENABLE_FORCE_FORWARD = "enable_force_forward"
        const val PREF_ENABLE_REMOVE_SPONSORED_ADS = "enable_remove_sponsored_ads"
        const val PREF_DISABLE_REACTION_POPUP = "disable_reaction_popup"
        const val PREF_DISABLE_QUICK_REACTION = "disable_quick_reaction"
        const val PREF_LOCK_PREMIUM_FEATURES = "lock_premium_features"
        const val PREF_ENABLE_REMOVE_EMOJI_SET = "enable_remove_emoji_set"
        const val PREF_ENABLE_SPEED_UP_DOWNLOAD = "enable_speed_up_download"
        const val PREF_DISABLE_TRACKING = "disable_tracking"
        const val PREF_DISABLE_CHAT_SWIPE = "disable_chat_swipe"

        @Volatile
        private var INSTANCE: Settings? = null
        fun getInstance(prefs: SharedPreferences): Settings {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Settings(prefs).also { INSTANCE = it }
            }
        }
    }
}
