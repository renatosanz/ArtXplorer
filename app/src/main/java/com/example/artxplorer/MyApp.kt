package com.example.artxplorer

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.artxplorer.di.dataModule
import com.example.artxplorer.di.databaseModule
import com.example.artxplorer.di.domainModule
import com.example.artxplorer.di.presentationModule
import com.example.artxplorer.presentation.screens.settings.ThemePreferences
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