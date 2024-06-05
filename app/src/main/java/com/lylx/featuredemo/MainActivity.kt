package com.lylx.featuredemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup


class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    findViewById<View>(R.id.btn_activity_result).setOnClickListener {
      startActivity(Intent(this, ResultActivity::class.java))
    }

    findViewById<View>(R.id.btn_notification).setOnClickListener {
      startActivity(Intent(this, NotificationActivity::class.java))
    }

    findViewById<View>(R.id.btn_jsoup).setOnClickListener {
      lifecycleScope.launch {
        val lyricUrl = Uri
          .parse("https://www.google.com/search")
          .buildUpon()
          .appendQueryParameter("q", "lyric 李志 - 关于郑州的记忆").toString()
        val result = withContext(Dispatchers.IO) {
          val document = Jsoup.connect(lyricUrl).timeout(5000).get()
          val search = document?.getElementsByClass("PZPZlf")?.firstOrNull {
            it.className() == "PZPZlf"
          }
          search?.select("span")?.mapNotNull {
            it.text()
          }
        }
        Log.d("lylx", "result:$result")
      }
    }
  }

}
