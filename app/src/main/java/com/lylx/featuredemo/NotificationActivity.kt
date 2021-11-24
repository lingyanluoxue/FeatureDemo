package com.lylx.featuredemo

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import com.lylx.learnfeature.util.checkNotificationChannel
import android.content.Intent
import androidx.core.app.TaskStackBuilder


class NotificationActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_notification)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      createNotificationChannel("attention", "关注", NotificationManager.IMPORTANCE_HIGH)

      createNotificationChannel("advertise", "广告", NotificationManager.IMPORTANCE_HIGH)
    }

    findViewById<View>(R.id.btn_push_attention).setOnClickListener {
      sendAttentionMsg()
    }

    findViewById<View>(R.id.btn_push_advertise).setOnClickListener {
      sendAdvertiseMsg()
    }
  }


  private fun sendAdvertiseMsg() {
    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notification = NotificationCompat.Builder(this, "advertise")
      .setContentTitle("收到一条广告")
      .setContentText("房贷利率恢复基准利率，你还在等什么？")
      .setWhen(System.currentTimeMillis())
      .setSmallIcon(R.mipmap.ic_launcher)
      .setAutoCancel(true)
      .build()
    manager.notify(1, notification)
  }

  private fun sendAttentionMsg() {
    if (!checkNotificationChannel(this, "attention")) {
      return
    }
    val resultIntent = Intent(this, ResultActivity::class.java)
    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notification = NotificationCompat.Builder(this, "attention")
      .setContentTitle("收到一条关注的信息")
      .setContentText("Android Q Beta 闪亮登场")
      .setWhen(System.currentTimeMillis())
      .setSmallIcon(R.mipmap.ic_launcher)
      .setAutoCancel(true)
      .setContentIntent(TaskStackBuilder.create(this).apply {
        this.addNextIntentWithParentStack(resultIntent)
      }.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT))
      .build()
    manager.notify(2, notification)
  }

  @TargetApi(Build.VERSION_CODES.O)
  private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) {
    val channel = NotificationChannel(channelId, channelName, importance)
    channel.setShowBadge(true)
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
  }
}