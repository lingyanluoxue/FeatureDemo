package com.lylx.featuredemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lylx.featuredemo.util.MetaUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.File


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
    findViewById<View>(R.id.btn_meta).setOnClickListener {
      writeMetadataToAudioFile()
//      MetaUtil.writeMetadataToMp3File(application.filesDir.path + File.separator + "lyrics"+ File.separator + "蔡依林 - 独占神话.mp3")
    }
  }


  fun writeMetadataToAudioFile() {

    val file = File(application.filesDir.path + File.separator + "lyrics", "蔡依林 - 独占神话.mp3")
//    val file = File(application.filesDir.path + File.separator + "lyrics", "李健_风吹麦浪.m4a")
//    val file = File(application.filesDir.path + File.separator + "lyrics", "Gigi Perez - Sailor Song.mp4")
//    val file = File(application.filesDir.path + File.separator + "lyrics", "往后余生.ogg")
//    val file = File(application.filesDir.path + File.separator + "lyrics", "往后余生.opus")

    MetaUtil.writeLyrics(file)
//    MetaUtil.deleteLyrics(file)

//    MetaUtil.writeStaticLyrics(file, "zho", "中文歌词", "新鲜的话题不少\n越古老越想知道")
//    MetaUtil.writeStaticLyrics(file, "eng", "英文歌词", "Get a little frightened sometimes\nA little cold inside\nCatching bad news on the radio")

//    MetaUtil.writeSyncLyrics(file, "zho","",
//      mutableListOf( SyncLyricLine(2, "text"),  SyncLyricLine(35, "蒙胧像过往的梦"), SyncLyricLine(41, "未能睡去"), SyncLyricLine(46, "徘徊路中")))

//    MetaUtil.readAllLyrics(file)

//    MetaUtil.writeMp4Metadata(file)

    MetaUtil.setCustomField(file, "https://example.com/referrer")
    Log.e("lylx", "customFieldValue:${MetaUtil.getCustomField(file)}")
  }

}
