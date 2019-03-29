package com.lylx.featuredemo.util

import android.app.Activity
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun createImageFile(activity: Activity, prefix: String? = "", suffix: String? = ""): File? {
    val file: File?
    val timeStamp: String = SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.US).format(Date())
    val storageDir: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    try {
        file = File.createTempFile(
            prefix?.plus(timeStamp) ?: timeStamp,
            suffix?.plus(".png"),
            storageDir
        )
    } finally {

    }
    return file
}