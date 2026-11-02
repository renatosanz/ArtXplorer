package com.renatosanz.artxplorer.presentation.screens.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object ThemePreferences {
    private const val PREFS = "settings"
    private const val KEY_THEME = "theme_mode"

    fun get(context: Context): Int =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    fun set(context: Context, mode: Int) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putInt(KEY_THEME, mode).apply()
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
