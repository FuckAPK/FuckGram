package org.baiyu.fuckgram;

import de.robv.android.xposed.XSharedPreferences;

public class Settings {

    private static XSharedPreferences prefs;
    private volatile static Settings INSTANCE;

    private static final String PREF_ENABLE_FORCE_FORWARD = "enable_force_forward";
    private static final String PREF_ENABLE_REMOVE_SPONSORED_ADS = "enable_remove_sponsored_ads";
    private static final String PREF_DISABLE_REACTION_POPUP = "disable_reaction_popup";
    private static final String PREF_DISABLE_QUICK_REACTION = "disable_quick_reaction";
    private static final String PREF_LOCK_PREMIUM_FEATURES = "lock_premium_features";
    private static final String PREF_ENABLE_REMOVE_EMOJI_SET = "enable_remove_emoji_set";
    private static final String PREF_ENABLE_SPEED_UP_DOWNLOAD = "enable_speed_up_download";
    private static final String PREF_DISABLE_TRACKING = "disable_tracking";

    private Settings() {
        prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID);
    }

    public static Settings getInstance() {
        if (INSTANCE == null) {
            synchronized (Settings.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Settings();
                }
            }
        }
        return INSTANCE;
    }

    public boolean enableForceForward() {
        return prefs.getBoolean(PREF_ENABLE_FORCE_FORWARD, true);
    }

    public boolean enableRemoveSponsoredAds() {
        return prefs.getBoolean(PREF_ENABLE_REMOVE_SPONSORED_ADS, true);
    }

    public boolean disableReactionPopup() {
        return prefs.getBoolean(PREF_DISABLE_REACTION_POPUP, true);
    }

    public boolean disableQuickReaction() {
        return prefs.getBoolean(PREF_DISABLE_QUICK_REACTION, true);
    }

    public boolean lockPremiumFeatures() {
        return prefs.getBoolean(PREF_LOCK_PREMIUM_FEATURES, true);
    }

    public boolean enableRemoveEmojiSet() {
        return prefs.getBoolean(PREF_ENABLE_REMOVE_EMOJI_SET, true);
    }

    public boolean enableSpeedUpDownload() {
        return prefs.getBoolean(PREF_ENABLE_SPEED_UP_DOWNLOAD, true);
    }

    public boolean disableTracking() {
        return prefs.getBoolean(PREF_DISABLE_TRACKING, true);
    }
}
