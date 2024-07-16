package org.lyaaz.fuckgram

import android.content.SharedPreferences

class Settings private constructor(private val prefs: SharedPreferences) {

    fun enableForceForward(): Boolean {
        return prefs.getBoolean(PREF_ENABLE_FORCE_FORWARD, DEFAULT_ENABLE_FORCE_FORWARD)
    }

    fun enableRemoveSponsoredAds(): Boolean {
        return prefs.getBoolean(
            PREF_ENABLE_REMOVE_SPONSORED_ADS,
            DEFAULT_ENABLE_REMOVE_SPONSORED_ADS
        )
    }

    fun disableReactionPopup(): Boolean {
        return prefs.getBoolean(PREF_DISABLE_REACTION_POPUP, DEFAULT_DISABLE_REACTION_POPUP)
    }

    fun disableQuickReaction(): Boolean {
        return prefs.getBoolean(PREF_DISABLE_QUICK_REACTION, DEFAULT_DISABLE_QUICK_REACTION)
    }

    fun lockPremiumFeatures(): Boolean {
        return prefs.getBoolean(PREF_LOCK_PREMIUM_FEATURES, DEFAULT_LOCK_PREMIUM_FEATURES)
    }

    fun enableRemoveEmojiSet(): Boolean {
        return prefs.getBoolean(PREF_ENABLE_REMOVE_EMOJI_SET, DEFAULT_ENABLE_REMOVE_EMOJI_SET)
    }

    fun enableSpeedUpDownload(): Boolean {
        return prefs.getBoolean(PREF_ENABLE_SPEED_UP_DOWNLOAD, DEFAULT_ENABLE_SPEED_UP_DOWNLOAD)
    }

    fun disableTracking(): Boolean {
        return prefs.getBoolean(PREF_DISABLE_TRACKING, DEFAULT_DISABLE_TRACKING)
    }

    fun disableChatSwipe(): Boolean {
        return prefs.getBoolean(PREF_DISABLE_CHAT_SWIPE, DEFAULT_DISABLE_CHAT_SWIPE)
    }

    fun disableChannelBottomButton(): Boolean {
        return prefs.getBoolean(
            PREF_DISABLE_CHANNEL_BOTTOM_BUTTON,
            DEFAULT_DISABLE_CHANNEL_BOTTOM_BUTTON
        )
    }

    fun prohibitSpoilers(): Boolean {
        return prefs.getBoolean(PREF_PROHIBIT_SPOILERS, DEFAULT_PROHIBIT_SPOILERS)
    }

    fun enableMessageFilter(): Boolean {
        return prefs.getBoolean(PREF_ENABLE_MESSAGE_FILTER, DEFAULT_ENABLE_MESSAGE_FILTER)
    }

    fun messageFilterPattern(): String {
        return prefs.getString(PREF_MESSAGE_FILTER_PATTERN, DEFAULT_MESSAGE_FILTER_PATTERN) ?: ""
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
        const val PREF_ENABLE_MESSAGE_FILTER = "enable_message_filter"
        const val PREF_MESSAGE_FILTER_PATTERN = "message_filter_pattern"

        const val DEFAULT_ENABLE_FORCE_FORWARD = true
        const val DEFAULT_ENABLE_REMOVE_SPONSORED_ADS = true
        const val DEFAULT_DISABLE_REACTION_POPUP = true
        const val DEFAULT_DISABLE_QUICK_REACTION = true
        const val DEFAULT_LOCK_PREMIUM_FEATURES = true
        const val DEFAULT_ENABLE_REMOVE_EMOJI_SET = true
        const val DEFAULT_ENABLE_SPEED_UP_DOWNLOAD = true
        const val DEFAULT_DISABLE_TRACKING = true
        const val DEFAULT_DISABLE_CHAT_SWIPE = true
        const val DEFAULT_DISABLE_CHANNEL_BOTTOM_BUTTON = true
        const val DEFAULT_PROHIBIT_SPOILERS = true
        const val DEFAULT_ENABLE_MESSAGE_FILTER = false
        const val DEFAULT_MESSAGE_FILTER_PATTERN = "车队|互推|飞机杯|优惠|机场|推广"

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
