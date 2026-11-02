package com.renatosanz.artxplorer

import android.util.Log

// Debug logs carry artwork data and search terms, so they must not run in release builds.
fun logDebug(tag: String, message: String) {
    if (BuildConfig.DEBUG) Log.d(tag, message)
}
