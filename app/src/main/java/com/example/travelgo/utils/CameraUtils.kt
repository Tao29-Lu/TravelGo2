package com.example.travelgo.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object CameraUtils {

    /**
     * Crea un archivo temporal para guardar la foto de la cámara
     */
    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir("images")

        if (storageDir?.exists() == false) {
            storageDir.mkdirs()
        }

        return File.createTempFile(
            "TRAVELGO_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    /**
     * Obtiene el URI para la foto usando FileProvider
     */
    fun getImageUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    /**
     * Limpia archivos antiguos de fotos
     */
    fun cleanOldImages(context: Context, daysOld: Int = 7) {
        val storageDir = context.getExternalFilesDir("images") ?: return
        val currentTime = System.currentTimeMillis()
        val maxAge = daysOld * 24 * 60 * 60 * 1000L

        storageDir.listFiles()?.forEach { file ->
            if (currentTime - file.lastModified() > maxAge) {
                file.delete()
            }
        }
    }
}