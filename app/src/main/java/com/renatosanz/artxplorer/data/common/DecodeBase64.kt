package com.renatosanz.artxplorer.data.common

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.core.graphics.drawable.toDrawable

fun decodeBase64ToDrawable(base64Str: String?): Drawable? {
    if (base64Str == null) return null
    return try {
        val pureBase64 = base64Str.substringAfter(",")
        val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        bitmap.toDrawable(Resources.getSystem())
    } catch (e: Exception) {
        null
    }
}