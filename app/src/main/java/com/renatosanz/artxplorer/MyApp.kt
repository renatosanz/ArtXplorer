package com.renatosanz.artxplorer

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.renatosanz.artxplorer.di.dataModule
import com.renatosanz.artxplorer.di.databaseModule
import com.renatosanz.artxplorer.di.domainModule
import com.renatosanz.artxplorer.di.presentationModule
import com.renatosanz.artxplorer.presentation.screens.settings.ThemePreferences
import com.google.android.material.color.DynamicColors
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(ThemePreferences.get(this))
        DynamicColors.applyToActivitiesIfAvailable(this)
        startKoin {
            androidContext(this@MyApp)
            modules(dataModule, domainModule, presentationModule, databaseModule)
        }
    }
}