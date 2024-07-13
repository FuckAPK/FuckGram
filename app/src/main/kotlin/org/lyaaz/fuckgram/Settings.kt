package org.lyaaz.fuckgram

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

    fun disableChannelBottomButton(): Boolean {
        return prefs.getBoolean(PREF_DISABLE_CHANNEL_BOTTOM_BUTTON, true)
    }

    fun prohibitSpoilers(): Boolean {
        return prefs.getBoolean(PREF_PROHIBIT_SPOILERS, true)
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
        const val PREF_DISABLE_CHANNEL_BOTTOM_BUTTON = "disable_channel_bottom_button"
        const val PREF_PROHIBIT_SPOILERS = "prohibit_spoilers"

        private const val DEFAULT_MAX_FILE_SIZE = 1024L * 1024L * 2000L
        const val DOWNLOAD_CHUNK_SIZE_BIG = 1024 * 1024
        const val MAX_DOWNLOAD_REQUESTS = 8
        const val MAX_DOWNLOAD_REQUESTS_BIG = 8
        const val MAX_CDN_PARTS = (DEFAULT_MAX_FILE_SIZE / DOWNLOAD_CHUNK_SIZE_BIG).toInt()

        @Volatile
        private var INSTANCE: Settings? = null
        fun getInstance(prefs: SharedPreferences): Settings {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Settings(prefs).also { INSTANCE = it }
            }
        }
    }
}