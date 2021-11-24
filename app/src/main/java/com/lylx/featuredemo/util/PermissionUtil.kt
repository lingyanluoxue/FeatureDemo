package com.lylx.learnfeature.util

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat

/**
 * 检测通知是否开启
 */
fun areNotificationsEnabled(context: Context): Boolean =
    NotificationManagerCompat.from(context).areNotificationsEnabled()

/**
 * 检测通知渠道是否开启
 *
 * 小米Note3 android8.1.0 关闭通知，不跳转到通知管理
 */
fun checkNotificationChannel(context: Activity, channelId: String): Boolean {
    if (areNotificationsEnabled(context)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = manager.getNotificationChannel(channelId)
            if (channel.importance == NotificationManager.IMPORTANCE_NONE) {
                // 跳转通知管理的渠道通知
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.applicationContext.packageName)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
                context.startActivity(intent)
                Toast.makeText(context, "请手动将${channel.name}通知打开", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    } else {
        navigateToNotificationSetting(context)
    }
    return false
}

fun navigateToNotificationSetting(context: Context) {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
            val intent = Intent()
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.applicationContext.packageName)
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.applicationInfo.uid)
            context.startActivity(intent)
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
            val intent = Intent()
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra("app_package", context.applicationContext.packageName)
            intent.putExtra("app_uid", context.applicationInfo.uid)
            context.startActivity(intent)
        }
        else -> {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse("package:${context.applicationContext.packageName}")
            context.startActivity(intent)
        }
    }
}