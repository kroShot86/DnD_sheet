package com.example.dnd_sheet

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun saveImage(context: Context, uri: Uri): String? {
    return try {
        val directory = File(context.filesDir, "avatars")
        if (!directory.exists()) directory.mkdirs()

        val fileName = "avatar_${System.currentTimeMillis()}.jpg"
        val file = File(directory, fileName)

        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun deleteImageFile(path: String?) {
    if (path == null) return
    try {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun formatWeight(weight: Float): String {
    return if (weight % 1.0f == 0.0f) {
        weight.toInt().toString()
    } else {
        String.format(java.util.Locale.US, "%.1f", weight)
    }
}