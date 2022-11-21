package com.example.mylocation.ui.component

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.example.mylocation.domian.model.ConstantGeneral.Companion.FILE_SAVE
import com.example.mylocation.domian.model.ConstantGeneral.Companion.QUALITY
import com.example.mylocation.domian.model.ConstantGeneral.Companion.TYPE_FILE
import com.example.mylocation.domian.model.ConstantGeneral.Companion.TYPE_IMG
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.UUID

fun Context.toast(text: String?) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Bitmap.saveMediaToStorage(context: Context) {
    val uuidNamex= UUID.randomUUID().toString()

    val filename = "${uuidNamex}$TYPE_FILE"
    var fos: OutputStream? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        context?.contentResolver?.also { resolver ->
            val contentValues = ContentValues().apply {

                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, TYPE_IMG)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val imageUri: Uri? =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            fos = imageUri?.let { resolver.openOutputStream(it) }
        }
    } else {

        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, filename)
        fos = FileOutputStream(image)
    }

    fos?.use {
        this.compress(Bitmap.CompressFormat.JPEG, QUALITY, it)
        context?.toast(FILE_SAVE)
    }
}
