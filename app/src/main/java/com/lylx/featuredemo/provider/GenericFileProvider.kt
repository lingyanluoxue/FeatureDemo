package com.lylx.featuredemo.provider

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

private const val authority = ".fileprovider"

fun getUriForFile(context: Context, file: File): Uri? {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    FileProvider.getUriForFile(context, context.packageName.plus(authority), file)
  } else {
    Uri.fromFile(file)
  }
}
