package org.lyaaz.fuckgram

import android.content.SharedPreferences
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

interface HookModule {
    fun hook(lpparam: LoadPackageParam): Boolean
    fun enabled(): Boolean

    companion object {
        lateinit var lpparam: LoadPackageParam

        val prefs: SharedPreferences by lazy { XSharedPreferences(BuildConfig.APPLICATION_ID) }
        val settings: Settings by lazy { Settings.getInstance(prefs) }

        val getClass = { name: String ->
            XposedHelpers.findClass(name, lpparam.classLoader)
        }

        val messagesControllerClass: Class<*> by lazy { getClass("org.telegram.messenger.MessagesController") }
        val chatActivityClass: Class<*> by lazy { getClass("org.telegram.ui.ChatActivity") }
        val mediaDataControllerClass: Class<*> by lazy { getClass("org.telegram.messenger.MediaDataController") }
        val fileLoadOperationClass: Class<*> by lazy { getClass("org.telegram.messenger.FileLoadOperation") }
        val dialogCellClass: Class<*> by lazy { getClass("org.telegram.ui.Cells.DialogCell") }
        val emojiTabsStripClass: Class<*> by lazy { getClass("org.telegram.ui.Components.EmojiTabsStrip") }
        val sharedConfigClass: Class<*> by lazy { getClass("org.telegram.messenger.SharedConfig") }
        val spoilerEffectClass: Class<*> by lazy { getClass("org.telegram.ui.Components.spoilers.SpoilerEffect") }
        val localeControllerClass: Class<*> by lazy { getClass("org.telegram.messenger.LocaleController") }
        val rStringClass: Class<*> by lazy { getClass("org.telegram.messenger.R\$string") }
        val messageObjectClass: Class<*> by lazy { getClass("org.telegram.messenger.MessageObject") }
        val dialogSwipeControllerClass: Class<*> by lazy { getClass("org.telegram.ui.DialogsActivity\$SwipeController") }
        val storiesControllerClass: Class<*> by lazy { getClass("org.telegram.ui.Stories.StoriesController") }
    }
}