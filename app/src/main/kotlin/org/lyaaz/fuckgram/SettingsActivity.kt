package org.lyaaz.fuckgram

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import de.robv.android.xposed.XposedBridge

class SettingsActivity : AppCompatActivity() {
    /** @noinspection deprecation
     */
    @SuppressLint("WorldReadableFiles")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)
        try {
            @Suppress("DEPRECATION")
            getSharedPreferences(BuildConfig.APPLICATION_ID + "_preferences", MODE_WORLD_READABLE)
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, MySettingsFragment())
            .commit()
    }

    class MySettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            preferenceScreen = preferenceManager.createPreferenceScreen(requireContext())

            val addSwitchPreference: (String, Int, Boolean) -> Unit =
                { key, titleResId, defaultValue ->
                    SwitchPreferenceCompat(requireContext()).apply {
                        this.key = key
                        this.title = getString(titleResId)
                        this.setDefaultValue(defaultValue)
                        this.isIconSpaceReserved = false
                    }.let {
                        preferenceScreen.addPreference(it)
                    }
                }

            addSwitchPreference(
                Settings.PREF_ENABLE_FORCE_FORWARD,
                R.string.title_enable_force_forward,
                Settings.DEFAULT_ENABLE_FORCE_FORWARD
            )
            addSwitchPreference(
                Settings.PREF_ENABLE_REMOVE_SPONSORED_ADS,
                R.string.title_enable_remove_sponsored_ads,
                Settings.DEFAULT_ENABLE_REMOVE_SPONSORED_ADS
            )
            addSwitchPreference(
                Settings.PREF_DISABLE_REACTION_POPUP,
                R.string.title_disable_reaction_popup,
                Settings.DEFAULT_DISABLE_REACTION_POPUP
            )
            addSwitchPreference(
                Settings.PREF_DISABLE_QUICK_REACTION,
                R.string.title_disable_quick_reaction,
                Settings.DEFAULT_DISABLE_QUICK_REACTION
            )
            addSwitchPreference(
                Settings.PREF_LOCK_PREMIUM_FEATURES,
                R.string.title_lock_premium_features,
                Settings.DEFAULT_LOCK_PREMIUM_FEATURES
            )
            addSwitchPreference(
                Settings.PREF_ENABLE_REMOVE_EMOJI_SET,
                R.string.title_enable_remove_emoji_set,
                Settings.DEFAULT_ENABLE_REMOVE_EMOJI_SET
            )
            addSwitchPreference(
                Settings.PREF_ENABLE_SPEED_UP_DOWNLOAD,
                R.string.title_enable_speed_up_download,
                Settings.DEFAULT_ENABLE_SPEED_UP_DOWNLOAD
            )
            addSwitchPreference(
                Settings.PREF_DISABLE_TRACKING,
                R.string.title_disable_tracking,
                Settings.DEFAULT_DISABLE_TRACKING
            )
            addSwitchPreference(
                Settings.PREF_DISABLE_CHAT_SWIPE,
                R.string.title_disable_chat_swipe,
                Settings.DEFAULT_DISABLE_CHAT_SWIPE
            )
            addSwitchPreference(
                Settings.PREF_DISABLE_CHANNEL_BOTTOM_BUTTON,
                R.string.title_disable_channel_bottom_button,
                Settings.DEFAULT_DISABLE_CHANNEL_BOTTOM_BUTTON
            )
            addSwitchPreference(
                Settings.PREF_PROHIBIT_SPOILERS,
                R.string.title_prohibit_spoilers,
                Settings.DEFAULT_PROHIBIT_SPOILERS
            )
            addSwitchPreference(
                Settings.PREF_ENABLE_MESSAGE_FILTER,
                R.string.title_enable_message_filter,
                Settings.DEFAULT_ENABLE_MESSAGE_FILTER
            )
            EditTextPreference(requireContext()).apply {
                this.key = Settings.PREF_MESSAGE_FILTER_PATTERN
                this.title = getString(R.string.title_message_filter_pattern)
                this.isIconSpaceReserved = false
                this.setDefaultValue(Settings.DEFAULT_MESSAGE_FILTER_PATTERN)
                this.setSummaryProvider(EditTextPreference.SimpleSummaryProvider.getInstance())
            }.let {
                preferenceScreen.addPreference(it)
            }
        }
    }
}
