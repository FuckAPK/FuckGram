package org.lyaaz.fuckgram

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object Utils {
    /**
     * Retrieves shared preferences considering security measures.
     *
     * @param context The context used to access shared preferences.
     * @return Shared preferences instance.
     */
    @SuppressLint("WorldReadableFiles")
    fun getPrefs(context: Context): SharedPreferences {
        val prefsName = "${context.packageName}_preferences"
        return runCatching {
            @Suppress("DEPRECATION")
            context.getSharedPreferences(
                prefsName,
                Activity.MODE_WORLD_READABLE
            )
        }.getOrNull() ?: run {
            context.getSharedPreferences(
                prefsName,
                Activity.MODE_PRIVATE
            )
        }
    }
}