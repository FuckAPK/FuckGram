package org.lyaaz.fuckgram

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import org.lyaaz.fuckgram.ui.AppTheme as Theme

class SettingsActivity : ComponentActivity() {

    private var currentUiMode: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        currentUiMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        setContent {
            Theme {
                SettingsScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val newUiMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (newUiMode != currentUiMode) {
            recreate()
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    Theme {
        SettingsScreen()
    }
}

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val prefs = Utils.getPrefs(context)
    val settings = Settings.getInstance(prefs)

    var enableForceForward by remember {
        mutableStateOf(settings.enableForceForward())
    }

    var removeSponsoredAds by remember {
        mutableStateOf(settings.enableRemoveSponsoredAds())
    }

    var disableReactionPopup by remember {
        mutableStateOf(settings.disableReactionPopup())
    }

    var disableQuickReaction by remember {
        mutableStateOf(settings.disableQuickReaction())
    }

    var lockPremiumFeatures by remember {
        mutableStateOf(settings.lockPremiumFeatures())
    }

    var removeEmojiSet by remember {
        mutableStateOf(settings.enableRemoveEmojiSet())
    }

    var speedUpDownload by remember {
        mutableStateOf(settings.enableSpeedUpDownload())
    }

    var disableTracking by remember {
        mutableStateOf(settings.disableTracking())
    }

    var disableChatSwipe by remember {
        mutableStateOf(settings.disableChatSwipe())
    }

    var disableChannelBottomButton by remember {
        mutableStateOf(settings.disableChannelBottomButton())
    }

    var disableStories by remember {
        mutableStateOf(settings.disableStories())
    }

    var prohibitSpoilers by remember {
        mutableStateOf(settings.prohibitSpoilers())
    }

    var enableMessageFilter by remember {
        mutableStateOf(settings.enableMessageFilter())
    }

    var messageFilterPattern by remember {
        mutableStateOf(settings.messageFilterPattern())
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .imePadding()
    ) {
        item {
            SwitchPreferenceItem(
                title = R.string.title_enable_force_forward,
                checked = enableForceForward,
                onCheckedChange = {
                    enableForceForward = it
                    prefs.edit {
                        putBoolean(Settings.PREF_ENABLE_FORCE_FORWARD, it)
                    }
                }
            )
        }

        item {
            SwitchPreferenceItem(
                title = R.string.title_enable_remove_sponsored_ads,
                checked = removeSponsoredAds,
                onCheckedChange = {
                    removeSponsoredAds = it
                    prefs.edit {
                        putBoolean(Settings.PREF_ENABLE_REMOVE_SPONSORED_ADS, it)
                    }
                }
            )
        }

        item {
            SwitchPreferenceItem(
                title = R.string.title_disable_reaction_popup,
                checked = disableReactionPopup,
                onCheckedChange = {
                    disableReactionPopup = it
                    prefs.edit {
                        putBoolean(Settings.PREF_DISABLE_REACTION_POPUP, it)
                    }
                }
            )
        }
        item {
            SwitchPreferenceItem(
                title = R.string.title_disable_quick_reaction,
                checked = disableQuickReaction,
                onCheckedChange = {
                    disableQuickReaction = it
                    prefs.edit {
                        putBoolean(Settings.PREF_DISABLE_QUICK_REACTION, it)
                    }
                }
            )
        }
        item {
            SwitchPreferenceItem(
                title = R.string.title_lock_premium_features,
                checked = lockPremiumFeatures,
                onCheckedChange = {
                    lockPremiumFeatures = it
                    prefs.edit {
                        putBoolean(Settings.PREF_LOCK_PREMIUM_FEATURES, it)
                    }
                }
            )
        }
        item {
            SwitchPreferenceItem(
                title = R.string.title_enable_remove_emoji_set,
                checked = removeEmojiSet,
                onCheckedChange = {
                    removeEmojiSet = it
                    prefs.edit {
                        putBoolean(Settings.PREF_ENABLE_REMOVE_EMOJI_SET, it)
                    }
                }
            )
        }
        item {
            SwitchPreferenceItem(
                title = R.string.title_enable_speed_up_download,
                checked = speedUpDownload,
                onCheckedChange = {
                    speedUpDownload = it
                    prefs.edit {
                        putBoolean(Settings.PREF_ENABLE_SPEED_UP_DOWNLOAD, it)
                    }
                }
            )
        }
        item {
            SwitchPreferenceItem(
                title = R.string.title_disable_tracking,
                checked = disableTracking,
                onCheckedChange = {
                    disableTracking = it
                    prefs.edit {
                        putBoolean(Settings.PREF_DISABLE_TRACKING, it)
                    }
                }
            )
        }
        item {
            SwitchPreferenceItem(
                title = R.string.title_disable_chat_swipe,
                checked = disableChatSwipe,
                onCheckedChange = {
                    disableChatSwipe = it
                    prefs.edit {
                        putBoolean(Settings.PREF_DISABLE_CHAT_SWIPE, it)
                    }
                }
            )
        }
        item {
            SwitchPreferenceItem(
                title = R.string.title_disable_channel_bottom_button,
                checked = disableChannelBottomButton,
                onCheckedChange = {
                    disableChannelBottomButton = it
                    prefs.edit {
                        putBoolean(Settings.PREF_DISABLE_CHANNEL_BOTTOM_BUTTON, it)
                    }
                }
            )
        }
        item {
            SwitchPreferenceItem(
                title = R.string.title_disable_stories,
                checked = disableStories,
                onCheckedChange = {
                    disableStories = it
                    prefs.edit {
                        putBoolean(Settings.PREF_DISABLE_STORIES, it)
                    }
                }
            )
        }
        item {
            SwitchPreferenceItem(
                title = R.string.title_prohibit_spoilers,
                checked = prohibitSpoilers,
                onCheckedChange = {
                    prohibitSpoilers = it
                    prefs.edit {
                        putBoolean(Settings.PREF_PROHIBIT_SPOILERS, it)
                    }
                }
            )
        }
        item {
            SwitchPreferenceItem(
                title = R.string.title_enable_message_filter,
                checked = enableMessageFilter,
                onCheckedChange = {
                    enableMessageFilter = it
                    prefs.edit {
                        putBoolean(Settings.PREF_ENABLE_MESSAGE_FILTER, it)
                    }
                }
            )
        }
        item {
            TextFieldPreference(
                title = R.string.title_message_filter_pattern,
                value = messageFilterPattern,
                onValueChange = {
                    messageFilterPattern = it
                    prefs.edit {
                        putString(Settings.PREF_MESSAGE_FILTER_PATTERN, it)
                    }
                },
                keyboardType = KeyboardType.Text
            )
        }
        item {
            Spacer(
                modifier = Modifier.windowInsetsBottomHeight(
                    WindowInsets.systemBars
                )
            )
        }
    }
}

@Composable
fun SwitchPreferenceItem(
    @StringRes title: Int,
    @StringRes summary: Int? = null,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
    noSwitch: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(if (enabled) 1.0f else 0.6f)
                )
                if (summary != null) {
                    Text(
                        text = stringResource(id = summary),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(if (enabled) 1.0f else 0.6f)
                    )
                }
            }
            if (!noSwitch) {
                Switch(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
            }
        }
    }
}

@Composable
fun TextFieldPreference(
    @StringRes title: Int,
    @StringRes summary: Int? = null,
    @StringRes unit: Int? = null,
    enabled: Boolean = true,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(if (enabled) 1.0f else 0.6f)
        )
        if (summary != null) {
            Text(
                text = stringResource(id = summary),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(if (enabled) 0.6f else 0.4f)
            )
        }
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            enabled = enabled,
            value = value,
            onValueChange = onValueChange,
            singleLine = false,
            maxLines = 3,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = ImeAction.Done
            ),
            suffix = unit?.let {
                @Composable {
                    Text(
                        text = stringResource(id = it),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary.copy(if (enabled) 1.0f else 0.6f)
                    )
                }
            },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            textStyle = MaterialTheme.typography.bodyMedium
        )
    }
}
