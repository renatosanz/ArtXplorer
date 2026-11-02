package com.renatosanz.artxplorer.data.common

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.toBitmap
import com.renatosanz.artxplorer.logDebug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


suspend fun saveImageToGallery(context: Context, imageUrl: String, title: String): Result<Uri> {
    return withContext(Dispatchers.IO) {
        try {
            // 1. Descargar imagen con Coil
            val request = ImageRequest.Builder(context).data(imageUrl).build()
            val result = context.imageLoader.execute(request)
            val bitmap = (result.image)?.toBitmap()

            // 2. Guardar en galería con MediaStore
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$title.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
            ) ?: return@withContext Result.failure(Exception("Error al guardar imagen"))

            context.contentResolver.openOutputStream(uri)?.use { stream ->
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            }
            logDebug("saveImageToGallery", "imagen guardada correctamente: $uri")
            Result.success(uri)
        } catch (e: Exception) {
            logDebug("saveImageToGallery", "error al guardar imagen: ${e.message}")
            Result.failure(e)
        }
    }
}

suspend fun saveImageToCache(context: Context, imageUrl: String, title: String): Result<Uri> {
    return withContext(Dispatchers.IO) {
        try {
            val request = ImageRequest.Builder(context).data(imageUrl).build()
            val result = context.imageLoader.execute(request)
            val bitmap = (result.image)?.toBitmap()

            val imagesDir = File(context.filesDir, "artworks").apply { mkdirs() }
            val file = File(imagesDir, "$title.jpg")

            FileOutputStream(file).use { stream ->
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            }

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            Result.success(uri)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

fun getPathFromUri(context: Context, uri: Uri): String? {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
        val column = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        if (cursor.moveToFirst()) {
            return cursor.getString(column)
        }
    }
    return null
}