package org.baiyu.fuckgram

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

            preferenceScreen.let {
                it.addPreference(
                    SwitchPreferenceCompat(requireContext()).apply {
                        key = Settings.PREF_ENABLE_FORCE_FORWARD
                        title = getString(R.string.title_enable_force_forward)
                        setDefaultValue(true)
                        isIconSpaceReserved = false
                    }
                )

                it.addPreference(
                    SwitchPreferenceCompat(requireContext()).apply {
                        key = Settings.PREF_ENABLE_REMOVE_SPONSORED_ADS
                        title = getString(R.string.title_enable_remove_sponsored_ads)
                        setDefaultValue(true)
                        isIconSpaceReserved = false
                    }
                )

                it.addPreference(
                    SwitchPreferenceCompat(requireContext()).apply {
                        key = Settings.PREF_DISABLE_REACTION_POPUP
                        title = getString(R.string.title_disable_reaction_popup)
                        setDefaultValue(true)
                        isIconSpaceReserved = false
                    }
                )

                it.addPreference(
                    SwitchPreferenceCompat(requireContext()).apply {
                        key = Settings.PREF_DISABLE_QUICK_REACTION
                        title = getString(R.string.title_disable_quick_reaction)
                        setDefaultValue(true)
                        isIconSpaceReserved = false
                    }

                )

                it.addPreference(
                    SwitchPreferenceCompat(requireContext()).apply {
                        key = Settings.PREF_LOCK_PREMIUM_FEATURES
                        title = getString(R.string.title_lock_premium_features)
                        setDefaultValue(true)
                        isIconSpaceReserved = false
                    }
                )

                it.addPreference(
                    SwitchPreferenceCompat(requireContext()).apply {
                        key = Settings.PREF_ENABLE_REMOVE_EMOJI_SET
                        title = getString(R.string.title_enable_remove_emoji_set)
                        setDefaultValue(true)
                        isIconSpaceReserved = false
                    }
                )

                it.addPreference(
                    SwitchPreferenceCompat(requireContext()).apply {
                        key = Settings.PREF_ENABLE_SPEED_UP_DOWNLOAD
                        title = getString(R.string.title_enable_speed_up_download)
                        setDefaultValue(true)
                        isIconSpaceReserved = false
                    }
                )

                it.addPreference(
                    SwitchPreferenceCompat(requireContext()).apply {
                        key = Settings.PREF_DISABLE_TRACKING
                        title = getString(R.string.title_disable_tracking)
                        setDefaultValue(true)
                        isIconSpaceReserved = false
                    }
                )

                it.addPreference(
                    SwitchPreferenceCompat(requireContext()).apply {
                        key = Settings.PREF_DISABLE_CHAT_SWIPE
                        title = getString(R.string.title_disable_chat_swipe)
                        setDefaultValue(true)
                        isIconSpaceReserved = false
                    }
                )

                it.addPreference(
                    SwitchPreferenceCompat(requireContext()).apply {
                        key = Settings.PREF_DISABLE_CHANNEL_BOTTOM_BUTTON
                        title = getString(R.string.title_disable_channel_bottom_button)
                        setDefaultValue(true)
                        isIconSpaceReserved = false
                    }
                )
            }
        }
    }
}
